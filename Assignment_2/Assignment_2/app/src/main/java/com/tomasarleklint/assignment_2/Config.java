package com.tomasarleklint.assignment_2;

import android.graphics.Color;

public class Config {
    public static final float DEFAULT_DIMENSION = 1.0f;

    public static final String NULLSPRITE = "nullsprite";
    public static final String PLAYER = "blue_left1";
    public static final String SPEAR = "spearsup_brown";
    public static final String COIN = "coinyellow_shade";
    public static final int NO_TILE = 0;

    public static final float MAX_DELTA = 0.48f;
    public static final float GRAVITY = 40f;

    public static final float PLAYER_RUN_SPEED = 6.0f; //meters per second
    public static final float PLAYER_JUMP_FORCE = -15f;    //whatever feels good
    public static final float MIN_INPUT_TO_TURN = 0.05f; //5% joystick input before we start turning animations

    public static final float COIN_VEL = 2f;
    public static final float COIN_OFFSET = 1f;

    public static final int PLAYER_START_HEALTH = 3;
    public static final int INVULN_DURATION = 1000;
    public static final int INVULN_TICK = INVULN_DURATION/8;
    public static final float PLAYER_KNOCKBACK_VEL = 7f;
    public static final int LEFT = 1;
    public static final int RIGHT = -1;

    public static int TARGET_HEIGHT = 720; //TODO GAME SETTING
    public static final double NANOS_TO_SECONDS = 1.0 / 1000000000;
    public static final float METERS_TO_SHOW_X = 16f; //set the value you want fixed
    public static final float METERS_TO_SHOW_Y = 0f;  //the other is calculated at runtime!
    public static final int BG_COLOR = Color.rgb(135, 206, 235);

    public static final int MAX_STREAMS = 3;
    public static int SOUND_JUMP = 0;
    public static int SOUND_GAME_OVER = 0;
    public static int SOUND_BGM = 0;
    public static int SOUND_DAMAGE = 0;
    public static int SOUND_COIN = 0;
    public static final float LEFT_VOLUME = 1f;
    public static final float RIGHT_VOLUME = 1f;
    public static final int PRIORITY = 1;
    public static final int LOOP_ONCE = 0;
    public static final int LOOP_ALWAYS = -1;
    public static final float RATE = 1.0f;
}