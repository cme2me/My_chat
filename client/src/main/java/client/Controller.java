package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable{

    private static final int PORT = 4421;
    private static final String SERV_ADRESS = "localhost";
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField passField;
    @FXML
    public HBox authPanel;
    @FXML
    public HBox workPanel;
    @FXML
    public TextArea chatField;
    @FXML
    public TextField messageField;
    @FXML
    public Button sendBtn;
    private boolean isAuth;
    private String nickname;

    private DataInputStream in;
    private DataOutputStream out;
    private Socket socket;

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
        authPanel.setVisible(!isAuth);
        authPanel.setManaged(!isAuth);
        workPanel.setVisible(isAuth);
        workPanel.setManaged(isAuth);
        if (!isAuth) {
            nickname = "";
        }
        chatField.clear();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setAuth(false);
    }
    private void connect() {
        try {
            socket = new Socket(SERV_ADRESS, PORT);
            System.out.println("Client connected ");
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(()-> {
                try {
                    while (true) {
                        String str = in.readUTF();
                        if (str.startsWith("/")) {
                            if (str.equals("/close")) {
                                break;
                            }
                            if (str.startsWith("/auth_complete")) {
                                nickname = str.split(" ")[1];
                                setAuth(true);
                                break;
                            }
                        }
                        else {
                            chatField.appendText(str + "\n");
                        }
                    }
                    while (isAuth) {
                        String str = in.readUTF();
                        if (str.equals("/close")) {
                            break;
                        }
                        chatField.appendText(str + "\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    try {
                        out.writeUTF("/close");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }finally {
                    setAuth(false);
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
    public void sendMsg(ActionEvent actionEvent) {
        try {
            out.writeUTF(messageField.getText());
            messageField.requestFocus();
            messageField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void Auth(ActionEvent actionEvent) {
        if (socket == null || socket.isClosed()) {
            connect();
        }
        String doMsg = String.format("/auth %s %s", loginField.getText().trim(), passField.getText().trim());
        passField.clear();
        try {
            out.writeUTF(doMsg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
