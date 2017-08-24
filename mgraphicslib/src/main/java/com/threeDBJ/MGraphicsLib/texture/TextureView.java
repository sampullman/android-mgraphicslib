package com.threeDBJ.MGraphicsLib.texture;

import com.threeDBJ.MGraphicsLib.Clickable;
import com.threeDBJ.MGraphicsLib.GLColor;
import com.threeDBJ.MGraphicsLib.GLEnvironment;
import com.threeDBJ.MGraphicsLib.GLFace;
import com.threeDBJ.MGraphicsLib.GLShape;
import com.threeDBJ.MGraphicsLib.GLVertex;
import com.threeDBJ.MGraphicsLib.math.Vec2;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

public class TextureView extends GLEnvironment implements Clickable {

    public interface TextureClickListener {
        void onClick();
    }

    private ArrayList<TextureView> children = new ArrayList<>();
    float l, r, b, t, z, percentX, percentY, texRight, texTop;
    GLVertex lb, lt, rt, rb;
    boolean visible = true;
    TextureClickListener listener;
    boolean pressed = false;

    private TextureViewAnimation animation;

    public TextureView() {
    }

    public void setFace(float l, float r, float b, float t, float z, GLColor c) {
        this.l = l;
        this.r = r;
        this.b = b;
        this.t = t;
        this.z = z;
        vertexList.clear();
        shapeList.clear();
        GLShape surface = new GLShape(this);
        rb = surface.addVertex(r, b, z);
        rt = surface.addVertex(r, t, z);
        lb = surface.addVertex(l, b, z);
        lt = surface.addVertex(l, t, z);
        GLFace f = new GLFace(rb, rt, lb, lt);
        f.setColor(c);
        surface.addFace(f);
        surface.setTexture(texture);
        addShape(surface);
        generate();
    }

    public void setTextureBounds(float percentX, float percentY) {
        this.percentX = percentX;
        this.percentY = percentY;
        texRight = (percentX * (r - l)) + l;
        texTop = (percentY * (t - b)) + b;
    }

    public void draw(GL11 gl) {
        super.draw(gl);
        gl.glBindTexture(GL11.GL_TEXTURE_2D, texture.id);
        indexBuffer.position(0);
        colorBuffer.position(0);
        vertexBuffer.position(0);
        textureBuffer.position(0);
        gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, textureBuffer);
        gl.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
        gl.glColorPointer(4, GL11.GL_FIXED, 0, colorBuffer);
        gl.glDrawElements(GL11.GL_TRIANGLES, indexCount, GL11.GL_UNSIGNED_SHORT, indexBuffer);
        for (TextureView child : children) {
            child.draw(gl);
        }
    }

    public void addChild(TextureView child) {
        children.add(child);
    }

    public void setClickListener(TextureClickListener listener) {
        this.listener = listener;
    }

    // public void setVisibility(boolean visible) {
    // 	this.visible = visible;
    // 	for(TextureView child : children) {
    // 	    child.setVisibility(visible);
    // 	}
    // }

    // public boolean isVisible() {
    // 	return visible;
    // }

    public void animate(TextureViewAnimation animation) {
        setAnimation(animation);
        startAnimation();
    }

    public void setAnimation(TextureViewAnimation animation) {
        this.animation = animation;
        animation.addView(this);
        for (TextureView child : children) {
            child.setAnimation(animation);
        }
    }

    public void startAnimation() {
        animation.startAnimation();
    }

    public void animate() {
        if (animation != null) {
            animation.stepAnimation();
            if (animation.finished()) {
                animation = null;
            }
        }
    }

    /* Translates the view. Not a good idea to translate in z direction,
       wierd things will happen unless translated back within a small time frame */
    public void translate(float x, float y, float z) {
        l += x;
        r += x;
        texRight += x;
        b += y;
        t += y;
        texTop += y;
        for (GLVertex v : vertexList) {
            v.translate(vertexBuffer, x, y, z);
        }
    }

    void setPressed(boolean pressed) {
        this.pressed = pressed;
    }

    public boolean touchHit(Vec2 p) {
        if (p.x > l && p.x < texRight && p.y > b && p.y < texTop) {
            return true;
        }
        return false;
    }

    public boolean handleActionDown(Vec2 p) {
        if (touchHit(p)) {
            setPressed(true);
            for (TextureView child : children) {
                child.handleActionDown(p);
            }
            return true;
        }
        return false;
    }

    public boolean handleActionMove(Vec2 p) {
        if (pressed) {
            for (TextureView child : children) {
                child.handleActionMove(p);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean handleActionUp(Vec2 p) {
        if (pressed) {
            if (listener != null && touchHit(p)) {
                listener.onClick();
            }
            for (TextureView child : children) {
                child.handleActionUp(p);
            }
            setPressed(false);
            return true;
        }
        return false;
    }

}