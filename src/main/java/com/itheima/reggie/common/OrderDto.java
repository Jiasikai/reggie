package com.itheima.reggie.common;

import com.itheima.reggie.entity.Orders;
import lombok.Data;

@Data
public class OrderDto extends Orders {

    private int sumNum;
}
