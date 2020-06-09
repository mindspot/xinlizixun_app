package com.kuangji.paopao.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.kuangji.paopao.Interceptor.LoginInterceptor;

import java.util.ArrayList;
import java.util.List;

/*
 *静态资源不拦截 
 *
 */
@Configuration
public class MvcInterceptorConfig extends WebMvcConfigurationSupport{

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        // 多个拦截器组成一个拦截器链
        // addPathPatterns 用于添加拦截规则，/**表示拦截所有请求
        // excludePathPatterns 用户排除拦截
        List<String> excludeUrls=new ArrayList<>();
        excludeUrls.add("/swagger-ui.html");
        excludeUrls.add("/webjars/**");
        excludeUrls.add("/v2/**");
        excludeUrls.add("/swagger-resources/**");
        excludeUrls.add("/static/**");
        registry.addInterceptor(loginInterceptor).addPathPatterns("/v1/**")
        .excludePathPatterns(excludeUrls);
        super.addInterceptors(registry);
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
       
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

}
