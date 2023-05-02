package edu.cmu.ds;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoServerUDP{
    /**
     * this code sets up a simple UDP server that listens for incoming packets on port 6789,
     * echoes the contents of each packet back to the client,
     * and prints the contents of each packet to the console.
     * The server runs in an infinite loop until it is terminated.
     * @param args
     */
    public static void main(String args[]){
        System.out.println("The UDP server is running.");
        DatagramSocket aSocket = null;
        byte[] buffer = new byte[1000];
        try{
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter server port number: ");
            int serverPort = scanner.nextInt();
            System.out.println("The server is listen on the port number: " + serverPort);
            aSocket = new DatagramSocket(serverPort);
            // create a new DatagramPacket object for incoming requests
            DatagramPacket request = new DatagramPacket(buffer, buffer.length);
            // Set a variable to control the while loop
            Boolean halt = false;
            while(!halt){
                // wait for incoming requests
                aSocket.receive(request);
                // Initialize a requestString message
                String requestString = new String(request.getData(), 0, request.getLength());
                // If String is "halt!"
                if (requestString.equals("halt!")) {
                    // Generate datagramPacket response
                    DatagramPacket response = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),request.getPort());
                    // Send response
                    aSocket.send(response);
                    // Try to receive the "halt!" message
                    aSocket.receive(request);
                    String haltString = new String(request.getData(), 0, request.getLength());
                    System.out.println(haltString);
                    if (haltString.equals("halt!")) {
                        aSocket.send(response);
                        // Stop while loop
                        halt = true;
                    }
                } else {
                    // create a new DatagramPacket object for the reply
                    DatagramPacket reply = new DatagramPacket(requestString.getBytes(),
                            requestString.getBytes().length, request.getAddress(), request.getPort());
                    // print the request string to the console
                    System.out.println("Echoing: " + requestString);
                    // send the reply to the client
                    aSocket.send(reply);
                }
            }
            System.out.println("UDP Server side quitting");
            // Catch a SocketException if one occurs and print the error message to the console
        }catch (SocketException e) {System.out.println("Socket Exception: " + e.getMessage());
            // Catch an IOException if one occurs and print the error message to the console
        }catch (IOException e){System.out.println("IO Exception: " + e.getMessage());
            // Finally, close the DatagramSocket if it is not null
        }finally {if(aSocket != null) aSocket.close();}
    }
}
