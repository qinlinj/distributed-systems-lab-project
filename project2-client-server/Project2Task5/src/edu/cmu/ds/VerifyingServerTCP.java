package edu.cmu.ds;

import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

/**
 * implements a simple server that performs addition and subtraction operations on numbers.
 * It listens on a port, accepts TCP connections from clients,
 * and performs the requested operation on a shared variable.
 * The server uses public-key encryption to ensure that requests are authorized.
 */
public class VerifyingServerTCP {
    static BigInteger n, e, id; // server's public key and client's ID
    static String operation; // operation requested by the client
    static Map<BigInteger, Integer> sums = new TreeMap<>(); // shared variable storing visitor's sum
    static PrintWriter out; // output stream to the client
    static Scanner in; // input stream from the client

    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            int serverPort = 7777;
            System.out.println("Server Running"); // indicate start of the server
            ServerSocket listenSocket = new ServerSocket(serverPort);
            clientSocket = listenSocket.accept(); // accept the first client connection
            in = new Scanner(clientSocket.getInputStream());
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            while (true) {
                String data;
                if(in.hasNextLine()){
                    // read the client's message
                    data = in.nextLine();
                    // parse the message, which is in the format "id,e,n,operation,number/signature"
                    String[] message = data.split("/");
                    String[] parts = message[0].split(",");
                    id = new BigInteger(parts[0]);
                    e = new BigInteger(parts[1]);
                    n = new BigInteger(parts[2]);
                    operation = parts[3];
                    int number = 0;
                    try{
                        if (parts[4].length() != 0) {
                            number = Integer.parseInt(parts[4]);
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                    // verify the signature of the message using the client's public key
                    if(!idMatch(message[0], message[1])){
                        // if the signature is invalid, terminate the connection
                        out.println("Request encryption error");
                        out.flush();
                        break;
                    }
                    // perform the requested operation on the shared variable
                    add(operation, id, number);
                }
                else {
                    // if there is no more data from the client, accept the next client connection
                    clientSocket = listenSocket.accept();
                    in = new Scanner(clientSocket.getInputStream());
                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            // close the connection when the client terminates
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }

    /**
     * Adds, subtracts, or gets the sum of the visitor identified by the given ID.
     * @param operation the operation to perform (add, subtract, or get)
     * @param id the ID of the visitor
     * @param number the number to add or subtract (ignored for get)
     * @return the current sum of the visitor
     */
    public static int add(String operation, BigInteger id, int number){
        System.out.println("Visitor's ID is: " + id);
        System.out.println("The operation requested is:" + operation);
        // get the current sum of the visitor
        int value = sums.getOrDefault(id,0);
        // update the sum based on the requested operation
        if(operation.equals("add")){
            sums.put(id, value + number);
        }
        else if (operation.equals("subtract")){
            sums.put(id, value - number);
        } else if (operation.equals("get")) {
            sums.put(id, value);
        }
        value = sums.getOrDefault(id,0);
        System.out.println("The value of the variable is: " + value +"\n");
        // return the number after calculation
        out.println(value);
        out.flush();
        return sums.getOrDefault(id,0);
    }

    public static boolean idMatch(String messageToCheck, String encryptedHashStr) {
        BigInteger decryptedHash = null;
        BigInteger bigIntegerToCheck = null;
        try{
            // Decrypt it
            decryptedHash = new BigInteger(encryptedHashStr).modPow(e, n);
            // Get the bytes from messageToCheck
            byte[] bytesOfMessageToCheck = messageToCheck.getBytes("UTF-8");
            // compute the digest of the message with SHA-256
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] bigDigest = md.digest(bytesOfMessageToCheck);

            // messageToCheckDigest is a full SHA-256 digest
            // add a zero byte in front of bigDigest
            byte[] messageToCheckDigest  = new byte[bigDigest.length + 1];
            messageToCheckDigest [0] = 0;   // most significant set to 0
            for(int i = 0; i < bigDigest.length; i++){
                messageToCheckDigest [i+1] = bigDigest[i];
            }
            bigIntegerToCheck = new BigInteger(messageToCheckDigest);
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Client hash message: " + decryptedHash);
        System.out.println("Hash value of the message:" + bigIntegerToCheck);
        System.out.println("Verify result: " + bigIntegerToCheck.equals(decryptedHash));
        return bigIntegerToCheck.equals(decryptedHash);
    }
}
