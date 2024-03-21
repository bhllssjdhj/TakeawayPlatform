package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {
    /**
     * 新增菜品with口味
     * @param dishDTO
     * @return
     */
    public void saveWithFlavor(DishDTO dishDTO);

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    PageResult pageQuery(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除dish菜品
     * @param id
     */
    public void delete(List<Long> id);

    /**
     * 根据id查询dish及口味flavor数据
     * @param id
     * @return
     */
    DishVO getByIdWithFlavor(Long id);

    /**
     * 修改菜品dish信息及口味flavor数据
     * @param dishVO
     */
    void updateWithFlavor(DishDTO dishVO);

    /**
     * 菜品出售状态更改
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);
}
