package client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
    private Stage regStage;
    private Stage stage;
    private RegistrationController regController;

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
                            if (str.equals("/reg_complete") || str.equals("/reg_broke")) {
                                regController.regFinish(str);
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
    private void showRegStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/registation.fxml"));
            Parent root = fxmlLoader.load();
            regStage = new Stage();

            regStage.setTitle("Kraber registration");
            regStage.setScene(new Scene(root, 600, 600));
            regController = fxmlLoader.getController();
            regController.setController(this);
            regStage.initStyle(StageStyle.UTILITY);
            regStage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void tryToReg(ActionEvent actionEvent) {
        if (regStage == null) {
            showRegStage();
        }
        regStage.show();
    }

    public void regProcess(String login, String password, String nickname) {
        String msg = String.format("/reg %s %s %s", login, password, nickname);
        if (socket == null || socket.isClosed()) {
            connect();
        }
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
