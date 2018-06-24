package com.gatav.tatzelwurm.tatzelwurm;

import com.gatav.tatzelwurm.tatzelwurm.handler.CollisionDetectionHandler;
import com.gatav.tatzelwurm.tatzelwurm.handler.PeriodicTimerHandler;
import com.gatav.tatzelwurm.tatzelwurm.objects.Obstacle;
import com.gatav.tatzelwurm.tatzelwurm.objects.Player;
import com.gatav.tatzelwurm.tatzelwurm.objects.Touchable;

import java.util.LinkedList;

public class Game {
    private TatzelwurmActivity Activity;
    private Player Tatzelwurm;

    // all touchable objects - list will be updated asynchronous by multiple PeriodicTimerHandlers
    private LinkedList<Touchable> Touchables = new LinkedList<>();
    private PeriodicTimerHandler obstacleTimeHandler;
    private PeriodicTimerHandler collisionTimeHandler;

    /**
     * starts new game
     */
    public Game(TatzelwurmActivity activity) {
        // use the Activity to get resources and the game view
        this.Activity = activity;
        // references current game to use later in inner class calls
        final Game _this = this;

        collisionTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                // TODO: After finishing Player and Obstacle-Class implement Collision Detection
//                for (Touchable t : _this.Touchables) {
//                  if (CollisionDetectionHandler.isCollisionDetected(Tatzelwurm, ...)) {
//                      t.onTouch();
//                  }
//                }
            }
        }, 1);

        obstacleTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                // create new obstacle the parameters will be initialised in class
                Obstacle NewObstacle = new Obstacle(_this);
                // add to general touchable list
                Touchables.add(NewObstacle);
                // add ImageView to the activites game view
                _this.Activity.getGameView().addView(NewObstacle.getTouchableImageView());
            }
        }, 1000);
    }

    public TatzelwurmActivity getActivity() {
        return this.Activity;
    }

    public LinkedList<Touchable> getTouchables() {
        return this.Touchables;
    }

    public void hit(Touchable sender) {
        System.out.println("Hit");
    }
}