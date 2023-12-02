import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.nio.charset.StandardCharsets;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Client {

    public static void main(String[] args) {
        // Initialise winsock
        WSADATA wsa = new WSADATA();
        System.out.print("Initialising Winsock...");
        if (WSAStartup(MAKEWORD(2, 2), wsa) != 0) {
            System.out.printf("Failed. Error Code: %d", WSAGetLastError());
            return;
        }
        System.out.print("Initialised.\n");

        // Create a socket
        DatagramSocket clientSocket = null;
        try {
            clientSocket = new DatagramSocket();

            // Set up server address information
            InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
            int serverPort = DefineConstants.PORT;

            while (true) {
                // Get user input for the command
                System.out.print("Enter command (execute/file w:/file a:/only allowed for the first client. file r: for every client), or just send a message: ");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String command = br.readLine();

                // Send the command to the server
                byte[] sendData = command.getBytes();
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
                clientSocket.send(sendPacket);

                // Receive and print the server's response
                byte[] receiveData = new byte[DefineConstants.BUFLEN];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                String serverResponse = new String(receivePacket.getData(), 0, receivePacket.getLength());
                System.out.printf("Server says: %s\n", serverResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closesocket(clientSocket);
            WSACleanup();
        }
    }
public class Client {
    static InetAdress dest;
    public static void main (String[] args) throws Exception {
        DatagramSocket clskt = new DatagramSocket();
        Scanner input new Scanner (System.in);
        int port = input.nextInt();
        System.out.println("Enter Destination Host name");
        String hostname = input.text();
        dest.getByName(hostname);
        int packetcount = 0;
        System.out.println("Enter the path of the file you want to send");
        String path = input.next();
        File initialFile = new File (path);
            FileInputStream targetStream = new FileInputStream(initialFile);
        int filesize = targetStream.available();
        byte[] data = new byte[1024];
        for(int i = 0; i <1024; i++){
            data[i]=(byte)targetStream.read();
        }
                DatagramPacket clpkt = new 
        DatagramPacket(data,data.length,dest,port);
        packetcount++;
        clskt.send(clpkt);
        if(packetcount > neededpackets)
        clskt.close();
    }

}
   
private static class WSADATA {
        // Define WSADATA structure as needed
    }

    private static int WSAStartup(int version, WSADATA wsa) {
        // Implement WSAStartup as needed
        return 0;
    }

    private static int MAKEWORD(int high, int low) {
        // Implement MAKEWORD as needed
        return (high << 8) | low;
    }

    private static int WSAGetLastError() {
        // Implement WSAGetLastError as needed
        return 0;
    }

    private static int closesocket(DatagramSocket socket) {
        // Implement closesocket as needed
        return 0;
    }

    private static void WSACleanup() {
        // Implement WSACleanup as needed
    }
}

final class DefineConstants {
    public static final int BUFLEN = 512;
    public static final int PORT = 8888;
}
