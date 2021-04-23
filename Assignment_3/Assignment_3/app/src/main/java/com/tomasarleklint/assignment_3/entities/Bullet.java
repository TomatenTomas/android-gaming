package com.tomasarleklint.assignment_3.entities;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.tomasarleklint.assignment_3.utilities.CollisionDetection;
import com.tomasarleklint.assignment_3.Config;
import com.tomasarleklint.assignment_3.graphics.Mesh;

public class Bullet extends GLEntity {
    private static Mesh BULLET_MESH = new Mesh(Mesh.POINT, GLES20.GL_POINTS); //Q&D pool, Mesh.POINT is just [0,0,0] float array
    public float _ttl = Config.TIME_TO_LIVE;

    public Bullet() {
        setColors(1, 0, 1, 1);
        _mesh = BULLET_MESH; //all bullets use the exact same mesh
    }

    public void fireFrom(GLEntity source){
        final float theta = source._rotation*Config.TO_RADIANS;
        _x = source._x + (float)Math.sin(theta) * (source._width*0.5f);
        _y = source._y - (float)Math.cos(theta) * (source._height*0.5f);
        _velX = source._velX;
        _velY = source._velY;
        _velX += (float)Math.sin(theta) * Config.BULLET_SPEED;
        _velY -= (float)Math.cos(theta) * Config.BULLET_SPEED;
        _ttl = Config.TIME_TO_LIVE;
    }
    public boolean isDead(){
        return _ttl < 1;
    }

    @Override
    public void update(double dt) {
        if(_ttl > 0) {
            _ttl -= dt;
            super.update(dt);
        }
    }
    @Override
    public void render(final float[] viewportMatrix){
        if(_ttl > 0) {
            super.render(viewportMatrix);
        }
    }

    @Override
    public boolean isColliding(final GLEntity that){
        if(!areBoundingSpheresOverlapping(this, that)){ //quick rejection
            return false;
        }
        final PointF[] asteroidVerts = that.getPointList();
        return CollisionDetection.polygonVsPoint(asteroidVerts, _x, _y);
    }

    @Override
    public void onCollision(final GLEntity that){
        _ttl = 0;
    }
}
