package com.itcast;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * Created by Administrator on 2019/6/20.
 */
public class ProcessHandler implements Runnable {
    private Socket client;
    private Map handelerMap;
    public ProcessHandler(Socket client,Map handelerMap) {
        this.client = client;
        this.handelerMap = handelerMap;
    }

    @Override
    public void run() {
        ObjectInputStream objectInputStream = null;
        ObjectOutputStream objectOutputStream  = null;
        try {
            objectInputStream = new ObjectInputStream(client.getInputStream());
            objectOutputStream = new ObjectOutputStream(client.getOutputStream());

            RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
            Object result = invoke(rpcRequest);

            objectOutputStream.writeObject(result);
            objectOutputStream.flush();

        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }finally {
            if(objectInputStream != null){
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(objectOutputStream != null){
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    private Object invoke(RpcRequest request) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //反射调用
        String serviceName=request.getClassName();
        String version=request.getVersion();
        //增加版本号的判断
        if(!StringUtils.isEmpty(version)){
            serviceName+="-"+version;
        }


        Object service = handelerMap.get(serviceName);
        if(service==null){
            throw new RuntimeException("service not found:"+serviceName);
        }
        Object[] args=request.getParameters(); //拿到客户端请求的参数
        Method method=null;
        if(args!=null) {
            Class<?>[] types = new Class[args.length]; //获得每个参数的类型
            for (int i = 0; i < args.length; i++) {
                types[i] = args[i].getClass();
            }
            Class clazz=Class.forName(request.getClassName()); //跟去请求的类进行加载
            method=clazz.getMethod(request.getMethodName(),types); //sayHello, saveUser找到这个类中的方法
        }else{
            Class clazz=Class.forName(request.getClassName()); //跟去请求的类进行加载
            method=clazz.getMethod(request.getMethodName()); //sayHello, saveUser找到这个类中的方法
        }

        Object result = method.invoke(service, args);

        return result;
    }
}
