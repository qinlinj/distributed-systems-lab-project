package edu.cmu.ds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class AddingClientUDP {
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
        // Read input from the user
        BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));

        int serverPort = 0;
        try {
            System.out.println("The client is running.");
            System.out.print("Please enter server port: ");
            // Read the server port number from the user input
            serverPort = Integer.parseInt(inputReader.readLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid port number: " + e.getMessage());
            System.exit(1);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
            System.exit(1);
        }
        System.out.println(" ");
        // Loop until user enters "halt!"
        while (true) {
            try {
                // Read input from the user
                String inputString = inputReader.readLine();

                if (inputString.equals("halt!")) {
                    System.out.println("Client side quitting.");
                    break;
                }
                // Convert user input to integer
                int inputInt = Integer.parseInt(inputString);
                // Send request to server and receive response
                int sum = add(inputInt, serverPort);
                System.out.println("The server returned " + sum + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input: " + e.getMessage());
            } catch (IOException e) {
                System.out.println("IO Exception: " + e.getMessage());
            }
        }
    }

    public static int add(int i, int serverPort) throws IOException {
        // Method to encapsulate the socket communication with the server
        final int BUFFER_SIZE = 1000;
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        // Send request to server
        String requestString = Integer.toString(i);
        DatagramPacket requestPacket = new DatagramPacket(requestString.getBytes(), requestString.getBytes().length, serverAddress, serverPort);
        clientSocket.send(requestPacket);
        // Receive response from server
        DatagramPacket responsePacket = new DatagramPacket(buffer, BUFFER_SIZE);
        clientSocket.receive(responsePacket);
        String responseString = new String(responsePacket.getData(), 0, responsePacket.getLength());
        int sum = Integer.parseInt(responseString);
        // Close the socket and return the sum
        clientSocket.close();
        return sum;
    }
}
