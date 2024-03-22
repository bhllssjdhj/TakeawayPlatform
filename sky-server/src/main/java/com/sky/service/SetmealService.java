package com.sky.service;


import com.sky.dto.SetmealDTO;


public interface SetmealService {

    /**
     * 新建套餐
     * @param setmealDTO
     */
    void addMeal(SetmealDTO setmealDTO);
}
