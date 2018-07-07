package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerHandler {
    protected Timer timer;
    protected  Runnable func;
    protected TimerTask AsynchronousTask;

    public TimerHandler(final Runnable func) {
        this.func = func;
        init();
    }

    public TimerHandler(final Runnable func, int time) {
        this.func = func;
        init();
        schedule(time);
    }

    public void init() {
        if (this.timer != null) {
            this.timer.cancel();
        }
        if (this.AsynchronousTask != null) {
            this.AsynchronousTask.cancel();
        }

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
    }

    public void schedule(int time) {
        init();
        scheduleTimer(time);
    }

    protected abstract void scheduleTimer(int time);
}
