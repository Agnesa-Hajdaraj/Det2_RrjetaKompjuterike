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