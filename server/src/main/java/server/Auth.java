package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Auth implements Authentication{

    private class UserData {
        private String login;
        private String password;
        private String nickname;

        public UserData(String login, String password, String nickname) {
            this.login = login;
            this.password = password;
            this.nickname = nickname;
        }
    }
    private List<UserData> userData;

    public Auth() {
        userData = new ArrayList<>();
    }

    @Override
    public String getNicknameByLoginAndPassword(String login, String password) {
        for (UserData users : userData) {
            if (users.login.equals(login) && users.password.equals(password)) {
                return users.nickname;
            }
        }
        return null;
    }

    @Override
    public boolean isRegistered(String login, String password, String nickname) {
        for (UserData users  : userData
             ) {
            if (users.login.equals(login) || users.nickname.equals(nickname)) {
                return false;
            }
        }
        userData.add(new UserData(login, password, nickname));
        return true;

    }
}
