package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class RegistrationController {
    @FXML
    public TextField nicknameField;
    @FXML
    public PasswordField passField;
    @FXML
    public TextField loginField;
    @FXML
    public TextArea chatField;

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    @FXML
    public void Reg(ActionEvent actionEvent) {
        String login = loginField.getText().trim();
        String password = passField.getText().trim();
        String nickname = nicknameField.getText().trim();
        controller.regProcess(login, password, nickname);
    }
}
