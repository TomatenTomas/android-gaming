package com.tomasarleklint.assignment_2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tomasarleklint.assignment_2.input.InputManager;
import com.tomasarleklint.assignment_2.input.TouchController;

public class MainActivity extends AppCompatActivity {
    Game _game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _game = new Game(this);
        setContentView(R.layout.activity_main);
        _game = findViewById(R.id.game);
        InputManager controls = new TouchController(findViewById(R.id.touchControl));
        _game.setControls(controls);

        TextView tv_health = findViewById(R.id.hud_health);
        TextView tv_coins = findViewById(R.id.hud_coins);
        TextView tv_victory = findViewById(R.id.hud_victory);
        _game.setTextviews(tv_health, tv_coins, tv_victory);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _game.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        _game.onPause();
    }

    @Override
    protected void onDestroy(){
        _game.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(!hasFocus) {
            return;
        }
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}