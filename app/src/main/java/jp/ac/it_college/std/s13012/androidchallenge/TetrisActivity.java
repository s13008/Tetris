package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;


public class TetrisActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tetris);
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.game_layout);
        mainLayout.addView(new Block(this));
        LinearLayout subLayout = (LinearLayout)findViewById(R.id.sub_layout);
        subLayout.addView(new NextBlock(this));


        //難易度によってブロックの落下速度を調整
        String difficulty = getIntent().getStringExtra("Difficulty");
        if (difficulty.equals("EASY")) {
            Block.setFallVelocity("EASY");
            NextBlock.setDifficulty("EASY");
        } else if (difficulty.equals("NORMAL")) {
            Block.setFallVelocity("NORMAL");
            NextBlock.setDifficulty("NORMAL");
        } else if (difficulty.equals("HARD")) {
            Block.setFallVelocity("HARD");
            NextBlock.setDifficulty("HARD");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            Block.stopLoop();
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("終了の確認")
                    .setMessage("難易度選択画面へ戻りますか？")
                    .setPositiveButton("はい",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Block.stopLoop();
                                    finish();
                                }
                            }
                    )
                    .setNegativeButton("キャンセル",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Block.stopLoop();
                                }
                            }
                    )
                    .show();
            return true;
        }
        return false;
    }


    @Override
    protected void onPause() {
        //スレッドを停止
        super.onPause();
        Block.finishLoop();
        NextBlock.finishLoop();
    }


}