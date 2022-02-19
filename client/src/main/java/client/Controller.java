package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller {
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
