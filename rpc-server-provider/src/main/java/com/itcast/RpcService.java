package com.itcast;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 自定义注解
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {

    Class<?> value(); //拿到服务的接口

    /**
     * 版本号
     */
    String version() default "";
}
