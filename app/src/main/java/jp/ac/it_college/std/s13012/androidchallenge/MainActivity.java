package jp.ac.it_college.std.s13012.androidchallenge;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       /* if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
    }

    public static class PlaceholderFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            final Button startButton = (Button) rootView.findViewById(R.id.start_button);
            startButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DifficultyFragment fragment = DifficultyFragment.newInstance();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.start_fragment, fragment);
                    ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    ft.commit();
                    startButton.setVisibility(View.INVISIBLE);
                }
            });

            return rootView;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("終了の確認")
                    .setMessage("Tetrisを終了しますか？")
                    .setPositiveButton("はい",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish();
                                }
                            }
                    )
                    .setNegativeButton("キャンセル",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }
                    )
                    .show();
            return true;
        }
        return false;
    }

    public static class DifficultyFragment extends Fragment {
        public static DifficultyFragment newInstance() {
            DifficultyFragment fragment = new DifficultyFragment();
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_difficulty, container, false);

            Button easyButton = (Button) rootView.findViewById(R.id.easy_button);
            Button normalButton = (Button) rootView.findViewById(R.id.normal_button);
            Button hardButton = (Button) rootView.findViewById(R.id.hard_button);

            easyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    difficultySelect("EASY");
                }
            });
            normalButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    difficultySelect("NORMAL");
                }
            });
            hardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    difficultySelect("HARD");
                }
            });
            return rootView;
        }

        private void difficultySelect(String difficulty) {
            Intent intent = new Intent(getActivity(), TetrisActivity.class);
            intent.putExtra("Difficulty", difficulty);
            startActivity(intent);
        }
    }
}