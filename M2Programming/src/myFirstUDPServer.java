import java.net.*;  // for DatagramSocket, DatagramPacket, and InetAddress
import java.io.*;   // for IOException

public class myFirstUDPServer {
    private static final int ECHOMAX = 255;  // Maximum size of echo datagram

    public static void main(String[] args) throws IOException {

        if (args.length != 1)  // Test for correct argument list
            throw new IllegalArgumentException("Parameter(s): <Port>");

        int servPort = Integer.parseInt(args[0]);

        DatagramSocket socket = new DatagramSocket(servPort);
        DatagramPacket packet = new DatagramPacket(new byte[ECHOMAX], ECHOMAX);
        DatagramPacket newPacket;

        for (;;) {  // Run forever, receiving and echoing datagrams
            socket.receive(packet);     // Receive packet from client
            System.out.println("Handling client at " +
                    packet.getAddress().getHostAddress() + " on port " + packet.getPort());

            // Convert received bytes to String
            String message = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Received Message: " + message);

            // Remove consonants from String and create new byte buffer
            String newMessage = removeConsonants(message);
            byte[] newBytes = newMessage.getBytes();

            System.out.println("DeConsonnantized Message: " + newMessage);
            System.out.println();

            newPacket = new DatagramPacket(newBytes, newBytes.length,
                                        packet.getAddress(), packet.getPort());
            socket.send(newPacket);       // Send the modified packet back to client
            packet.setLength(ECHOMAX); // Reset length to avoid shrinking buffer
        }
        /* NOT REACHED */
    }

    // Used to remove consonants from message received/sent
    public static String removeConsonants(String message) {
        return message.replaceAll("[BCDFGHJKLMNPQRSTVWXZbcdfghjklmnpqrstvwxz]", "");
    }

}
