## Project 3 Blockchain

### Principles

One of our primary objectives in this course is to make clear the fundamental distinction between functional and nonfunctional characteristics of distributed systems. The functional characteristics describe the business or organizational purpose of the system. The non-functional characteristics affect the quality of the system. Is it fast? Does it easily interoperate with others? Is it fault tolerant? Is it reliable and secure?

In this project, we will illustrate an important nonfunctional characteristic of blockchain technology - its tamper evident design. We will build a stand-alone blockchain (Task 0) and a distributed system where a remote client interacts with a blockchain API (Task 1).

Another important non-functional characteristic is interoperability. This will be illustrated in Task 2 where HTTP and JSON are used to interact with the Algorand blockchain.

Unlike Bitcoin, Ethereum is based on an environmentally friendly consensus protocol. In Ethereum, there is no proof-of-work. It is based on proof-of-stake.

### Prerequisites

In this project we will be using the Gson class to parse JSON messages. In this prerequisite section, there is guidance on setting up Gson in Intellij. JSON is a popular data format. It competes with XML. Either JSON or XML is appropriate to transfer textual data from one machine to another. Be sure to review the JSON grammar at [www.json.org](http://www.json.org/).

1. Create a new project named TestGsonProject and select "Maven" as the build system.
2. Edit the pom.xml file and include this XML element at the end of the file (before the closing project tag).

```
<dependency>
        <groupId>com.google.code.gson</groupId>
        <artifactId>gson</artifactId>
        <version>2.9.0</version>
</dependency>
```



1. Select File/Project Structure/Libraries/+/From Maven/. Using the search bar, search for com.google.code.gson. And then select com.google.code.gson:2.9.0.
2. Include the following import in your source code:

```
import com.google.gson.Gson;
```



1. Test by compiling and running the following program (you may need to add a package):

```
import com.google.gson.Gson;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Gson gson = new Gson();
        System.out.println("Gson is available!");
    }
}
```



1. Suppose we have a message object that we want to serialize to JSON. Why do this? We may want to transmit the data over a network in an interoperable and textual way.

```
package org.example;
import com.google.gson.Gson;

class Message {
    String name;
    int id;
    public Message(String name, int id) {
        this.name = name;
        this.id = id;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create a message
        Message msg = new Message("Alice", 30);
        // Create a Gson object
        Gson gson = new Gson();
        // Serialize to JSON
        String messageToSend = gson.toJson(msg);
        // Display the JSON string
        System.out.println(messageToSend);
    }
}
```



1. Suppose we receive a message as a JSON string. We may want to deserialize the JSON string to a Java object. Why do this? This is a huge convenience. We do not have to parse the message ourselves.

```
package org.example;
import com.google.gson.Gson;

class Message {
    String name;
    int id;
    public Message(String name, int id) {
        this.name = name;
        this.id = id;
    }
}

public class Main {
    public static void main(String[] args) {
        // Create a message
        Message msg = new Message("Alice", 30);
        // Create a Gson object
        Gson gson = new Gson();
        // Serialize to JSON
        String messageToSend = gson.toJson(msg);
        // Display the JSON string
        System.out.println(messageToSend);

        // Suppose we receive the following JSON string from a network or file.
        // Double quotes would be used in a real message. Single quotes are used
        // here because we are doing this within a Java program.
        String someJSON = "{'id':45,'name':'Bob'}";
        Message incommingMsg = gson.fromJson(someJSON,Message.class);
        System.out.println(incommingMsg.name);
        System.out.println(incommingMsg.id);

    }
}
```



1. As a warm up exercise, write a program that reads a financial transaction from the keyboard. The transaction will be expressed as a JSON string. Use Gson to build a Java object from the JSON string. Display the values within the object and then use Gson to display the Java object in JSON format.
2. It is fine to use a large language model for this exercise. It is also fine to simply code this yourself.

Here is an example execution:

```
Enter a transaction in Json format. Include from, to, and amount.       
{"from":"Mike","to":"Marty","amount":123.50}      This line is entered by the user.
From: Mike                                        Display values within the object
To: Marty
Amount: 123.5
{"from":"Mike","to":"Marty","amount":123.5}       Use Gson to generate the JSON
```



### Overview

In Task 0, you will write a blockchain by carefully following the directions found in our JavaDoc:

[See Block JavaDoc](https://www.andrew.cmu.edu/course/95-702/examples/javadoc/blockchaintask0/Block.html).

[See BlockChain Javadoc](https://www.andrew.cmu.edu/course/95-702/examples/javadoc/blockchaintask0/BlockChain.html).

Note that the Javadoc describes writing "data" or a "transaction" to the blockchain. In this project, our "data" or "transaction" will be simple statements transferring "dscoin" from one player to another.

The Javadoc describes two classes that you need to write - Block.java and BlockChain.java.

In Task 1, you will distribute the application that you created in Task 0. You will write a client server application. The interaction between the client and the server will be with JSON messages (using Gson) over TCP sockets. Thus, some work from Project2 will be very useful and may be reused here.

You will be submitting complete Java programs and console screen interactions on a single PDF file. These should be clearly labelled as described below.

You will also be submitting two IntelliJ projects as two zip files. See below for a precise description of what needs to be submitted.

Documenting code is important. Be sure to provide comments in your code explaining what the code is doing and why.

Be sure to separate concerns when appropriate. It is fine to add methods that you feel are appropriate.

Be sure to include comments within your methods.

Data validation (of user input) is very important but we are not doing that here.

Any code from external sources, e.g., stack overflow or a large language model, **must be clearly cited with a URL or, in the case of an LLM, a comment saying what tool it is that you are using**.

You are not allowed to use a Large Language Model on Task 0 or Task 2 but may use one on Task 1.

Note that if you do use code from an external source or an LLM, you are still responsible for that code. That is, you may be asked questions about the code on exams. You will not have access to stack overflow or an LLM on exams.

In Task 2, there is no programming. You will work with an Ethereum wallet and learn how to transfer funds (Eth) from one account to another. You will learn how to examine account values and transactions on Ethereum's testnet blockchain. Ethereum's REST API will be accessed with HTTP requests. To demonstrate that you have completed your work, you will submit screenshots on your single pdf.

## Task 0

Write a solution to Task 0 by studying the Javadoc provided (Block.java and BlockChain.java). The logic found in Task 0 will be reused in Task 1.

The execution of Task 0, a non-distributed stand-alone program, will look like the following interaction. As part of the submission of Task 0, you must turn in the output from the console - use copy and paste. The execution will appear similar to the one below.

Note that the last call to add a transaction requires that you use your first name to pay Marty 100 dscoins.

Label this first section ***Task 0 Execution*** in your PDF. Of course, your code - not mine - will produce the console interaction.

The order of the name value pairs within a JSON message is not important. It is fine if your order differs from mine.

### Task 0 Execution

```
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
0
Current size of chain: 1
Difficulty of most recent block: 2
Total difficulty for all blocks: 2
Approximate hashes per second on this machine: 3231017
Expected total hashes required for the whole chain: 256.000000
Nonce for most recent block: 286
Chain hash: 0026883909AA470264145129F134489316E6A38439240D0468D69AA9665D4993
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
1
Enter difficulty > 0
2
Enter transaction
Mike pays Marty 100 DSCoin
Total execution time to add this block was 19 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
1
Enter difficulty > 0
2
Enter transaction
Marty pays Joe 50 DSCoin
Total execution time to add this block was 42 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
1
Enter difficulty > 0
2
Enter transaction
Joe pays Andy 10 DS Coin
Total execution time to add this block was 6 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
2
Chain verification: TRUE
Total execution time to verify the chain was  1 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
3
View the Blockchain
{"ds_chain" : [ {"index" : 0,"time stamp " : "2022-02-25 17:41:11.927","Tx ": "Genesis","PrevHash" : "","nonce" : 286,"difficulty": 2},
{"index" : 1,"time stamp " : "2022-02-25 17:42:46.053","Tx ": "Mike pays Marty 100 DSCoin","PrevHash" : "0026883909AA470264145129F134489316E6A38439240D0468D69AA9665D4993","nonce" : 165,"difficulty": 2},
{"index" : 2,"time stamp " : "2022-02-25 17:44:27.43","Tx ": "Marty pays Joe 50 DSCoin","PrevHash" : "000D14B83028DD36BD6330B8DAB185012F8625E9C9D1A8704E9C1189FD98D9DF","nonce" : 819,"difficulty": 2},
{"index" : 3,"time stamp " : "2022-02-25 17:45:22.044","Tx ": "Joe pays Andy 10 DSCoin","PrevHash" : "00B4CC539C5CC36AE2F09CC7B857A1330D2D02C00CA90D4A34ACD7E01D7225FC","nonce" : 167,"difficulty": 2}
 ], "chainHash":"002EEC64A0ABB7FF1FBBF72BE17BD3DC3C1D5FE5FB01360680930B1CFCF5A84A"}
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
4
corrupt the Blockchain
Enter block ID of block to corrupt
1
Enter new data for block 1
Mike pays Marty 76 DSCoin
Block 1 now holds Mike pays Marty 76 DSCoin
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
3
View the Blockchain
{"ds_chain" : [ {"index" : 0,"time stamp " : "2022-02-25 17:41:11.927","Tx ": "Genesis","PrevHash" : "","nonce" : 286,"difficulty": 2},
{"index" : 1,"time stamp " : "2022-02-25 17:42:46.053","Tx ": "Mike pays Marty 76 DSCoin","PrevHash" : "0026883909AA470264145129F134489316E6A38439240D0468D69AA9665D4993","nonce" : 165,"difficulty": 2},
{"index" : 2,"time stamp " : "2022-02-25 17:44:27.43","Tx ": "Marty pays Joe 50 DSCoin","PrevHash" : "000D14B83028DD36BD6330B8DAB185012F8625E9C9D1A8704E9C1189FD98D9DF","nonce" : 819,"difficulty": 2},
{"index" : 3,"time stamp " : "2022-02-25 17:45:22.044","Tx ": "Joe pays Andy 10 DSCoin","PrevHash" : "00B4CC539C5CC36AE2F09CC7B857A1330D2D02C00CA90D4A34ACD7E01D7225FC","nonce" : 167,"difficulty": 2}
 ], "chainHash":"002EEC64A0ABB7FF1FBBF72BE17BD3DC3C1D5FE5FB01360680930B1CFCF5A84A"}
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
2
Chain verification: FALSE
Improper hash on node 1 Does not begin with 00
Total execution time to verify the chain was  0 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
5
Total execution time required to repair the chain was 8 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
2
Chain verification: TRUE
Total execution time to verify the chain was  1 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
1
Enter difficulty > 0
4
Enter transaction
Andy pays Sean 25 DSCoin
Total execution time to add this block was 224 milliseconds
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
0
Current size of chain: 5
Difficulty of most recent block: 4
Total difficulty for all blocks: 12
Approximate hashes per second on this machine: 3231017
Expected total hashes required for the whole chain: 66560.000000
Nonce for most recent block: 9610
Chain hash: 0000DF114971BAF2F0DCC51777451973DF1AFE93189B64D7AC8BA06E39681067
0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
1
Enter difficulty > 0
5
Enter transaction
<PLACE YOUR FIRST NAME HERE> pays Marty 100 DSCoin
Total execution time to add this block was 224 milliseconds

0. View basic blockchain status.
1. Add a transaction to the blockchain.
2. Verify the blockchain.
3. View the blockchain.
4. Corrupt the chain.
5. Hide the corruption by repairing the chain.
6. Exit
6

Process finished with exit code 0
```



Label this second section **Task 0 Block.java** and include a complete listing of Block.java.

Label this third section **Task 0 BlockChain.java** and include a complete listing of BlockChain.java.

See the Javadoc's main routine. You are asked to experiment and provide some timing data and analysis. That commentary should be present in the comments of your main routine.

------

### Task 0 Grading Rubric 40 Points

Rubric:

1. The execution (as shown by the console interaction) is correct and includes the same tests as above - using the same names and in the same order: 20 points.
2. The code is well documented: 5 points.
3. The analysis in the main routine is detailed and clear: 5 Points. Within your comments in the main routine, you must describe how this system behaves as the difficulty level increases. Run some experiments by adding new blocks with increasing difficulties. Describe what you find. Be specific and quote some times. You need not employ a system clock. You should be able to make clear statements describing the approximate run times associated with addBlock(), isChainValid(), and chainRepair().
4. The code illustrates separation of concerns and good style: 5 points.
5. The single PDF file includes sections correctly labelled: 5 Points.

------

## Task 1

The client side execution of Task 1 will appear exactly the same as in Task 0. The primary difference will be that, behind the scenes, there will be a client server interaction using JSON over TCP sockets. The blockchain will exist on the server. It will be constructed there and the client will make requests for operations over a TCP socket. The client will be focused on driving the menu driven interaction and communicating with the server on the backend. If the client exits, the server will still handle new requests with the existing blockchain intact.

You are required to design and use two JSON messages types - a message to encapsulate requests from the client and a message to encapsulate responses from the server. The server side display will show each request message (received from the client -in JSON) and each response message (being sent to the client - in JSON).

You should have a class named RequestMessage and a class named ResponseMessage to encapsulate the JSON data. You need to include these classes in your submission. You will use the RequestMessage class on both the client and the server. And, you will use the ResponseMessage class on both the client and the server.

Use the following four labels in your single PDF:

**Task 1 Client Side Execution**

Copy and paste your client side console. This will appear as in Task 0.

**Task 1 Server Side Execution**

Here, we will see the request and response messages in JSON format.

```
Blockchain server running
We have a visitor
THE JSON REQUEST MESSAGE IS SHOWN HERE
THE JSON RESPONSE MESSAGE IS SHOWN HERE

We have a visitor
THE JSON REQUEST MESSAGE IS SHOWN HERE
THE JSON RESPONSE MESSAGE IS SHOWN HERE

We have a visitor
THE JSON REQUEST MESSAGE IS SHOWN HERE
THE JSON RESPONSE MESSAGE IS SHOWN HERE

etc.
```



**Task 1 Client Source Code**

Include all client side source code clearly labelled. This includes the RequestMessage and ResponseMessage classes.

**Task 1 Server Source Code**

Include all server side source code clearly labelled. This includes the RequestMessage and ResponseMessage classes. These will be the same classes as found on the client side.

**Task 1 Grading Rubric 40 Points**

Rubric:

1. The execution is correct and includes the same tests as above - in the same order. A client server architecture based on TCP sockets is used. 20 points.
2. The JSON message being sent to the server is well designed (RequestMessage.java): 5 Points
3. The JSON message being sent from the server to the client is well designed (ResponseMessage.java): 5 Points.
4. Separation of concerns is well done: 5 points.
5. The single PDF file includes sections correctly labelled: 5 Points.

------

## Task 2 Exploring Remote Procedure Calls using JSON-RPC and Ethereum

## Infura is not working properly. So, Task 2 is dropped from Project 2

We might define Web 1.0 as a read only distributed system. Fetch a document and read it in your browser. Web 2.0 might be described as a "read-write" distributed system. Using a browser, the user is able to enter data and communicate that data to servers. Web 3.0 might be categorized as a "read-write-own" distributed system. Cryptocurrency and tokens may be owned and transferred.

The overall architecture on most Web3 systems is "decentralized peer-to-peer" rather than centralized "client server".

Ethereum is a Web3 blockchain that competes with Bitcoin.

Here, we will visit the Ethereum blockchain using the REST API provided by Infura. To get started using Infura, we need an account and an API Key.

1. Visit the Infura website: https://app.infura.io/register
2. Enter your first and last name, email address, and a password.
3. Check the “Terms of Use” confirmation checkbox.
4. Click the “CREATE A FREE ACCOUNT” button.
5. You will receive an email from Infura to verify your email address.
6. Go to your email inbox and look for an email from Infura with the title "Verify your email".
7. Click on the link in the email to verify your account.
8. After you’ve registered, you can create an API key and start using Infura’s services. Be sure to handle your API keys securely.
9. To create an API key, visit https://app.infura.io/dashboard and select Create New API Key.
10. Give the key a name.
11. We need a client. IntelliJ allows you to directly make HTTPS requests.
12. In an IntelliJ project, right click the Project node and select New HTTP Request.
13. Requests written using curl are supported. The IntelliJ HTTP client will convert the curl request into IntelliJ's format.

The JSON-RPC API is described here: https://docs.infura.io/networks/ethereum/json-rpc-methods

1. Here, we will view the details of the genesis block on the Ethereum blockchain, enter the following HTTP request and click the green triangle just to the left of the request:

```
curl -X POST -H "Content-Type: application/json" --data '{"jsonrpc":"2.0","method":"eth_getBlockByNumber","params":["0x0", true],"id":1}' https://mainnet.infura.io/v3/PLACE-YOUR-API-KEY-HERE-WITH-NO-QUOTES
```



1. Make a copy of your request (from IntelliJ) and make a copy of your response (from IntelliJ).
2. The above was a request for block number 0. Do the same for block number 1.
3. Make a copy of your request (from IntelliJ) and make a copy of your response (from IntelliJ).
4. To examine the balance of a particular account, try the following:

```
curl -X POST -H "Content-Type: application/json" --data '{"jsonrpc":"2.0","method":"eth_getBalance","params":["0x742d35Cc6634C0532925a3b844Bc454e4438f44e", "latest"],"id":1}' https://mainnet.infura.io/v3/PLACE-YOUR-API-KEY-HERE-WITH-NO-QUOTES
```