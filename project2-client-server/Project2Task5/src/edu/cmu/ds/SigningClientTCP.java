package edu.cmu.ds;

import java.math.BigInteger;
import java.net.*;
import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;
import java.security.MessageDigest;

/**
 * implements a client that connects to a server and sends requests
 * to perform arithmetic operations on a variable stored in the server.
 * The client encrypts its requests using a digital signature and sends them to the server.
 * The server verifies the signature and performs the requested operation on the variable.
 * The result is then sent back to the client for display.
 */
public class SigningClientTCP {
    // Initialize variables for input/output streams and key information
    static BufferedReader in;
    static PrintWriter out;
    static BigInteger n, e, d, id;

    public static void main(String args[]) {
        // Initialize variables for user input and key generation
        Socket clientSocket = null;
        Scanner scanner = new Scanner(System.in);

        // Inform user that the client is running and generate a key ID
        System.out.println("The client is running.");
        generateKeyID();

        // Request server port from user
        System.out.print("Please enter server port: \n");
        int serverPort = Integer.parseInt(scanner.nextLine());

        try {
            // Connect to the server using the specified port number
            clientSocket = new Socket("localhost", serverPort);
            String message;

            // Initialize input and output streams
            System.out.println();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            while (true) {
                // Read input from the user
                System.out.println("1. Add a value to your sum.");
                System.out.println("2. Subtract a value from your sum.");
                System.out.println("3. Get your sum.");
                System.out.println("4. Exit client.");
                System.out.print("Please enter your choice: \n");
                int choice = Integer.parseInt(scanner.nextLine());

                // Check if user input is valid and prompt for new input if not
                if (choice < 1 || choice > 4) {
                    System.out.println("Invalid input. Please enter a number between 1 and 4.");
                    continue;
                }

                // If user selects option 4, inform them that the client is quitting and break out of loop
                if (choice == 4) {
                    System.out.println("Client side quitting. The remote variable server is still running.");
                    break;
                }

                // Prompt user for value to add/subtract and build message string
                int value = 0;
                if (choice != 3) {
                    System.out.print("Enter value: \n");
                    value = Integer.parseInt(scanner.nextLine());
                }
                message = id + "," + e + "," + n + ",";

                // Determine operation selected by user and add corresponding information to message string
                if (choice == 1) {
                    message += "add," + value;
                } else if (choice == 2) {
                    message += "subtract," + value;
                } else if (choice == 3) {
                    message += "get,";
                }

                // Sign message and append signature to end of message string
                String encryptedMessage = message + "/" + sign(message);

                // Send message to server
                out.println(encryptedMessage);
                out.flush();

                // Receive response from server and print result
                String data = in.readLine();
                System.out.println("The result is " + data + "\n");
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            // Print exception message if one occurs
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            try {
                // Close the client socket if it is not null
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // Ignore exception on close
            }
        }
    }

    // Generate the public key and private key everytime the client
    public static void generateKeyID(){
        System.out.println("Generating keys");
        Random rnd = new Random();
        // Step 1: Generate two large random primes.
        // We use 400 bits here, but best practice for security is 2048 bits.
        // Change 400 to 2048, recompile, and run the program again, and you will
        // notice it takes much longer to do the math with that many bits.
        BigInteger p = new BigInteger(400, 100, rnd);
        BigInteger q = new BigInteger(400, 100, rnd);

        // Step 2: Compute n by the equation n = p * q.
        n = p.multiply(q);

        // Step 3: Compute phi(n) = (p-1) * (q-1)
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        // Step 4: Select a small odd integer e that is relatively prime to phi(n).
        // By convention the prime 65537 is used as the public exponent.
        e = new BigInteger("65537");

        // Step 5: Compute d as the multiplicative inverse of e modulo phi(n).
        d = e.modInverse(phi);

        String publicKey, privateKey;

        String public_key = "(" + e + "," + n + ")";
        String private_key = "(" + d + "," + n + ")";
        System.out.println("your public key = " + public_key);  // Step 6: (e,n) is the RSA public key
        System.out.println("your private key = " + private_key);  // Step 7: (d,n) is the RSA private key
        // Generate ID
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(public_key.getBytes());
            byte[] hash_value = md.digest();
            byte[] id_byte = new byte[20];
            // copy the last 20 bytes to id_byte
            for(int i = 0; i < 20; i++){
                id_byte[20 - i - 1] = hash_value[hash_value.length - i - 1];
            }
            id = new BigInteger(id_byte);
            System.out.println("Your id is: " + id);
        }
        catch(NoSuchAlgorithmException e) {
            System.out.println("No Hash available" + e);
        }
    }


    static public String sign(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        // compute the digest with SHA-256
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);
        // we only want two bytes of the hash for ShortMessageSign
        // we add a 0 byte as the most significant byte to keep
        // the value to be signed non-negative.
        byte[] messageDigest = new byte[bigDigest.length + 1];
        messageDigest[0] = 0;   // most significant set to 0
        for(int i = 0; i < bigDigest.length; i++){
            messageDigest[i+1] = bigDigest[i]; // take a byte from SHA-256
        }
        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(messageDigest);
        // encrypt the digest with the private key
        BigInteger c = m.modPow(d, n);
        // return this as a big integer string
        return c.toString();
    }
}
