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
} else if (message.startsWith("file w: ")) {
                    if (firstClient.isEmpty() || firstClient.contains(clientKey)) {
                        String[] parts = message.substring(8).split(" ", 2);
                        String filename = parts[0];
                        String content = parts[1];
                        writeFile(filename, content);
                        String successMsg = "Content written to file: " + filename;
                        sendToClient(serverSocket, successMsg, clientAddr);
                        firstClient.add(clientKey);
                    } else {
                        String errorMsg = "Write operation not allowed for this client";
                        sendToClient(serverSocket, errorMsg, clientAddr);
                    }
                } else {
                    System.out.println("Client says: " + message);

                    System.out.print("Enter message to client: ");
                    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                    String serverMessage = br.readLine();

                    sendToClient(serverSocket, serverMessage, clientAddr);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sendToClient(DatagramSocket socket, String message, InetSocketAddress clientAddr)
            throws IOException {
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddr);
        socket.send(sendPacket);
    }

    private static String receiveFromClient(DatagramSocket socket, InetSocketAddress clientAddr) throws IOException {
        byte[] receiveData = new byte[BUFLEN];
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length, clientAddr);
        socket.receive(receivePacket);
        return new String(receivePacket.getData(), 0, receivePacket.getLength());
    }

    private static void sendFileContent(DatagramSocket socket, String filename, InetSocketAddress clientAddr)
            throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line).append("\n");
            }
            String fileContent = contentBuilder.toString();
            sendToClient(socket, fileContent, clientAddr);
        } catch (FileNotFoundException e) {
            String errorMsg = "Error opening file: " + filename;
            sendToClient(socket, errorMsg, clientAddr);
        }
    }

    private static void appendFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, true))) {
            writer.write(content);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error opening file: " + filename);
        }
    }

    private static void writeFile(String filename, String content) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write(content);
        } catch (IOException e) {
            System.out.println("Error opening file: " + filename);
        }
    }
}
