package com.tomasarleklint.assignment_2.utilities;

public abstract class Utils {

    public static float wrap(float val, final float min, final float max){
        if(val < min){
            val = max;
        } else if(val > max) {
            val = min;
        }
        return val;
    }

    public static float clamp(float val, final float min, final float max){
        if(val > max){
            val = max;
        } else if(val < min){
            val = min;
        }
        return val;
    }
}
