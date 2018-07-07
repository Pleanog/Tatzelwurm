package com.gatav.tatzelwurm.tatzelwurm.handler;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

public class PeriodicTimerHandler extends TimerHandler {
    public PeriodicTimerHandler(final Runnable func, int time) {
        super(func, time);
    }

    public PeriodicTimerHandler(final Runnable func, int time, int delay) {
        super(func);
        scheduleTimer(time, delay);
    }

    public void schedule(int time, int delay) {
        init();
        scheduleTimer(time,delay);
    }

    protected void scheduleTimer(int time) { this.timer.schedule(this.AsynchronousTask, 0, time); }

    protected void scheduleTimer(int time, int delay) { this.timer.schedule(this.AsynchronousTask, delay, time); }
}
