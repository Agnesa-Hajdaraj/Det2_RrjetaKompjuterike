import java.net.*;
import java.io.*;
import java.util.*;

public class UdpServer {
    public static void main(String[] args) throws IOException {
        // 1. Të krijohet socket lidhja me server;
        // Krijimi i soket lidhjes
        DatagramSocket ServerSocket = new DatagramSocket(5000);
        DatagramSocket ServerSocket2 = new DatagramSocket(5001);

        // 4. Të jetë në gjendje të lexoje mesazhet që dërgohen nga klientët;
        // Leximi i te dhenave
        byte [] leximiDhena = new byte[1024];

        // Krijimi i pranuesit te paketes
        DatagramPacket pranuesiDhenave = new DatagramPacket(leximiDhena, leximiDhena.length);

        // 3. Të pranojë kërkesat e pajisjeve që dërgojnë request
        ServerSocket.receive(pranuesiDhenave);
        ServerSocket2.receive(pranuesiDhenave);

        // Extract i mesazhi
        String mesazhi = new String(leximiDhena, 0, pranuesiDhenave.getLength());
        String mesazhi2 = new String(leximiDhena, 0, pranuesiDhenave.getLength());
        // Leximi/Shfaqja e mesazhit nga Klienti
        System.out.println("Mesazhi klientit: " + mesazhi);
        System.out.println("Mesazhi i klientit (pa priviligje): " + mesazhi2);
        System.out.println("IP e klientit: " + pranuesiDhenave.getAddress());
    }
}
class Server1 {
    public static void main (String args[]) throws Exception {
        System.out.println("Enter Port Number!!!");
        Scanner input = new Scanner(System.in);
        int SPort = input.nextInt();
        DatagramSocket srvskt = new DatagramSocket(SPort);
        byte[] data = new byte[1024];
        System.out.println("Enter a full file name to save data to it? ");
        String path = input.next();
        System.out.println("file: " + path + "will be created");
        FileOutputStream FOS = new FileOutputStream(path);
        DatagramPacket srvpkt = new DatagramPacket(data,1024);
        System.out.println("Listening to portL " + SPort);
        int Packetcounter=0;

        while (true){
            srvskt.receive(srvpkt);
            Packetcounter++;
            String words = new String (srvpkt.getData());
            InetAdress ip = srvpkt.getAddress();
            int port = srvpkt.getPort();
            System.out.println("Packet #" + Packetcounter + "Recieved from Host/ port" + ip + "/" + port);
            FOS.write(data);
            //out 16.flush();
            if(Packetcounter >=100)
            break;
        }
        FOS.close();
        System.out.println("Data has been written to the file!")
            }
}
