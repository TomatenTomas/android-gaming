package com.tomasarleklint.assignment_3.entities;

import android.opengl.Matrix;

import com.tomasarleklint.assignment_3.Config;
import com.tomasarleklint.assignment_3.graphics.GLManager;
import com.tomasarleklint.assignment_3.graphics.GLPixelFont;
import com.tomasarleklint.assignment_3.graphics.Mesh;

public class Text extends GLEntity {
    public static final GLPixelFont FONT = new GLPixelFont();
    Mesh[] _meshes = null;
    private float _spacing = Config.GLYPH_SPACING; //spacing between characters
    private float _glyphWidth = Config.GLYPH_WIDTH;
    private float _glyphHeight = Config.GLYPH_HEIGHT;

    public Text(final String s, final float x, final float y) {
        setString(s);
        _x = x;
        _y = y;
        setScale(Config.TEXT_SCALE);
    }

    @Override
    public void render(final float[] viewportMatrix){
        final int OFFSET = 0;
        for(int i = 0; i < _meshes.length; i++){
            if(_meshes[i] == null){ continue; }
            Matrix.setIdentityM(modelMatrix, OFFSET); //reset model matrix
            Matrix.translateM(modelMatrix, OFFSET, _x + (_glyphWidth+_spacing)*i, _y, _depth);
            Matrix.scaleM(modelMatrix, OFFSET, _scale, _scale, 1f);
            Matrix.multiplyMM(viewportModelMatrix, OFFSET, viewportMatrix, OFFSET, modelMatrix, OFFSET);
            GLManager.draw(_meshes[i], viewportModelMatrix, _color);
        }
    }

    public void setScale(float factor){
        _scale = factor;
        _spacing = Config.GLYPH_SPACING*_scale;
        _glyphWidth = Config.GLYPH_WIDTH*_scale;
        _glyphHeight = Config.GLYPH_HEIGHT*_scale;
        _height = _glyphHeight;
        _width = (_glyphWidth+_spacing)*_meshes.length;
    }

    public void setString(final String s){
        _meshes = FONT.getString(s);
    }
}