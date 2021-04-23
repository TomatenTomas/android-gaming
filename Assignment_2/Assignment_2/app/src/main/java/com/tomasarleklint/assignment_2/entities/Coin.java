package com.tomasarleklint.assignment_2.entities;

import com.tomasarleklint.assignment_2.Config;

public class Coin extends DynamicEntity {
    private int _direction = 1;
    private float _start_y = 0;
    public Coin(String spriteName, int xpos, int ypos) {
        super(spriteName, xpos, ypos);
        _sprite = spriteName;
        _start_y = (float) ypos;
    }

    @Override
    public void update(final double dt) {
        _velY = _direction * Config.COIN_VEL;
        super.update(dt);
        if(_y >= _start_y + Config.COIN_OFFSET){
            _direction = -1;
        } else if (_y <= _start_y - Config.COIN_OFFSET){
            _direction = 1;
        }
    }
}
