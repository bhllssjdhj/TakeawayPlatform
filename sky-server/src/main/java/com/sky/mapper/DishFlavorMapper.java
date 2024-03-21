package com.sky.mapper;

import com.sky.entity.DishFlavor;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertFlavor(List<DishFlavor> flavors);

    /**
     * 根据菜品dishid删除flavor中数据
     * @param id
     */
    void deleteByDishId(Long id);

    /**
     * 根据菜品dishid批量删除flavor中数据
     * @param id
     */
    void deleteAllByDishId(List<Long> id);

    /**
     * 根据菜品id查询菜品口味flavor
     * @param id
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{id}")
    List<DishFlavor> getByDishId(Long id);

    /**
     * 修改菜品dish信息及口味flavor数据
     * @param dishVO
     */
    void updateFlavor(DishVO dishVO);
}
