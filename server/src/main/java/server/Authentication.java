package server;

public interface Authentication {
    String getNicknameByLoginAndPassword(String login, String password);
}
