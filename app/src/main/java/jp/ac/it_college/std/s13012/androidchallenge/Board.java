package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class Board extends View {

    public Board(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*ShapeDrawable rect = new ShapeDrawable(new RectShape());
        rect.setBounds(0, 0, 210, 410);
        //ボード枠
        rect.getPaint().setColor(0xFF000000);
        rect.draw(canvas);
        canvas.translate(5, 5);
        rect.setBounds(0, 0, 200, 400);
        //ボード背景
        rect.getPaint().setColor(0xFFFFFFFF);
        rect.draw(canvas);
*/
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(0,0,200,400,paint);
    }
}