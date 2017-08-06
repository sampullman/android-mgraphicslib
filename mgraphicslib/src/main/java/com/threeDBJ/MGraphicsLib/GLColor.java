package com.threeDBJ.MGraphicsLib;

public class GLColor {

    public static final double THRESH = 0.01;

    float red, green, blue, alpha;

    public GLColor(float red, float green, float blue, float alpha) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = alpha;
    }

    public GLColor(float red, float green, float blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.alpha = 1f;
    }

    public GLColor(GLColor color) {
        this.red = color.red;
        this.green = color.green;
        this.blue = color.blue;
        this.alpha = color.alpha;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GLColor) {
            GLColor color = (GLColor) other;
            return (closeEnough(red, color.red) &&
                    closeEnough(green, color.green) &&
                    closeEnough(blue, color.blue) &&
                    closeEnough(alpha, color.alpha));
        }
        return false;
    }

    public static boolean closeEnough(float c1, float c2) {
        return Math.abs(c1 - c2) < THRESH;
    }

    public String toString() {
        return "[ " + alpha + " " + red + " " + green + " " + blue + " ]";
    }
}
