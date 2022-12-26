package Server_Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {

    public static int port;

    public static ArrayList<Socket> listSK;

    public Server(int port) {
        this.port = port;
    }

    public void execute() throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("Server is listening...");

        while (true) {
            Socket socket = server.accept();
            System.out.println("Connected: " + socket);
            Server.listSK.add(socket);
            ReadWriteServer readwrite = new ReadWriteServer(socket);
            readwrite.run();
        }
    }

    public static void main(String[] args) throws IOException {
        Server.listSK = new ArrayList<>();
        Scanner sc = new Scanner(System.in);
        System.out.print("Nhập vào port: ");
        int port = sc.nextInt();
        Server server = new Server(port);
        server.execute();
    }

    public static void run(int port) {
        Server.listSK = new ArrayList<>();
        Server server = new Server(port);
        try {
            server.execute();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

class ReadWriteServer extends Thread {

    private Socket socket;
    public static int cno = 0;

    public ReadWriteServer(Socket socket) {
        this.socket = socket;
    }

    public void disconnected() {
        Thread t = new Thread() {
            public void run() {
                try {
                    DataInputStream dis = new DataInputStream(socket.getInputStream());
                    while (true) {
                        String sms = dis.readUTF();
                        if (sms.contains("exits")) {
                            Server.listSK.remove(socket);
                            System.out.println("Disconnected: +" + socket);
                            dis.close();
                            socket.close();
                            cno=Server.listSK.size();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }

    public void run() {
        try {
            int temp = Server.listSK.size() - 1;
            Socket i = Server.listSK.get(temp);
            PrintWriter out;
            cno = Server.listSK.size();
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(System.currentTimeMillis());
            disconnected();
        } catch (Exception e) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Ngắt kết nối Server");
            }
        }
    }
}
