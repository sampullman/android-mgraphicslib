package com.threeDBJ.MGraphicsLib.texture;

import java.util.ArrayList;

abstract class TextureViewAnimation {

    ArrayList<TextureView> views = new ArrayList<>();
    private int tick, nTicks;
    private boolean isStarted = false, isFinished = false;

    /* TODO - Think about implementing real time duration */
    public TextureViewAnimation(int nTicks) {
        this.nTicks = nTicks;
    }

    public void addView(TextureView view) {
        views.add(view);
    }

    public void startAnimation() {
        tick = 0;
        isStarted = true;
    }

    public void stepAnimation() {
        if (tick >= nTicks) {
            isFinished = true;
            isStarted = false;
        }
        tick += 1;
    }

    public boolean finished() {
        return isFinished;
    }

    public boolean isStarted() { return this.isStarted; }

}