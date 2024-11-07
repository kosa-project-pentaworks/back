package com.reservation.global.audit;

import com.reservation.global.interceptor.RequestedByInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class RequestedByMvcConfigurer implements WebMvcConfigurer {

    private final RequestedByInterceptor requestedByInterceptor;

    public RequestedByMvcConfigurer(RequestedByInterceptor requestedByInterceptor) {
        this.requestedByInterceptor = requestedByInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(requestedByInterceptor);
    }
}
