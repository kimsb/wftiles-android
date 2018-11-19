package kimstephenbovim.wordfeudtiles.event;

public class LoginEvent {

    final private LoginResult loginResult;

    public LoginEvent(final LoginResult loginResult) {
        this.loginResult = loginResult;
    }

    public LoginResult getLoginResult() {
        return loginResult;
    }
}
