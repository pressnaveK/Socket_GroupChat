package com.aiforfly;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat : ");
        String userName = scanner.nextLine();
        Socket socket = new Socket("localhost",4576);
        Client client = new Client(socket,userName);
        client.listenForMessage();
        client.sendMessage();
    }
}

