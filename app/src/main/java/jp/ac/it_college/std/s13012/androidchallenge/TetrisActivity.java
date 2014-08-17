package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class TetrisActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new Block(this));

        //難易度によってブロックの落下速度を調整
        String difficulty = getIntent().getStringExtra("Difficulty");
        if (difficulty.equals("EASY")) {
            Block.setFallVelocity(100);
        } else if (difficulty.equals("NORMAL")) {
            Block.setFallVelocity(50);
        } else if (difficulty.equals("HARD")) {
            Block.setFallVelocity(30);
        }
        Log.v("Difficulty", getIntent().getStringExtra("Difficulty"));
    }

    @Override
    protected void onPause() {
        //Blockクラスのスレッドを停止
        super.onPause();
        Block.finishLoop();
    }


}