package com.itheima.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
*登录检查拦截。用拦截器或过滤器来判断用户是否已完成登录
*/
@WebFilter(filterName = "loginCheckFillter",urlPatterns = "/*")//urlPatterns设置拦截路径
@Slf4j

public class LoginCheckFilter implements Filter {
    //用来匹配路径
    public  static  final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        //1.获取本次请求的url
        String requestURI = request.getRequestURI();
        log.info("拦截到请求：{}",requestURI);



        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg", //移动端发送短信
                "/user/login"  //移动端登录
        };

        //2.判断当前请求是否需要处理
        boolean check = check(urls, requestURI);

        //3.如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }

       //4-1.判断后台登录状态，如果已经登录，则直接放行
       if(request.getSession().getAttribute("employee")!=null){
           log.info("用户已登录");
           filterChain.doFilter(request,response);
           return;
       }

        //4-1.判断移动端用户登录状态，如果已经登录，则直接放行
        if(request.getSession().getAttribute("user")!=null){
            log.info("用户已登录");
            filterChain.doFilter(request,response);
            return;
        }

        //5.如果未登录，则返回登录页面
        log.info("用户未登录");
       response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));

    }

    //路径匹配，检查此次请求是否需要放行
    public boolean check(String[] urls,String requestUrl){
        for (String url:urls) {
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if(match){
                return  true;
            }
        }
        return  false;
    }

}
