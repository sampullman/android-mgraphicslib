package com.threeDBJ.MGraphicsLib;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.util.Log;

import com.threeDBJ.MGraphicsLib.math.Mat4;
import com.threeDBJ.MGraphicsLib.texture.Texture;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.Iterator;

import javax.microedition.khronos.opengles.GL11;

public abstract class GLEnvironment {

    public ArrayList<GLShape> mShapeList = new ArrayList<>();
    public ArrayList<GLVertex> vertexList = new ArrayList<>();
    public Texture texture;

    public int indexCount = 0;

    public FloatBuffer vertexBuffer, colorBuffer, textureBuffer;
    public ShortBuffer indexBuffer;

    private int w, h;
    public float adjustWidth, adjustHeight, ratio;

    private boolean texturesEnabled = false;

    public void clear() {
        mShapeList.clear();
        vertexList.clear();
        indexCount = 0;
    }

    public void addShape(GLShape shape) {
        mShapeList.add(shape);
        indexCount += shape.getIndexCount();
    }

    public GLVertex addVertex(float x, float y, float z) {
        GLVertex vertex = new GLVertex(x, y, z, vertexList.size());
        vertexList.add(vertex);
        return vertex;
    }

    public void transformVertex(GLVertex vertex, Mat4 transform) {
        vertex.update(vertexBuffer, transform);
    }

    private ByteBuffer genBuffer(int size) {
        ByteBuffer bb = ByteBuffer.allocateDirect(size);
        bb.order(ByteOrder.nativeOrder());
        return bb;
    }

    public void genBuffers(int colorSize, int vertSize, int indSize, int texSize) {

        colorBuffer = genBuffer(colorSize).asFloatBuffer();
        vertexBuffer = genBuffer(vertSize).asFloatBuffer();
        indexBuffer = genBuffer(indSize).asShortBuffer();
        textureBuffer = genBuffer(texSize).asFloatBuffer();
    }

    public void fillBuffers() {
        for(GLVertex vertex : vertexList) {
            vertex.put(vertexBuffer, colorBuffer);
        }

        for(GLShape shape : mShapeList) {
            shape.putIndices(indexBuffer);
            shape.putTextures(textureBuffer);
        }
    }

    public void generate() {
        genBuffers(vertexList.size() * 4 * 4, vertexList.size() * 4 * 3, indexCount * 2, vertexList.size() * 4 * 8);
        fillBuffers();
    }

    public void setDimensions(int w, int h) {
        this.w = w;
        this.h = h;
        this.adjustWidth = 1f / w;
        this.adjustHeight = 1f / h;
        this.ratio = (float) w / (float) h;
    }

    public void draw(GL11 gl) {
        if (texturesEnabled) {
            gl.glEnable(GL11.GL_TEXTURE_2D);
        } else {
            gl.glDisable(GL11.GL_TEXTURE_2D);
        }
    }

    public void setTexture(GL11 gl, Context c, int res) {
        texture = new Texture(res);
        loadTexture(gl, c, texture);
        enableTextures();
    }

    public void enableTextures() {
        texturesEnabled = true;
    }

    public void disableTextures() {
        texturesEnabled = false;
    }

    // Get a new texture id:
    public static int newTextureId(GL11 gl) {
        int[] temp = new int[1];
        gl.glGenTextures(1, temp, 0);
        return temp[0];
    }

    // Will load a texture out of a drawable resource file, and return an OpenGL texture ID:
    public static void loadTexture(GL11 gl, Context context, Texture t) {
        //Get the texture from the Android resource directory
        InputStream is = context.getResources().openRawResource(t.resource);
        Bitmap bitmap;
        try {
            //BitmapFactory is an Android graphics utility for images
            bitmap = BitmapFactory.decodeStream(is);

        } finally {
            //Always clear and close
            try {
                is.close();
            } catch (IOException e) {
            }
        }

        //Generate one texture pointer...
        t.id = newTextureId(gl);
        int error = gl.glGetError();
        if (error != GL11.GL_NO_ERROR) {
            Log.e("Cube", "GLError: " + error + " (" + GLU.gluErrorString(error) + ")");
        }
        //...and bind it to our array
        gl.glBindTexture(GL11.GL_TEXTURE_2D, t.id);

        //Create Nearest Filtered Texture
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        //Different possible texture parameters, e.g. GL11.GL_CLAMP_TO_EDGE
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
        //gl.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
        //gl.glTexEnvf(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        //Use the Android GLUtils to specify a two-dimensional texture image from our bitmap
        GLUtils.texImage2D(GL11.GL_TEXTURE_2D, 0, bitmap, 0);

        //Clean up
        bitmap.recycle();
        t.loaded = true;
    }

}