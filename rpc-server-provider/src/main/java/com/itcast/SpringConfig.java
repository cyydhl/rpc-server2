package com.itcast;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 定义spring容器启动类
 */
@Configuration
@ComponentScan("com.itcast")
public class SpringConfig {

    @Bean(name="gpRpcProxyServer")
    public GpRpcServer gpRpcProxyServer(){
//        return new GpRpcProxyServer(8080);

        return new GpRpcServer(8080);
    }
}
