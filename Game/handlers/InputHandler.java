package com.devour.all.handlers;

/**
 * Created by Jacob on 6/28/2015.
 */
public class InputHandler {
    static boolean touchDown = false;
    static float xPos, yPos;
    public static void setTouch(float x, float y) {
        xPos = x;
        yPos = y;
    }
    public static float getXPos() { return xPos; }
    public static float getYPos() { return yPos; }

    public static boolean ifTouch(){ return touchDown; }
    public static void setTouchDown(boolean b) { touchDown = b; }
}
