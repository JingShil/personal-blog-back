package com.ccsu.blog.personal_blog.component;


import com.ccsu.blog.personal_blog.utils.TokenUtil;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginIntercept implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        //1.得到session对象
        String token = request.getHeader("Token");
        TokenUtil tokenUtil = new TokenUtil();
        if (token != null && tokenUtil.validateToken(token)) {
            //说明已经登陆，可以放行
            return true;
        }
        response.setStatus(401);
        // 执行到这行表示未登录，未登录就重定向到到登陆页面
//        response.sendRedirect("/user/login");
        return false;
    }

}
