package com.itcast;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2019/6/20.
 */
public class RpcProxyServer {
//    ExecutorService threadPool = Executors.newCachedThreadPool();
//        public void publisher(int port, Object service){
//            ServerSocket serverSocket = null;
//            try{
//                serverSocket = new ServerSocket(port);
//                while(true){
//                    Socket client = serverSocket.accept();
//                    threadPool.execute(new ProcessHandler(client,service));
//
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }finally {
//                if(serverSocket!=null){
//                    try {
//                        serverSocket.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
}
