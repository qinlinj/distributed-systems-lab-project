# lab6-rmi-programming

# Distributed Systems RMI Programming

Suppose that we have a remote procedure or remote method that we want others to be able call from across a network. We might do that with low level socket programming - but that is a tedious task. We might prefer to work at a higher level. Two general approaches exist: Remote Procedure Calls (RPC) and Remote Method Invocation (RMI). In this lab, we will work with Java's RMI.

Remote Method Invocation is similar in nature to Remote Procedure Calls. RMI, as opposed to RPC, is found in the world of object oriented programming and distributed objects. RPC is associated with the more traditional procedural programming languages.

In both cases, the application developer is free to focus on the business application and, to a large extent, is able to ignore all of the details that are associated with how messages are passed over the network. This separation of concerns is important.

Binary approaches to RPC or RMI are typically far faster than web service based solutions - using HTTP along with JSON or XML messages. If a binary message is passed from one machine to another, and if the two machines are using the same programming language, very little translation work needs to be done. This may result in a much faster distributed system - there is no need to translate text (XML or JSON) into a binary format on the receiving end.

In lab work, it is OK to work with others. However, in the end, you should have a working solution running on your own laptop.

Note: Before working through this lab, make sure all of your firewalls are turned off. You should turn them back on after class. Also, make sure your java -version and javac -version produce the same result. During compilation, if you get a class not found error, it is probably wise to visit your environment variables and delete your "classpath" variable. Or, add a "." to the classpath variable. This will tell java to look for classes in the current directory.

It is suggested that you do not use an IDE for this lab. You are encouraged to use a simple text editor and the command line tooling (javac, java and rmiregistry).

# Part 1. An Introductory Java RMI example

The first part of this lab takes you through the steps of deploying a simple Java RMI application. It is suggested that you complete this introductory exercise before attempting part 2.

Part 1 is a very simple Java RMI application that is designed to work from the command line. If you are working on a Windows PC, you will need to have the java bin directory in your "path" (not "classpath") variable.

1. Create two directories from a DOS or Unix prompt. Name one directory 'RMILabServer' and the other directory 'RMILabClient'.
2. Within the server directory, we want to make an interface available. An interface describes the methods available for remote clients to call. Save this interface as Calculator.java.

```
//************************************************************
// Calculator.java                Interface for a Calculator
import java.rmi.*;
public interface Calculator extends Remote {
   // this method will be called from remote clients
   int add (int x, int y) throws RemoteException;
}
```



1. Within the server directory, we want to make the remote object (that implements the calculator interface) available to clients. Save this as CalculatorServant.java.

```
//*************************************************************
// CalculatorServant.java
// A Remote object class that implements Calculator.
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
public class CalculatorServant extends UnicastRemoteObject implements Calculator {
       public CalculatorServant() throws RemoteException {
       }
       public int add(int x, int y) throws RemoteException {
            System.out.println("Got request to add " + x + " and " + y);

            return x + y;
    }
}
```



1. Within the server directory, we need a program to create the servant object and provide it with a name in the rmi registry. Save this file as CalculatorServer.java.

```
//**************************************************************
// CalculatorServer.java             Serve remote Calculator
// Creates a calculator object and gives
// it a name in the registry.
import java.rmi.*;
public class CalculatorServer {
	public static void main(String args[]){
          System.out.println("Calculator Server Running");
          try{
            // create the servant
            Calculator c = new CalculatorServant();
            System.out.println("Created Calculator object");
            System.out.println("Placing in registry");
            // publish to registry
	          Naming.rebind("CoolCalculator", c);
            System.out.println("CalculatorServant object ready");
           }catch(Exception e) {
            System.out.println("CalculatorServer error main " + e.getMessage());
        }
    }
}
```



1. Compile all the server side code with the command "javac *.*".
2. Copy the interface Calculator.java to the client directory. The interface will now exist in both the server and client directories.
3. In the client directory, enter the program CalculatorClient.java.

```
//*******************************************************
// CalculatorClient.java
// This client gets a remote reference from the rmiregistry
// that is listening on the default port of 1099.
// It allows the client to quit with a "!".
// Otherwise, it computes the sum of two integers
// using the remote calculator.

import java.rmi.*;
import java.rmi.server.*;
import java.io.*;
import java.util.StringTokenizer;

public class CalculatorClient {
   public static void main(String args[]) throws Exception {
        BufferedReader in  =
                     new BufferedReader(
                         new InputStreamReader(System.in));
        // connect to the rmiregistry and get a remote reference to the Calculator
        // object.
        Calculator c  = (Calculator) Naming.lookup("//localhost/CoolCalculator");
 	System.out.println("Found calculator. Enter ! to quit");
   	while(true) {
           try {
                 // prompt the user
                 System.out.print("client>");
                 // get a line
                 String line  = in.readLine();
                 // if a "!" is entered just exit
                 if(line.equals("!")) System.exit(0);
                 // if it's not a return get the two integers and call add
                 // on the remote calculator.
                 if(!line.equals("")) {
                  StringTokenizer st = new StringTokenizer(line);
                  String v1 = st.nextToken();
                  String v2 = st.nextToken();
                  int i  = Integer.parseInt(v1);
                  int j  = Integer.parseInt(v2);
                  int sum = c.add(i,j);
                  System.out.println(sum);
                  }
               }

	      catch(RemoteException e) {
                   System.out.println("allComments: " + e.getMessage());
              }
	   }
    }
}
```



1. Open two different shells on the server side directory.
2. Within one of the two shells opened on the server side directory, start the rmiregistry with the command "rmiregistry".

​    Note: The rmi registry is providing a **naming service**. One client of the registry (the server on the server side) provides a name for the remote object. The other client of the registry (on the client side) accesses a **remote object reference** by providing the registry with the name. Naming services, such as the rmiregistry, are very common - in everyday life as well as in computing.

1. In the other shell opened on the server side directory, run the server with the command "java CalculatorServer". It should respond with:

```
Calculator Server Running
Created Calculator object
Placing in registry
CalculatorServant object ready
```



1. Compile the code on the client and run with the command "java CalculatorClient". The interaction appears as follows:

```
java CalculatorClient
Found calculator. Enter ! to quit
client>2 3
5
client>1 1
2
client>6 7
13
client>!
```



# Part 2. Java RMI Programming Challenge

1. Write a Java RMI service according to the following specification:

```
    // The servant class extends UnicastRemoteObject and implements Bumper

    // The server calls rebind on the rmiregistry giving the remote
    // object the name "bumper".

    // The remote bump() method behaves as follows:
    public boolean bump() throws RemoteException {
      // A call on bump() adds 1 to a BigInteger held by the service.
      // It then returns true on completion.
      // The BigInteger is changed by the call on bump(). That is,
      // 1 is added to the BigInteger and that value persists until
      // another call on bump occurs.
    }

    public BigInteger get() throws RemoteException {
      // a call on get returns the BigInteger held by the service
    }
```



1. Write a Java RMI client according to the following specification:

   The main routine performs a lookup on the rmi registry for a remote object that implements the Bumber interface. The object is called "bumper".

   The main routine creates a BigInteger (called ctr) initialized to 0. The main routine creates another BigInteger (called n) initialized to 10000. The main routine loops until the ctr equals n. Each time through the loop, ctr is incremented by 1. Each time through the loop, the remote method bump is called.

   Before the loop in the main routine begins, set a timer like this: long start = System.currentTimeMillis(); When the loop is finished, find the ending time like this: long stop = System.currentTimeMillis();

   At the end of the main routine, display the value of the BigInteger held on the server by calling the remote method get(). In addition, display the number of seconds that it took to call this service 10,000 times.