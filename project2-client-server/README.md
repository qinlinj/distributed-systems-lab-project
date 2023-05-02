# Project 2 Client-Server Computing

#### Six Tasks

The single PDF will contain your responses to the questions marked with a checkered flag. It is important that you ***clearly label*** each answer with the labels provided below. It is also important to ***be prepared*** to demonstrate your working code if we need to verify your submission. Be sure to provide your name and email address at the top of the PDF submission.

The six IntelliJ projects will be submitted as six zip files, each one will be the zip of your WHOLE IntelliJ project for tasks 0, 1, 2, 3, 4 and 5. Each IntelliJ project, except Task 1, will contain one client and one server. Task 1 will contain one client, one server, and one malicious player in the middle. For each project, zip the whole project, you need to use "File->Export Project->To Zip" in IntelliJ.

When all of your work is complete, zip the one PDF and the six project zip files into one big zip file for submission. Name this final file your_andrew_id.zip.

### Learning Objectives:

Our **first objective** is for you to be able to work with the User Datagram Protocol (UDP) and the Transmission Control Protocol (TCP). UDP is used in many internet applications. The Domain Name Service (DNS) and the Dynamic Host Configuration Protocol (DHCP) both use UDP. Most video and audio traffic uses UDP. We use UDP when we need high performance and do not mind an occasional dropped packet.

TCP, on the other hand, is also widely used. It works hard to make sure that not a single bit of information is lost in transit. The Hyper Text Transfer Protocol (HTTP) uses TCP.

Our **second objective** is to understand the implications of a malicious player in the middle.

