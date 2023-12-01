import java.net.*;
import java.io.*;
import java.util.Scanner;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class UdpKlienti {
    public static void main(String[] args) throws SocketException, UnknownHostException, IOException {

        Scanner input = new Scanner(System.in);
        System.out.println("Shkruj mesazh");

        // Krijimi i socket
        DatagramSocket diagramiSocket = new DatagramSocket();

        // Destimi i IP Serverit
        InetAddress ipAdresa = InetAddress.getByName("localhost");

        // Krijimi i buffered per te derguar te dhena
        byte [] dergimiDhena = new byte[1024];

        // Mesazhi
        String mesazhi = input.nextLine();
//        input.next();

        // Konvertimi i mesazhit
        dergimiDhena = mesazhi.getBytes();

        //4. Të behet lidhja me serverin duke përcaktuar sakt portin dhe IP Adresën e serverit;
        // Pergaditja e paketes dhe dergimi i paketave ne server
        DatagramPacket paketa = new DatagramPacket(dergimiDhena, dergimiDhena.length, ipAdresa, 5000);
        diagramiSocket.send(paketa);
        diagramiSocket.close();
    }
}
