package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class PeriodicTimerHandler {
    public PeriodicTimerHandler(final Runnable func, int period) {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            func.run();
                        } catch (Exception e) {}
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, period);
    }
}
