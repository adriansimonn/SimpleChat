package com.jetbrains;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.net.ServerSocket;

public class Server extends Thread {
    private Socket socket;
    private ArrayList<Server> threadList;
    private PrintWriter output;

    public Server(Socket socket, ArrayList<Server> threads) {
        this.socket = socket;
        this.threadList = threads;
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true);

            while(true) {
                String outputString = input.readLine();
                if(outputString.equals(".LEAVE")) {
                    break;
                }
                printToALlClients(outputString);
                System.out.println(outputString);
            }
        } catch (Exception e) {
        }
    }

    private void printToALlClients(String outputString) {
        for(Server sT: threadList) {
            sT.output.println(outputString);
        }
    }

    public static void main(String[] args) {
        ArrayList<Server> threadList = new ArrayList<>();
        try (ServerSocket serversocket = new ServerSocket(5000)){
            while(true) {
                Socket socket = serversocket.accept();
                Server serverThread = new Server(socket, threadList);
                threadList.add(serverThread);
                serverThread.start();
            }
        } catch (Exception e) {
        }
    }
}