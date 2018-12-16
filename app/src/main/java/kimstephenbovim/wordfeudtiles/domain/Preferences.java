package kimstephenbovim.wordfeudtiles.domain;

import java.io.Serializable;

public class Preferences implements Serializable {

    private int localeIndex;
    private boolean viewOverview;
    private boolean sortVowelsConsonants;

    public int getLocaleIndex() {
        return localeIndex;
    }

    public void setLocaleIndex(int localeIndex) {
        this.localeIndex = localeIndex;
    }

    public boolean isViewOverview() {
        return viewOverview;
    }

    public void setViewOverview(boolean viewOverview) {
        this.viewOverview = viewOverview;
    }

    public boolean isSortVowelsConsonants() {
        return sortVowelsConsonants;
    }

    public void setSortVowelsConsonants(boolean sortVowelsConsonants) {
        this.sortVowelsConsonants = sortVowelsConsonants;
    }
}
