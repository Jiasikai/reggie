package com.itheima.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/*
* mybatis的公共字段自动填充
*
* */
@Slf4j
@Component
public class MyMetaObjecthandler implements MetaObjectHandler {
  @Autowired
  private HttpServletRequest request;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充【insert】。。。");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        Long empId = (Long) request.getSession().getAttribute("employee");

        metaObject.setValue("createUser",empId);
        metaObject.setValue("updateUser",empId);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充【update】。。。");
        log.info(metaObject.toString());
        Long empId = (Long) request.getSession().getAttribute("employee");
        metaObject.setValue("updateTime",LocalDateTime.now());
        metaObject.setValue("updateUser",empId);
    }
}
