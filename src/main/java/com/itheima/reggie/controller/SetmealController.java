package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.DishDto;
import com.itheima.reggie.common.R;
import com.itheima.reggie.common.SetmealDto;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.Setmeal;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;
    @Autowired
    private CategoryService categoryService;

    /*
     * 分页查询套餐
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {


        //构造分页构造器
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dishDtoPage = new Page<>();
        //构造条件构造器
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        //执行查询
        setmealService.page(pageInfo, queryWrapper);

        //执行分页查询
        //对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        List<Setmeal> records = pageInfo.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {

            SetmealDto setmealDto = new SetmealDto();

            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            //根据id查分类对象
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }
            return setmealDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /*
     * 保存套餐
     * */
    @PostMapping

    public R<String> save(@RequestBody SetmealDto setmealDto) {
        setmealService.saveWithDish(setmealDto);
        return R.success("新增套餐成功！");
    }

    /*
     * 根据id查询套餐
     *
     * */
    @GetMapping("/{id}")
    public R<SetmealDto> query(@PathVariable Long id) {
        SetmealDto setmealDto = setmealService.getByIdWithDish(id);
        return R.success(setmealDto);
    }


    /*
    * 修改套餐
    * */

    @PutMapping

    public  R<String> update(@RequestBody SetmealDto setmealDto){

          setmealService.updateWithDish(setmealDto);
          return  R.success("修改套餐成功");
    }


    /*
    * 修改套餐销售状态
    * */
   @PostMapping("/status/{status}")
    public  R<String>  save(@PathVariable int status,String []ids){

       for ( String id:ids) {
           Setmeal setmeal = setmealService.getById(id);
           setmeal.setStatus(status);
           setmealService.updateById(setmeal);
       }
       return R.success("修改销售状态成功");
   }


    /*
     * 删除菜品信息
     * */
    @DeleteMapping

    public R<String> delete(String[] ids){

        int index=0;
        for(String id:ids) {
            Setmeal setmeal = setmealService.getById(id);
            if(setmeal.getStatus()!=1){
                setmealService.removeById(id);
            }else {
                index++;
            }
        }
        if (index>0&&index==ids.length){
            return R.error("选中的套餐均为启售状态，不能删除");
        }else {
            return R.success("删除成功");
        }
    }

    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId()!=null,Setmeal::getCategoryId,setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus()!=null,Setmeal::getStatus,setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setmealService.list(queryWrapper);
        return R.success(list);
    }

}