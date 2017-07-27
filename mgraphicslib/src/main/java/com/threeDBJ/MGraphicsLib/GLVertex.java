package com.threeDBJ.MGraphicsLib;

import com.threeDBJ.MGraphicsLib.math.Mat4;
import com.threeDBJ.MGraphicsLib.math.Vec3;

import java.nio.FloatBuffer;

public class GLVertex extends Vec3 {

    final short index; // index in vertex table
    GLColor color;

    public GLVertex() {
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.index = -1;
    }

    public GLVertex(GLVertex v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.index = v.index;
        //this.color = v.color;
    }

    public GLVertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = -1;
    }

    public GLVertex(float x, float y, float z, int index) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.index = (short) index;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof GLVertex) {
            GLVertex v = (GLVertex) other;
            return (x == v.x && y == v.y && z == v.z);
        }
        return false;
    }

    // static public int toFixed(float x) {
    //     return (int)(x * 65536.0f);
    // }

    static public float toFixed(float x) {
        return x;
    }

    public void put(FloatBuffer vertexBuffer, FloatBuffer colorBuffer) {
        vertexBuffer.put(toFixed(x));
        vertexBuffer.put(toFixed(y));
        vertexBuffer.put(toFixed(z));
        if (color == null) {
            colorBuffer.put(0);
            colorBuffer.put(0);
            colorBuffer.put(0);
            colorBuffer.put(0);
        } else {
            colorBuffer.put(color.red);
            colorBuffer.put(color.green);
            colorBuffer.put(color.blue);
            colorBuffer.put(color.alpha);
        }
    }

    public void update(FloatBuffer vertexBuffer, Mat4 transform) {
        // skip to location of vertex in mVertex buffer
        if (transform == null) {
            vertexBuffer.position(index * 3);
            vertexBuffer.put(toFixed(x));
            vertexBuffer.put(toFixed(y));
            vertexBuffer.put(toFixed(z));
        } else {
            int ind = index * 3;
            GLVertex temp = new GLVertex(this);
            //mul(transform);
            //temp.mul(transInv);
            temp.mul(transform);
            //temp.mul(trans);
            vertexBuffer.put(ind, toFixed(temp.x));
            vertexBuffer.put(ind + 1, toFixed(temp.y));
            vertexBuffer.put(ind + 2, toFixed(temp.z));
        }

    }

    public void translate(FloatBuffer vertexBuffer, float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        int ind = index * 3;
        vertexBuffer.put(ind, toFixed(this.x));
        vertexBuffer.put(ind + 1, toFixed(this.y));
        vertexBuffer.put(ind + 2, toFixed(this.z));
    }

    public String toString() {
        return x + " " + y + " " + z;
    }
}
