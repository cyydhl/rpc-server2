package com.itcast;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * Hello world!
 *
 *
 */
public class App 
{
    public static void main( String[] args )
    {
//        IHelloService service = new HelloServiceImpl();
//        RpcProxyServer server = new RpcProxyServer();
//        server.publisher(8080,service);
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringConfig.class);
        context.start();
    }
}
