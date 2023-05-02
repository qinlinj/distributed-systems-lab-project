import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Andrew ID: qinlinj
 * @author  Justin Jia
 */

/**

 A class to perform text analytics on Shakespeare's play "All's Well That Ends Well".
 */
public class ShakespeareAnalytics {
    // Create a Spark configuration and context to be used throughout the program.
    static SparkConf sparkConf = new SparkConf().setMaster("local").setAppName("JD Word Counter");
    static JavaSparkContext sparkContext = new JavaSparkContext(sparkConf);
    /**

     Main method that runs all text analytics tasks on "All's Well That Ends Well".

     @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("No files provided.");
            System.exit(0);
        }

        // Task 1: Count the number of lines in the txt file.
        lineCount(args[0]);

        // Task 2: Count the number of words in the txt file.
        wordCount(args[0]);

        // Task 3: Count the number of distinct words in the txt file.
        distinctWordCount(args[0]);

        // Task 4: Count the number of symbols in the txt file.
        symbols(args[0]);

        // Task 5: Count the number of distinct symbols in the txt file.
        distinctSymbols(args[0]);

        // Task 6: Count the number of distinct English letters in the txt file.
        distinctLetters(args[0]);

        // Task 7: User can search for a word and the program will print out all the lines containing that word.
        search(args[0]);
    }
    /**

     Count the number of lines in the provided text file.
     @param fileName Name of the text file to analyze.
     */
    private static void lineCount(String fileName) {
        // Load the text file into an RDD, split each line, and count the number of lines.
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> lines = inputFile.flatMap(content -> Arrays.asList(content.split("\n")));
        System.out.println("Number of lines: " + lines.count());
    }
    /**

     Count the number of words in the provided text file.
     @param fileName Name of the text file to analyze.
     */
    private static void wordCount(String fileName) {
        // Load the text file into an RDD, split each line into words using regular expression, and count the number of words.
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> words = inputFile.flatMap(content -> Arrays.asList(content.split("[^a-zA-Z]+"))).filter(k -> !k.isEmpty());
        System.out.println("Number of words in \"All's Well That Ends Well\": " + words.count());
    }
    /**

     Count the number of distinct words in the provided text file.
     @param fileName Name of the text file to analyze.
     */
    private static void distinctWordCount(String fileName) {
        // Load the text file into an RDD, split each line into words using regular expression, and count the number of distinct words.
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> distinctWords = inputFile.flatMap(content -> Arrays.asList(content.split("[^a-zA-Z]+"))).filter(k -> !k.isEmpty()).distinct();
        System.out.println("Number of distinct words in \"All's Well That Ends Well\": " + distinctWords.count());
    }
    /**

     Count the number of symbols (non-letter, non-digit characters) in the provided text file.
     @param fileName Name of the text file to analyze.
     */
    private static void symbols(String fileName) {
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> symbols = inputFile.flatMap(content -> Arrays.asList(content.split(""))).filter(k -> !k.isEmpty());
        System.out.println("Number of symbols in \"All's Well That Ends Well\": " + symbols.count());
    }
    /**
     Count the number of distinct symbols (non-letter, non-digit characters) in the provided text file.
     @param fileName Name of the text file to analyze.
     */
    private static void distinctSymbols(String fileName) {
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> distinctSymbols = inputFile.flatMap(content -> Arrays.asList(content.split(""))).filter(k -> !k.isEmpty()).distinct();
        System.out.println("Number of distinct symbols in \"All's Well That Ends Well\": " + distinctSymbols.count());
    }
    /**
     Count the number of distinct English letters (case insensitive) in the provided text file.
     @param fileName Name of the text file to analyze.
     */
    private static void distinctLetters(String fileName) {
        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> distinctLetters = inputFile.flatMap(content -> Arrays.asList(content.split(""))).filter(k -> !k.isEmpty() && ((k.charAt(0) >= 'a' && k.charAt(0) <= 'z') || (k.charAt(0) >= 'A' && k.charAt(0) <= 'Z'))).distinct();
        System.out.println("Number of distinct Letters in \"All's Well That Ends Well\": " + distinctLetters.count());
    }
    /**
     Searches for a user-provided word in the provided text file and prints out all the lines that contain that word.
     @param fileName Name of the text file to analyze.
     */
    private static void search(String fileName) {
        System.out.println("Enter a word and show all of the lines of \"All's Well That Ends Well\" that contain that word");
        Scanner readInput = new Scanner(System.in);
        String key = readInput.nextLine();

        JavaRDD<String> inputFile = sparkContext.textFile(fileName);
        JavaRDD<String> lines = inputFile.flatMap(content -> Arrays.asList(content.split("\n")));

        JavaRDD<String> matchedLines = lines.filter(k -> k.contains(key));
        System.out.println("Lines containing the searched word:");
        for (String line : matchedLines.collect()) {
            System.out.println(line);
        }
    }
}