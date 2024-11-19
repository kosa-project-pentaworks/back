//package com.reservation.global.config;
//
//import feign.Logger;
//import feign.RequestInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//@Configuration
//public class FeignClientConfig {
//    @Bean
//    Logger.Level feignLoggerLevel(){
//        return Logger.Level.FULL;
//    }
//
//    @Bean
//    public RequestInterceptor requestInterceptor(){
//        return requestTemplate -> {
//            requestTemplate.header("Content-Type", "application/json");
//            requestTemplate.header("Accept", "application/json");
//        };
//    }
//}
