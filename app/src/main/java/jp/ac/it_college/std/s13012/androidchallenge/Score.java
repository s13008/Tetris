package jp.ac.it_college.std.s13012.androidchallenge;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Score extends View{
    public static int mDifficulty;
    private TextView scoreText;
    private int score = 0;
    private static final int EASY = 0;
    private static final int NORMAL = 1;
    private static final int HARD = 2;
    public static final int EASY_SCORE = 100;
    public static final int NORMAL_SCORE = 200;
    public static final int HARD_SCORE = 300;
    private int[][] nextBlock;
    private static final int NEXT_BLOCK = 3;

    public Score(Context context) {
        super(context);
        scoreText = (TextView)((TetrisActivity)context).findViewById(R.id.score);
        mHandler.sendEmptyMessage(0);
    }

    public static void setDifficulty(String difficulty) {
        if (difficulty.equals("EASY")) {
            mDifficulty = EASY;

        } else if (difficulty.equals("NORMAL")) {
            mDifficulty = NORMAL;

        } else if (difficulty.equals("HARD")) {
            mDifficulty = HARD;

        }
    }

    public void setScore(int difficulty){
        switch (difficulty) {
            case EASY:
                mHandler.sendEmptyMessage(EASY_SCORE);
                break;
            case NORMAL:
                mHandler.sendEmptyMessage(NORMAL_SCORE);
                break;
            case HARD:
                mHandler.sendEmptyMessage(HARD_SCORE);
                break;
        }
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case NEXT_BLOCK:
                    Log.v("NEXT_BLOCK", "true");
                    break;
                default:
                    score += msg.what;
                    scoreText.setText("Score: " + String.valueOf(score));
                    break;
            }
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
