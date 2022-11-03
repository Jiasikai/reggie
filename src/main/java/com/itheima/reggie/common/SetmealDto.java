package com.itheima.reggie.common;


import com.itheima.reggie.entity.DishFlavor;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.entity.SetmealDish;
import lombok.Data;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {
 private List<SetmealDish> setmealDishes = new ArrayList<>();

 private String categoryName;

 private Integer copies;

}
