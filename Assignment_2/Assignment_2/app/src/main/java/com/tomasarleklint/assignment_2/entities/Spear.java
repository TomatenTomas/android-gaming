package com.tomasarleklint.assignment_2.entities;

public class Spear extends StaticEntity{
    static final int DAMAGE_DEALT = 1;

    public Spear(String spriteName, int xpos, int ypos) {
        super(spriteName, xpos, ypos);
        _sprite = spriteName;
    }
}