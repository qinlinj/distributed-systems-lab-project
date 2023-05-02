# 95-702 Distributed Systems
# JMS Lab

As discussed in class, TomEE comes packaged with the message oriented middleware, Apache ActiveMQ.  You can use @resource annotations and JMS to access ActiveMQ.
## Task 1 – Single Queue

Task 1 just requires cloning a GitHub repository directly into IntelliJ.

1. You are reading the readme.md of a GitHub repository. From this repository, click "Code", then the "Local" tab, and copy the URL to Clone the repository.
2. Start IntelliJ, and from the "Welcome to IntelliJ IDEA" window, choose "Get from Version Control (VCS)".  Or if you have other projects already open in IntelliJ, you can use the File menu to select New ... Project from Version Control.
3. Paste the GitHub repository URL into the dialog box (change the Directory if you like) and select "Clone".
4. When asked whether to "Trust Maven Project", click "Trust Project".
5. A popup window may say "Frameworks Detected", but you can ignore and close it.
6. Once the project has fully loaded, click on Run, and you should eventually be sent to a browser with the URL `http://localhost:8080/lab-10/`  
(You may run into configuration issues to resolve regarding choosing your level of Java and TomEE.  Adjust them to fit your configuration.)
7. In the browser, enter a message in the input box, replacing "Enter text here", and click on "Submit text to servlet".
8. You should get a message that your text has been written to the queue.
9. Look at the IntelliJ TomEE console and you should see two messages
 - `"Servlet sent" your text "to jms/myQueue"`
 - `"MyQueueListener received:" your text`

What you have running is shown in the following diagram:
 ![Task 1 Flow](diagrams/task1.png)        

**_Code is poetry._**  
Study the code in the MyQueueWriter as a poem, understanding the meaning of each line.
 - What class does MyQueueWriter extend? (You have worked with those before.)  
 - By reading the comments and the code, note how the JMS architecture is used as described in the diagrams in class this week.  (You can refer back to the diagrams from the class slides also.)

Also study the code in MyQueueListener.
 - It implements MessageListener, which we discussed in class.  
 - Whenever a message is available in the Queue, its onMessage method is called.  
 - Note what Queue is being listened to, and how that linkage is set up.

Neither of these programs are very long, but it is important to understand how the code works and achieve the communication represented in the diagram above.

## Task 2 – Two Phase Process

Now that you understand how to send to a Queue, and how to listen to a Queue, lets create the following two phase process:

![Task 2 Flow](diagrams/task2.png)

Modify your project in the following ways:
1. In the same directory as MyQueueListener, create a new class called MyQueueListener2 modeled after MyQueueListener, but have it listen to a new Queue named jms/myQueue2. Modify the string written to the console so you know the string is coming from MyQueueListener2 (not MyQueueListener).
2. Modify the original MyQueueListener to add text to the message it receives (e.g. <received text> + " after processing by MyQueueListener") and then send the new message to jms/myQueue2. You can find how to write to a Queue from the code in MyQueueWriter.

Once you get it working correctly, you should see the console messages of the original text input in the browser, sent to myQueue, then appended to, sent to myQueue2, and finally also printed by MyQueueListener2.

## Task 3 – Servlet Reading From a Queue

Finally, we will create a second Servlet that synchronously reads from a queue.
![Task 3 Flow](diagrams/task3.png)

Note that the FetchResponses Servlet is not a Listener, rather it synchronously tries to `receive` from the Queue.

1. Modify MyQueueListener to write to myQueue3 instead of myQueue2.
2. Create a new Servlet called FetchResponses that reads all available messages in myQueue3 and displays them on a web page.  
 - If no messages are available, the servlet should clearly state that on the response page.
 - If there are one or more messages in the Queue, all should be displayed.

 Test Task 3 by using MyQueueWriter a few times, then use FetchResponses to retrieve the messages.  

 Here are some code hints for synchronously receiving from a Queue:

```
// The following 5 variables should be instance variables:

// Lookup the ConnectionFactory using resource injection and assign to cf
@Resource(mappedName = "jms/myConnectionFactory")
private ConnectionFactory cf;

// Lookup the Queue using resource injection and assign to q
@Resource(mappedName = "jms/myQueue3")
private Queue q;

private Connection con;
private Session session;
private MessageConsumer reader;

// --------------------------------------------------
// Within the Servlet's init(), initialize the remaining 3 instance variable
// With the ConnectionFactory, establish a Connection
con = cf.createConnection();
// Establish a Session on that Connection
session = con.createSession(false, Session.AUTO_ACKNOWLEDGE);
// Be sure to start to connection
con.start();
// Create a MessageConsumer that will read from q
reader = session.createConsumer(q);

// --------------------------------------------------
// Then within the doGet() method, here is the code to receive from a queue
// You need to add the code to handle the HTTP request and make the response.

/*
 * You can now try to receive a message from q.  If you give a
 * time argument to receive, it will time out in that many milliseconds.
 * In this way you can receive until there are no more messages to be read
 * at this time from the q.
 */
TextMessage tm = null;
while ((tm = (TextMessage) reader.receive(1000)) != null) {
    // Do something with tm.getText() to add it to the HTTP response
}

```
:checkered_flag: **FINISH: Show a working Task 3 to any TA (not just your lab TA) before next Wednesday 2:00pm**
