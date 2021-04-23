package com.tomasarleklint.assignment_2;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;

import com.tomasarleklint.assignment_2.entities.Entity;

import java.util.Locale;

public class Viewport {
    private final PointF _lookAt = new PointF(0f, 0f);
    private int _pixelsPerMeterX; //viewport "density"
    private int _pixelsPerMeterY;
    private int _screenWidth; //resolution
    private int _screenHeight;
    private int _screenCenterX; //center screen
    private int _screenCenterY;
    private float _metersToShowX; //Field of View
    private float _metersToShowY;
    private float _halfDistX; //cached value (0.5*FOV)
    private float _halfDistY;
    private final static float BUFFER = 2f; //overdraw, to avoid visual gaps
    private RectF _worldEdges = null;
    private float _stageWidth = 0;
    private float _stageHeight = 0;

    public Viewport(final int screenWidth, final int screenHeight, final float metersToShowX, final float metersToShowY){
        _screenWidth = screenWidth;
        _screenHeight = screenHeight;
        _screenCenterX = _screenWidth / 2;
        _screenCenterY = _screenHeight / 2;
        _lookAt.x = 0.0f;
        _lookAt.y = 0.0f;
        setMetersToShow(metersToShowX, metersToShowY);
    }

    @Override
    public String toString(){
        return String.format(Locale.getDefault(), "Viewport [%dpx, %dpx / %.1fm, %.1fm]", _screenWidth, _screenHeight, _metersToShowX, _metersToShowY);
    }

    //setMetersToShow calculates the number of physical pixels per meters
    //so that we can translate our game world (meters) to the screen (pixels)
    //provide the dimension(s) you want to lock. The viewport will automatically
    // size the other axis to fill the screen perfectly.
    private void setMetersToShow(float metersToShowX, float metersToShowY){
        if (metersToShowX <= 0f && metersToShowY <= 0f) throw new IllegalArgumentException("One of the dimensions must be provided!");
        //formula: new height = (original height / original width) x new width
        _metersToShowX = metersToShowX;
        _metersToShowY = metersToShowY;
        if(metersToShowX == 0f || metersToShowY == 0f){
            if(metersToShowY > 0f) { //if Y is configured, calculate X
                _metersToShowX = ((float) _screenWidth / _screenHeight) * metersToShowY;
            }else { //if X is configured, calculate Y
                _metersToShowY = ((float) _screenHeight / _screenWidth) * metersToShowX;
            }
        }
        _halfDistX = (_metersToShowX+BUFFER) * 0.5f;
        _halfDistY = (_metersToShowY+BUFFER) * 0.5f;
        _pixelsPerMeterX = (int)(_screenWidth / _metersToShowX);
        _pixelsPerMeterY = (int)(_screenHeight / _metersToShowY);
    }

    public void lookAt(float x, float y){
        if(_worldEdges != null){
            if(y < _metersToShowY/2){
                y = _metersToShowY/2;
            } else if(y > (_stageHeight - _metersToShowY/2)){
                y = _stageHeight - _metersToShowY/2;
            }

            if(x < _metersToShowX/2){
                x = _metersToShowX/2;
            } else if(x > (_stageWidth - _metersToShowX/2)){
                x = _stageWidth - _metersToShowX/2;
            }
        }
        _lookAt.x = x;
        _lookAt.y = y;
    }
    public void lookAt(final Entity obj){ lookAt(obj.centerX(), obj.centerY()); }
    public void lookAt(final PointF pos){
        lookAt(pos.x, pos.y);
    }

    public void worldToScreen(final float worldPosX, final float worldPosY, Point screenPos){
        screenPos.x = (int) (_screenCenterX - ((_lookAt.x - worldPosX) * _pixelsPerMeterX));
        screenPos.y = (int) (_screenCenterY - ((_lookAt.y - worldPosY) * _pixelsPerMeterY));
    }
    public void worldToScreen(final PointF worldPos, Point screenPos){
        worldToScreen(worldPos.x, worldPos.y, screenPos);
    }
    public void worldToScreen(final Entity e, final Point screenPos){
        worldToScreen(e._x, e._y, screenPos);
    }

    public boolean inView(final Entity e) {
        final float maxX = (_lookAt.x + _halfDistX);
        final float minX = (_lookAt.x - _halfDistX)-e._width;
        final float maxY = (_lookAt.y + _halfDistY);
        final float minY  = (_lookAt.y - _halfDistY)-e._height;
        return (e._x > minX && e._x < maxX)
                && (e._y > minY && e._y < maxY);
    }

    public boolean inView(final RectF bounds) {
        final float right = (_lookAt.x + _halfDistX);
        final float left = (_lookAt.x - _halfDistX);
        final float bottom = (_lookAt.y + _halfDistY);
        final float top  = (_lookAt.y - _halfDistY);
        return (bounds.left < right && bounds.right > left)
                && (bounds.top < bottom && bounds.bottom > top);
    }

    public void setBounds(final RectF worldEdges){
        _worldEdges = worldEdges;
        _stageHeight = worldEdges.bottom;
        _stageWidth = worldEdges.right;
    }

    public float getHorizontalView(){ return _metersToShowX; }
    public float getVerticalView(){ return _metersToShowY; }
    public int getScreenWidth() { return _screenWidth; }
    public int getScreenHeight(){ return _screenHeight; }
    public int getPixelsPerMeterX(){ return _pixelsPerMeterX; }
    public int getPixelsPerMeterY(){ return _pixelsPerMeterY; }
}