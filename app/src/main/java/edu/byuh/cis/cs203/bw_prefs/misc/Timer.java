package edu.byuh.cis.cs203.bw_prefs.misc;

import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.List;

public class Timer extends Handler {

    List<TickListener> observers;
    private boolean paused;

    public Timer() {
        observers = new ArrayList<>();
        resume();
    }

    public void subscribe(TickListener t) {
        observers.add(t);
    }

    public void unsubscribe(TickListener t) {
        observers.remove(t);
    }

    private void notifyListeners() {
        observers.forEach(t -> t.tick());
    }

    @Override
    public void handleMessage(Message msg) {
        notifyListeners();
        if (!paused) {
            sendMessageDelayed(obtainMessage(), 100);
        }
    }

    /**
     * Temporarily stop the timer
     */
    public void pause() {
        paused = true;
    }

    /**
     * Restart the timer after it's been paused
     */
    public void resume() {
        paused = false;
        sendMessageDelayed(obtainMessage(), 0);
    }

    /**
     * stop the timer
     */
    public void stop() {
        pause();
        observers.clear();
    }

}

