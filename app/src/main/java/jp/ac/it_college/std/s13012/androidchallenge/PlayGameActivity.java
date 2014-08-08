package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class PlayGameActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.v("PlayGame", getIntent().getStringExtra("Difficulty"));
    }
}
