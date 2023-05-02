package edu.cmu.ds;
import com.google.gson.Gson;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.net.*;
import java.io.*;
import java.util.Scanner;

/**
 This class is responsible for sending requests to the blockchain server and printing responses to the console.
 */
public class RequestMessage {
    // Declare necessary variables
    static Socket clientSocket;
    static int serverPort = 7777;
    static BufferedReader in;
    static PrintWriter out;
    static Scanner readInput = new Scanner(System.in);
    static boolean finish = false;
    static JSONObject json = new JSONObject();

    /**
     This method initializes a connection with the blockchain server and prompts the user to enter a selection until
     they choose to exit.
     @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Connect to the server
            clientSocket = new Socket("localhost", serverPort);
            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            do {
                // Prompt the user for a selection and send it to the server
                int option = getSelection();
                if (finish) break;
                pass(option);
            } while (true);
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
        } finally {
            // Close the connection with the server
            try {
                if (clientSocket != null) clientSocket.close();
            } catch (IOException e) {
                // Ignored
            }
        }
    }

    /**
     This method prompts the user for a selection and returns the integer value of the selection.
     @return an integer representing the user's selection
     */
    public static int getSelection() {
        // Declare an array of messages to display to the user for each selection option
        String[] message = {"View basic blockchain status.", "Add a transaction to the blockchain.",
                "Verify the blockchain.", "View the blockchain.", "Corrupt the chain.",
                "Hide the corruption by repairing the chain.", "Exit."};
        // Clear the JSON object
        json.clear();

        // Display the selection options to the user
        for (int i = 0; i < message.length; i++) {
            System.out.println(i + ". " + message[i]);
        }

        // Read the user's selection and add the appropriate data to the JSON object
        int option = Integer.parseInt(readInput.nextLine());
        switch (option) {
            case 0:
                addSelectionToJSON(option);
                break;
            case 1:
                addTransactionDataToJSON(option);
                break;
            case 2:
            case 3:
                addSelectionToJSON(option);
                break;
            case 4:
                addCorruptDataToJSON(option);
                break;
            case 5:
                addSelectionToJSON(option);
                break;
            case 6:
                finish = true;
                break;
            default:
                break;
        }
        return option;
    }

    /**
     This method sends the user's selection to the server and waits for a response. It then handles the response
     appropriately based on the user's original selection.
     @param option an integer representing the user's selection
     */
    public static void pass(int option) {
        try {
            // Initialize input and output streams
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
            // Send the JSON object to the server
            out.println(json.toJSONString());
            out.flush();

            // Receive the server's response as a JSON object
            json = (JSONObject) JSONValue.parse(in.readLine());

            // Handle the server's response based on the user's selection
            handleServerResponse(option);
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        }
    }

    /**

     This method adds the user's selection to the JSON object.
     @param option an integer representing the user's selection
     */
    private static void addSelectionToJSON(int option) {
        json.put("selection", option);
    }

    /**
     This method adds the user's transaction data to the JSON object.
     @param option an integer representing the user's selection
     */
    private static void addTransactionDataToJSON(int option) {
        // Prompt the user for the transaction data and difficulty
        System.out.println("Enter difficulty > 0");
        int difficulty = Integer.parseInt(readInput.nextLine());
        System.out.println("Enter transaction");
        String data = readInput.nextLine();

        // Add the transaction data and difficulty to the JSON object
        json.put("selection", option);
        json.put("difficulty", difficulty);
        json.put("data", data);
    }

    /**
     This method adds the user's corrupt data to the JSON object.
     @param option an integer representing the user's selection
     */
    private static void addCorruptDataToJSON(int option) {
        // Prompt the user for the block index and new corrupt data
        System.out.println("Enter block ID of block to corrupt");
        int index = Integer.parseInt(readInput.nextLine());
        System.out.println("Enter new data for block " + index);
        String corruptMessage = readInput.nextLine();

        // Add the block index and new corrupt data to the JSON object
        json.put("selection", option);
        json.put("index", index);
        json.put("data", corruptMessage);
    }

    /**
     This method handles the server's response based on the user's selection.
     @param option an integer representing the user's selection
     */
    private static void handleServerResponse(int option) {
        switch (option) {
            case 0:
                printBlockchainStatus();
                break;
            case 1:
            case 4:
            case 5:
                printResponseMessage();
                break;
            case 2:
                printVerificationResult();
                break;
            case 3:
                printBlockchain();
                break;
        }
    }

    /**
     This method prints the blockchain status to the console.
     */
    private static void printBlockchainStatus() {
        System.out.println("Current size of chain: " + ((Long) json.get("size")).intValue());
        System.out.println("Difficulty of most recent block: " + ((Long) json.get("diff")).intValue());
        System.out.println("Total difficulty for all blocks: " + ((Long) json.get("totalDiff")).intValue());
        System.out.println("Approximate hashes per second on this machine: " + ((Long) json.get("hps")).intValue());
        System.out.println("Expected total hashes required for the whole chain: " + (double) json.get("totalHashes"));
        System.out.println("Nonce for most recent block: " + ((Long) json.get("recentNonce")).intValue());
        System.out.println("Chain hash: " + json.get("chainHash"));
    }

    /**
     This method prints the server's response message to the console.
     */
    private static void printResponseMessage() {
        System.out.println(json.get("response"));
    }

    /**
     This method prints the verification result to the console.
     */
    private static void printVerificationResult() {
        System.out.println("Chain verification: " + json.get("verification"));
        if (json.get("verification").equals("False")) {
            System.out.println(json.get("errorMessage"));
        }
        printResponseMessage();
    }

    /**
     This method prints the blockchain to the console.
     */
    private static void printBlockchain() {
        System.out.println("View the Blockchain");
        printResponseMessage();
    }
}
