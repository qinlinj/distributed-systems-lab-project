# Lab7-rest-programming

In this lab, we explore JAX-RS. This is an important JEE API that supports the building of RESTFul web services. Through annotations, JAX-RS allows us to match URL's and HTTP methods to particular Java methods. [You might want to browse the documentation on JAX-RS.](https://docs.oracle.com/javaee/6/tutorial/doc/giepu.html). The central idea is to associate a URL path and an HTTP verb with each Java method that we want to serve.

## Modifying a working client and server handling PUT, GET, and DELETE

In RESTful design, the same HTTP operation may be applied to URL's that are related to each other but differ in what resource each represents. In this lab, we experiment with using the HTTP GET with several URL's - one representing a variable name and value stored on the server and the other representing a set of variables stored on the server.

## Task 0

In Task 0, you will get the following code running in IntelliJ. Create a standard Java project named REST_Client_Project. Within that project, use the client side code provided here (in the package name, replace "mm6" with your andrew id):

### Client side code

```
// Client side code making calls to an HTTP service
// The service provides GET, DELETE, and PUT
// Simple example client storing and deleting name, value pairs on the server

package edu.cmu.andrew.mm6;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

// A simple class to wrap an RPC result.
class Result {
    private int responseCode;
    private String responseText;

    public int getResponseCode() { return responseCode; }
    public void setResponseCode(int code) { responseCode = code; }
    public String getResponseText() { return responseText; }
    public void setResponseText(String msg) { responseText = msg; }

    public String toString() { return responseCode + ":" + responseText; }
}
public class Main {

    public static void main(String[] args) throws Exception {

        System.out.println("Begin main of REST lab.");
        System.out.println("Assign 100 to the variable named x");
        System.out.println(assign("x", "100"));

        System.out.println("Assign 199 to the variable named x");
        System.out.println(assign("x", "199"));
        System.out.println("Send a request to read x");
        System.out.println(read("x"));


        System.out.println("Sending a DELETE request for x");
        System.out.println(clear("x"));
        System.out.println("x is deleted but let's try to read it");
        System.out.println(read("x"));


        // place a quote in some variables
        assign("Line1", "Computer Science is no more about computers\n ");
        assign("Line2", "than astronomy is about telescopes.\n");
        assign("Line3", "Edsger W. Dijkstra\n");

        // read them from the server
        System.out.println(read("Line1"));
        System.out.println(read("Line2"));
        System.out.println(read("Line3"));

        //Code commented out for the moment
        //System.out.println(getVariableList());

        System.out.println("End main of REST lab");

    }

    // Call doPut with name and value pair
    public static Result assign(String name, String value) {
        Result r = doPut(name, value);
        return r;
    }

    // Call doGet with a name
    public static Result read(String name) {
        Result r = doGet(name);
        return r;
    }

    // call doDelete with a name
    public static Result clear(String name) {
        Result r = doDelete(name);
        return r;
    }

    // Make an HTTP GET request
    public static Result doGet(String name) {

        HttpURLConnection conn;
        int status = 0;
        Result result = new Result();
        try {
            // GET wants us to pass the name on the URL line
            URL url = new URL("http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory/" + name);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // we are sending plain text
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/plain");

            // wait for response
            status = conn.getResponseCode();

            // set http response code
            result.setResponseCode(status);
            // set http response message - this is just a status message
            // and not the body returned by GET
            result.setResponseText(conn.getResponseMessage());

            if (status == 200) {
                String responseBody = getResponseBody(conn);
                result.setResponseText(responseBody);
            }

            conn.disconnect();

        }
        // handle exceptions
        catch (MalformedURLException e) {
            System.out.println("URL Exception thrown" + e);
        } catch (IOException e) {
            System.out.println("IO Exception thrown" + e);
        } catch (Exception e) {
            System.out.println("IO Exception thrown" + e);
        }
        return result;
    }


    // Make an HTTP PUT request and pass the name and value
    public static Result doPut(String name, String value) {

        HttpURLConnection conn;
        int status = 0;
        Result result = new Result();

        try {
            // establish the URL
            // Note, PUT does not use the URL line for its message to the server
            URL url = new URL("http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory/");
            conn = (HttpURLConnection) url.openConnection();
            // we are sending plain text
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            conn.setRequestProperty("Accept", "text/plain");
            // prepare to put
            conn.setRequestMethod("PUT");
            // we are sending data with this put request
            conn.setDoOutput(true);
            // write to the connection
            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
            // write name value pair
            out.write(name + "=" + value);
            out.close();

            // see how things went
            status = conn.getResponseCode();
            result.setResponseCode(status);
            result.setResponseText(conn.getResponseMessage());

            if (status == 200) {
                // things went well, gather up the response body
                String responseBody = getResponseBody(conn);
                result.setResponseText(responseBody);
            }

        }
        // handle exceptions
        catch (MalformedURLException e) {
            System.out.println("URL Exception thrown" + e);
        } catch (IOException e) {
            System.out.println("IO Exception thrown" + e);
        }

        // return result
        return result;
    }


    // Make an HTTP DELETE request with a name
    public static Result doDelete(String name) {

        HttpURLConnection conn;
        int status = 0;
        Result result = new Result();

        // Send an HTTP DELETE to server along with name on the URL line
        try {
            URL url = new URL("http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory/" + name);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("DELETE");
            // we are sending plain text
            conn.setRequestProperty("Content-Type", "text/plain; charset=utf-8");
            // tell the server what format we want back
            conn.setRequestProperty("Accept", "text/plain");
            // wait for response
            status = conn.getResponseCode();
            result.setResponseCode(status);
            result.setResponseText(conn.getResponseMessage());
            if (status == 200) {
                // things went well, gather up the response body
                String responseBody = getResponseBody(conn);
                result.setResponseText(responseBody);
            }
        }
        // handle exceptions
        catch (MalformedURLException e) {
            System.out.println("URL Exception thrown" + e);
        } catch (IOException e) {
            System.out.println("IO Exception thrown" + e);
        }
        return result;
    }

    // Gather up a response body from the connection
    // and close the connection.
    public static String getResponseBody(HttpURLConnection conn) {
        String responseText = "";
        try {
            String output = "";
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                responseText += output;
            }
            conn.disconnect();
        } catch (IOException e) {
            System.out.println("Exception caught " + e);
        }
        return responseText;
    }
}
```



