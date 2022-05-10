package com.eleks.academy.whoami.service_client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(888);
        System.out.println("Server starts");

        System.out.println("Waiting for client...");
        Socket accept = serverSocket.accept();
        System.out.println("connected!");
        try (DataInputStream dataInputStream = new DataInputStream(accept.getInputStream());
             DataInputStream dataInputStream1 = new DataInputStream(System.in);
             DataOutputStream dataOutputStream = new DataOutputStream(accept.getOutputStream());) {

            while (true) {
                String str1, str2;
                while ((str1 = dataInputStream.readUTF()) != null) {
                    System.out.println("Client" + str1);
                    str2 = dataInputStream1.readUTF();
                    dataOutputStream.writeUTF(str2);
                }
                System.exit(0);
            }
        }
    }
}
