package com.gatav.tatzelwurm.tatzelwurm.handler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class OneShotTimerHandler extends TimerHandler {
    public OneShotTimerHandler(final Runnable func, int time) {
        super(func, time);
    }

    protected void scheduleTimer(int time) { this.timer.schedule(this.AsynchronousTask, time); }
}
