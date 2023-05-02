package edu.cmu.ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

public class RemoteVariableClientUDP {
    /**
     * Client component that sends integer values to the server,
     * receives the sum of those integers computed by the server,
     * and displays the result to the user.
     * The client reads user input from the console,
     * and sends requests to the server by creating a DatagramPacket containing the user input as a string,
     * and sends it to the server at the specified port. The server receives the packet, parses the integer value,
     * computes the sum, and sends it back to the client. The client then receives the response from the server
     * and displays it to the user.
     */
    public static void main(String[] args) {
        // Creates a new Scanner object to read input from the user
        Scanner scanner = new Scanner(System.in);

        int serverPort = 0;
        try {
            System.out.println("The client is running.");
            System.out.print("Please enter server port: \n");
            // Read the server port number from the user input
            serverPort = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number: " + e.getMessage());
            System.exit(1);
        }
        System.out.println(" ");
        // prompts the user to enter the server port number,
        // reads the input from the user, and stores the value in the "serverPort" variable.
        // If the user enters an invalid value, the program prints an error message and exits.
        // Loop until user enters "halt!"
        while (true) {
            try {
                // Read input from the user
                System.out.println("1. Add a value to your sum.");
                System.out.println("2. Subtract a value from your sum.");
                System.out.println("3. Get your sum.");
                System.out.println("4. Exit client.");
                System.out.print("Please enter your choice: \n");
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    continue;
                }
                if (choice == 4) {
                    System.out.println("Client side quitting. The remote variable server is still running.");
                    break;
                }
                int value = 0;
                if (choice != 3) {
                    System.out.print("Enter value: \n");
                    value = Integer.parseInt(scanner.nextLine());
                }
                System.out.print("Enter your ID: \n");
                int id = Integer.parseInt(scanner.nextLine());
                // create message
                String message = id + ",";

                if (choice == 1) {
                    message += "add," + value;
                } else if (choice == 2) {
                    message += "subtract," + value;
                } else if (choice == 3) {
                    message += "get";
                }
                sendMessage(message, serverPort);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        scanner.close();
    }

    /**
     * Send message to server and receive the reply and print.
     * @param message
     * @param serverPort
     * @throws IOException
     */
    public static void sendMessage(String message, int serverPort) throws IOException {
        //  Declare and initialize a DatagramSocket variable
        DatagramSocket clientSocket = null;
        try {
            // Create a new DatagramSocket
            clientSocket = new DatagramSocket();
            // Socket communication with the server
            final int BUFFER_SIZE = 1000;
            // Send message
            byte[] messageBytes = message.getBytes();
            // Get the server address
            InetAddress serverAddress = InetAddress.getByName("localhost");
            // Create a DatagramPacket to send the message
            DatagramPacket request = new DatagramPacket(messageBytes, messageBytes.length, serverAddress, serverPort);
            clientSocket.send(request);
            // receive response
            byte[] buffer = new byte[1000];
            // Create a DatagramPacket to receive the response
            DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(reply);
            // display response
            String response = new String(reply.getData(), 0, reply.getLength());
            System.out.println("The result is " + response + ".\n");
        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (clientSocket != null)
                clientSocket.close();
        }
    }
}
