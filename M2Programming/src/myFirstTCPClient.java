import java.net.*;  // for Socket
import java.io.*;   // for IOException and Input/OutputStream
import java.util.Scanner; // for Scanner object to collect user input

public class myFirstTCPClient {

    public static void main(String[] args) throws IOException {
        // Variables needed for collecting round trip time
        long start;
        long elapsedTime;

        if (args.length != 2) {  // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Server> [<Port>]");
        }

        String server = args[0]; // Server name or IP address

        int servPort = Integer.parseInt(args[1]);

        // Create socket that is connected to server on specified port
        Socket socket = new Socket(server, servPort);
        System.out.println("Connected to server.");
        System.out.println();

        InputStream in = socket.getInputStream();
        OutputStream out = socket.getOutputStream();

        // Loop used to collect round trip time a fixed amount of times
        for (int i = 0; i < 8; i++) {
            // Prompt user for a sentence to send
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a sentence: ");
            String s = scan.nextLine();

            // Convert input String to bytes using the default character encoding
            byte[] byteBuffer = s.getBytes();
            out.write(byteBuffer);  // Send the encoded string to the server

            start = System.currentTimeMillis(); // Begin round trip time collection

            // Receive the same string back from the server
            int totalBytesRcvd = 0;  // Total bytes received so far
            int bytesRcvd;           // Bytes received in last read

            if ((bytesRcvd = in.read(byteBuffer, totalBytesRcvd,
                    byteBuffer.length - totalBytesRcvd)) == -1) {
                throw new SocketException("Connection close prematurely");
                }

            elapsedTime = System.currentTimeMillis() - start; // End round trip time collection

            System.out.println("Message Received: " + new String(byteBuffer, 0, bytesRcvd));
            System.out.println("Round Trip Time (message sent to response received): " +
                                elapsedTime + " ms");
            System.out.println();

        }
        socket.close();  // Close the socket and its streams
    }
}
