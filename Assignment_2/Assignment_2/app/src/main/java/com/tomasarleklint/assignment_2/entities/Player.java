package com.tomasarleklint.assignment_2.entities;

import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;

import com.tomasarleklint.assignment_2.Config;
import com.tomasarleklint.assignment_2.input.InputManager;
import com.tomasarleklint.assignment_2.levels.LevelManager;

public class Player extends DynamicEntity {

    private int _facing = Config.LEFT;
    private boolean _knockback = false;

    public Player(final String spriteName, final int xpos, final int ypos){
        super(spriteName, xpos, ypos);
        _gravity = Config.GRAVITY;
        _width = 1.0f;
        _height = 1.0f;
        loadBitmap(spriteName, xpos, ypos); //TODO this is cause resetting size
    }

    @Override
    public void render(Canvas canvas, Matrix transform, Paint paint) {
        int saturation = (_game._invulnframe) ? 0 : 1;
        setGrayscale(saturation, paint);

        transform.preScale(_facing, 1.0f);
        if(_facing == Config.RIGHT) {
            final float offset = _game.worldToScreenX(_width);
            transform.postTranslate(offset, 0);
        }
        super.render(canvas, transform, paint);
        setGrayscale(1, paint);
    }

    private void setGrayscale(final int saturation, final Paint paint){
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(saturation);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(filter);
    }

    @Override
    public void update(final double dt) {
        final InputManager controls = _game.getControls();
        final float direction = controls._horizontalFactor;
        updateFacingDirection(direction);
        if(_knockback){
            _velX = _facing * Config.PLAYER_KNOCKBACK_VEL;
        } else {
            _velX = direction * Config.PLAYER_RUN_SPEED;
        }
        if(controls._isJumping && _isOnGround){
            _velY = Config.PLAYER_JUMP_FORCE;
            _isOnGround = false;
            _game._jukebox.play(Config.SOUND_JUMP);
        }
        super.update(dt);
    }

    private void updateFacingDirection(final float controlDirection){
        if(Math.abs(controlDirection) < Config.MIN_INPUT_TO_TURN){ return; }
        if(controlDirection < 0) { _facing = Config.LEFT; }
        else if(controlDirection > 0) { _facing = Config.RIGHT; }
    }

    @Override
    public void onCollision(Entity that){
        Entity.getOverlap(this, that, Entity.overlap);
        if(that._sprite.equals(Config.SPEAR)){
            if(!_game._invulnerable){
                _game._player_health--;
                if(_game._player_health > 0) {
                    _game._jukebox.play(Config.SOUND_DAMAGE);
                    _knockback = true;
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            new CountDownTimer(Config.INVULN_DURATION, Config.INVULN_TICK) {
                                @Override
                                public void onTick(long l) {
                                    if(_game._invulnframe){ //get knockbacked for one invulnframe tick
                                        _knockback = false;
                                    }
                                    _game._invulnframe = !_game._invulnframe;
                                }

                                @Override
                                public void onFinish() {
                                    _game._invulnerable = false;
                                    _game._invulnframe = false;
                                }
                            }.start();
                        }
                    });
                    _game._invulnerable = true;
                } else {
                    _game._game_over = true;
                    _game._jukebox.play(Config.SOUND_GAME_OVER);
                }
            }
        } else if (that._sprite.equals(Config.COIN)) {
            LevelManager.removeEntity(that);
            _game._coins_taken++;
            _game._jukebox.play(Config.SOUND_COIN);

        } else { //colliding with the world
            _x += Entity.overlap.x;
            _y += Entity.overlap.y;
            if(Entity.overlap.y != 0) {
                _velY = 0;
                if (Entity.overlap.y < 0f) { //we've hit our feet
                    _isOnGround = true;
                }// if overlap.y > 0f, we've hit our head
            }
        }
    }
}
