package com.tomasarleklint.assignment_3.entities;

import android.opengl.GLES20;
import android.util.Log;

import com.tomasarleklint.assignment_3.Config;
import com.tomasarleklint.assignment_3.graphics.Mesh;
import com.tomasarleklint.assignment_3.utilities.Utils;

public class Asteroid extends GLEntity {
    private static final String TAG = "Asteroids";
    public int _size = 0;

    public Asteroid(final float x, final float y, int points, int size){
        _size = size;
        switch(_size) {
            case Config.ASTEROID_SMALL:
                _width = Config.ASTEROID_WIDTH_SMALL;
                _velX = Utils.between(-Config.ASTEROID_SMALL_VEL, Config.ASTEROID_SMALL_VEL);
                _velY = Utils.between(-Config.ASTEROID_SMALL_VEL, Config.ASTEROID_SMALL_VEL);
                break;
            case Config.ASTEROID_MEDIUM:
                _width = Config.ASTEROID_WIDTH_MEDIUM;
                _velX = Utils.between(-Config.ASTEROID_MEDIUM_VEL, Config.ASTEROID_MEDIUM_VEL);
                _velY = Utils.between(-Config.ASTEROID_MEDIUM_VEL, Config.ASTEROID_MEDIUM_VEL);
                break;
            case Config.ASTEROID_LARGE:
                _width = Config.ASTEROID_WIDTH_LARGE;
                _velX = Utils.between(-Config.ASTEROID_LARGE_VEL, Config.ASTEROID_LARGE_VEL);
                _velY = Utils.between(-Config.ASTEROID_LARGE_VEL, Config.ASTEROID_LARGE_VEL);
                break;
            default:
                Log.d(TAG, "Somehow an incorrect asteroid size was requested, setting to large");
                _width = Config.ASTEROID_WIDTH_LARGE;
                break;
        }

        if(points < 3){ points = 3; } //triangles or more, please. :)
        _x = x;
        _y = y;
        _height = _width;
        _velR = Utils.between(-Config.ASTEROID_ROTATION_VEL, Config.ASTEROID_ROTATION_VEL);
        final double radius = _width*0.5;
        final float[] verts = Mesh.generateLinePolygon(points, radius);
        _mesh = new Mesh(verts, GLES20.GL_LINES);
        _mesh.setWidthHeight(_width, _height);
    }

    public void onCollision(final boolean give_birth){
        if(give_birth){
            _size--;
        }else{
            _size = 0;
        }
        _isAlive = false;
    }

    @Override
    public void update(final double dt){
        _rotation += _velR;
        super.update(dt);
    }
}
