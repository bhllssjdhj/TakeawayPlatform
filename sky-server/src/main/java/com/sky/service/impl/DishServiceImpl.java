package com.sky.service.impl;

import com.github.pagehelper.Constant;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

//声明是一个服务层组件，以用来自动注入
@Service
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品with口味
     * @param dishDTO
     */
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);

        //向dish表中添加一条数据
        dishMapper.insert(dish);

        //由于两表之间是有关联的，这里不使用外键，通过逻辑来处理两表之间关联
        //由于id为自增主键，只有在dish表执行完后才可以拿到，因此需要dish表执行完insert后返回其id，然后才能拿到（见dishmapper.xml中insert）
        long id = dish.getId();

        //向dish_flavor表中插入数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(id);
            });
        }
        dishFlavorMapper.insertFlavor(flavors);
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> page = dishMapper.pageQuery(dishPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());

    }


    public void delete(List<Long> id) {
        //判断能否删除，查询id是否正在销售
        for (Long i : id){
            Dish dish = dishMapper.getById(i);
            if (Objects.equals(dish.getStatus(), StatusConstant.ENABLE))
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
        }

        //查询是否跟套餐表setmeal_dish关联
        for (Long i : id) {
            Long id1 = setmealDishMapper.getSetealIdById(i);
            if (id1 != null) throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }

//        //删除dish表以及对应flavor表
//        for (Long i : id) {
//            dishMapper.deleteById(i);
//            dishFlavorMapper.deleteByDishId(i);
//        }
        dishMapper.deleteAllById(id);
        dishFlavorMapper.deleteAllByDishId(id);

    }



}
