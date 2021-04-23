package com.tomasarleklint.assignment_3;

import android.graphics.Color;

import com.tomasarleklint.assignment_3.graphics.GLPixelFont;

public class Config {
    public static final int BG_COLOR = Color.rgb(135, 206, 235);
    public static float WORLD_WIDTH = 320;

    public static long SECOND_IN_NANOSECONDS = 1000000000;
    public static long MILLISECOND_IN_NANOSECONDS = 1000000;
    public static float NANOSECONDS_TO_MILLISECONDS = 1.0f / MILLISECOND_IN_NANOSECONDS;
    public static float NANOSECONDS_TO_SECONDS = 1.0f / SECOND_IN_NANOSECONDS;

    public static final float TO_DEGREES = (float)(180.0/Math.PI);
    public static final float TO_RADIANS = (float)Math.PI/180.0f;

    public static final int STAR_COUNT = 100;
    public static final int ASTEROID_COUNT = 10;
    public static final float ASTEROID_POINTS_MIN = 8;
    public static final float ASTEROID_POINTS_MAX = 12;
    public static final int ASTEROID_WIDTH_LARGE = 18;
    public static final int ASTEROID_WIDTH_MEDIUM = 13;
    public static final int ASTEROID_WIDTH_SMALL = 9;
    public static final int ASTEROID_LARGE = 3;
    public static final int ASTEROID_MEDIUM = 2;
    public static final int ASTEROID_SMALL = 1;
    public static final float ASTEROID_LARGE_VEL = 14f;
    public static final float ASTEROID_MEDIUM_VEL = 20f;
    public static final float ASTEROID_SMALL_VEL = 26f;
    public static final float ASTEROID_ROTATION_VEL = 1f;

    //GAMEPLAY VALUES
    public static final float TIME_TO_LIVE = 3.0f; //seconds
    public static final float TIME_BETWEEN_SHOTS = 0.25f;
    public static final int BULLET_COUNT = (int)(TIME_TO_LIVE/TIME_BETWEEN_SHOTS)+1;
    public static final float BULLET_SPEED = 120f;

    //player stuff
    public static final float ROTATION_VELOCITY = 360f;
    public static final float THRUST = 8f;
    public static final float DRAG = 0.99f;
    public static final float PLAYER_HEIGHT = 9f;
    public static final float PLAYER_WIDTH = 6f;
    public static final float FLAME_HEIGHT = 5f;
    public static final float FLAME_WIDTH = 3f;
    public static final float FLAME_SPAWN_OFFSET = 6f;
    public static final float FLAME_RENDER_TIMER = 0.1f;
    public static final int STARTING_HEALTH = 3;

    public static final int FONT_WIDTH = 5; //characters are 5 units wide
    public static final int FONT_HEIGHT = 7; //characters are 7 units tall
    public static final int FONT_CHAR_COUNT = 46; //the font definition contains 46 entries
    public static final int FONT_OFFSET = 45; //it start at ASCII code 45 "-", and ends at 90 "Z".

    public static final float GLYPH_WIDTH = FONT_WIDTH;
    public static final float GLYPH_HEIGHT = FONT_HEIGHT;
    public static final float GLYPH_SPACING = 1f;
    public static final float TEXT_SCALE = 2f;
    public static final float TEXT_X_OFFSET = 10f;
    public static final float TEXT_Y_OFFSET = 10f;
    public static final float TEXT_X_FPS_OFFSET = WORLD_WIDTH - 75;
}
