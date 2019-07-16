package com.itcast;
import com.itcast.Registry.IRegistryCenter;
import com.itcast.Registry.RegistryCenterWithZk;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2019/6/20.
 */
public class GpRpcServer implements ApplicationContextAware,InitializingBean{
    private Map<String,Object> handlerMap = new HashMap<>();
    private int port;
    private IRegistryCenter registryCenter=new RegistryCenterWithZk();

    public GpRpcServer(int port){
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
                //拿到注解
                RpcService rpcService=serviceBean.getClass().getAnnotation((RpcService.class));
                String serviceName=rpcService.value().getName();//拿到接口类定义
                String version=rpcService.version(); //拿到版本号
                if(!StringUtils.isEmpty(version)){
                    serviceName+="-"+version;
                }

                handlerMap.put(serviceName,serviceBean);
                registryCenter.registry(serviceName,getAddress()+":"+port);
            }
        }


    }

    private static String getAddress(){
        InetAddress inetAddress=null;
        try {
            inetAddress=InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return inetAddress.getHostAddress();// 获得本机的ip地址
    }

}
