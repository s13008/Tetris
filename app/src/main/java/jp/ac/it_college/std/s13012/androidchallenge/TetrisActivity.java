package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.util.Log;



public class TetrisActivity extends Activity {

    private SoundPool se;
    private MediaPlayer bgm;

    private int seSoundId;
    private int bgmSoundId;

    private boolean gameIsRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        se = new SoundPool(1, AudioManager.STREAM_MUSIC, 1);
        seSoundId = se.load(this, R.raw.drum02, 0);
        setContentView(R.layout.activity_tetris);
        LinearLayout mainLayout = (LinearLayout) findViewById(R.id.game_layout);
        mainLayout.addView(new Block(this));
        LinearLayout subLayout = (LinearLayout) findViewById(R.id.sub_layout);
        subLayout.addView(new NextBlock(this));
        bgm = MediaPlayer.create(this, R.raw.gamebgm2);
        bgm.setLooping(true);
        bgm.setVolume(0.3f, 0.3f);
        bgm.start();

        Button hold = (Button) findViewById(R.id.hold_Button);
        Button pouse = (Button) findViewById(R.id.pouse_Button);

        pouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Block.loopFlagReversal();
            }
        });

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
            Block.loopFlagReversal();
            bgm.pause();
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setTitle("終了の確認")
                    .setMessage("難易度選択画面へ戻りますか？")
                    .setPositiveButton("はい",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Block.loopFlagReversal();
                                    finish();
                                }
                            }
                    )
                    .setNegativeButton("キャンセル",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Block.loopFlagReversal();
                                    bgm.start();
                                }
                            }
                    )
                    .show();
            return true;
        }
        return false;
    }



    @Override
    protected void onStart() {
        super.onStart();
        bgm.start();
    }

    @Override
    protected void onPause() {
        //スレッドを停止
        super.onPause();
        Block.finishLoop();
        NextBlock.finishLoop();

        bgm.pause();
    }
}