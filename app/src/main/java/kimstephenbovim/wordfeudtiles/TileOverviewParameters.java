package kimstephenbovim.wordfeudtiles;

public class TileOverviewParameters {

    final int top,
            bottom,
            right,
            left,
            tileRight,
            letterX,
            letterY,
            scoreX,
            scoreY,
            countLabelX,
            countLabelY;

    public TileOverviewParameters(int top,
                                  int bottom,
                                  int right,
                                  int left,
                                  int tileRight,
                                  int letterX,
                                  int letterY,
                                  int scoreX,
                                  int scoreY,
                                  int countLabelX,
                                  int countLabelY) {
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.left = left;
        this.tileRight = tileRight;
        this.letterX = letterX;
        this.letterY = letterY;
        this.scoreX = scoreX;
        this.scoreY = scoreY;
        this.countLabelX = countLabelX;
        this.countLabelY = countLabelY;
    }
}
