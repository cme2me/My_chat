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
    private Authentication authentication;
    public Server() {
        authentication = new Auth();
        clients = new CopyOnWriteArrayList<>();
        //Socket socket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server started");

            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client connected " + socket.getRemoteSocketAddress());
                new ClientHandler(this, socket);
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
    public void clientToEveryOne(String msg) {
        for (ClientHandler client : clients) {
            client.sendMsg(msg);
        }
    }
    public void sub(ClientHandler clientHandler) {
        clients.add(clientHandler);
    }
    public void unsub(ClientHandler clientHandler) {
        clients.remove(clientHandler);
    }

    public Authentication getAuthentication() {
        return authentication;
    }
}
