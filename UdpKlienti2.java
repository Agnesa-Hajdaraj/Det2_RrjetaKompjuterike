import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.nio.charset.StandardCharsets;

public class UdpClient2 {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {

        Scanner input = new Scanner(System.in);
        // Krijimi i socket
        DatagramSocket diagramiSocket = new DatagramSocket();

        // Destimi i IP Serverit
        InetAddress ipAdresa = InetAddress.getByName("localhost");

        // Krijimi i buffered per te derguar te dhena
        byte [] dergimiDhena = new byte[1024];

        DatagramPacket paketa = new DatagramPacket(dergimiDhena, dergimiDhena.length, ipAdresa, 5001);
        diagramiSocket.send(paketa);
        diagramiSocket.close();
    }
}
       