Our **third objective** is for you to understand the abstraction provided by Remote Procedure Calls (RPC's). We do this by asking that you use a proxy design and hide communication code and keep it separate from your application code. RPC has been used for four decades and is at the foundation of many distributed systems.

Our **fourth objective** is to expose you to digital signatures and their implementation in RSA. In modern distributed systems, we want to know exactly who sent us a message and if that message was tampered with or modified in any way. Digital signatures allow us to do that.

Optionally, you may use a large language model, such as ChatGPT or Copilot, to create some of your code. Task 0, Task 1, and Task 5 must be done without the help of a large language model. There will be exam questions that ask specifically about the code in these three tasks. While you are allowed to use AI tools for these three tasks, it is totally optional. There will be questions about these tasks, too, but these questions will be more generic (since different students may code these tasks using different techniques).

### Submission notes:

When you are asked to submit Java code (on the single pdf) it should be documented. Points will be deducted if code is not well documented. Each significant block of code will contain a comment describing what the block of code is being used for. See Canvas/Home/Documentation for an example of good and bad documentation.

### Rubric

See the General Course Rubric (on Canvas). We will use a specific, unpublished rubric for this assignment but the general rubric provides rough guidance on how this assignment will be evaluated.

### Some simplifications:

In all of what follows, we are concerned with designing servers to handle one client at a time. We are not exploring the important issues surrounding multiple, simultaneous visitors. If you write a multi-threaded server to handle several visitors at once, that is great but is not required. It gains no additional credit.

In addition, for all of what follows, we are assuming that the server is run before the client is run. If you want to handle the case where the client is run first, without a running server, that is great but will receive no additional credit.

In Task 1, we are assuming that the server is run before the malicious player and the malicious player is run before the client.

In this assignment, you need not be concerned with data validation. You may assume that the data entered by users is correctly formatted.

In general, if these requirements do not explicitly ask for a certain feature, then you are not required to provide that feature. No additional points are awarded for extra features.

### Cite your sources

If you use any code that is not yours (including code from a large language model), you are required to clearly cite the source - include a full URL in a comment and place it just above the code that is copied. If you use a large language model to generate code, be sure to say so. Be careful to cite your sources. If you submit code that you did not create on your own and you fail to include proper citations then that will be reported as an academic violation.

## Task 0 introduces UDP. Name the IntelliJ project "Project2Task0".

In Task 0, you will make several modifications to EchoServerUDP.java and EchoClientUDP.java. Note that
these two programs are standard Java and we do not need to construct a web application in IntelliJ. Both of these programs will be placed in the same IntelliJ project.

EchoServerUDP.java from Coulouris text

```
import java.net.*;
import java.io.*;
public class EchoServerUDP{
	public static void main(String args[]){
	DatagramSocket aSocket = null;
	byte[] buffer = new byte[1000];
	try{
		aSocket = new DatagramSocket(6789);
		DatagramPacket request = new DatagramPacket(buffer, buffer.length);
 		while(true){
			aSocket.receive(request);     
			DatagramPacket reply = new DatagramPacket(request.getData(),
      	   	request.getLength(), request.getAddress(), request.getPort());
			String requestString = new String(request.getData());
			System.out.println("Echoing: "+requestString);
			aSocket.send(reply);
		}
	}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
	}catch (IOException e) {System.out.println("IO: " + e.getMessage());
	}finally {if(aSocket != null) aSocket.close();}
	}
}

```

Note the difference between a DatagramSocket and a DatagramPacket. The server uses a DatagramPacket to receive data from a client (in the request object). And it uses a DatagramPacket to send data back to the client (in the reply object). A DatagramPacket is always based on a byte array. So, to send a message in a DatagramPacket, we must first convert the message to a byte array. To receive a message from a DatagramPacket, we must convert the byte array to a String message (if we are expecting a String message).

Note below how the client does the same thing. The client wants to send a String message. So, it extracts a byte array from the String (the variable m). And we then use m to build the DatagramPacket.

When the client receives a reply, the method reply.getData() returns a byte array - which we use to build a String object.

EchoClientUDP.java from Coulouris text

```
import java.net.*;
import java.io.*;
public class EchoClientUDP{
    public static void main(String args[]){
	// args give message contents and server hostname
	DatagramSocket aSocket = null;
	try {
		InetAddress aHost = InetAddress.getByName(args[0]);
		int serverPort = 6789;
		aSocket = new DatagramSocket();
		String nextLine;
		BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
		while ((nextLine = typed.readLine()) != null) {
		  byte [] m = nextLine.getBytes();
		  DatagramPacket request = new DatagramPacket(m,  m.length, aHost, serverPort);
  		aSocket.send(request);
  		byte[] buffer = new byte[1000];
  		DatagramPacket reply = new DatagramPacket(buffer, buffer.length);
  		aSocket.receive(reply);
  		System.out.println("Reply from server: " + new String(reply.getData()));
    }

	}catch (SocketException e) {System.out.println("Socket Exception: " + e.getMessage());
	}catch (IOException e){System.out.println("IO Exception: " + e.getMessage());
	}finally {if(aSocket != null) aSocket.close();}
    }
}
```

0. Get these programs running in IntelliJ. The two programs are placed in the same Intellij project and you are provided with two windows to interact with the two programs. Make the following modifications to the client and the server.
1. Change the client&#39;s &quot;arg[0]&quot; to a hardcoded &quot;localhost&quot;.
2. Document the client and the server. Describe what each line of code does.
3. Add a line at the top of the client so that it announces, by printing a message on the console, &quot;The UDP client is running.&quot; at start up.
4. After the announcement that the client is running, have the client prompt the user for the server side port number. It will then use that port number to contact the server. For now, enter 6789.
5. Add a line at the top of the server so that it announces &quot;The UDP server is running.&quot; at start up.
6. After the announcement that the server is running, have the server prompt the user for the port number that the server is supposed to listen on. Enter 6789 when prompted.
7. On the server, examine the length of the requestString and note that it is too large. Make modifications to the server code so that the request data is copied to an array with the correct number of bytes. Use this array of bytes to build a requestString of the correct size. Without these modifications, incorrect data may be displayed on the server. Upon each visit, your server will display the request arriving from the client.
8. Do the same on the client side to properly handle the size of the response.
9. If the client enters the command &quot;halt!&quot;, both the client and the server will halt execution. When the client program receives &quot;halt!&quot; from the user, it sends &quot;halt!&quot; to the sever and after hearing the &quot;halt!&quot; message from the server, the client exits. When the server receives &quot;halt!&quot; from the client, it will respond to the client with &quot;halt!&quot; and then exit.
10. Add a line in the client so that it announces when it is quitting. It will write "UDP Client side quitting" to the client side console.
11. Add a line in the server so that it makes an announcement when it is quitting. The server only quits when it is told to do so by the client (and has just responded to the client with the &quot;halt!&quot; message). It will write "UDP Server side quitting" to the server side console.

:checkered_flag:**On your single pdf, make a copy of your client and label it clearly as "Project2Task0Client".**

:checkered_flag:**On your single pdf, make a copy of your server and label it clearly as "Project2Task0Server".**

:checkered_flag:**Make a screenshot of your client console screen. It will include five lines of data sent by the client to the server and the client's response to a request by the user to &quot;halt!&quot;. On your single pdf, label this screenshot as "Project2Task0ClientConsole".**

:checkered_flag:**Make a screenshot of your server console screen. It will include five lines of data sent by the client and the server's response to the &quot;halt!&quot; request by the client. On your single pdf, label this screenshot as "Project2Task0ServerConsole".**


## Task 1 illustrates a malicious player in the middle attack on UDP. Name the IntelliJ Project "Project2Task1".

In Task 1, you will experiment with a malicious player in the middle attack. This malicious player is interested in more than simply eavesdropping on the conversation between the client and the server. It is an active malicious player. You might want to get started by working on a passive malicious player - one that only eavesdrops and passes messages along to the server and then back to the client.

We will have three UDP programs in one project.

Name your malicious player EavesdropperUDP.java. You will need to design and write EavesdropperUDP.java as it is described below.

First, run EchoServerUDP.java as it has been modified in Task 0. EchoServerUDP will prompt you for its port. Enter the port 6789 for EchoServerUDP to listen on.

Second, run EavesdropperUDP.java. EavesdropperUDP will state that it is running and will ask you for two ports. One port will be the port that the EavesdropperUDP.java will listen on and the other port will be the port number of the server that Eavesdropper.java is masquerading as. We want Eavesdropper.java to display (on its console) all messages that go through it. We want it to eavesdrop on the wire. It will be masquerading as the server on port 6789. It will listen on port 6798 - hoping a foolish client will make a transposition error.

Third, when you run EchoClientUDP.java, provide it with either the correct port (of the real server) or the port that Eavesdropper is listening on. That is, it will work with either 6789 or 6798.  

Eavesdropper is an active attacker. If the client sends a string containing the word &quot;like&quot; the eavesdropper will replace the word &quot;like&quot; with the word &quot;dislike&quot;. The eavesdroppr will not bother with the string &quot;like&quot; if it is included as a substring of another word, such as &quot;dislike&quot;.

If the client sends the message &quot;halt!&quot; then the server will respond, as usual, and then halt execution. The client will halt when it hears from the server. Our malicious player runs forever. And it displays everything it sees to its console.

:checkered_flag:**On your single pdf, make a copy of your documented EavesdropperUDP.java program.

:checkered_flag:**Make a screenshot showing your client, server, and eavesdropper consoles. The shot will show a few lines of data sent by the client and the server's response to the &quot;halt!&quot; request by the client. It will also show the eavesdropper console - showing the entire interaction between the client and the server. On your single pdf, label this screenshot as "Project2Task1ThreeConsoles". Be sure to show the client using port 6789 (correct server) and 6798 (malicious player). The idea is to provide screenshots that demonstrate that the client works against both servers. You also need to show the word &quot;like&quot; being replaced with the word &quot;dislike&quot;**

In the remaining tasks (Tasks 2 through 5), we do not provide the client with the ability to stop the server. We are doing that only in Tasks 0 and 1. In the remaining Tasks, the server is left running - forever. In the remaining tasks, we are not using an eavesdropper.

## Task 2 illustrates a proxy design using UDP. Name the IntelliJ Project "Project2Task2".

Make the following modifications to "EchoServerUDP.java" and "EchoClientUDP.java":

0. Name the client "AddingClientUDP.java". Name the server "AddingServerUDP.java".
1. The server will hold an integer value sum, initialized to 0, and will receive requests from the client - each of which includes an integer value (positive or negative or 0) to be added to the sum. Upon each request, the server will return the new sum as a response to the client. On the server side console, upon each visit by the client, the client's request and the new sum will be displayed.
2. Separate concerns on the client. On the client, all of the communication code will be placed in a method named &quot;add&quot;. In other words, the main method of the client will have no code related to client server communications. Instead, the main routine will simply call a local method named &quot;add&quot;. The client side &quot;add&quot; method will not perform any addition, instead, it will request that the server perform the addition. The &quot;add&quot; method will encapsulate or hide all communication with the server. It is within the &quot;add&quot; method where we actually work with sockets. This is a variation of what is called a &quot;proxy design&quot;. The &quot;add&quot; method is serving as a proxy for the server. When your code makes a call on the local "add" method, you are actually making a remote procedure call (RPC). The client side &quot;add&quot; method has the following signature:

```
public static int add(int i)

```

3. Separate concerns on the server. Your code that listens for a socket connection should be separate from the code that performs the add operation. In other words, the actual arithmetic should be done in a separate method. The UDP socket communication code will make calls to this method.

4. Write a client and server that has the following client side interaction with a user:

```
The client is running.
Please enter server port:
6789

3
The server returned 3.
2
The server returned 5.
-1
The server returned 4.
6
The server returned 10.
halt!
Client side quitting.

If the client is restarted (note that the server is still running) we have:
The client is running.
Please enter server port:
6789
1
The server returned 11.
halt!
Client side quitting.

```

5. On the server, the console will show an interaction like the following:

```
Server started
Adding: 3 to 0
Returning sum of 3 to client

Adding: 2 to 3
Returning sum of 5 to client

etc...

```

Note: UDP messages are made up of byte arrays. You will need to take an int and place it into a four byte (32 bit) byte array before sending. When receiving, you will need to extract an int from the byte array. You may use code from external sources to help you do this. But be careful to site your sources with a clear URL.

Another approach would be to only transmit byte arrays containing String data. You may use either approach.

:checkered_flag:**On your single pdf, make a copy of your client and label it clearly as "Project2Task2Client".**

:checkered_flag:**On your single pdf, make a copy of your server and label it clearly as "Project2Task2Server".**

:checkered_flag:**Take a screenshot of your client console screen. It will include five integer inputs (1,2,-3,4, and 5) and show the sums as they arrive back from the server. It will also show the client being stopped, and re-run a second time with the inputs (6,7,-8,9, and 10) and the client's response to a request by the user to &quot;halt!&quot;. On your single pdf, label this screenshot as "Project2Task2ClientConsole".**

:checkered_flag:**Take a screenshot of your server console screen. It will include the 10 lines of data being sent by the client and the server's responses. On your single pdf, label this screenshot as "Project2Task2ServerConsole".**


## Task 3 maintains server state using UDP. Name the IntelliJ project "Project2Task3"

0. Name the client "RemoteVariableClientUDP.java". Name the server "RemoteVariableServerUDP.java".

1. Modify your work in Task 2 so that the client may request either an &quot;add&quot; or &quot;subtract&quot; or &quot;get&quot; operation be performed by the server. The &quot;add&quot; and &quot;subtract&quot; operations are not idempotent but the &quot;get&quot; operation is idempotent. In addition, each request will pass along an integer ID. This ID is used to uniquely identify the user. Thus, the client will form a packet with the following values: ID, operation (add or subtract or get), and value (if the operation is other than get). The server will carry out the correct computation (add or subtract or get) using the sum associated with the ID found in each request. The client will be menu driven and will repeatedly ask the user for the user ID, operation, and value (if not a get request). When the operation is &quot;get&quot;, the value held on the server is simply returned. When the operation is &quot;add&quot; or &quot;subtract&quot; the server performs the operation and returns the sum. During execution, the client will display each returned value from the server to the user. If the server receives an ID that it has not seen before, that ID will initially be associated with a sum of 0. ID's will range between 0 and 999.

2. On the server, you will need to map each ID to the value of a sum. Different ID&#39;s may be presented and each will have its own sum. The server is given no prior knowledge of what ID&#39;s will be transmitted to it by the client. You may only assume that ID&#39;s are positive integers. You are required to store each ID and its associated sum, in a Java TreeMap.

The client side menu will provide an option to exit the client. Exiting the client has no impact on the server. Here is an example client side interaction:

```
The client is running.
Please enter server port:
6789

1. Add a value to your sum.
2. Subtract a value from your sum.
3. Get your sum.
4. Exit client
1
Enter value to add:
5
Enter your ID:
102
The result is 5.

1. Add a value to your sum.
2. Subtract a value from your sum.
3. Get your sum.
4. Exit client.
1
Enter value to add:
14
Enter your ID:
102
The result is 19.

1. Add a value to your sum.
2. Subtract a value from your sum.
3. Get your sum.
4. Exit client.
1
Enter value to add:
10
Enter your ID:
199
The result is 10.

1. Add a value to your sum.
2. Subtract a value from your sum.
3. Get your sum.
4. Exit client.
3
Enter your ID:
102
The result is 19.

1. Add a value to your sum.
2. Subtract a value from your sum.
3. Get your sum.
4. Exit client.
2
Enter value to subtract:
-3
Enter your ID:
199
The result is 13.

1. Add a value to your sum.
2. Subtract a value from your sum.
3. Get your sum.
4. Exit client.
4
Client side quitting. The remote variable server is still running.

```

3. As you did in Task 2, use a proxy design to encapsulate the communication code.

:checkered_flag:**On your single pdf, make a copy of your client and label it clearly as "Project2Task3Client".**

:checkered_flag:**On your single pdf, make a copy of your server and label it clearly as "Project2Task3Server".**

:checkered_flag:**Take a screenshot of your client console screen. Show three different clients interacting with the server using three distinct ID&#39;s. Each client will perform one addition, one subtraction, and finally a get request. It will also show the client being stopped, and re-run a second time with get requests from each of the three clients.  On your single pdf, label this screenshot as "Project2Task3ClientConsole".**

:checkered_flag:**Take a screenshot of your server console. It will show each visitor's ID, the operation requested, and the value of the variable being returned. On your single pdf, label this screenshot as "Project2Task3ServerConsole".**

## Task 4 maintains server state using TCP. Name the IntelliJ project "Project2Task4".

0. This is almost the same task as Task 3. The only difference is you will use TCP rather than UDP. Make the necessary modifications to EchoServerTCP.java and EchoClientTCP.java (from Coulouris) so that they behave the same way as does your solution to Task 3. Rename these files "RemoteVariableClientTCP.java" and "RemoteVariableServerTCP.java".

1. As in Task 3, be sure to use a **proxy design** to encapsulate the communication code. This requires a re-organization of the code but it is important to separate concerns.


EchoServerTCP.java from Coulouris text

```
import java.net.*;
import java.io.*;
import java.util.Scanner;

public class EchoServerTCP {

    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            int serverPort = 7777; // the server port we are using

            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);

            /*
             * Block waiting for a new connection request from a client.
             * When the request is received, "accept" it, and the rest
             * the tcp protocol handshake will then take place, making
             * the socket ready for reading and writing.
             */
            clientSocket = listenSocket.accept();
            // If we get here, then we are now connected to a client.

            // Set up "in" to read from the client socket
            Scanner in;
            in = new Scanner(clientSocket.getInputStream());

            // Set up "out" to write to the client socket
            PrintWriter out;
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));

            /*
             * Forever,
             *   read a line from the socket
             *   print it to the console
             *   echo it (i.e. write it) back to the client
             */
            while (true) {
                String data = in.nextLine();
                System.out.println("Echoing: " + data);
                out.println(data);
                out.flush();
            }

        // Handle exceptions
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());

        // If quitting (typically by you sending quit signal) clean up sockets
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }
}
```

EchoClientTCP.java from Coulouris text

```
import java.net.*;
import java.io.*;

public class EchoClientTCP {

    public static void main(String args[]) {
        // arguments supply hostname
        Socket clientSocket = null;
        try {
            int serverPort = 7777;
            clientSocket = new Socket(args[0], serverPort);

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));


            BufferedReader typed = new BufferedReader(new InputStreamReader(System.in));
            String m;
            while ((m = typed.readLine()) != null) {
                out.println(m);
                out.flush();
                String data = in.readLine(); // read a line of data from the stream
                System.out.println("Received: " + data);
            }
        } catch (IOException e) {
            System.out.println("IO Exception:" + e.getMessage());
        } finally {
            try {
                if (clientSocket != null) {
                    clientSocket.close();
                }
            } catch (IOException e) {
                // ignore exception on close
            }
        }
    }
}
```

:checkered_flag:**On your single pdf, make a copy of your client and label it clearly as "Project2Task4Client".**

:checkered_flag:**On your single pdf, make a copy of your server and label it clearly as "Project2Task4Server".**

:checkered_flag:**Take a screenshot of your client console screen. Show three different clients interacting with the server using three distinct ID&#39;s. Each client will perform one addition, one subtraction, and finally a get request. It will also show the client being stopped, and re-run a second time with get requests from each of the three clients.  On your single pdf, label this screenshot as "Project2Task4ClientConsole".**

:checkered_flag:**Take a screenshot of your server console screen. It will show each visitor's ID, the operation requested, and the value of the variable being returned. On your single pdf, label this screenshot as "Project2Task4ServerConsole".**

## Task 5 illustrates client authentication using signatures. Name the IntelliJ project "Project2Task5".

Before starting this task, study the three programs below. RSAExample.java shows how you can generate RSA keys in Java. ShortMessageSign.java and ShortMessageVerify.java shows you how you can sign and check the signature on very small messages.   

This Task is modeled after the way an Ethereum blockchain client signs requests.

Make the following modifications to your work in Task 4.

0. Rename these files "SigningClientTCP.java" and "VerifyingServerTCP.java".

1. As before, the client will be interactive and menu driven. It will transmit add or subtract or get requests to the server, along with the ID computed in 3 below, and provide an option to exit.

2. We want to send signed request from the client. Each time the client program runs, it will create new RSA public and private keys and **display** these keys to the user. See the RSAExample.java program below for guidance on how to build these keys. It is fine to use the code that you find in RSAExample.java (with citations, of course). After the client program creates and displays these keys, it interacts with the user and the server.

3. The client&#39;s ID will be formed by taking the least significant 20 bytes of the hash of the client&#39;s public key. Note: an RSA public key is the pair e and n. Prior to hashing, you will combine these two integers with concatenation. Unlike in Task 4, we are no longer prompting the user to enter the ID – the ID is computed in the client code. As in Bitcoin or Ethereum, the user's ID is derived from the public key.

4. The client will also transmit its public key with each request. Again, note that this key is a combination of e and n. These values will be transmitted in the clear and will be used by the server to verify the signature.

5. Finally, the client will sign each request. So, by using its private key (d and n), the client will encrypt the hash of the message it sends to the server. The signature will be added to each request. It is very important that the big integer created with the hash (before signing) is positive. RSA does not work with negative integers. See details in the code of ShortMessageSign.java and ShortMessageVerify.java below. You may use this code if cited.

6. The server will make two checks before servicing any client request. First, does the public key (included with each request) hash to the ID (also provided with each request)? Second, is the request properly signed? If both of these are true, the request is carried out on behalf of the client. The server will add, subtract or get. Otherwise, the server returns the message &quot;Error in request&quot;.
7. By studying ShortMessageVerify.java and ShortMessageSign.java you will know how to compute a signature. Your solution, however, will not use the short message approach as exemplified there. Note that we are not using any Java crypto API&#39;s that abstract away the details of signing.
8. We will use SHA-256 for our hash function h(). To clarify further:

The client will send the id: last20BytesOf(h(e+n)), the public key: e and n in the clear, the operation (add, get, or subtract), the operand, and the signature E(h(all prior tokens),d). The signature is thus an encrypted hash. It is encrypted
using d and n - the client&#39;s private key. E represents standard RSA encryption. The function h(e+n) is the SHA-256 hash of e concatenated with n.

During one client session, the ID will always be the same. If the client quits and restarts, it will have a new ID and operate on a new sum. The server is left running and survives client restarts.

As before, use a **proxy design** to encapsulate the communication code.

Produce a screen shot illustrating a successful execution and submit the screenshot in the description folder as described at the end of this document. The screen shot will show three different clients interacting with the server using three distinct ID&#39;s.

:checkered_flag:**On your single pdf, make a copy of your client and label it clearly as "Project2Task5Client".**

:checkered_flag:**On your single pdf, make a copy of your server and label it clearly as "Project2Task5Server".**

:checkered_flag:**Take a screenshot of your client console screen. Show a single client interacting with the server using the keys generated when the client code is run. All of the client's key material must be displayed on the client side console. Display the private key (d and n) and the public key (e and n). The client will perform one addition, one subtraction, and finally a get request. On your single pdf, label this screenshot as "Project2Task5ClientConsole".**

:checkered_flag:**Take a screenshot of your server console screen. It will show each visitor's public key material (e and n) and whether or not the signature is verified, the operation requested, and the value of the variable being returned. On your single pdf, label this screenshot as "Project2Task5ServerConsole".**

RSAExample.java - Key generation and sample encryption and decryption

```
/* Demonstrate RSA in Java using BigIntegers */

package edu.cmu.andrew.mm6;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 *  RSA Algorithm from CLR
 *
 * 1. Select at random two large prime numbers p and q.
 * 2. Compute n by the equation n = p * q.
 * 3. Compute phi(n)=  (p - 1) * ( q - 1)
 * 4. Select a small odd integer e that is relatively prime to phi(n).
 * 5. Compute d as the multiplicative inverse of e modulo phi(n). A theorem in
 *    number theory asserts that d exists and is uniquely defined.
 * 6. Publish the pair P = (e,n) as the RSA public key.
 * 7. Keep secret the pair S = (d,n) as the RSA secret key.
 * 8. To encrypt a message M compute C = M^e (mod n)
 * 9. To decrypt a message C compute M = C^d (mod n)
 */

public class RSAExample {

        public static void main(String[] args) {
                // Each public and private key consists of an exponent and a modulus
                BigInteger n; // n is the modulus for both the private and public keys
                BigInteger e; // e is the exponent of the public key
                BigInteger d; // d is the exponent of the private key

                Random rnd = new Random();

                // Step 1: Generate two large random primes.
                // We use 400 bits here, but best practice for security is 2048 bits.
                // Change 400 to 2048, recompile, and run the program again and you will
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

                System.out.println(" e = " + e);  // Step 6: (e,n) is the RSA public key
                System.out.println(" d = " + d);  // Step 7: (d,n) is the RSA private key
                System.out.println(" n = " + n);  // Modulus for both keys

                // Encode a simple message. For example the letter 'A' in UTF-8 is 65
                BigInteger m = new BigInteger("65");

                // Step 8: To encrypt a message M compute C = M^e (mod n)
                BigInteger c = m.modPow(e, n);

                // Step 9: To decrypt a message C compute M = C^d (mod n)
                BigInteger clear = c.modPow(d, n);
                System.out.println("Cypher text = " + c);
                System.out.println("Clear text = " + clear); // Should be "65"

                // Step 8 (reprise) Encrypt the string 'RSA is way cool.'
                String s = "RSA is way cool.";
                m = new BigInteger(s.getBytes()); // m is the original clear text
                c = m.modPow(e, n);     // Do the encryption, c is the cypher text

                // Step 9 (reprise) Decrypt...
                clear = c.modPow(d, n); // Decrypt, clear is the resulting clear text
                String clearStr = new String(clear.toByteArray());  // Decode to a string

                System.out.println("Cypher text = " + c);
                System.out.println("Clear text = " + clearStr);

        }
}

```

ShortMessageSign.java - Signing

```
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/** ShortMessageSign.java provides capabilities to sign
 *  very short messages. These messages are 4 hex digits.
 *  ShortMessageSign has three private members: RSA e,d and n.
 *  These are all very small java BigIntegers. ShortMessageSign is
 *  only used for instructional purposes.
 *
 *  For signing: the ShortMessageSign object is constructed with RSA
 *  keys (e,d,n). These keys are not created here but are passed in by the caller.
 *  Then, a caller can sign a message - the string returned by the sign
 *  method is evidence that the signer has the associated private key.
 *  After a message is signed, the message and the string may be transmitted
 *  or stored.
 *  The signature is represented by a base 10 integer.
 */


public class ShortMessageSign {

    private BigInteger e,d,n;

    /** A ShortMessageSign object may be constructed with RSA's e, d, and n.
     *  The holder of the private key (the signer) would call this
     *  constructor. Only d and n are used for signing.
     */
    public ShortMessageSign(BigInteger e, BigInteger d, BigInteger n) {
        this.e = e;
        this.d = d;
        this.n = n;
    }

    /**
     * Signing proceeds as follows:
     * 1) Get the bytes from the string to be signed.
     * 2) Compute a SHA-1 digest of these bytes.
     * 3) Copy these bytes into a byte array that is one byte longer than needed.
     *    The resulting byte array has its extra byte set to zero. This is because
     *    RSA works only on positive numbers. The most significant byte (in the
     *    new byte array) is the 0'th byte. It must be set to zero.
     * 4) Create a BigInteger from the byte array.
     * 5) Encrypt the BigInteger with RSA d and n.
     * 6) Return to the caller a String representation of this BigInteger.
     * @param message a sting to be signed
     * @return a string representing a big integer - the encrypted hash.
     * @throws Exception
     */
    public String sign(String message) throws Exception {

        // compute the digest with SHA-256
        byte[] bytesOfMessage = message.getBytes("UTF-8");
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bigDigest = md.digest(bytesOfMessage);

        // we only want two bytes of the hash for ShortMessageSign
        // we add a 0 byte as the most significant byte to keep
        // the value to be signed non-negative.
        byte[] messageDigest = new byte[3];
        messageDigest[0] = 0;   // most significant set to 0
        messageDigest[1] = bigDigest[0]; // take a byte from SHA-256
        messageDigest[2] = bigDigest[1]; // take a byte from SHA-256

        // The message digest now has three bytes. Two from SHA-256
        // and one is 0.

        // From the digest, create a BigInteger
        BigInteger m = new BigInteger(messageDigest);

        // encrypt the digest with the private key
        BigInteger c = m.modPow(d, n);

        // return this as a big integer string
        return c.toString();
    }

    public static void main(String args[]) throws Exception {

        // Test driver for ShortMessageSign

        // Since we are signing only very short messages, we can generate some really small keys.
        // The keys were generated by the RSA algorithm
        // p and q were 20 bits each.
        // In practice, the keys would be larger.
        BigInteger e = new BigInteger("65537");
        BigInteger d = new BigInteger("5420920152787448033");
        BigInteger n = new BigInteger("9013594933187057813");

        ShortMessageSign sov = new ShortMessageSign(e,d,n);

        Scanner sc = new Scanner(System.in);

        // Get some data to sign
        System.out.println("Enter data to be signed (at most 4 hex digits)");
        System.out.println("All data will be converted to lower case");
        String inStr = sc.nextLine();
        if(inStr.length() != 4) {
            System.out.println("Error in input " + inStr);
            return;
        }
        inStr = inStr.toLowerCase();

        String signedVal = sov.sign(inStr);
        System.out.println("Signed Value");
        System.out.println(signedVal);


    }
    // From Stack overflow
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}

```

ShortMessageVerify - Signature verification

```
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/** ShortMessageVerify.java provides capabilities to verify
 *  very short messages. These messages are 4 hex digits.
 *  ShortMessageVerify has two private members: RSA e and n.
 *  These are all very small java BigIntegers. ShortMessageVerify is
 *  only used for instructional purposes.
 *
 *
 *  For verification: the object is constructed with keys (e and n). The verify
 *  method is called with two parameters - the string to be checked and the
 *  evidence that this string was indeed manipulated by code with access to the
 *  private key d.
 *  The message that is signed or verified is 4 hex digits.
 *  The signature is represented by a base 10 integer.
 */


public class ShortMessageVerify {

    private BigInteger e,n;

    /** For verifying, a SignOrVerify object may be constructed
     *   with a RSA's e and n. Only e and n are used for signature verification.
     */
    public ShortMessageVerify(BigInteger e, BigInteger n) {
        this.e = e;
        this.n = n;
    }

    /**
     * Verifying proceeds as follows:
     * 1) Decrypt the encryptedHash to compute a decryptedHash
     * 2) Hash the messageToCheck using SHA-256 (be sure to handle
     *    the extra byte as described in the signing method.)
     * 3) If this new hash is equal to the decryptedHash, return true else false.
     *
     * @param messageToCheck  a normal string (4 hex digits) that needs to be verified.
     * @param encryptedHashStr integer string - possible evidence attesting to its origin.
     * @return true or false depending on whether the verification was a success
     * @throws Exception
     */
    public boolean verify(String messageToCheck, String encryptedHashStr)throws Exception  {

        // Take the encrypted string and make it a big integer
        BigInteger encryptedHash = new BigInteger(encryptedHashStr);
        // Decrypt it
        BigInteger decryptedHash = encryptedHash.modPow(e, n);

        // Get the bytes from messageToCheck
        byte[] bytesOfMessageToCheck = messageToCheck.getBytes("UTF-8");

        // compute the digest of the message with SHA-256
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        byte[] messageToCheckDigest = md.digest(bytesOfMessageToCheck);

        // messageToCheckDigest is a full SHA-256 digest
        // take two bytes from SHA-256 and add a zero byte
        byte[] extraByte = new byte[3];
        extraByte[0] = 0;
        extraByte[1] = messageToCheckDigest[0];
        extraByte[2] = messageToCheckDigest[1];

        // Make it a big int
        BigInteger bigIntegerToCheck = new BigInteger(extraByte);

        // inform the client on how the two compare
        if(bigIntegerToCheck.compareTo(decryptedHash) == 0) {

            return true;
        }
        else {
            return false;
        }
    }


    public static void main(String args[]) throws Exception {

        // Test driver for ShortMessageVerify

        // ShortMessageVerify may use some really small keys
        // The keys were generated by the RSA algorithm
        // p and q were 20 bits each
        BigInteger e = new BigInteger("65537");

        BigInteger n = new BigInteger("9013594933187057813");

        ShortMessageVerify verifySig = new ShortMessageVerify(e,n);

        Scanner sc = new Scanner(System.in);

        // Check an existing signature

        System.out.println("Enter hash that was signed (4 hex digits)");
        System.out.println("All data will be converted to lower case");
        String data = sc.nextLine();
        if(data.length() != 4) {
            System.out.println("Invalid input");
            System.exit(0);
        }
        data = data.toLowerCase();
        System.out.println("Enter an integer representing the signature");
        String sig = sc.nextLine();
        if(verifySig.verify(data, sig)) {
            System.out.println("Valid signature");
        }
        else {
            System.out.println("invalid signature");
        }

    }
    // from Stack overflow
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}
```
