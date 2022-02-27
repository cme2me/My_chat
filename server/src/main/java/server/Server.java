package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Server {
    private final int PORT = 4421;
    private Socket socket;
    private ServerSocket serverSocket;

    private List<ClientHandler> clients;
    public Server() {
        clients = new CopyOnWriteArrayList<>();
        //Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client connected ");
                clients.add(new ClientHandler(this, socket));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
