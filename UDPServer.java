import java.io.*;
import java.net.*;
import java.util.*;

public class UDPServer {

    private static final int BUFLEN = 512;
    private static final int PORT = 8888;

    private static final Set<String> firstClient = new HashSet<>();
    private static final Map<String, InetSocketAddress> clientAddresses = new HashMap<>();

    public static void main(String[] args) {
        System.out.println("UDP Server");

        try {
            DatagramSocket serverSocket = new DatagramSocket(PORT);

            byte[] receiveData = new byte[BUFLEN];

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                System.out.println("Waiting for data...");
                serverSocket.receive(receivePacket);

                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                InetSocketAddress clientAddr = new InetSocketAddress(clientAddress, clientPort);

                String clientKey = clientAddress.getHostAddress() + ":" + clientPort;

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());

                if (message.equals("execute")) {
                    if (firstClient.isEmpty() || firstClient.contains(clientKey)) {
                        int magicNumber = (int) (Math.random() * 100) + 1;

                        String response = "Magic number generated. Try to guess! You have 5 guesses";
                        sendToClient(serverSocket, response, clientAddr);

                        int i = 5;
                        while (i > 0) {
                            i--;
                            String guessMessage = receiveFromClient(serverSocket, clientAddr);

                            int clientGuess = Integer.parseInt(guessMessage);

                            if (clientGuess == magicNumber) {
                                String successMessage = "Congratulations! You guessed the magic number!";
                                sendToClient(serverSocket, successMessage, clientAddr);
                                System.out.println("Client guessed the correct number!");
                                break;
                            } else if (clientGuess > magicNumber && i != 0) {
                                String errorMessage = "The magic number is smaller! Your tries: " + i;
                                sendToClient(serverSocket, errorMessage, clientAddr);
                            } else if (clientGuess < magicNumber && i != 0) {
                                String errorMessage = "The magic number is bigger! Your tries: " + i;
                                sendToClient(serverSocket, errorMessage, clientAddr);
                            }
                            if (i == 0) {
                                String errorMessage = "You lost the game! The magic number was: " + magicNumber;
                                sendToClient(serverSocket, errorMessage, clientAddr);
                                System.out.println("Client didn't guess the correct number");
                            }
                        }
                        firstClient.add(clientKey);
                    } else {
                        String errorMsg = "Magic Number Game not allowed for this client";
                        sendToClient(serverSocket, errorMsg, clientAddr);
                    }
                } else if (message.startsWith("file r: ")) {
                    String filename = message.substring(8);
                    sendFileContent(serverSocket, filename, clientAddr);
                } else if (message.startsWith("file a: ")) {
                    if (firstClient.isEmpty() || firstClient.contains(clientKey)) {
                        String[] parts = message.substring(8).split(" ", 2);
                        String filename = parts[0];
                        String content = parts[1];
                        appendFile(filename, content);
                        String successMsg = "Content appended to file: " + filename;
                        sendToClient(serverSocket, successMsg, clientAddr);
                        firstClient.add(clientKey);
                    } else {
                        String errorMsg = "Append operation not allowed for this client";
                        sendToClient(serverSocket, errorMsg, clientAddr);
                    }
