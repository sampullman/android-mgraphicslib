package com.threeDBJ.MGraphicsLib.texture;

import android.content.Context;

import com.threeDBJ.MGraphicsLib.GLEnvironment;
import com.threeDBJ.MGraphicsLib.math.Vec2;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

public class TextureStateView extends TextureView {

    ArrayList<Texture> states = new ArrayList<Texture>();
    int curState = 0;

    public boolean handleActionDown(Vec2 p) {
        if (touchHit(p)) {
            pressed = true;
            curState += 1;
            if (curState >= states.size()) curState = 0;
            mTexture = states.get(curState);
            return true;
        }
        return false;
    }

    public void addTexture(GL11 gl, Context c, int res) {
        Texture t = new Texture(res);
        GLEnvironment.loadTexture(gl, c, t);
        states.add(t);
        if (states.size() == 1) {
            mTexture = t;
            enableTextures();
        }
    }

}