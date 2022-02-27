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
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private static final int PORT = 4421;
    private static final String SERV_ADRESS = "localhost";
    private DataInputStream in;
    private DataOutputStream out;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            Socket socket = new Socket(SERV_ADRESS, PORT);
            System.out.println("Client connected ");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()-> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        chatField.appendText(str + "\n");
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
            }).start();
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
        try {
            out.writeUTF(messageField.getText());
            messageField.requestFocus();
            messageField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
