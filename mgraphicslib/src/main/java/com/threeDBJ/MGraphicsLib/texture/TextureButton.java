package com.threeDBJ.MGraphicsLib.texture;

import android.content.Context;

import com.threeDBJ.MGraphicsLib.GLEnvironment;
import com.threeDBJ.MGraphicsLib.math.Vec2;

import javax.microedition.khronos.opengles.GL11;

public class TextureButton extends TextureTextView {

    private Texture normalTexture, pressedTexture;

    public TextureButton(TextureFont font) {
        super(font);
    }

    public void draw(GL11 gl) {
        super.draw(gl);
    }

    public void setPressedTexture(GL11 gl, Context c, int res) {
        pressedTexture = new Texture(res);
        GLEnvironment.loadTexture(gl, c, pressedTexture);
    }

    public void setTexture(GL11 gl, Context c, int res) {
        super.setTexture(gl, c, res);
        normalTexture = texture;
    }

    @Override
    void setPressed(boolean pressed) {
        super.setPressed(pressed);
        this.texture = pressed ? pressedTexture : normalTexture;
    }

    public boolean handleActionDown(Vec2 p) {
        if (touchHit(p)) {
            setPressed(true);
            return true;
        }
        return false;
    }

    public boolean handleActionMove(Vec2 p) {
        if (pressed) {
            if (!touchHit(p)) {
                setPressed(false);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean handleActionUp(Vec2 p) {
        if (pressed) {
            setPressed(false);
            if (listener != null && touchHit(p)) {
                listener.onClick();
                return true;
            }
        }
        return false;
    }

}
