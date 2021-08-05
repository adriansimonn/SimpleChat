package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.util.Date;
import java.text.*;

public class Client extends Thread {
    private Socket socket;
    private BufferedReader input;

    public Client(Socket s) throws IOException {
        this.socket = s;
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        try {
            while(true) {
                String response = input.readLine();
                System.out.println(response);
            }
        } catch (IOException e) {

        } finally {
            try {
                input.close();
                System.out.println("You have left the server");
            } catch (Exception e) {
            }
        }
    }
    public static void main(String[] args) {
        String helpDialogue = "SIMPLECHAT HELP MENU\n" +
                "COMMANDS: (ALL COMMANDS ARE CASE SENSITIVE AND BEGIN WITH A \'.\')\n" +
                ".HELP = Displays this menu\n" +
                ".HELPALL = Displays this menu to all users online\n" +
                ".LEAVE = Disables your client and disconnects from the server\n" +
                ".MYNAME = Displays your current name in the server\n" +
                ".CHANGENAME = Changes your name, you will be prompted to change your name to whatever you type after the command\n" +
                ".DATETIME = Displays current date and time (Does not live update)";

        System.out.println("WELCOME TO SIMPLECHAT v1");
        System.out.println("Developed by Adrian Simon");
        try (Socket socket = new Socket("localhost", 5000)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));

            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            Scanner scanner = new Scanner(System.in);
            String userInput;
            String response;
            String clientName = "";

            Client clientRun = new Client(socket);

            new Thread(clientRun).start();

            do {
                if (clientName.equals("")) {
                    System.out.print("Enter your name: ");
                    userInput = scanner.nextLine();
                    System.out.println();
                    clientName = userInput;
                    output.println(userInput + " has joined the server");
                    if (userInput.equals(".LEAVE")) {
                        break;
                    }
                }
                else {
                    String head = ( "[" + clientName + "]" );
                    userInput = scanner.nextLine();
                    if (userInput.equals(".LEAVE")) {
                        //reading the input from server
                        output.println(clientName + " has left the server");
                        break;
                    }
                    output.println(head + " " + userInput);
                    if (userInput.equals(".HELP") || userInput.equalsIgnoreCase("help")) {
                        System.out.println(helpDialogue);
                    }
                    if (userInput.equals(".MYNAME")) {
                        System.out.println("Your name is: " + clientName);
                    }
                    if (userInput.equals(".CHANGENAME")) {
                        System.out.print("Enter your new name: ");
                        clientName = scanner.nextLine();
                        System.out.println("Successfully changed your name to: " + clientName);
                    }
                    if (userInput.equals(".DATETIME")) {
                        Date dt = new Date();
                        SimpleDateFormat fdt = new SimpleDateFormat("'Date: 'E, MM / dd / yyyy ' Time: 'HH:mm:ss z");
                        System.out.println(fdt.format(dt));
                    }
                }
            } while (!userInput.equals(".LEAVE"));
        } catch (Exception e) {
        }
    }
}