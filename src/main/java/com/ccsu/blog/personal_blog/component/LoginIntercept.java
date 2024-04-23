package com.ccsu.blog.personal_blog.component;


import com.ccsu.blog.personal_blog.utils.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class LoginIntercept implements HandlerInterceptor {
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

//        return true;
        if (HttpMethod.OPTIONS.toString().equals(request.getMethod())){
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }
        //1.得到session对象
        String token = request.getHeader("Token");
        String userId = request.getHeader("UserId");
        String key = "blog:token:" + userId;
        Long ttl = redisTemplate.getExpire(key);

        if (ttl != null && ttl == -1) {
            // Key exists and is expired
            redisTemplate.delete(key);

        } else if (ttl != null && ttl == -2) {

        } else {
            String tokenRedis = redisTemplate.opsForValue().get(key);
            if(tokenRedis!=null){
                if(tokenRedis.equals(token)){
                    return true;
                }
            }
        }

//        TokenUtil tokenUtil = new TokenUtil();
//        if (token != null && tokenUtil.validateToken(token)) {
//            //说明已经登陆，可以放行
//            return true;
//        }

//        response.sendError(2000);
        response.setStatus(401);//设置状态码为401
        // 执行到这行表示未登录，未登录就重定向到到登陆页面
//        response.sendRedirect("/user/login");
        return false;
    }

}
