package edu.cmu.ds;

import java.io.*;
import java.net.*;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class RemoteVariableServerUDP {
    /**
     * This is the server class for the remote variable program using UDP protocol.
     * The server listens for incoming packets from clients,
     * calculates and updates the sum of integer inputs for each client,
     * and sends back the updated sum as a response.
     */
    // The server listens on this port for incoming requests from clients
    final static int SERVER_PORT = 6789;
    // The maximum size of the byte array used for packet transmission
    final static int BUFFER_SIZE = 1000;
    // The current sum of all integer inputs received from clients
    static int sum = 0;
    // The buffer used to store incoming and outgoing packets
    static byte[] buffer = new byte[BUFFER_SIZE];
    // The UDP socket used to listen for incoming packets
    static DatagramSocket serverSocket = null;

    /**
     * The main method that starts the server and listens for incoming packets from clients.
     * @param args
     */
    public static void main(String[] args) {
        // A map to store the sum for each client
        Map<Integer, Integer> sums = new TreeMap<Integer, Integer>();
        try {
            // Create a new scanner to read user input
            Scanner scanner = new Scanner(System.in);
            // Create a new UDP socket to listen for incoming packets
            serverSocket = new DatagramSocket(SERVER_PORT);
            System.out.println("Server started");
            // Listen for incoming packets indefinitely
            while (true) {
                // Create a new packet to store incoming data
                DatagramPacket requestPacket = new DatagramPacket(buffer, BUFFER_SIZE);
                // Wait for incoming packets
                serverSocket.receive(requestPacket);
                // Extract the value from the incoming packet data
                String message = new String(requestPacket.getData(), 0, requestPacket.getLength());
                String[] parts = message.split(",");
                int id = Integer.parseInt(parts[0]);
                String operation = parts[1];
                int value = 0;
                // If the operation is add or subtract, extract the value from the message
                if (operation.equals("add") || operation.equals("subtract")) {
                    value = Integer.parseInt(parts[2]);
                }
                // Calculate the sum
                if (sums.containsKey(id)) {
                    sum = sums.get(id);
                }
                if (operation.equals("add")) {
                    sum += value;
                } else if (operation.equals("subtract")) {
                    sum -= value;
                }
                // Update the map with the new sum
                sums.put(id, sum);
                // Prepare the response
                byte[] responseBuffer;
                responseBuffer = Integer.toString(sum).getBytes();
                DatagramPacket response = new DatagramPacket(responseBuffer, responseBuffer.length,
                        requestPacket.getAddress(), requestPacket.getPort());
                serverSocket.send(response);
                // Print the Tree Map
//                System.out.println("Print the Tree Map");
//                Iterator iter = sums.keySet().iterator();
//                while (iter.hasNext()) {
//                    Object key = iter.next();
//                    Object val = sums.get(key);
//                    System.out.println("[" + key + "," + val + "]");
//                }
//                System.out.println("\n");
                // Print visitor's ID
                System.out.println("Visitor's ID is: " + id);
                // Print the operation requested
                System.out.println("The operation requested is: " + operation);
                // Print the value of the variable
                System.out.println("The value of the variable is: " + sum + "\n");
                sum = 0;
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