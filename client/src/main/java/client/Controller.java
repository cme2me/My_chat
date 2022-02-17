package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class Controller {
    @FXML
    public TextArea chatField;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendBtn;

    private Socket socket;
    private static final int PORT = 8189;
    private static final String ADDRESS = "localhost";


    @FXML
    public void sendMsg(ActionEvent actionEvent) {
        chatField.appendText(messageField.getText()+"\n");
        messageField.requestFocus();
        messageField.clear();
    }
}
