package Server_Client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {

    static String host;
    static int port;

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private void execute() throws IOException {

        Socket client = new Socket(host, port);
        ReadWriteClient readwrite = new ReadWriteClient(client);
        readwrite.start();

    }

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập vào host: ");
        host = sc.nextLine();

        System.out.print("Nhập vào port: ");
        port = sc.nextInt();
        Client client = new Client(host, port);
        client.execute();

    }

    public static void run(String host, int port) throws IOException {
        Client client = new Client(host, port);
        client.execute();
    }
}

class ReadWriteClient extends Thread {

    private Socket client;
    public static String a ="1";

    public ReadWriteClient(Socket client) {
        this.client = client;
    }
    
    public void disconnected() throws IOException {
        DataOutputStream dos = null;
        dos = new DataOutputStream(client.getOutputStream());
        while (true) {  
            dos.writeUTF(a);
        }
    }

    @Override
    public void run() {
        try {
            
           
            PrintWriter out = new PrintWriter(client.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            long t0, t1, serverTime, finalTime;
            out.println(t0 = System.currentTimeMillis());
            serverTime = Long.parseLong(in.readLine());
            t1 = System.currentTimeMillis();
            finalTime = serverTime + (t1 - t0) / 2;
            DateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            System.out.println("Client time:" + formatter.format(new Date(t1)));
            System.out.println("Server Time: " + formatter.format(new Date(serverTime)));
            String STM = formatter.format(new Date(finalTime));
            /////// 
            System.out.println();
            System.out.println(STM);
            try {
                Runtime.getRuntime().exec("cmd /C time " + STM); // hh:mm:ss
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Client Time after reset: " + formatter.format(new Date(finalTime)));
            //out.println("EXit");   
            
            
            disconnected();
            in.readLine();
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + Client.host);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + Client.host);
            System.exit(1);
        }
    }
}
