package com.threeDBJ.MGraphicsLib;

import com.threeDBJ.MGraphicsLib.math.Mat4;
import com.threeDBJ.MGraphicsLib.math.Quaternion;
import com.threeDBJ.MGraphicsLib.math.Vec3;
import com.threeDBJ.MGraphicsLib.texture.Texture;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;

public class GLShape {

    private Mat4 transform = new Mat4();
    private Quaternion rot = new Quaternion();
    private Mat4 animateTransform;
    private ArrayList<GLFace> faceList = new ArrayList<>();
    private ArrayList<GLVertex> vertexList = new ArrayList<>();
    // TODO -- make more efficient?
    public ArrayList<Integer> indexList = new ArrayList<>();
    private GLEnvironment env;

    public GLShape(GLEnvironment env) {
        this.env = env;
    }

    public void addFace(GLFace face) {
        faceList.add(face);
    }

    public GLFace getFace(int face) {
        return faceList.get(face);
    }

    public ArrayList<GLFace> getFaceList() { return faceList; }

    public Quaternion getRotation() { return rot; }

    public GLEnvironment getEnv() { return env; }

    public void setTexture(Texture t) {
        setFaceTexture(0, t);
    }

    public void setFaceTexture(int face, Texture t) {
        faceList.get(face).setTexture(t);
    }

    public void setFaceColor(int face, GLColor color) {
        //for(GLFace f : faceList)
        //    f.setColor(color);
        faceList.get(face).setColor(color);
    }

    public GLColor getFaceColor(int face) {
        return faceList.get(face).getColor();
    }

    public void setFaceColorAll(int face, GLColor color) {
        faceList.get(face).setColorAll(color);
    }

    public void putIndices(ShortBuffer buffer) {
        Iterator<GLFace> iter = faceList.iterator();
        while (iter.hasNext()) {
            GLFace face = iter.next();
            face.putIndices(buffer);
        }
    }

    public void putTextures(FloatBuffer buffer) {
        for (GLFace face : faceList) {
            face.putTexture(buffer);
        }
    }

    public int getIndexCount() {
        int count = 0;
        Iterator<GLFace> iter = faceList.iterator();
        while (iter.hasNext()) {
            GLFace face = iter.next();
            count += face.getIndexCount();
        }
        return count;
    }

    public GLVertex addVertex(float x, float y, float z) {

        // // look for an existing GLVertex first
        // Iterator<GLVertex> iter = vertexList.iterator();
        // while (iter.hasNext()) {
        //     GLVertex vertex = iter.next();
        //     if (vertex.x == x && vertex.y == y && vertex.z == z) {
        // 	return vertex;
        //     }
        // }

        // doesn't exist, so create new vertex
        GLVertex vertex = env.addVertex(x, y, z);
        vertexList.add(vertex);
        return vertex;
    }

    public GLVertex addVertex(GLVertex v) {
        return addVertex(v.x, v.y, v.z);
    }

    public void animateTransform(Quaternion transform) {
        //animateTransform = transform;

        if (rot != null) {
            rot.mulLeft(transform);
            this.transform.set(rot);
        }
        Iterator<GLVertex> iter = vertexList.iterator();
        while (iter.hasNext()) {
            GLVertex vertex = iter.next();
            env.transformVertex(vertex, this.transform);
        }
    }

    public void startAnimation() {
    }

    public void endAnimation() {
        if (transform == null) {
            transform = new Mat4(animateTransform);
        } else {
            transform = transform.mul(animateTransform);
        }
    }

    @Override
    public int hashCode() {
        if (vertexList == null) return 0;
        if (vertexList.size() == 0) return 1;
        int ret = 0;
        for (Vec3 v : vertexList) {
            ret += ((int) v.x) * 7;
        }
        return ret;
    }

    @Override
    public boolean equals(Object o) {
        GLShape s = (GLShape) o;
        if (vertexList == s.vertexList) return true;
        int len = vertexList.size();
        if (len != s.vertexList.size()) return false;
        for (int i = 0; i < len; i += 1) {
            Vec3 v1 = vertexList.get(i);
            Vec3 v2 = s.vertexList.get(i);
            if (v1.x != v2.x || v1.y != v2.y || v1.z != v2.z)
                return false;
        }
        return true;
    }

}
