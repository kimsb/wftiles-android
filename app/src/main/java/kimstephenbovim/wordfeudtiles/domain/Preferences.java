package kimstephenbovim.wordfeudtiles.domain;

import java.io.Serializable;

public class Preferences implements Serializable {

    private int localeIndex;

    public int getLocaleIndex() {
        return localeIndex;
    }

    public void setLocaleIndex(int localeIndex) {
        this.localeIndex = localeIndex;
    }
}
