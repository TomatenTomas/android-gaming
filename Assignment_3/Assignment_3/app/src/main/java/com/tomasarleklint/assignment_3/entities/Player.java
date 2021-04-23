package com.tomasarleklint.assignment_3.entities;

import android.graphics.PointF;
import android.opengl.GLES20;

import com.tomasarleklint.assignment_3.utilities.CollisionDetection;
import com.tomasarleklint.assignment_3.Config;
import com.tomasarleklint.assignment_3.graphics.Mesh;

public class Player extends GLEntity {
    private float _bulletCooldown = 0;

    public Player(float x, float y){
        super();
        _x = x;
        _y = y;
        _width = Config.PLAYER_WIDTH;
        _height = Config.PLAYER_HEIGHT;
        float vertices[] = { // in counterclockwise order:
                0.0f,  0.5f, 0.0f, 	// top
                -0.5f, -0.5f, 0.0f,	// bottom left
                0.5f, -0.5f, 0.0f  	// bottom right
        };
        _mesh = new Mesh(vertices, GLES20.GL_TRIANGLES);
        _mesh.setWidthHeight(_width, _height);
        _mesh.flipY();
    }

    @Override
    public void update(double dt){
        //ship movement
        _rotation += (dt* Config.ROTATION_VELOCITY) * _game._inputs._horizontalFactor;
        if(_game._inputs._pressingB){
            final float theta = _rotation*Config.TO_RADIANS;
            _velX += (float)Math.sin(theta) * Config.THRUST;
            _velY -= (float)Math.cos(theta) * Config.THRUST;
        }
        _velX *= Config.DRAG;
        _velY *= Config.DRAG;

        //bullets
        _bulletCooldown -= dt;
        if(_game._inputs._pressingA && _bulletCooldown <= 0){
            setColors(1, 0, 1, 1);
            if(_game.maybeFireBullet(this)){
                _bulletCooldown = Config.TIME_BETWEEN_SHOTS;
            }
        }else{
            setColors(1.0f, 1, 1,1);
        }

        super.update(dt);

        //flame
        _game.attachFlame(this);
    }

    @Override
    public boolean isColliding(final GLEntity that){
        if(!areBoundingSpheresOverlapping(this, that)){ //quick rejection test
            return false;
        }
        final PointF[] shipHull = _mesh.getPointList(_x, _y, _rotation);
        final PointF[] asteroidHull =  that.getPointList();

        if(CollisionDetection.polygonVsPolygon(shipHull, asteroidHull)){
            return true;
        }
        return CollisionDetection.polygonVsPoint(asteroidHull, _x, _y); //finally, check if we're inside the asteroid
    }

    @Override
    public void onCollision(final GLEntity that){
        _game._health--;
        if(_game._health <= 0){
            _game._gameOver = true;
        }
    }
}