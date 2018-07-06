package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class PeriodicTimerHandler extends TimerHandler {
    public PeriodicTimerHandler(final Runnable func, int time) {
        super(func, time);
    }

    public void schedule(int time) {
        this.timer.schedule(this.AsynchronousTask, 0, time);
    }
}
