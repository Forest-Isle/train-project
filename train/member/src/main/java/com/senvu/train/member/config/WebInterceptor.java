package com.senvu.train.member.config;

import com.senvu.interceptor.LogInterceptor;
import com.senvu.interceptor.MemberInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebInterceptor implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor());
        registry.addInterceptor(new MemberInterceptor()).addPathPatterns("/**").excludePathPatterns("/member/sendCode","/member/login");
    }
}
