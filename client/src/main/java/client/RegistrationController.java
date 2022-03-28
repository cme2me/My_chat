package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
    private static Connection connection;
    private static Statement statement;

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
    public void regFinish(String command) {
        if (command.equals("/reg_complete")) {
            registerNewUser(loginField.getText(), passField.getText(), nicknameField.getText());
            chatField.appendText("Registration completed\n");
        }
        else {
            chatField.appendText("Oops, maybe you did something wrong\n");
        }
    }
    public static void connectSQL() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:users.db");
        statement = connection.createStatement();
    }
    public static void registerNewUser(String login, String password, String nickname) {
        try {
            connectSQL();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        String sqlQuery = String.format("INSERT INTO user_base (login, password, nickname) VALUES ('%s', '%s', '%s')", login, password, nickname);
        try {
            boolean registred = statement.execute(sqlQuery);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
