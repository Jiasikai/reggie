package com.itheima.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.entity.ShoppingCart;
import com.itheima.reggie.mapper.ShoppingChartMapper;
import com.itheima.reggie.service.ShoppingChartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingChartServiceImpl extends ServiceImpl<ShoppingChartMapper, ShoppingCart> implements ShoppingChartService {
}
