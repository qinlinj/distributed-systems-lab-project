package edu.cmu.ds;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class AddingServerUDP {
    /**
     * The main method that creates a server socket, listens for incoming requests,
     * updates the sum with the integer
     * input from the clients, and returns the updated sum to the clients.
     */
    public static void main(String[] args) {
        // The server listens on this port for incoming requests from clients
        final int SERVER_PORT = 6789;
        // The maximum size of the byte array used for packet transmission
        final int BUFFER_SIZE = 1000;
        // The current sum of all integer inputs received from clients
        int sum = 0;
        // The buffer used to store incoming and outgoing packets
        byte[] buffer = new byte[BUFFER_SIZE];
        // The UDP socket used to listen for incoming packets
        DatagramSocket serverSocket = null;

        try {
            // Create a new DatagramSocket and bind it to the specified port
            serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("Server started");

            while (true) {
                // Create a new packet to store incoming data
                DatagramPacket requestPacket = new DatagramPacket(buffer, BUFFER_SIZE);
                // Wait for incoming packets
                serverSocket.receive(requestPacket);
                // Extract the integer value from the incoming packet data
                String requestString = new String(requestPacket.getData(), 0, requestPacket.getLength());
                int requestInt = Integer.parseInt(requestString);
                System.out.println("Adding: " + requestInt + " to " + sum);
                // Update the sum with the incoming integer value
                sum += requestInt;
                // Convert the updated sum to a string and create a new packet to send the sum back to the client
                String responseString = Integer.toString(sum);
                DatagramPacket responsePacket = new DatagramPacket(responseString.getBytes(),
                        responseString.getBytes().length, requestPacket.getAddress(), requestPacket.getPort());
                // Send the packet containing the updated sum to the client
                serverSocket.send(responsePacket);
                System.out.println("Returning sum of " + sum + " to client");
                System.out.println(" ");
            }
        } catch (SocketException e) {
            System.out.println("Socket Exception: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            if (serverSocket != null) {
                serverSocket.close();
            }
        }
    }
}

