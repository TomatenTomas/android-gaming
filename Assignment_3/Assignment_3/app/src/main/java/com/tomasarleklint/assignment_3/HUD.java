package com.tomasarleklint.assignment_3;

import com.tomasarleklint.assignment_3.entities.Text;

import java.util.ArrayList;

public class HUD {
    public static Game _game = null;
    private ArrayList<Text> _texts = new ArrayList<>();

    public HUD(){
    }

    public void render(float[] viewportMatrix){
        String _fpsText = _game.getResources().getString(R.string.FPS, _game._fps);
        String _healthText = _game.getResources().getString(R.string.HEALTH, _game._health);
        String _levelText = _game.getResources().getString(R.string.LEVEL, _game._level);
        String _scoreText = _game.getResources().getString(R.string.SCORE, _game._score);
        String _gameOverText = _game.getResources().getString(R.string.GAME_OVER);
        String _victoryText = _game.getResources().getString(R.string.VICTORY);

        _texts.clear();
        _texts.add(new Text(_fpsText, Config.TEXT_X_FPS_OFFSET, 1 * Config.TEXT_Y_OFFSET));
        _texts.add(new Text(_levelText, Config.TEXT_X_OFFSET, 1 * Config.TEXT_Y_OFFSET));
        _texts.add(new Text(_healthText, Config.TEXT_X_OFFSET, 2 * Config.TEXT_Y_OFFSET));
        _texts.add(new Text(_scoreText, Config.TEXT_X_OFFSET, 3 * Config.TEXT_Y_OFFSET));

        if(_game._gameOver){
            _texts.add(new Text(_gameOverText, Config.TEXT_X_OFFSET, 5 * Config.TEXT_Y_OFFSET));
        }else if(_game._victory){
            _texts.add(new Text(_victoryText, Config.TEXT_X_OFFSET, 5 * Config.TEXT_Y_OFFSET));
        }

        for(final Text t : _texts){
            t.render(viewportMatrix);
        }
    }
}
