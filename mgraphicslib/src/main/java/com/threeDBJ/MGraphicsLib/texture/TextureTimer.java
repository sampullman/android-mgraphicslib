package com.threeDBJ.MGraphicsLib.texture;

import android.os.Handler;

import static com.threeDBJ.MGraphicsLib.texture.TextureTimer.TimerState.*;

public class TextureTimer extends TextureTextView {

    enum TimerState {
        OFF, STARTED, PAUSED
    }

    private int time = 0;
    private TimerState state = OFF;
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

    public boolean isPaused() {
        return state == PAUSED;
    }

    public boolean isStarted() {
        return state == STARTED;
    }

    public boolean isOff() {
        return state == OFF;
    }

    public void pause(boolean pause) {
        if (pause && !isPaused()) {
            state = PAUSED;
            stop();
        } else if (!pause && isPaused()) {
            state = STARTED;
            start();
        }
    }

    public void start() {
        if (!isStarted()) {
            state = STARTED;
            setText(formatTime());
            handler.postDelayed(timerEvent, 1000);
        }
    }

    public void stop() {
        if (isStarted()) {
            state = OFF;
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
            if (isStarted()) {
                handler.postDelayed(timerEvent, 1000);
            }
        }
    };

}