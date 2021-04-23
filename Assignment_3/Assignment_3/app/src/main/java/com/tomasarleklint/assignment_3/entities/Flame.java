package com.tomasarleklint.assignment_3.entities;

import android.opengl.GLES20;

import com.tomasarleklint.assignment_3.Config;
import com.tomasarleklint.assignment_3.graphics.Mesh;

public class Flame extends GLEntity{
    public Flame(float x, float y) {
        _x = x;
        _y = y + Config.FLAME_SPAWN_OFFSET;
        _width = Config.FLAME_WIDTH;
        _height = Config.FLAME_HEIGHT;
        float vertices[] = { // in counterclockwise order:
                0.0f,  0.5f, 0.0f, 	// top
                -0.5f, -0.5f, 0.0f,	// bottom left
                0.5f, -0.5f, 0.0f  	// bottom right
        };
        _mesh = new Mesh(vertices, GLES20.GL_TRIANGLES);
        _mesh.setWidthHeight(_width, _height);
    }

    public void attachToSource(GLEntity source){
        final float theta = source._rotation*Config.TO_RADIANS;
        _rotation = source._rotation;
        _x = source._x - (float)Math.sin(theta) * Config.FLAME_SPAWN_OFFSET;
        _y = source._y + (float)Math.cos(theta) * Config.FLAME_SPAWN_OFFSET;
    }

    @Override
    public void render(float[] viewportMatrix) {
        boolean fire = _game.maybeFireBoost();
        if(fire){
            super.render(viewportMatrix);
        }
    }
}
