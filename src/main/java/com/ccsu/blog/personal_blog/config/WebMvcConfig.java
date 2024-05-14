package com.ccsu.blog.personal_blog.config;

import com.ccsu.blog.personal_blog.component.LoginIntercept;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private LoginIntercept loginIntercept;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginIntercept).
                addPathPatterns("/**").
                excludePathPatterns("/user/login").
                excludePathPatterns("/user/register").
                excludePathPatterns("/article/list").
                excludePathPatterns("/tag/list").excludePathPatterns("/user/admin/info").
                excludePathPatterns("/img/download").excludePathPatterns("/user/download/avatar").
                excludePathPatterns("/article/get-one").excludePathPatterns("/user/article/info");
//                excludePathPatterns("/article/list");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 对所有路径生效
                .allowedOrigins("*") //允许所有源地址
//                .allowedOrigins("http://localhost:8080")
//                .allowedOrigins("http://43.129.171.44:80","http://furinafontaine.love")
                // .allowedOrigins("https://mijiupro.com","https://mijiu.com ") // 允许的源地址（数组）
                .allowedMethods("GET", "POST", "PUT", "DELETE","OPTIONS") // 允许的请求方法
                .allowedHeaders("*"); // 允许的请求头
    }



//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//
//        registry.addMapping("/**")  // 对所有的路径都生效
//                .allowedOrigins("http://localhost:8080")  // 允许来自http://localhost:8080的请求
//                .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的HTTP方法
//                .allowedHeaders("Authorization")// 允许的请求头
//                .allowCredentials(false)  // 是否允许发送身份凭证（如cookies）
//                .maxAge(3600);  // 预检请求的有效期
//    }
//        @Override
//        public void addCorsMappings(CorsRegistry registry) {
//            registry.addMapping("/**")
//                    .allowedOrigins("*")
//                    .allowedMethods("*")
//                    .allowedHeaders("*");
//        }

}
