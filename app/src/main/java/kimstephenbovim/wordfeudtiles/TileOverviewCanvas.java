package kimstephenbovim.wordfeudtiles;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Paint.Align.CENTER;
import static android.graphics.Paint.Align.LEFT;
import static android.graphics.Paint.Align.RIGHT;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

public class TileOverviewCanvas extends View {

    int cornerRadius = Math.round(getResources().getDimension(R.dimen.tile_corner_radius));
    Paint fillPaint = new Paint();
    Paint emptyFillPaint = new Paint();
    Paint strokePaint = new Paint();
    Paint letterPaint = new Paint();
    Paint scorePaint = new Paint();
    Paint countPaint = new Paint();
    List<RectF> rects = new ArrayList<>();
    String[] letters;
    HashMap<String, Integer> letterCounts = new HashMap<>();
    Map<String, Integer> points;
    List<TileOverviewParameters> tileOverviewParameters;

    public TileOverviewCanvas(Context context, List<String> remainingLetters, int ruleset) {
        super(context);
        if (remainingLetters == null) {
            return;
        }

        this.letters = Arrays.copyOf(Constants.shared.getLetters(ruleset), Constants.shared.getLetters(ruleset).length);
        if (!WFTiles.instance.getPreferences().isSortVowelsConsonants()) {
            Arrays.sort(letters, Collator.getInstance(Constants.shared.getLocale(ruleset)));
        }
        this.points = Constants.shared.getPoints(ruleset);

        for (String letter : letters) {
            letterCounts.put(letter, 0);
        }
        for (String letter : remainingLetters) {
            letterCounts.put(letter, letterCounts.get(letter) + 1);
        }

        fillPaint.setStyle(FILL);
        fillPaint.setColor(getResources().getColor(R.color.tileColor));

        emptyFillPaint.setStyle(FILL);
        emptyFillPaint.setColor(Color.parseColor("lightgrey"));

        strokePaint.setStyle(STROKE);
        strokePaint.setColor(BLACK);
        strokePaint.setStrokeWidth(getResources().getDimension(R.dimen.tile_border_width));

        //TODO hvilke av disse trenger jeg?
        letterPaint.setTypeface(Typeface.SANS_SERIF);
        letterPaint.setColor(getResources().getColor(R.color.textColor));
        letterPaint.setTextSize(getResources().getDimension(R.dimen.tile_letter_font_size));
        letterPaint.setAntiAlias(true);
        letterPaint.setStyle(FILL);
        letterPaint.setTextAlign(CENTER);

        countPaint.setTypeface(Typeface.SANS_SERIF);
        countPaint.setColor(getResources().getColor(R.color.textColor));
        countPaint.setTextSize(getResources().getDimension(R.dimen.tile_letter_font_size));
        countPaint.setAntiAlias(true);
        countPaint.setStyle(FILL);
        countPaint.setTextAlign(LEFT);

        scorePaint.setTypeface(Typeface.SANS_SERIF);
        scorePaint.setColor(getResources().getColor(R.color.textColor));
        scorePaint.setTextSize(getResources().getDimension(R.dimen.tile_score_font_size));
        scorePaint.setAntiAlias(true);
        scorePaint.setStyle(FILL);
        scorePaint.setTextAlign(RIGHT);

        tileOverviewParameters = Constants.shared.getTileOverviewParameters();

        if (this.letters != null) {
            for (int i = 0; i < this.letters.length; i++) {
                TileOverviewParameters tileOverviewParameters = this.tileOverviewParameters.get(i);

                rects.add(new RectF((float) tileOverviewParameters.left,
                        (float) tileOverviewParameters.top,
                        (float) tileOverviewParameters.tileRight,
                        (float) tileOverviewParameters.bottom));
            }
        }

        int height = rects.isEmpty()
                ? 0
                : Math.round(rects.get(rects.size() - 1).bottom + getResources().getDimension(R.dimen.min_margin));
        setLayoutParams(new ViewGroup.LayoutParams(Constants.shared.getAvailableWidth(), height));

        //TODO trenger jeg denne?
        setFocusableInTouchMode(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (letters != null) {
            for (int i = 0; i < letters.length; i++) {
                TileOverviewParameters tileOverviewParameters = this.tileOverviewParameters.get(i);
                RectF rectF = rects.get(i);
                String letter = letters[i];
                if (letterCounts.get(letter) == 0) {
                    canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, emptyFillPaint);
                } else {
                    canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint);
                }
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint);
                canvas.drawText(letter, tileOverviewParameters.letterX, tileOverviewParameters.letterY, letterPaint);
                canvas.drawText(points.get(letter) == 0 ? "" : String.valueOf(points.get(letter)), tileOverviewParameters.scoreX, tileOverviewParameters.scoreY, scorePaint);
                canvas.drawText("x" + (letterCounts.get(letter) > 9 ? "" : " ") + letterCounts.get(letter), tileOverviewParameters.countLabelX, tileOverviewParameters.countLabelY, countPaint);
            }
        }
    }
}