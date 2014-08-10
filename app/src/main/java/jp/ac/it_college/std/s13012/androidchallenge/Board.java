package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Board extends View{

    public Board(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.translate(100,0);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        LinearLayout layout = (LinearLayout)findViewById(R.id.tetris);
    }
}
