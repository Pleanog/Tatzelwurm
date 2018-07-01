package com.gatav.tatzelwurm.tatzelwurm.handler;

import java.util.Timer;
import java.util.TimerTask;

public class OneShotTimerHandler extends TimerHandler {
    public OneShotTimerHandler(final Runnable func, int time) {
        super(func, time);
    }

    protected void schedule(Timer timer, TimerTask doAsynchronousTask, int time) {
        timer.schedule(doAsynchronousTask, time);
    }
}
