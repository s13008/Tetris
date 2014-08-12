package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Board extends View {

    public Board(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        //ボード枠
        rect.getPaint().setColor(Color.BLACK);
        rect.draw(canvas);
//        canvas.translate(5, 5);
        //ボード背景
        rect.setBounds(0, 0, canvas.getWidth() - 10, canvas.getHeight() - 10);
        rect.getPaint().setColor(Color.CYAN);
        rect.draw(canvas);
    }
}