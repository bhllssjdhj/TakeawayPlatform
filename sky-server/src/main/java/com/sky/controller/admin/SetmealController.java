package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    /**
     * 新建套餐
     * @param setmealDTO
     * @return
     */
    @RequestMapping("")
    @ApiOperation("新建套餐")
    public Result addMeal(@RequestBody SetmealDTO setmealDTO){
        log.info("新建套餐：{}",setmealDTO.getName());
        setmealService.addMeal(setmealDTO);
        return Result.success();
    }
}
