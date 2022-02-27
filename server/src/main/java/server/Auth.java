package server;

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
        userData.add(new UserData("nick1", "pass1", "nick1"));
        userData.add(new UserData("nick2", "pass2", "nick2"));
        userData.add(new UserData("nick3", "pass3", "nick3"));
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
}