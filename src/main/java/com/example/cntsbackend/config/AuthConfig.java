package com.example.cntsbackend.config;

import com.example.cntsbackend.interceptor.AuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuthConfig implements WebMvcConfigurer {

    @Bean
    public AuthInterceptor initAuthInterceptor(){
        return new AuthInterceptor();
    }

    /**
     * 添加拦截器
     * @param registry 拦截器注册
     *                 addPathPatterns：添加拦截路径
     *                 excludePathPatterns：排除拦截路径
     *                 拦截器的执行顺序是按照添加的顺序执行的
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(initAuthInterceptor())
//                .addPathPatterns("/**")    TODO 为了方便测试，先注释掉，后续需要放开进行拦截
                .addPathPatterns("/general/account_info")
                .addPathPatterns("/general/password")
                .addPathPatterns("/general/phone")
                .addPathPatterns("/general/email")
                .excludePathPatterns("/general/**")
                .excludePathPatterns("/enterprise/info")
                .excludePathPatterns("/thirdParty/info");
    }

}
