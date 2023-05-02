package edu.cmu.ds;

import java.net.*;
import java.io.*;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 *  This is the server class for the remote variable program using UDP protocol.
 *  the server listens for incoming packets from clients,
 *  calculates and updates the sum of integer inputs for each client,
 *  and sends back the updated sum as a response.
 */

public class EchoServerTCP {
    public static void main(String args[]) {
        System.out.println("Server Running"); //indicate start of the server
        Socket clientSocket = null;
        Map<Integer, Integer> sums = new TreeMap<Integer, Integer>();
        try {
            int serverPort = 7777; // the server port we are using
            int sum = 0;
            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);

            /*
             * Forever,
             *   wait for a new connection
             *   read a line from the socket
             */
            while (true) {
                /*
                 * Block waiting for a new connection request from a client.
                 * When the request is received, "accept" it, and the rest
                 * the tcp protocol handshake will then take place, making
                 * the socket ready for reading and writing.
                 */
                clientSocket = listenSocket.accept();
                // If we get here, then we are now connected to a client.

                // Set up "in" to read from the client socket
                Scanner in = new Scanner(clientSocket.getInputStream());
                // Set up "out" to write to the client socket
                while (in.hasNextLine()) {
                    String message = in.nextLine();
                    //retrieve id, operation, val from byte arr
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
                    // Print visitor's ID
                    System.out.println("Visitor's ID is: " + id);
                    // Print the operation requested
                    System.out.println("The operation requested is: " + operation);
                    // Print the value of the variable
                    System.out.println("The value of the variable is: " + sum + "\n");
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                    out.println(sum);
                    out.flush();
                    sum = 0;
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }
}
