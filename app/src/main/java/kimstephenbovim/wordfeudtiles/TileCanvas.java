package kimstephenbovim.wordfeudtiles;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.graphics.Color.BLACK;
import static android.graphics.Paint.Align.CENTER;
import static android.graphics.Paint.Align.RIGHT;
import static android.graphics.Paint.Style.FILL;
import static android.graphics.Paint.Style.STROKE;

public class TileCanvas extends View {

    int cornerRadius = Math.round(getResources().getDimension(R.dimen.tile_corner_radius));
    Paint fillPaint = new Paint();
    Paint strokePaint = new Paint();
    Paint letterPaint = new Paint();
    Paint scorePaint = new Paint();
    List<RectF> rects = new ArrayList<>();
    List<String> letters;
    Map<String, Integer> points;
    List<TileParameters> tileParameterList;

    public TileCanvas(Context context, List<String> letters, Map<String, Integer> points) {
        super(context);

        this.letters = letters;
        this.points = points;

        fillPaint.setStyle(FILL);
        fillPaint.setColor(getResources().getColor(R.color.tileColor));

        strokePaint.setStyle(STROKE);
        strokePaint.setColor(BLACK);
        strokePaint.setStrokeWidth(getResources().getDimension(R.dimen.tile_border_width));

        //TODO hvilke av disse trenger jeg?
        letterPaint.setTypeface(Typeface.SANS_SERIF);
        letterPaint.setColor(getResources().getColor(R.color.textColor));
        letterPaint.setTextSize(getResources().getDimension(R.dimen.tile_letter_font_size));
        letterPaint.setAntiAlias(true);
        //letterPaint.setFakeBoldText(true);
        letterPaint.setStyle(FILL);
        letterPaint.setTextAlign(CENTER);

        //scorePaint.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        scorePaint.setTypeface(Typeface.SANS_SERIF);
        scorePaint.setColor(getResources().getColor(R.color.textColor));
        scorePaint.setTextSize(getResources().getDimension(R.dimen.tile_score_font_size));
        scorePaint.setAntiAlias(true);
        //scorePaint.setFakeBoldText(true);
        scorePaint.setStyle(FILL);
        scorePaint.setTextAlign(RIGHT);

        tileParameterList = Constants.shared.getTileParameters();

        if (letters != null) {
            for (int i = 0; i < letters.size(); i++) {
                TileParameters tileParameters = tileParameterList.get(i);

                rects.add(new RectF((float) tileParameters.left,
                        (float) tileParameters.top,
                        (float) tileParameters.right,
                        (float) tileParameters.bottom));
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
            for (int i = 0; i < letters.size(); i++) {
                TileParameters tileParameters = tileParameterList.get(i);
                RectF rectF = rects.get(i);
                String letter = letters.get(i);
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, fillPaint);
                canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, strokePaint);
                canvas.drawText(letter, tileParameters.letterX, tileParameters.letterY, letterPaint);
                canvas.drawText(points.get(letter) == 0 ? "" : String.valueOf(points.get(letter)), tileParameters.scoreX, tileParameters.scoreY, scorePaint);
            }
        }
    }
}