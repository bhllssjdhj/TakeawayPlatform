package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    @Select({"select setmeal_id from setmeal_dish where dish_id = #{id}"})
    Long getSetealIdById(Long id);

    void addMealDish(List<SetmealDish> setmealDishes);

    /**
     * 根据套餐id查询套餐和菜品的关联关系
     * @param id
     * @return
     */
    @Select("select * from setmeal_dish where setmeal_id = #{id}")
    List<SetmealDish> getBySetmealId(Long id);

    /**
     * 根据setmealId删除对应表项
     * @param id
     */
    void deleteBySetmealId(List<Long> id);
}
