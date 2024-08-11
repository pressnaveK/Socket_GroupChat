package com.aiforfly;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private final ServerSocket serverSocket;
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    public void startServer(){
       while(!serverSocket.isClosed()){
           try {
               Socket socket = serverSocket.accept();
               System.out.println("A new client is connected!!!");
               ClientHandler clientHandler = new ClientHandler(socket);
               Thread thread = new Thread(clientHandler);
               thread.start();

           } catch (IOException e) {
               closeServer();
           }
       }
    }

    public void closeServer(){
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch (IOException e){
            throw new RuntimeException();
        }
    }



}
