package com.itheima.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.itheima.reggie.common.R;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.entity.Dish;
import com.itheima.reggie.entity.User;
import com.itheima.reggie.service.AddressService;
import com.itheima.reggie.service.UserService;
import com.sun.prism.impl.BaseContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressService addressService;

    //提交地址
    @PostMapping
    public R<AddressBook> save (@RequestBody AddressBook addressBook, HttpSession session){
       Long userid = (Long) session.getAttribute("user");
        addressBook.setUserId(userid);
        addressBook.setCreateUser1(userid);
        addressBook.setUpdateUser1(userid);
        addressBook.setCreateTime(LocalDateTime.now());
        addressBook.setUpdateTime(LocalDateTime.now());
        log.info("addressBook:{}", addressBook);
        addressService.save(addressBook);
        return R.success(addressBook);
    }

 //获取地址列表
    @GetMapping("/list")
    public  R<List<AddressBook>>  list(AddressBook addressBook,HttpSession session){
       // System.out.println(session.getAttribute("user").toString());
      //   session.getAttribute("user").toString();
      addressBook.setUserId((Long) session.getAttribute("user"));

        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(addressBook.getUserId()!=null,AddressBook::getUserId,addressBook.getUserId());
        queryWrapper.orderByAsc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressService.list(queryWrapper);
        return R.success(list);
    }


/*
* 设置默认地址
*
* */
@PutMapping("/default")
public R<AddressBook> setDefault(@RequestBody AddressBook addressBook,HttpSession session) {
    log.info("addressBook:{}", addressBook);
    LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();

    wrapper.eq(AddressBook::getUserId, (Long)session.getAttribute("user"));
    wrapper.set(AddressBook::getIsDefault, 0);
    //SQL:update address_book set is_default = 0 where user_id = ?
    addressService.update(wrapper);

    addressBook.setIsDefault(1);
    //SQL:update address_book set is_default = 1 where id = ?
   addressService.updateById(addressBook);
   return R.success(addressBook);
}



    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R<AddressBook> getDefault(HttpSession session) {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId,(Long)session.getAttribute("user"));
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /*
    * 修改收货地址
    * */
    @PutMapping()
    public R<AddressBook> Update(@RequestBody AddressBook addressBook) {

        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();


        //SQL:update address_book set is_default = 1 where id = ?
        addressService.updateById(addressBook);
        return R.success(addressBook);

    }


    @DeleteMapping
    public R<String> delete(Long id ){
        addressService.removeById(id);
        return R.success("地址信息删除成功");
    }



}
