package com.tomasarleklint.assignment_3;

import android.content.res.Resources;
import android.opengl.Matrix;

public class Camera {
    // Create the projection Matrix. This is used to project the scene onto a 2D viewport.
    private float[] _viewportMatrix = new float[4*4]; //In essence, it is our our Camera

    public Camera(final float x, final float y){
        final int offset = 0;
        final float left = 0;
        final float right = x;
        final float bottom = y;
        final float top = 0;
        final float near = 0f;
        final float far = 1f;
        Matrix.orthoM(_viewportMatrix, offset, left, right, bottom, top, near, far);
    }
    public float[] getViewportMatrix(){
        return _viewportMatrix;
    }
}
