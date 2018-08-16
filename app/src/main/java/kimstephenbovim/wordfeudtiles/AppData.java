package kimstephenbovim.wordfeudtiles;

import kimstephenbovim.wordfeudtiles.domain.User;

public class AppData {

    public static AppData shared = new AppData();
    private User user;

    private AppData() { }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
