package com.itheima.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itheima.reggie.common.DishDto;
import com.itheima.reggie.entity.Dish;
import org.springframework.stereotype.Service;


public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);

    public  DishDto getByIdWithFlavor(Long id);
    public void updateWithFlavor(DishDto dishDto);
}
