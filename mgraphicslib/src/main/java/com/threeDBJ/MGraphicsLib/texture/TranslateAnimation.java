package com.threeDBJ.MGraphicsLib.texture;

public class TranslateAnimation extends TextureViewAnimation {

    private float xStep, yStep, zStep;

    public TranslateAnimation(int nSteps, float x, float y, float z) {
        super(nSteps);
        xStep = x / (float) nSteps;
        yStep = y / (float) nSteps;
        zStep = z / (float) nSteps;
    }

    public synchronized void stepAnimation() {
        super.stepAnimation();
        for (TextureView view : views) {
            view.translate(xStep, yStep, zStep);
        }
    }

}