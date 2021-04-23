package com.tomasarleklint.assignment_3.utilities;

import android.util.Log;

public class Utils {

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

    public static float between(float min, float max){
        return (float) Math.random() * (max-min) + min;
    }

    public static void expect(final boolean condition, final String tag) {
        Utils.expect(condition, tag, "Expectation was broken.");
    }

    public static void expect(final boolean condition, final String tag, final String message) {
        if(!condition) {
            Log.e(tag, message);
        }
    }

    public static void require(final boolean condition) {
        Utils.require(condition, "Assertion failed!");
    }

    public static void require(final boolean condition, final String message) {
        if (!condition) {
            throw new AssertionError(message);
        }
    }
}
