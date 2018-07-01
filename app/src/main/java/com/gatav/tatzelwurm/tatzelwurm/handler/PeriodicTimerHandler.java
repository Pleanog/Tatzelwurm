package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class PeriodicTimerHandler extends TimerHandler {
    public PeriodicTimerHandler(final Runnable func, int time) {
        super(func, time);
    }

    protected void schedule(Timer timer, TimerTask doAsynchronousTask, int time) {
        timer.schedule(doAsynchronousTask, 0, time);
    }
}
