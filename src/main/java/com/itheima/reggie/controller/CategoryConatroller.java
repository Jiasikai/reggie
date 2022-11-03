package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.Category;
import com.itheima.reggie.entity.Employee;
import com.itheima.reggie.service.CategoryService;
import com.itheima.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryConatroller {


    @Autowired
    private CategoryService categoryService;


  /*
  * 新增分类
  *
  * */
  @PostMapping
  public  R<String>save (@RequestBody Category category){
      log.info("新增分类，分类信息：{}",category.toString());
      //设置初始密码为123456，进行md5加密处理

      //因为已经使用了mybatis的公共字段自动填充，所有这些字段mybatis会自动填充


//      category.setCreateTime(LocalDateTime.now());
//      category.setUpdateTime(LocalDateTime.now());
//
//      //获取当前登录用户的id
//      Long empId = (Long) request.getSession().getAttribute("employee");
//      category.setCreateUser(empId);
//      category.setUpdateUser(empId);
      //存储员工信息到数据库
      //使用全局异常捕获，如果存储失败，可以进行异常处理
      categoryService.save(category);
      return  R.success("新增分类成功");

  }

    /*
     * 分类分页查询
     * */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("page={},pagesize={},name={}",page,pageSize,name );

        //构造分页构造器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper <Category>queryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Category::getName,name);
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort);
        //执行查询
        categoryService.page(pageInfo,queryWrapper);
        return  R.success(pageInfo);
    }

/*
* 删除分类
* */
@DeleteMapping
public R<String> delete(Long ids){
    log.info("删除分类，id为{}",ids);
  //  categoryService.removeById(ids);
    categoryService.remove(ids);
    return R.success("分类信息删除成功");
}

/*
*
* 修改分类
* */
    @PutMapping
    public  R<String> update (@RequestBody  Category category){
        log.info("修改分类信息：{}",category.toString());
        categoryService.updateById(category);
        return  R.success("修改分类信息成功");

    }
/*
* 菜品页面输出菜品分类
*
* */
    @GetMapping("/list")
    public  R<List<Category>>  list(Category category){
      //条件构造器
        LambdaQueryWrapper<Category> QueryWrapper = new LambdaQueryWrapper<>();
        //添加条件
        QueryWrapper.eq(category.getType()!=null,Category::getType,category.getType());
        QueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(QueryWrapper);
        return  R.success(list);
    }



}
