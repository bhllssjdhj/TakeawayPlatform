package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    /**
     * 新建菜品
     * @param setmealDTO
     */
    @Transactional
    public void addMealWithDishes(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);
        //更新setmeal套餐表
        setmealMapper.addMeal(setmeal);

        //获得setmeal的主键id
        Long id = setmeal.getId();

        //更新setmeal_dish表
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishMapper.addMealDish(setmealDishes);
    }

    /**
     * 套餐分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 删除套餐
     * @param id
     */
    @Transactional
    public void deleteMeal(List<Long> id) {
        //判断能否删除，查询id是否正在销售
        for (Long i : id) {
            Setmeal setmeal = setmealMapper.getById(i);
            if (Objects.equals(setmeal.getStatus(), StatusConstant.ENABLE)) {
                //正在销售，无法删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }

        //删除两张表中的对应行
        setmealMapper.deleteMealById(id);
        setmealDishMapper.deleteBySetmealId(id);

    }

    /**
     * 根据id查询套餐和套餐菜品关系
     *
     * @param id
     * @return
     */
    public SetmealVO getByIdWithDish(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(id);

        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    /**
     * 修改套餐
     *
     * @param setmealDTO
     */
    @Transactional
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //更新setmeal表的信息
        setmealMapper.update(setmeal);
        Long setmealId = setmeal.getId();
        //删除对应的setmeal_dish表项，并添加新的项
        List<Long> setmealIdList = Collections.singletonList(setmealId);
        setmealDishMapper.deleteBySetmealId(setmealIdList);

        //将前端传来的新setmealDishes按套餐setmealId加入表，由于套餐内一定含有菜品，因此不必判断是否为空
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.forEach(
                setmealDish -> {
                    setmealDish.setSetmealId(setmealId);
        });

        //添加新的setmeal_dish表项
        setmealDishMapper.addMealDish(setmealDishes);
    }

    /**
     * 更改出售状态
     * @param status
     * @param id
     */
    @Transactional
    public void statusChange(Integer status, Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        List<SetmealDish> setmealDishes = setmealDishMapper.getBySetmealId(setmeal.getId());

        //当起售套餐时，若包括停售菜品dish则报错
        if (status == StatusConstant.ENABLE)
            setmealDishes.forEach(setmealDish -> {
                Long dishId = setmealDish.getDishId();
                Dish dish = dishMapper.getById(dishId);
                if (dish.getStatus() == StatusConstant.DISABLE)
                    throw new BaseException(MessageConstant.SETMEAL_ENABLE_FAILED);
            });
        //不包含，则改变setmeal状态
        Setmeal setmealNew = Setmeal.builder()
                .id(id)
                .status(status)
                .build();

        setmealMapper.update(setmealNew);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
