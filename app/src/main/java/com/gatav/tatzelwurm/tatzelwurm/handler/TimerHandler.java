package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerHandler {
    protected Timer timer;
    protected TimerTask AsynchronousTask;

    public TimerHandler(final Runnable func, int time) {
        final Handler handler = new Handler();
        this.timer = new Timer();
        this.AsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            func.run();
                        } catch (Exception e) {
                            System.err.println("run method failed: " + e.getMessage());
                        }
                    }
                });
            }
        };
        schedule(time);
    }

    public abstract void schedule(int time);
}
