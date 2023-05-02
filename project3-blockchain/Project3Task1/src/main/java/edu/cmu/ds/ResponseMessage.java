package edu.cmu.ds;

import com.google.gson.Gson;
import java.net.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
/**
 A class to handle responses to client requests in a blockchain server
 */
public class ResponseMessage {
    // Static variables to be shared across instances of ResponseMessage
    static Socket clientSocket = null;
    static int serverPort = 7777;
    static JSONObject listenJson = new JSONObject();
    static JSONObject returnJson = new JSONObject();
    static BlockChain bc;
    /**
     The main method to start the blockchain server and handle client requests
     @param args Command line arguments (not used)
     */
    public static void main(String[] args) {
        initializeBlockChain();
        System.out.println("Blockchain server running");
        try (ServerSocket listenSocket = new ServerSocket(serverPort)) {
            while (true) {
                clientSocket = listenSocket.accept();
                handleClientRequest();
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            closeClientSocket();
        }
    }
    /**
     A helper method to initialize the blockchain
     */
    private static void initializeBlockChain() {
        bc = new BlockChain();
        Block b = new Block(bc.getChainSize(), bc.getCurrentTime(), "Genesis", 2);
        bc.insertBlock(b);
        bc.calculateHashesPerSecond();
    }

    /**
     A helper method to handle a client request
     */
    private static void handleClientRequest() {
        try (Scanner in = new Scanner(clientSocket.getInputStream());
             PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())))) {

            System.out.println("We have a visitor");
            // Continuously listen for client messages and respond to them
            while (true) {
                if (in.hasNextLine()) {
                    String info = in.nextLine();
                    listenJson = (JSONObject) JSONValue.parse(info);
                    Long l = (Long) listenJson.get("selection");
                    int option = l.intValue();
                    process(option);
                    out.println(returnJson.toJSONString());
                    out.flush();
                } else {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        }
    }
    /**
     A helper method to close the client socket
     */
    private static void closeClientSocket() {
        try {
            if (clientSocket != null) clientSocket.close();
        } catch (IOException e) {
            // Ignored
        }
    }

    /**
     This method processes the selected option and updates the returnJson object with the appropriate response.
     @param option an integer representing the selected option
     */
    public static void process(int option) {
        // Clear the returnJson object to start with a fresh response
        returnJson.clear();

        // Declare variables to hold start and end times for timing execution and result messages
        long startTime, endTime;
        String resultMessage;

        // Process the selected option based on its integer value
        switch (option) {
            case 0:
                // If option 0 is selected, process the case for option 0
                processCaseZero();
                break;
            case 1:
                // If option 1 is selected, insert a new block into the blockchain and time the execution
                System.out.println("Adding a block");
                startTime = System.currentTimeMillis();
                bc.insertBlock(new Block(bc.getChainSize(), bc.getCurrentTime(), (String) listenJson.get("data"), ((Long) listenJson.get("difficulty")).intValue()));
                endTime = System.currentTimeMillis();
                // Calculate the total execution time and add a message to the response object
                resultMessage = "Total execution time to add this block was " + (endTime - startTime) + " milliseconds";
                updateReturnJson(1, resultMessage);
                break;
            case 2:
                // If option 2 is selected, validate the entire blockchain and time the execution
                System.out.println("Verifying entire chain");
                startTime = System.currentTimeMillis();
                String validation = bc.validateChain();
                endTime = System.currentTimeMillis();
                // Calculate the total execution time and add a message and validation result to the response object
                resultMessage = "Total execution time required to verify the chain was " + (endTime - startTime) + " milliseconds";
                updateReturnJson(2, validation, resultMessage);
                break;
            case 3:
                // If option 3 is selected, update the response object with the current state of the blockchain
                updateReturnJson(bc.toString());
                break;
            case 4:
                // If option 4 is selected, corrupt the data of a block at a specified index and add a message to the response object
                int index = ((Long) listenJson.get("index")).intValue();
                String corruptMessage = (String) listenJson.get("data");
                processCaseFour(index, corruptMessage);
                break;
            case 5:
                // If option 5 is selected, repair the entire blockchain and time the execution
                processCaseFive();
                break;
        }
    }

    /**
     This method updates the returnJson object with the appropriate response for option 0.
     */
    private static void processCaseZero() {
        // Add the necessary data to the response object
        returnJson.put("selection", 0);
        returnJson.put("size", bc.getChainSize());
        returnJson.put("chainHash", bc.getChainHash());
        returnJson.put("totalHashes", bc.calculateTotalExpectedHashes());
        returnJson.put("totalDiff", bc.computeTotalDifficulty());
        returnJson.put("recentNonce", bc.getBlock(bc.getChainSize() - 1).getNonce());
        returnJson.put("diff", bc.getBlock(bc.getChainSize() - 1).getDifficulty());
        returnJson.put("hps", bc.getHashRate());
        // Print the response object to the console
        System.out.println("Response : " + returnJson.toJSONString());
    }

    /**
     This method corrupts the data of a block at a specified index and updates the returnJson object with a response message.
     @param index an integer representing the index of the block to corrupt
     @param corruptMessage a string containing the new, corrupted data to add to the block
     */
    private static void processCaseFour(int index, String corruptMessage) {
        System.out.println("Corrupt the Blockchain");
        // Set the data of the specified block to the new, corrupted data
        bc.getBlock(index).setData(corruptMessage);
        // Create a response message with information about the corrupted block and add it to the response object
        String resultMessage = "Block " + index + " now holds " + corruptMessage;
        System.out.println(resultMessage);
        returnJson.put("response", resultMessage);
    }

    /**
     This method repairs the entire blockchain and updates the returnJson object with a response message.
     */
    private static void processCaseFive() {
        System.out.println("Repairing the entire chain");
        long startTime = System.currentTimeMillis();
        // Validate the chain and, if it is not valid, repair it
        if (!bc.validateChain().equals("True")) {
            bc.fixChain();
        }
        long endTime = System.currentTimeMillis();
        // Calculate the total execution time and add a message to the response object
        String resultMessage = "Total execution time required to repair the chain was " + (endTime - startTime) + " milliseconds";
        System.out.println("Setting response to " + resultMessage);
        returnJson.put("response", resultMessage);
    }

    /**
     This method updates the returnJson object with the selected option and a result message.
     @param selection an integer representing the selected option
     @param resultMessage a string containing the result message
     */
    private static void updateReturnJson(int selection, String resultMessage) {
        returnJson.put("selection", selection);
        returnJson.put("response", resultMessage);
        System.out.println("Setting response to " + resultMessage);
        System.out.println("..." + returnJson.toJSONString());
    }

    /**
     This method updates the returnJson object with the current state of the blockchain.
     @param resultMessage a string containing the current state of the blockchain
     */
    private static void updateReturnJson(String resultMessage) {
        System.out.println("View the Blockchain");
        System.out.println("Setting response to " + resultMessage);
        returnJson.put("response", resultMessage);
    }

    /**
     This method updates the returnJson object with the selected option, a validation result, and a result message.
     @param selection an integer representing the selected option
     @param validation a string containing the validation result of the blockchain
     @param resultMessage a string containing the result message
     */
    private static void updateReturnJson(int selection, String validation, String resultMessage) {
        returnJson.put("selection", selection);
        // If the validation result is true, add "True" to the response object, otherwise add "False" and an error message
        returnJson.put("verification", validation.equals("True") ? "True" : "False");
        if (!validation.equals("True")) {
            returnJson.put("errorMessage", validation);
            System.out.println("Chain verification: False");
            System.out.println(validation);
        } else {
            System.out.println("Chain verification: TRUE");
        }
        returnJson.put("response", resultMessage);
        System.out.println(resultMessage);
        System.out.println("Setting response to " + resultMessage);
    }

    public static class BlockChain extends java.lang.Object{
        // instance variables
        private List<Block> blockchain;
        private String chainHash;
        private int hashesPerSecond;
        private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

        /**
         * Constructor for BlockChain class.
         */
        public BlockChain() {
            blockchain = new ArrayList<>();
            chainHash = "";
            hashesPerSecond = 0;
        }
        /**
         Gets the hash of the blockchain.
         @return The hash of the blockchain.
         */
        public String getChainHash() {
            return chainHash;
        }

        /**
         Gets the current time.
         @return The current time.
         */
        public Timestamp getCurrentTime() {
            return new Timestamp(System.currentTimeMillis());
        }

        /**
         Gets the latest block in the blockchain.
         @return The latest block in the blockchain.
         */
        public Block getLatestBlock() {
            return blockchain.get(this.getChainSize() - 1);
        }

        /**
         Gets the size of the blockchain.
         @return The size of the blockchain.
         */
        public int getChainSize() {
            return blockchain.size();
        }

        /**
         Gets the block at the specified index.
         @param i The index of the block to get.
         @return The block at the specified index.
         */
        public Block getBlock(int i) {
            if (i >= getChainSize()) {
                System.out.println("Insert number exceed block size");
                return null;
            }
            return blockchain.get(i);
        }

        /**
         * Calculates the number of hashes per second that can be computed by the system.
         */
        public void calculateHashesPerSecond() {
            String s = "0";
            Timestamp start = getCurrentTime();
            for (int i = 0; i < 1000000; i++) {
                computeHash(s);
            }
            Timestamp end = getCurrentTime();
            hashesPerSecond = (int) ((double) 1000000 / (end.getTime() - start.getTime()) * 1000);
        }

        /**
         * Returns the number of hashes per second that can be computed by the system.
         */
        public int getHashRate() {
            return hashesPerSecond;
        }

        /**
         * Adds a block to the chain.
         * If the chain is empty, sets the previous hash to an empty string.
         * Otherwise, sets the previous hash to the hash of the previous block in the chain.
         * Updates the chain hash to the proof of work of the new block.
         */
        public void insertBlock(Block block) {
            if (getChainSize() == 0) {
                block.setPreviousHash("");
            } else {
                block.setPreviousHash(chainHash);
            }
            blockchain.add(block);
            chainHash = block.proofOfWork();
        }

        /**
         * Converts the blockchain to a JSON string.
         */
        @Override
        public String toString() {
            BlockChain tempChain = new BlockChain();
            for (int i = 0; i < getChainSize(); i++) {
                tempChain.blockchain.add(getBlock(i));
            }
            tempChain.hashesPerSecond = getHashRate();
            tempChain.chainHash = getChainHash();
            Gson gson = new Gson();
            return gson.toJson(tempChain);
        }

        /**
         * Returns the total difficulty of the chain.
         */
        public int computeTotalDifficulty() {
            int totalDifficulty = 0;
            for (Block b : blockchain) {
                totalDifficulty += b.getDifficulty();
            }
            return totalDifficulty;
        }

        /**
         * Returns the total expected hashes of the chain.
         */
        public double calculateTotalExpectedHashes() {
            double totalExpectedHashes = 0;
            for (Block b : blockchain) {
                totalExpectedHashes += Math.pow(16, b.getDifficulty());
            }
            return totalExpectedHashes;
        }

        /**
         * Checks the validity of the chain.
         * Returns "True" if the chain is valid, otherwise returns an error message.
         */
        public String validateChain() {
            for (int i = 0; i < getChainSize(); i++) {
                Block b = getBlock(i);
                String s = b.calculateHash();
                for (int j = 0; j < b.getDifficulty(); j++) {
                    if (s.charAt(j) != '0') {
                        return "Improper hash on node " + i + " does not begin with " +
                                "00";
                    }
                }
                if (i != 0 && !getBlock(i - 1).calculateHash().equals(b.getPreviousHash())) {
                    return "Block " + i + " does not have a matching hash.";
                }
            }
            if (!getBlock(getChainSize() - 1).calculateHash().equals(chainHash)) {
                return "The chain hash is different from the hash of the last block.";
            }
            return "True";
        }

        /**
         * This method repairs the chain by updating any invalid hashes.
         */
        public void fixChain() {
            for (int i = 0; i < getChainSize(); i++) {
                Block b = getBlock(i);
                if (i != getChainSize() - 1) {
                    getBlock(i + 1).previousHash = b.proofOfWork();
                } else {
                    chainHash = b.proofOfWork();
                }
            }
        }

        /**
         Calculates the SHA-256 hash of the given string.
         @param s the string to be hashed
         @return the hash value as a hexadecimal string
         */
        public String computeHash(String s) {
            String hashValue = null;
            try {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-256");
                md.update(s.getBytes(StandardCharsets.UTF_8));
                hashValue = byteArrayToHex(md.digest());
            } catch (NoSuchAlgorithmException e) {
                System.out.println("No Hash available" + e);
            }
            return String.valueOf(hashValue);
        }
        /**
         Converts the given byte array to a hexadecimal string.
         @param bytes the byte array to be converted
         @return the hexadecimal string
         */
        public static String byteArrayToHex(byte[] bytes) {
            char[] hexChars = new char[bytes.length * 2];
            for (int j = 0; j < bytes.length; j++) {
                int v = bytes[j] & 0xFF;
                hexChars[j * 2] = HEX_ARRAY[v >>> 4];
                hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
            }
            return new String(hexChars);
        }
    }
}
