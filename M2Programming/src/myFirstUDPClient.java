import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException
import java.util.Scanner; // for Scanner object to collect user input

public class myFirstUDPClient {

    private static final int TIMEOUT = 3000;   // Resend timeout (milliseconds)
    private static final int MAXTRIES = 5;     // Maximum retransmissions

    public static void main(String[] args) throws IOException {
        // Variables needed for collecting round trip time
        long start;
        long elapsedTime;

        if (args.length != 2) {  // Test for correct # of args
            throw new IllegalArgumentException("Parameter(s): <Server> [<Port>]");
        }

        InetAddress serverAddress = InetAddress.getByName(args[0]);  // Server address
        int servPort = Integer.parseInt(args[1]); // Server port

        DatagramSocket socket = new DatagramSocket();

        socket.setSoTimeout(TIMEOUT);  // Maximum receive blocking time (milliseconds)

        for (int i = 0; i < 8; i++) {
            // Prompt user for a sentence to send
            Scanner scan = new Scanner(System.in);
            System.out.println("Enter a sentence: ");
            String s = scan.nextLine();

            // Convert input String to bytes using the default character encoding
            byte[] bytesToSend = s.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(bytesToSend,  // Sending packet
                    s.length(), serverAddress, servPort);

            DatagramPacket receivePacket =                              // Receiving packet
                    new DatagramPacket(new byte[bytesToSend.length], bytesToSend.length);

            int tries = 0;      // Packets may be lost, so we have to keep trying
            boolean receivedResponse = false;
            do {
                socket.send(sendPacket); // Send the echo string
                start = System.currentTimeMillis(); // Begin round trip time collection
                try {
                    socket.receive(receivePacket);  // Attempt echo reply reception
                    if (!receivePacket.getAddress().equals(serverAddress)) {  // Check source
                        throw new IOException("Received packet from an unknown source");
                    }
                    receivedResponse = true;

                } catch (InterruptedIOException e) {  // We did not get anything
                    tries += 1;
                    System.out.println("Timed out, " + (MAXTRIES - tries) + " more tries...");
                }
                elapsedTime = System.currentTimeMillis() - start; // End round trip time collection
            } while ((!receivedResponse) && (tries < MAXTRIES));

            if (receivedResponse) {
                System.out.println("Message Received: " + new String(receivePacket.getData()));
                System.out.println("Round Trip Time (message sent to response received): " +
                        elapsedTime + " ms");
                System.out.println();
            }
            else {
                System.out.println("No response -- giving up.");
            }
        }
        socket.close();
    }
}
