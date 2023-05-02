/**

 Block class represents a single block in a blockchain.
 It contains the index, timestamp, data, previous hash, nonce, and difficulty of the block.
 */
package edu.cmu.ds;
import com.google.gson.Gson;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class Block {
    // The hexadecimal characters used to convert byte arrays to strings
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    // The index of the block in the blockchain
    private int index;

    // The timestamp of when the block was created
    private Timestamp timestamp;

    // The data stored in the block
    private String data;

    // The hash of the previous block in the blockchain
    public String previousHash;

    // The nonce used to find a hash that meets the block's difficulty level
    private BigInteger nonce;

    // The difficulty level of the block
    private int difficulty;

    /**
     * Constructor for the Block class.
     *
     * @param index      The index of the block in the blockchain
     * @param timestamp  The timestamp of when the block was created
     * @param data       The data stored in the block
     * @param difficulty The difficulty level of the block
     */
    Block(int index, Timestamp timestamp, String data, int difficulty) {
        this.index = index;
        this.timestamp = timestamp;
        this.data = data;
        this.difficulty = difficulty;
        this.nonce = BigInteger.ZERO;
    }

    /**
     * Calculates the SHA-256 hash value of the block.
     *
     * @return The SHA-256 hash value of the block
     */
    public String calculateHash() {
        String information = String.format("%d%s%s%s%d%d", index, timestamp.toString(), data, previousHash, nonce, difficulty);
        String hash_value = null;

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(information.getBytes(StandardCharsets.UTF_8));
            hash_value = bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException var4) {
            System.out.println("No Hash available" + var4);
        }

        return hash_value;
    }

    /**
     * Returns the data stored in the block.
     *
     * @return The data stored in the block
     */
    public String getData() {
        return data;
    }

    /**
     * Returns the difficulty level of the block.
     *
     * @return The difficulty level of the block
     */
    public int getDifficulty() {
        return difficulty;
    }

    /**
     * Returns the index of the block in the blockchain.
     *
     * @return The index of the block in the blockchain
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns the nonce used to find a hash that meets the block's difficulty level.
     *
     * @return The nonce used to find a hash that meets the block's difficulty level
     */
    public BigInteger getNonce() {
        return nonce;
    }

    /**
     * Returns the hash of the previous block in the blockchain.
     *
     * @return The hash of the previous block in the blockchain
     */
    public String getPreviousHash() {
        return previousHash;
    }

    /**
     * Returns the timestamp of when the block was created.
     *
     * @return The timestamp of when the block was created
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * Calculates the proof-of-work for the current block.
     *
     * @return the hash value of the block after successful proof-of-work
     */
    public String proofOfWork() {
        String hash_value;

        while (true) {
            // Calculate the hash value
            hash_value = calculateHash();
            // Check if the hash value satisfies the difficulty
            if (hash_value.substring(0, difficulty).matches("0{" + difficulty + "}")) {
                break;
            }
            // Increment the nonce value and try again
            nonce = nonce.add(BigInteger.ONE);
        }

        return hash_value;
    }

    /**
     * Sets the data of the block.
     *
     * @param data the data to be set
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Sets the difficulty level for proof-of-work.
     *
     * @param difficulty the difficulty level to be set
     */
    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Sets the index of the block.
     *
     * @param index the index to be set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    /**
     * Sets the previous hash of the block.
     *
     * @param previousHash the previous hash to be set
     */
    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    /**
     * Sets the timestamp of the block.
     *
     * @param timestamp the timestamp to be set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Converts a byte array to a hexadecimal string.
     *
     * @param bytes the byte array to be converted
     * @return the hexadecimal string representation of the byte array
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];

        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 255;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 15];
        }

        return new String(hexChars);
    }
}

