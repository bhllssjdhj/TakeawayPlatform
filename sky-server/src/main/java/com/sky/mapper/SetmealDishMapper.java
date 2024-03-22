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
}
