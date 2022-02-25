package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    private static final int PORT = 4421;

    public static void main(String[] args) {
        Socket clientSocket = null;
        Scanner sc = new Scanner(System.in);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server started");
            clientSocket = serverSocket.accept();
            System.out.println("Client connected " + clientSocket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());

            Thread threadRead = new Thread(() -> {
                try {
                    while (true){
                        out.writeUTF(sc.nextLine());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            threadRead.setDaemon(true);
            threadRead.start();

            while (true) {
                String str = in.readUTF();
                if (str.equals("/close")) {
                    System.out.println("Client disconnected");
                    out.writeUTF("/close");
                    break;
                }
                else {
                    System.out.println("Client: " + str);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
