package com.sky.mapper;

import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {

    @Select({"select setmeal_id from setmeal_dish where dish_id = #{id}"})
    Long getSetealIdById(Long id);

}
