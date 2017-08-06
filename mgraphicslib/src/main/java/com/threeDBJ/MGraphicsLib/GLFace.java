package com.threeDBJ.MGraphicsLib;

import com.threeDBJ.MGraphicsLib.math.Vec3;
import com.threeDBJ.MGraphicsLib.texture.Texture;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

public class GLFace {

    private ArrayList<GLVertex> vertexList = new ArrayList<>();
    private GLColor color;
    // These vectors are normalized
    private Vec3 normal;
    public Texture texture;

    public GLFace() {
    }

    // for triangles
    public GLFace(GLVertex v1, GLVertex v2, GLVertex v3) {
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
        // TODO -- to add triangle intersection, calculate normal here
    }

    /* Make this a quadrilateral face. Vertice args must be in clockwise
       order for normal calculation to work. */
    public GLFace(GLVertex v1, GLVertex v2, GLVertex v3, GLVertex v4) {
        addVertex(v1);
        addVertex(v2);
        addVertex(v3);
        addVertex(v4);
        Vec3 vec1 = new Vec3(v1).sub(v2);
        Vec3 vec2 = new Vec3(v3).sub(v2);
        normal = vec1.crs(vec2);
        normal.nor();
    }

    /* TODO -- probably shouldn't be passing in env */
    public void setTexture(Texture tex) {
        this.texture = tex;
    }

    public void addVertex(GLVertex v) {
        vertexList.add(v);
    }

    public GLVertex getVertex(int i) {
        return vertexList.get(i);
    }

    // must be called after all vertices are added
    public void setColor(GLColor c) {
        //setColorAll(c);
        vertexList.get(2).color = c;
    /*
    int last = vertexList.size() - 1;
	if (last < 2) {
	    Log.e("GLFace", "not enough vertices in setColor()");
	} else {
	    GLVertex vertex = vertexList.get(last);
	    // only need to do this if the color has never been set
	    if (color == null) {
		while (vertex.color != null) {
		    vertexList.add(0, vertex);
		    vertexList.remove(last + 1);
		    vertex = vertexList.get(last);
		}
	    }
	    vertex.color = c;
	}
	*/
        color = c;
    }

    public GLColor getColor() {
        return vertexList.get(2).color;
    }

    public void setColorAll(GLColor c) {
        for (GLVertex v : vertexList) {
            v.color = c;
        }
        color = c;
    }

    public int getIndexCount() {
        return (vertexList.size() - 2) * 3;
    }

    public void putIndices(ShortBuffer buffer) {
        buffer.put(vertexList.get(1).index);
        buffer.put(vertexList.get(0).index);
        buffer.put(vertexList.get(2).index);

        if (vertexList.size() > 3) {
            buffer.put(vertexList.get(3).index);
            buffer.put(vertexList.get(1).index);
            buffer.put(vertexList.get(2).index);
        }
    }

    public void putTexture(FloatBuffer buffer) {
        if (texture != null) {
            texture.putCoords(buffer);
        } else {
            for (int i = 0; i < 8; i += 1) {
                buffer.put(0f);
            }
        }
    }

}
