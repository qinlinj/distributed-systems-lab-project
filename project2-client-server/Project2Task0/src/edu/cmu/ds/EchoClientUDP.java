package edu.cmu.ds;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoClientUDP{
    /**
     * Initialize the DatagramSocket
     * This is a Java program for a UDP client that sends messages to a server and receives echoed messages back.
     * The program takes input from the user, sends it to the server on a specified port number,
     * receives a reply from the server, and prints the reply to the console.
     * @param args
     */
    public static void main(String args[]){
        // args give message contents and server hostname
        // Here we initialize the DatagramSocket
        System.out.println("The UDP client is running.");
        DatagramSocket aSocket = null;
        try {
            // Initialize the InetAddress with the hostname "localhost"
            InetAddress aHost = InetAddress.getByName("localhost");
            // Assign the server port to the variable "serverPort"
            // Creates a new scanner object that reads from System.in
            Scanner scanner = new Scanner(System.in);
            // Print Tips.
            System.out.print("Enter server port number: ");
            // Set portNum
            int serverPort = scanner.nextInt();
            // Initialize the DatagramSocket object "aSocket"
            aSocket = new DatagramSocket();
            System.out.println("Connect the server in port number: " + serverPort);
            // Initialize the BufferedReader "typed" to read from standard input
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            // Start a loop that runs until the user stops entering input
            String nextLine;
            while ((nextLine = typed.readLine()) != null) {
                // Convert the input string to a byte array
                byte [] m = nextLine.getBytes();
                // Create a DatagramPacket "request" with the message and the server address/port
                DatagramPacket request = new DatagramPacket(m, m.length, aHost, serverPort);
                // Send the request packet using the DatagramSocket "aSocket"
                aSocket.send(request);
                // Initialize a buffer to receive the reply
                byte[] buffer = new byte[1000];
                // Create a DatagramPacket "reply" to hold the reply
                DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
                // Receive the reply packet using the DatagramSocket "aSocket"
                aSocket.receive(reply);
                // Get requestString data
                String requestString = new String(reply.getData(), 0, reply.getLength());
                // Check if string equals "halt!"
                if (requestString.equals("halt!")) {
                    // Generate datagramPacket response
                    DatagramPacket response = new DatagramPacket(request.getData(), request.getLength(), request.getAddress(),request.getPort());
                    // Send response
                    aSocket.send(response);
                    // Print reminder
                    System.out.println("UDP Client side quitting");
                    break;
                }
                // Set the string in correct length
                String replyString = new String(reply.getData(), 0, reply.getLength());
                // Print the reply message to the console
                System.out.println("Reply from server: " +  replyString);
            }
        // Catch a SocketException if one occurs and print the error message to the console
        }catch (SocketException e) {System.out.println("Socket Exception: " + e.getMessage());
        // Catch an IOException if one occurs and print the error message to the console
        }catch (IOException e){System.out.println("IO Exception: " + e.getMessage());
        // Finally, close the DatagramSocket if it is not null
        }finally {if(aSocket != null) aSocket.close();}
    }
}
