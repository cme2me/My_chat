package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private static final int PORT = 4421;
    private static final String SERV_ADRESS = "localhost";

    public static void main(String[] args) {
        Socket socket = null;
        Scanner sc = new Scanner(System.in);

        try {
            socket = new Socket(SERV_ADRESS, PORT);
            System.out.println("Client connected " + socket.getRemoteSocketAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            Thread threadRead = new Thread(() -> {
                try {
                    while (true) {
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
                    System.out.println("Lost connection to server");
                    out.writeUTF("/close");
                    break;
                }
                else {
                    System.out.println("Server " + str);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