### Server side code

1. Create a new project in IntelliJ named RESTServicePrj. This will be a Java Jakarta EE project. The Project template will be "REST Service". We are using TomEE and a recent Java SDK. Select Java and Maven.
2. Be sure to select a recent version of Jakarta EE in the version drop down box.
3. Select three dependencies: Context and Dependency Injection (CDI), RESTFul Web Services (JAX-RS), and Servlet.
4. In the Run/Debug Configurations, set the Open Browser URL to http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/hello-world
5. Run the RESTServicePrj service. A browser should display "hello world".
6. In the code of HelloResource class, there is a Path annotation. Change this from "hello-world" to "variable-memory".
7. In the Run/Debug Configurations, set the Open Browser URL to http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory
8. Run the RESTServicePrj service. A browser should again display "hello world".
9. Change the name of the Java HelloApplication file to "VariableApplication". Change the name of the Java "HelloResource" file to "VariableMemory".
10. Replace the existing code in the file "VariableMemory.java" with the following JAX-RS service.

```
// This is a JAX-RS service that allows visitors to store, retrieve, and delete
// name value pairs.

package com.example.restserviceprj;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

// handle requests to /variablememory

@Path("/variable-memory")
public class VariableMemory {

    // This map holds variable names and values
    private static Map memory = new TreeMap();

    // This GET will run on a visit to /variable-memory
    @GET
    @Produces("text/plain")
    public Response getDefault() {
        String defaultString = "Visited with /variable-memory add a /name to visit the other GET";
        System.out.println("GET request by visiting with /variable-memory");
        // generate a response
        return Response.status(200).entity(defaultString).build();
    }

    // This GET will run on a visit to /variable-memory/variableName
    @Path("{variableName}")
    @GET
    @Produces("text/plain")
    public Response getFromMemory(@PathParam("variableName") String variableName) {
        System.out.println("GET request by visiting with /variable-memory/" + variableName);
        String output = "";
        // get variable's value by name from the map
        Object lookUp = memory.get(variableName);
        // figure return value
        if (lookUp == null) output = "No variable named " + variableName + " found in memory";
        else output = (String) lookUp;
        // generate a response
        return Response.status(200).entity(output).build();
    }

    // This PUT will run with the body holding a name=value
    @PUT
    @Consumes("text/plain")
    @Produces(value = "text/plain")
    public Response storeInMemory(String fromVisitor) {
        System.out.println("From a call to put " + fromVisitor);
        // spit the name and the value from the visitor
        String[] input = fromVisitor.split("=");
        // store the pair in the map
        memory.put(input[0], input[1]);
        // report visit on the server's console
        System.out.println(input[0] + " stored with value " + input[1]);
        // return a response to the visitor
        return Response.status(200).entity(input[0] + " assigned the value "+ input[1]).build();

    }

    // This DELETE will run with the body holding a name
    @Path("{fromVisitor}")
    @DELETE
    @Produces("text/plain")
    public Response deleteFromMemory(@PathParam("fromVisitor") String fromVisitor) {
        System.out.println("Deleting " + fromVisitor);
        // remove the key value pair from the map
        memory.remove(fromVisitor);
        // report visit on the server's console
        System.out.println("Removing key " + fromVisitor + " from the map");
        // return a response to the visitor
        return Response.status(200).entity("DELETE completed on server").build();

    }

}
```



1. You should be able to visit the GET methods with a browser. Visit with the following URLs. This is for testing.

```
http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory
http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory/someName
```



1. With your restful service running, run the client side code from Task 0.



## Task 1

Take some time to study the working solution in Task 0.

In Task 1, you will modify the client so that it provides a method called getVariableList(). Note the call to the getVariableList() method (commented out) within the client side code. After modifying the server, remove the comment symbols from this call. That is, the method should actually work.

The getVariableList() method will call doGet and will retrieve a list of all of the names (but without their values) found on the server.

An additional Java method needs to be added to the server to handle visits to this new URL:

```
http://localhost:8080/RESTServicePrj-1.0-SNAPSHOT/api/variable-memory/list/variables
```



Note that the client must generate a GET request to this new URL. On the server, you will want to match the path "/list/variables".

Here is the client side execution of my solution. Note that "Line1Line2Line3" is a list of variable names that came back from the server.

```
Begin main of REST lab.
Assign 100 to the variable named x
200:x assigned the value 100
Assign 199 to the variable named x
200:x assigned the value 199
Send a request to read x
200:199
Sending a DELETE request for x
200:DELETE completed on server
x is deleted but let's try to read it
200:No variable named x found in memory
200:Computer Science is no more about computers
200:than astronomy is about telescopes.
200:Edsger W. Dijkstra
200:Line1Line2Line3
End main of REST lab
```