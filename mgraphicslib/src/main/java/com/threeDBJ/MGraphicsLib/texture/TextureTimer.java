package com.threeDBJ.MGraphicsLib.texture;

import android.os.Handler;

public class TextureTimer extends TextureTextView {

    private int time = 0;
    private boolean started = false, paused = false;
    private Handler handler = new Handler();
    private char[] timeChars = {'0', '0', ':', '0', '0', ':', '0', '0'};

    public TextureTimer(TextureFont font) {
        super(font);
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void pause(boolean pause) {
        if (pause && !paused) {
            paused = true;
            stop();
        } else if (!pause && paused) {
            paused = false;
            start();
        }
    }

    public void start() {
        if (!started) {
            started = true;
            setText(formatTime());
            handler.postDelayed(timerEvent, 1000);
        }
    }

    public void stop() {
        if (started) {
            started = false;
            handler.removeCallbacks(timerEvent);
        }
    }

    public void reset() {
        time = 0;
        setText(formatTime());
    }

    public String getTimeString() {
        return String.valueOf(timeChars);
    }

    private char[] formatTime() {
        int hrs = time / 3600;
        int min = (time % 3600) / 60;
        int sec = time % 60;
        timeChars[0] = (char) (hrs / 10 + 48);
        timeChars[1] = (char) (hrs % 10 + 48);
        timeChars[3] = (char) (min / 10 + 48);
        timeChars[4] = (char) (min % 10 + 48);
        timeChars[6] = (char) (sec / 10 + 48);
        timeChars[7] = (char) (sec % 10 + 48);
        return timeChars;
    }

    private Runnable timerEvent = new Runnable() {
        public void run() {
            time += 1;
            setText(formatTime());
            if (started) {
                handler.postDelayed(timerEvent, 1000);
            }
        }
    };

}