package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /**
     * 新增菜品with口味
     * @param dishDTO
     * @return
     */
    public void saveWithFlavor(DishDTO dishDTO);
}
