package server;

public interface Authentication {
    String getNicknameByLoginAndPassword(String login, String password);

    boolean isRegistered(String login, String password, String nickname);
}
