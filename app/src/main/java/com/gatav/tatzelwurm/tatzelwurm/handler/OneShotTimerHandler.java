package com.gatav.tatzelwurm.tatzelwurm.handler;

import java.util.Timer;
import java.util.TimerTask;

public class OneShotTimerHandler extends TimerHandler {
    public OneShotTimerHandler(final Runnable func, int time) {
        super(func, time);
    }

    public void schedule(int time) {
        timer.schedule(this.AsynchronousTask, time);
    }
}
