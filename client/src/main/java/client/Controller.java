package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller implements Initializable{

    private static final int PORT = 4421;
    private static final String SERV_ADRESS = "localhost";
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket(SERV_ADRESS, PORT);
            System.out.println("Client connected " + socket.getRemoteSocketAddress());
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread threadRead = new Thread(()-> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        chatField.appendText("Server: " + str + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            threadRead.setDaemon(true);
            threadRead.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public TextArea chatField;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendBtn;

    @FXML
    public void sendMsg(ActionEvent actionEvent) {
        chatField.appendText(messageField.getText()+"\n");
        messageField.requestFocus();
        messageField.clear();
    }
}
