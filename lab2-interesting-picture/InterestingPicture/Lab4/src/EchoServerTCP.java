import java.net.*;
import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class EchoServerTCP {
    public static String ReadHTML(String htmlFile) {
        StringBuilder htmlContentBuilder = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new FileReader(htmlFile));
            String str;
            while ((str = in.readLine()) != null) {
                htmlContentBuilder.append(str);
            }
            in.close();
        } catch (IOException e) {
        }
        System.out.println(htmlContentBuilder.toString());
        return htmlContentBuilder.toString();
    }


    public static void main(String args[]) {
        Socket clientSocket = null;
        try {
            int serverPort = 7777; // the server port we are using
            // Create a new server socket
            ServerSocket listenSocket = new ServerSocket(serverPort);
            while (true) {
                /*
                 * Block waiting for a new connection request from a client.
                 * When the request is received, "accept" it, and the rest
                 * the tcp protocol handshake will then take place, making
                 * the socket ready for reading and writing.
                 */
                clientSocket = listenSocket.accept();
                // If we get here, then we are now connected to a client.
                // Set up "inFromSocket" to read from the client socket
                Scanner inFromSocket;
                inFromSocket = new Scanner(clientSocket.getInputStream());

                // Set up "outToSocket" to write to the client socket
                PrintWriter outToSocket;
                outToSocket = new PrintWriter(new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream())));
                String filePath;
                String fileName = null;

                /*
                 * Forever,
                 *   read a line from the socket
                 *   print it to the console
                 *   echo it (i.e. write it) back to the client
                 */
                String line = inFromSocket.nextLine();
                System.out.println(line);
                inFromSocket.nextLine();
                inFromSocket.reset();
                fileName = line.split(" ")[1].substring(1);
                filePath = "/Users/macbook/Desktop/Lab4/" + fileName;
                boolean indicate_file = false;
                outToSocket.flush();
                boolean fileExist = true;
                boolean readingFinish = false;

                if (fileExist) {
                    outToSocket.println("HTTP/1.1 200 OK");
                    outToSocket.println();
                    try {
                        BufferedReader in = new BufferedReader(new FileReader(fileName));
                        String str;
                        outToSocket.println("HTTP/1.1 200 OK");
                        while ((str = in.readLine()) != null) {
                            outToSocket.println(str);
                            outToSocket.flush();
                        }
                        if(!readingFinish){
                            System.out.println("HTML Read complete");
                            readingFinish = true;
                        }
                        in.close();
                    } catch (IOException e) {
                        outToSocket.println("HTTP/1.1 404 File Not Found\n\n");
                        outToSocket.flush();
                        outToSocket.close();
                    }
                    outToSocket.close();
                }
                else {
                    outToSocket.println("HTTP/1.1 404 File Not Found\n\n");
                    outToSocket.flush();
                    outToSocket.close();
                }
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
