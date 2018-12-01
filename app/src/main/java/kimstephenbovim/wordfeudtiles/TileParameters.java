package kimstephenbovim.wordfeudtiles;

public class TileParameters {

    final int top,
            bottom,
            right,
            left,
            letterX,
            letterY,
            scoreX,
            scoreY;

    public TileParameters(int top,
                          int bottom,
                          int right,
                          int left,
                          int letterX,
                          int letterY,
                          int scoreX,
                          int scoreY) {
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
        this.letterX = letterX;
        this.letterY = letterY;
        this.scoreX = scoreX;
        this.scoreY = scoreY;
    }
}
