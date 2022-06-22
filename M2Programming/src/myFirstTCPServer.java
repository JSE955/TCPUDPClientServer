import java.net.*;  // for Socket, ServerSocket, and InetAddress
import java.io.*;   // for IOException and Input/OutputStream

public class myFirstTCPServer {

    private static final int BUFSIZE = 32;   // Size of receive buffer

    public static void main(String[] args) throws IOException {

        if (args.length != 1)  // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Port>");

        int servPort = Integer.parseInt(args[0]);

        // Create a server socket to accept client connection requests
        ServerSocket servSock = new ServerSocket(servPort);

        int recvMsgSize;   // Size of received message
        byte[] byteBuffer = new byte[BUFSIZE];  // Receive buffer

        for (;;) { // Run forever, accepting and servicing connections
            Socket clntSock = servSock.accept();     // Get client connection

            InputStream in = clntSock.getInputStream();
            OutputStream out = clntSock.getOutputStream();

            // Receive until client closes connection, indicated by -1 return
            while ((recvMsgSize = in.read(byteBuffer)) != -1) {
                System.out.println("Handling client at " +
                        clntSock.getInetAddress().getHostAddress() + " on port " +
                        clntSock.getPort());

                // Convert received bytes to String
                String message = new String(byteBuffer, 0, recvMsgSize);
                System.out.println("Received Message: " + message);

                // Remove consonants from String and create new byte buffer
                String newMessage = removeConsonants(message);
                byte[] newBuffer = newMessage.getBytes();

                System.out.println("DeConsonnantized Message: " + newMessage);
                System.out.println();

                out.write(newBuffer); // Send modified buffer back to client
            }

            clntSock.close();  // Close the socket.  We are done with this client!
        }
        /* NOT REACHED */
    }

    // Used to remove consonants from message received/sent
    public static String removeConsonants(String message) {
        return message.replaceAll("[BCDFGHJKLMNPQRSTVWXZbcdfghjklmnpqrstvwxz]", "");
    }


}
