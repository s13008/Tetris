package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;


public class ResultActivity extends Activity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        findViewById(R.id.retry_button).setOnClickListener(this);
        findViewById(R.id.end_button).setOnClickListener(this);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
       return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.retry_button:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.end_button:
                moveTaskToBack(true);
                break;
        }
    }
}
