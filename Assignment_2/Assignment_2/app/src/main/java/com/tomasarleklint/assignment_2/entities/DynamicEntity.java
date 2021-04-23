package com.tomasarleklint.assignment_2.entities;

import com.tomasarleklint.assignment_2.Config;
import com.tomasarleklint.assignment_2.utilities.Utils;

public class DynamicEntity extends StaticEntity {

    public float _velX = 0;
    public float _velY = 0;
    public float _gravity = 0;
    boolean _isOnGround = false;

    public DynamicEntity(String spriteName, int xpos, int ypos) {
        super(spriteName, xpos, ypos);
    }

    @Override
    public void update(double dt){
        _x += Utils.clamp((float) (_velX * dt), -Config.MAX_DELTA, Config.MAX_DELTA);

        if(!_isOnGround){
            final float gravityThisTick = (float) (_gravity * dt);
            _velY += gravityThisTick;
        }
        _y += Utils.clamp((float) (_velY * dt), -Config.MAX_DELTA, Config.MAX_DELTA);
        if(_y > _game.getWorldHeight()){
            _y = 0f;
        }
        _isOnGround = false;
    }
}
