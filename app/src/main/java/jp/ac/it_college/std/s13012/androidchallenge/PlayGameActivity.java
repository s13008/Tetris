package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class PlayGameActivity extends Activity{
    public PlayGameActivity(){
        Log.v("PlayGame", "PlayGame");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_main);
    }
}
