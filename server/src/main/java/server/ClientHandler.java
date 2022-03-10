package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class ClientHandler {
    private Server server;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean isAuth;
    private String nickname;

    public ClientHandler(Server server, Socket socket) {
        try {
            this.server = server;
            this.socket = socket;
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread working = new Thread(() -> {
                try {
                    socket.setSoTimeout(120000);
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/close")) {
                                sendMsg("/close");
                                break;
                            }
                            if (str.startsWith("/auth")) {
                                String[] logAndPass = str.split(" ", 3);
                                if (logAndPass.length < 3) {
                                    continue;
                                }
                                String newNickname = server.getAuthentication().getNicknameByLoginAndPassword(logAndPass[1], logAndPass[2]);
                                if (newNickname != null) {
                                    nickname = newNickname;
                                    sendMsg("/auth_complete " + nickname);
                                    isAuth = true;
                                    server.sub(this);
                                    break;
                                } else {
                                    sendMsg("Login or password are incorrect");
                                }
                            }
                            if (str.startsWith("/reg")) {
                                String[] token = str.split(" ");
                                if (token.length < 4) {
                                    continue;
                                }
                                if (server.getAuthentication().isRegistered(token[1], token[2], token[3])) {
                                    sendMsg("/reg_complete");
                                }
                                else {
                                    sendMsg("/reg_broke");
                                }
                            }
                        }
                    }
                    socket.setSoTimeout(0);

                    while (isAuth) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/close")) {
                                System.out.println("Client disconnected" + socket.getRemoteSocketAddress());
                                sendMsg("/close");
                                break;
                            }
                            if (str.startsWith("/w")) {
                                String[] privateMesg = str.split(" ", 3);
                                if (privateMesg.length < 3) {
                                    continue;
                                }
                                server.clientToClient(this, privateMesg[1], privateMesg[2]);
                            }
                        } else {
                            server.clientToEveryOne(this, str);
                        }
                    }
                } catch (SocketTimeoutException e) {
                    sendMsg("/close");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    server.unsub(this);
                    try {
                        out.writeUTF("Client disconnected");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            working.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNickname() {
        return nickname;
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}