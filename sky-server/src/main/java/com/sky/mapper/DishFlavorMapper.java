package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertFlavor(List<DishFlavor> flavors);

    /**
     * 根据菜品dishid删除flavor中数据
     * @param id
     */
    void deleteByDishId(Long id);

}
