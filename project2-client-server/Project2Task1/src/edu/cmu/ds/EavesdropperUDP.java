package edu.cmu.ds;

import java.net.*;
import java.io.*;

public class EavesdropperUDP {
    /**
     * Acts as a passive malicious player,
     * eavesdropping on the conversation between the client and server,
     * and forwarding the messages without any modification.
     * @param args
     */
    public static void main(String[] args) {
        // Defining and initializing the constants for the masquerading port, server port and buffer size.
        final int MASQUERADING_PORT = 6789;
        final int SERVER_PORT = 6798;
        final int BUFFER_SIZE = 1000;
        // Defining the buffer of byte type and initializing the server and client sockets to null.
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramSocket serverSocket = null;
        DatagramSocket clientSocket = null;
        // Initializing the shouldHalt flag to false.
        boolean shouldHalt = false;
        try {
            // Creating a new DatagramSocket object for the server socket and client socket.
            serverSocket = new DatagramSocket(SERVER_PORT);
            clientSocket = new DatagramSocket();
            // Printing the server port number to the console.
            System.out.println("The server is listening on port number: " + SERVER_PORT);
            // Running the while loop until shouldHalt flag is set to true.
            while (true) {
                // Creating a new DatagramPacket object to receive the request from the client.
                DatagramPacket requestPacket = new DatagramPacket(buffer, BUFFER_SIZE);
                serverSocket.receive(requestPacket);
                // Converting the byte array received to string.
                String requestString = new String(requestPacket.getData(), 0, requestPacket.getLength());
                // Printing the received message to the console.
                System.out.println("Received message from client: " + requestString);
                // Checking if the received message is "halt!".
                if (!requestString.equals("halt!")) {
                    // Appending "!" to the received message.
                    requestString += "!";
                }
                // Creating a new InetAddress object for the client address.
                InetAddress clientAddress = InetAddress.getByName("localhost");
                // Creating a new DatagramPacket object to send the modified request to the masquerading port.
                DatagramPacket modifiedPacket = new DatagramPacket(requestString.getBytes(),
                        requestString.getBytes().length, clientAddress, MASQUERADING_PORT);
                clientSocket.send(modifiedPacket);
                System.out.println("Sent message to server: " + requestString);
                // Creating a new DatagramPacket object to receive the response from the server.
                DatagramPacket responsePacket = new DatagramPacket(buffer, BUFFER_SIZE);
                clientSocket.receive(responsePacket);
                // Converting the byte array received to string.
                String responseString = new String(responsePacket.getData(), 0, responsePacket.getLength());
                // Printing the response from the server to the console.
                System.out.println("Received response from server: " + responseString);
                // Removing the last character "!" from the response string.
                if (!requestString.equals("halt!")) {
                    responseString = responseString.substring(0, responseString.length() - 1);
                }
                // Creating a new DatagramPacket object to send the response back to the client.
                DatagramPacket replyPacket = new DatagramPacket(responseString.getBytes(),
                        responseString.getBytes().length, requestPacket.getAddress(), requestPacket.getPort());
                serverSocket.send(replyPacket);
                System.out.println("Sent message to client: " + responseString);

            }
        } catch (SocketException e) {

            // Catching and printing the SocketException to the console.
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {

            // Catching and printing the IOException to the console.
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
            if (clientSocket != null) {
                clientSocket.close();
            }
        }
    }
}
