package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.LinearLayout;


public class TetrisActivity extends Activity implements GestureDetector.OnGestureListener {
    int fallSpeed;
    GestureDetector gestureDetector;
    Block block;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);
        gestureDetector = new GestureDetector(this, this);
        block = new Block(this);
        LinearLayout tetrisLayout = (LinearLayout) findViewById(R.id.tetris_layout);
        tetrisLayout.addView(block);


        /*String difficulty = getIntent().getStringExtra("Difficulty");
        switch (difficulty){
            case "EASY":
                fallSpeed = 3;
                break;
            case "NORMAL":
                fallSpeed = 5;
                break;
            case "HARD":
                fallSpeed = 8;
                break;
        }*/

        String difficulty = getIntent().getStringExtra("Difficulty");
        if (difficulty.equals("EASY")) {
            fallSpeed = 3;

        } else if (difficulty.equals("NORMAL")) {
            fallSpeed = 5;

        } else if (difficulty.equals("HARD")) {
            fallSpeed = 8;

        }


        Log.v("Difficulty", getIntent().getStringExtra("Difficulty"));
        Log.v("FallSpeed", Integer.toString(fallSpeed));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        //クリック時ブロックを下げる
        block.posy++;
        block.invalidate();
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float v, float v2) {
        //フリック方向の判定
        if (motionEvent.getX() < motionEvent2.getX()) {
            block.posx++;
            block.invalidate();
            } else {
            block.posx--;
            block.invalidate();
        }
        return true;
    }
}