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
    public void clientToEveryOne(ClientHandler sender, String msg) {
        String message = String.format("%s: %s", sender.getNickname(), msg);
        for (ClientHandler client : clients) {
            client.sendMsg(message);
        }
        System.out.println(message);
    }
    public void clientToClient(ClientHandler sender, String receiver, String msg) {
        String message = String.format("%s to %s: %s", sender.getNickname(), receiver, msg);
        for (ClientHandler client : clients) {
            if (client.getNickname().equals(receiver)) {
                client.sendMsg(message);
                sender.sendMsg(message);
                if (client.getNickname().equals(sender)) {
                    sender.sendMsg("You can't send a message to yourself");
                }
                return;
            }
            else {
                sender.sendMsg("This user does not exist");
            }
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
