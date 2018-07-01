package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public abstract class TimerHandler {
    public TimerHandler(final Runnable func, int time) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            func.run();
                        } catch (Exception e) {
                            System.out.println("run method failed: " + e.getMessage());
                        }
                    }
                });
            }
        };
        schedule(timer, doAsynchronousTask, time);
    }

    protected abstract void schedule(Timer timer, TimerTask doAsynchronousTask, int time);
}
