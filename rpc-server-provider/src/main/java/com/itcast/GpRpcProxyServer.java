package com.itcast;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2019/6/20.
 */
public class GpRpcProxyServer implements ApplicationContextAware,InitializingBean{
    private Map<String,Object> handlerMap = new HashMap<>();
    private int port;
    public GpRpcProxyServer(int port){
        this.port = port;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        ExecutorService threadPool = Executors.newCachedThreadPool();
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(port);
            while(true){
                Socket client = serverSocket.accept();
                threadPool.execute(new ProcessHandler(client,handlerMap));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket!=null){
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        if(!serviceMap.isEmpty()){
            for(Object serviceBean : serviceMap.values()){
                RpcService annotation = serviceBean.getClass().getAnnotation(RpcService.class);
                String serviceName = annotation.value().getName();

                handlerMap.put(serviceName,serviceBean);
            }
        }


    }

}
