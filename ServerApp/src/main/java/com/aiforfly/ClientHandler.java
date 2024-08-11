package com.aiforfly;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUserName;
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            /* sockets emits byte stream
                     to convert byte streams into character stream should user Input or Output stream Reader or Writer
            */
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUserName = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("Server: " + clientUserName + " has entered the chat!");
        }catch(IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while(socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                if(messageFromClient != null){
                    broadcastMessage(messageFromClient);
                }else{
                    closeEverything(socket,bufferedReader,bufferedWriter);
                    break;
                }

            }catch(IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }

    }
    public void broadcastMessage(String messageToSend){
            try{
                for(ClientHandler clientHandler : clientHandlers) {
                    if (!clientHandler.clientUserName.equals(clientUserName)) {
                        clientHandler.bufferedWriter.write(messageToSend);
                        clientHandler.bufferedWriter.newLine();
                        clientHandler.bufferedWriter.flush();
                    }
                }

            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);

            }

    }
    public void removeClientHandler(){
        clientHandlers.remove(this);
        broadcastMessage("Server : " + clientUserName + " has left the chat!!");
        System.out.println("Server : " + clientUserName + " has left the chat!!");
    }

    public void closeEverything(Socket socket , BufferedReader bufferedReader , BufferedWriter bufferedWriter){
        removeClientHandler();
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }if(bufferedWriter != null){
                bufferedWriter.close();
            }if(socket != null){
                socket.close();
            }
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
