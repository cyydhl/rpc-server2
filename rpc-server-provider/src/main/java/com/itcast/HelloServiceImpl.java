package com.itcast;

/**
 * Created by Administrator on 2019/6/20.
 */
@RpcService(value=IHelloService.class)
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String content) {
        System.out.println("request in sayHello:"+content);
        return "Say Hello:"+content;
    }

}
