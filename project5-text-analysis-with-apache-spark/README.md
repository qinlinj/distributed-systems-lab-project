# Project 5

## Text Analysis with Apache Spark

### Overview

The `ShakespeareAnalytics` Java application is designed to perform a variety of text analysis tasks on the play "All's Well That Ends Well" by William Shakespeare. This application is built using the Apache Spark framework, a powerful tool for handling large-scale data processing and analytics in a distributed computing environment. Below is a detailed explanation of the principles and functions of this application:

### Principle:

1. **Apache Spark Framework**:
   - Apache Spark is utilized for its efficiency in processing large datasets. It's a distributed computing system that can process data across multiple nodes in a cluster, but can also run in a local mode for smaller datasets or testing purposes.
   - The application uses Spark's Resilient Distributed Dataset (RDD) feature, which is a fundamental data structure of Spark. RDDs are immutable, distributed collections of objects, which can be processed in parallel.
2. **Functional Programming Paradigm**:
   - The application makes extensive use of functional programming concepts, such as map and filter, which are key components of Spark's design.
   - This approach is particularly effective for processing and transforming large datasets, as it simplifies the code and improves performance.

### Functionality:

1. **Line Count**:
   - This feature counts the number of lines in the text file.
   - It loads the text file into an RDD, splits the content by new lines, and then counts the elements.
2. **Word Count**:
   - Counts the total number of words in the text.
   - The text is split into words using a regular expression that matches word boundaries, filtering out any empty results.
3. **Distinct Word Count**:
   - Determines the number of unique words in the text.
   - Similar to word count, but it additionally applies a distinct operation to count only unique instances.
4. **Symbol Count**:
   - Counts all symbols (characters that are neither letters nor digits) in the text.
   - It involves splitting the text into individual characters and filtering out letters and digits.
5. **Distinct Symbol Count**:
   - Identifies the number of unique symbols in the text.
6. **Distinct Letters Count**:
   - Counts the distinct English letters, considering case-insensitivity.
   - It filters out non-letter characters and applies a distinct operation.
7. **Word Search**:
   - Allows users to search for occurrences of a specific word.
   - The application reads user input, then filters the lines that contain the specified word, displaying them as output.

### Execution Flow:

1. The application starts with the `main` method, which checks for the text file's presence in the command-line arguments.
2. For each task (like line count, word count, etc.), there is a dedicated method. The main method calls these methods sequentially with the file name as an argument.
3. Each method applies a series of transformations and actions on the RDD created from the text file. The results are then outputted to the console.

### Practical Usage:

This application serves as a practical tool for literary analysis, specifically for large text files like Shakespeare's plays. It can be used in educational contexts for teaching text analysis or by researchers who need to quickly extract quantitative data from texts.

### Conclusion:

`ShakespeareAnalytics` is a example of combining modern big data processing techniques with classical literature study. It demonstrates how Apache Spark can be used for efficient text processing, offering insights into both the content and structure of literary works.