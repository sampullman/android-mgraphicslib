package com.threeDBJ.MGraphicsLib.texture;

import com.threeDBJ.MGraphicsLib.GLColor;
import com.threeDBJ.MGraphicsLib.GLVertex;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL11;

public class TextureTextView extends TextureView {

    private int MAX_CHAR_COUNT = 20;
    public int fontIndexCount = 0, colorStart, vertStart, indStart, texStart;
    public char[] prevText = {};
    public float textSize = 16, paddingLeft, paddingTop, paddingRight, paddingBottom;
    private TextureFont font;
    private GLColor textColor = new GLColor(0, 0, 0);
    ArrayList<GLVertex> fontVerts = new ArrayList<>();

    public TextureTextView(TextureFont font) {
        this.font = font;
    }

    public synchronized void setText(char[] chars) {
        font.generateText(this, chars, l, b, z + 0.02f, textColor);
        prevText = chars.clone();
    }

    public void setText(String text) {
        char[] chars = text.toCharArray();
        setText(chars);
    }

    public void setPadding(float left, float top, float right, float bottom) {
        paddingLeft = left;
        paddingTop = top;
        paddingRight = right;
        paddingBottom = bottom;
    }

    public void setTextSize(float size) {
        textSize = size;
    }

    public void setTextColor(GLColor c) {
        textColor = c;
    }

    public synchronized void draw(GL11 gl) {
        super.draw(gl);
        if (fontIndexCount > 0) {
            gl.glBindTexture(GL11.GL_TEXTURE_2D, font.id);
            gl.glTexCoordPointer(2, GL11.GL_FLOAT, 0, textureBuffer);
            gl.glVertexPointer(3, GL11.GL_FLOAT, 0, vertexBuffer);
            gl.glColorPointer(4, GL11.GL_FIXED, 0, colorBuffer);
            indexBuffer.position(indStart);
            colorBuffer.position(colorStart);
            vertexBuffer.position(vertStart);
            textureBuffer.position(texStart);
            gl.glDrawElements(GL11.GL_TRIANGLES, fontIndexCount, GL11.GL_UNSIGNED_SHORT, indexBuffer);
        }
    }

    public void translate(float x, float y, float z) {
        for (GLVertex v : fontVerts) {
            v.translate(vertexBuffer, x, y, z);
        }
        super.translate(x, y, z);
    }

    public void generate() {
        int maxVerts = MAX_CHAR_COUNT * 4;
        int maxVertSize = vertexList.size() + maxVerts;
        genBuffers(maxVertSize * 4 * 4, maxVertSize * 4 * 3, (indexCount + maxVerts) * 2, maxVertSize * 4 * 8);
        fillBuffers();
        colorStart = colorBuffer.position();
        vertStart = vertexBuffer.position();
        indStart = indexBuffer.position();
        texStart = textureBuffer.position();
    }

}
