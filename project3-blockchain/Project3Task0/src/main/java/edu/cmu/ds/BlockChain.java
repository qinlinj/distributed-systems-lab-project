/**

 This class represents a simple implementation of a blockchain, allowing for transactions to be added,
 verified, and repaired if necessary. It uses SHA-256 for hashing and proof of work to ensure the integrity
 of the chain.
 */
package edu.cmu.ds;

import com.google.gson.Gson;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BlockChain {
    // instance variables
    private List<Block> BlockChain = new ArrayList<>();
    private String chainHash = "";
    private int hashesPerSecond = 0;
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * Constructor for BlockChain class.
     */
    public BlockChain() {
    }

    /**
     * Main method for running the blockchain application.
     */
    public static void main(String[] args) {
        String[] menuOptions = new String[]{
                "View basic BlockChain status.",
                "Add a transaction to the BlockChain.",
                "Verify the BlockChain.",
                "View the BlockChain.",
                "Corrupt the chain.",
                "Hide the corruption by repairing the chain.",
                "Exit."
        };
        BlockChain bc = new BlockChain();
        Block genesisBlock = new Block(bc.getChainSize(), bc.getTime(), "Genesis", 2);
        bc.addBlock(genesisBlock);
        bc.computeHashesPerSecond();
        Scanner inputScanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            for (int i = 0; i < menuOptions.length; ++i) {
                System.out.println(i + ". " + menuOptions[i]);
            }

            int userOption = Integer.parseInt(inputScanner.nextLine());
            Timestamp startTime;
            Timestamp endTime;
            int timeElapsed;
            switch (userOption) {
                case 0:
                    displayBlockChainStatus(bc);
                    break;
                case 1:
                    addTransactionToBlockChain(inputScanner, bc);
                    break;
                case 2:
                    verifyBlockChain(bc);
                    break;
                case 3:
                    viewBlockChain(bc);
                    break;
                case 4:
                    corruptChain(inputScanner, bc);
                    break;
                case 5:
                    repairChain(bc);
                    break;
                case 6:
                    exit = true;
            }
        }

        inputScanner.close();
    }

    /**
     * Displays the current status of the blockchain.
     * @param bc BlockChain instance to display status for.
     */
    private static void displayBlockChainStatus(BlockChain bc) {
        System.out.println("Current size of chain: " + bc.getChainSize());
        System.out.println("Difficulty of most recent block: " + bc.getLatestBlock().getDifficulty());
        System.out.println("Total difficulty for all blocks: " + bc.getTotalDifficulty());
        System.out.println("Approximate hashes per second on this machine: " + bc.getHashespersecond());
        System.out.println("Expected total hashes required for the whole chain: " + bc.getTotalExpectedHashes());
        System.out.println("Nonce for most recent block: " + bc.getLatestBlock().getNonce());
        System.out.println("Chain hash: " + bc.getChainHash());
    }

    /**
     * Allows a user to add a transaction to the blockchain.
     * @param inputScanner Scanner instance to read user input.
     * @param bc BlockChain instance to add transaction to.
     */
    private static void addTransactionToBlockChain(Scanner inputScanner, BlockChain bc) {
        System.out.println("Enter difficulty > 0");
        int difficulty = Integer.parseInt(inputScanner.nextLine());
        System.out.println("Enter transaction");
        String data = inputScanner.nextLine();
        Timestamp startTime = bc.getTime();
        bc.addBlock(new Block(bc.getChainSize(), bc.getTime(), data, difficulty));
        Timestamp endTime = bc.getTime();
        int timeElapsed = (int) (endTime.getTime() - startTime.getTime());
        System.out.println("Total execution time to add this block was " + timeElapsed + " milliseconds");
    }

    /**

     Verifies the integrity of the blockchain.
     @param bc The blockchain to verify.
     */
    private static void verifyBlockChain(BlockChain bc) {
        Timestamp startTime = bc.getTime();
        String validationResult = bc.isChainValid();
        Timestamp endTime = bc.getTime();
        int timeElapsed = (int) (endTime.getTime() - startTime.getTime());
        System.out.print("Chain verification: ");
        System.out.println(validationResult);
        System.out.println("Total execution time to verify the chain was " + timeElapsed + " milliseconds");
    }
    /**

     Displays the blockchain.
     @param bc The blockchain to display.
     */
    private static void viewBlockChain(BlockChain bc) {
        System.out.println("View the BlockChain");
        System.out.println(bc.toString());
    }
    /**

     Corrupts a block in the blockchain.
     @param inputScanner The scanner to use for input.
     @param bc The blockchain to corrupt.
     */
    private static void corruptChain(Scanner inputScanner, BlockChain bc) {
        System.out.println("corrupt the BlockChain");
        System.out.println("Enter block ID of block to corrupt");
        int index = Integer.parseInt(inputScanner.nextLine());
        System.out.println("Enter new data for block " + index);
        String corruptMessage = inputScanner.nextLine();
        bc.getBlock(index).setData(corruptMessage);
        System.out.println("Block " + index + " now holds " + corruptMessage);
    }
    /**

     Repairs the blockchain.
     @param bc The blockchain to repair.
     */
    private static void repairChain(BlockChain bc) {
        Timestamp startTime = bc.getTime();
        if (!bc.isChainValid().equals("True")) {
            bc.repairChain();
        }
        Timestamp endTime = bc.getTime();
        int timeElapsed = (int) (endTime.getTime() - startTime.getTime());
        System.out.println("Total execution time required to repair the chain was " + timeElapsed + " milliseconds");
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
    public Timestamp getTime() {
        return new Timestamp(System.currentTimeMillis());
    }
    /**

     Gets the latest block in the blockchain.
     @return The latest block in the blockchain.
     */
    public Block getLatestBlock() {
        return BlockChain.get(getChainSize() - 1);
    }
    /**
     Gets the size of the blockchain.
     @return The size of the blockchain.
     */
    public int getChainSize() {
        return BlockChain.size();
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
        } else {
            return BlockChain.get(i);
        }
    }

    /**
     * Calculates the number of hashes per second that can be computed by the system.
     */
    public void computeHashesPerSecond() {
        String s = "0";
        Timestamp start = getTime();

        for (int i = 0; i < 1000000; ++i) {
            calculateHash(s);
        }

        Timestamp end = getTime();
        hashesPerSecond = (int) (1000000 / (double) (end.getTime() - start.getTime()) * 1000.0);
    }

    /**
     * Returns the number of hashes per second that can be computed by the system.
     */
    public int getHashespersecond() {
        return hashesPerSecond;
    }

    /**
     * Adds a block to the chain.
     * If the chain is empty, sets the previous hash to an empty string.
     * Otherwise, sets the previous hash to the hash of the previous block in the chain.
     * Updates the chain hash to the proof of work of the new block.
     */
    public void addBlock(Block block) {
        if (getChainSize() == 0) {
            block.setPreviousHash("");
        } else {
            block.setPreviousHash(chainHash);
        }

        BlockChain.add(block);
        chainHash = block.proofOfWork();
    }

    /**
     * Converts the blockchain to a JSON string.
     */
    public String toString() {
        BlockChain bc = new BlockChain();

        for (int i = 0; i < getChainSize(); ++i) {
            bc.BlockChain.add(getBlock(i));
        }

        bc.hashesPerSecond = getHashespersecond();
        bc.chainHash = getChainHash();
        Gson gson = new Gson();
        String messageToSend = gson.toJson(bc);
        return messageToSend;
    }

    /**
     * Returns the total difficulty of the chain.
     */
    public int getTotalDifficulty() {
        int totalDifficulty = 0;

        for (Block b : BlockChain) {
            totalDifficulty += b.getDifficulty();
        }

        return totalDifficulty;
    }

    /**
     * Returns the total expected hashes of the chain.
     */
    public double getTotalExpectedHashes() {
        double totalExpectedHashes = 0.0;

        for (Block b : BlockChain) {
            totalExpectedHashes += Math.pow(16.0, (double) b.getDifficulty());
        }

        return totalExpectedHashes;
    }

    /**
     * Checks the validity of the chain.
     * Returns "True" if the chain is valid, otherwise returns an error message.
     */
    public String isChainValid() {
        for (int i = 0; i < getChainSize(); ++i) {
            Block b = getBlock(i);
            String s = b.calculateHash();

            if (!hashIsValid(s, b.getDifficulty())) {
                return "FALSE\nImproper hash on node " + i + " does not begin with " + "00";
            }

            if (i != 0 && !previousHashIsValid(i, b)) {
                return "Block " + i + " does not have a matching hash.";
            }
        }

        if (!lastBlockHashIsValid()) {
            return "The chain hash is different from the hash of the last block.";
        } else {
            return "True";
        }
    }

    /**
     * This method checks if a hash is valid for a given difficulty level.
     *
     * @param hash       the hash to check.
     * @param difficulty the difficulty level to check against.
     * @return true if the hash is valid for the given difficulty level, false otherwise.
     */
    private boolean hashIsValid(String hash, int difficulty) {
        for (int j = 0; j < difficulty; ++j) {
            if (hash.charAt(j) != '0') {
                return false;
            }
        }
        return true;
    }

    /**
     * This method checks if the previous block's hash is valid for a given block.
     *
     * @param index the index of the previous block.
     * @param block the current block to check against.
     * @return true if the previous block's hash is valid for the given block, false otherwise.
     */
    private boolean previousHashIsValid(int index, Block block) {
        return getBlock(index - 1).calculateHash().equals(block.getPreviousHash());
    }

    /**
     * This method checks if the last block's hash is valid for the current chain.
     *
     * @return true if the last block's hash is valid for the current chain, false otherwise.
     */
    private boolean lastBlockHashIsValid() {
        return getBlock(getChainSize() - 1).calculateHash().equals(chainHash);
    }

    /**
     * This method repairs the chain by updating any invalid hashes.
     */
    public void repairChain() {
        for (int i = 0; i < getChainSize(); ++i) {
            Block b = getBlock(i);
            updateChainHashes(i, b);
        }
    }

    /**
     * This method updates the hashes for the chain.
     *
     * @param index the index of the block to update.
     * @param block the block to update.
     */
    private void updateChainHashes(int index, Block block) {
        if (index != getChainSize() - 1) {
            getBlock(index + 1).previousHash = block.proofOfWork();
        } else {
            chainHash = block.proofOfWork();
        }
    }

    /**
     Calculates the SHA-256 hash of the given string.
     @param s the string to be hashed
     @return the hash value as a hexadecimal string
     */
    public String calculateHash(String s) {
        String hashValue = null;
        try {
            // Create a new SHA-256 message digest instance
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            // Update the digest with the UTF-8 encoded bytes of the input string
            md.update(s.getBytes(StandardCharsets.UTF_8));
            // Convert the digest bytes to a hexadecimal string
            hashValue = bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException var4) {
            // If SHA-256 is not available, print an error message and return null
            System.out.println("SHA-256 hash algorithm is not available: " + var4);
        }
        return String.valueOf(hashValue);
    }

    /**
     Converts the given byte array to a hexadecimal string.
     @param bytes the byte array to be converted
     @return the hexadecimal string
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; ++j) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 15];
        }
        return new String(hexChars);
    }
}
