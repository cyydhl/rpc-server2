package com.itcast;

/**
 * 腾讯课堂搜索 咕泡学院
 * 加群获取视频：608583947
 * 风骚的Michael 老师
 */
@RpcService(value = IHelloService.class,version = "v2.0")
public class HelloServiceImpl2 implements IHelloService{

    @Override
    public String sayHello(String money) {
        System.out.println("【v2.0】request in sayHello:"+money);
        return "【v2.0】Say Hello:"+money;
    }

}
