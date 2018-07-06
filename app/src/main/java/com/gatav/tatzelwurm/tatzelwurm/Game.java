package com.gatav.tatzelwurm.tatzelwurm;

import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;
import com.gatav.tatzelwurm.tatzelwurm.handler.CollisionDetectionHandler;
import com.gatav.tatzelwurm.tatzelwurm.handler.PeriodicTimerHandler;
import com.gatav.tatzelwurm.tatzelwurm.objects.Obstacle;
import com.gatav.tatzelwurm.tatzelwurm.objects.Player;
import com.gatav.tatzelwurm.tatzelwurm.objects.PlayerPart;
import com.gatav.tatzelwurm.tatzelwurm.objects.Touchable;

import java.util.LinkedList;

public class Game {
    private TatzelwurmActivity Activity;
    private Player Tatzelwurm;

    // game parameters
    private GravityState Gravity = GravityState.NORMAL;
    private boolean invulnerable = false;
    private int difficulty = 1; // for "movementspeed" = ObstacleDelay , etc

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

        // Collision Detection, every 17 ms, because (1/60fps)*1000=16.67 ms
        collisionTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                if (_this.Tatzelwurm.getLifes() > 0) {
                    // TODO: After finishing Player and Obstacle-Class: implement Collision Detection
                    for (Touchable t : _this.Touchables) {
                        ImageView HeadIV = Tatzelwurm.getHead().getPartImageView();
                        ImageView HitTouchableIV = t.getTouchableImageView();

                        if (CollisionDetectionHandler.isCollisionDetected(HeadIV, (int)HeadIV.getX(), (int)HeadIV.getY(), HitTouchableIV, (int)HitTouchableIV.getX(), (int)HitTouchableIV.getY())) {
                            t.onTouch();
                        }
                    }
                }
            }
        }, 17);

        // TODO: make new method for post start animation game startand move obstacleTimeHandler to that method
        obstacleTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                // create new obstacle the parameters will be initialised in class
                Obstacle NewObstacle = new Obstacle(_this);
                // add to general touchable list
                Touchables.add(NewObstacle);
                // add ImageView to the activities game view
                _this.Activity.getGameView().addView(NewObstacle.getTouchableImageView());
            }
        }, 2000);

        // go!
        start();
    }

    public TatzelwurmActivity getActivity() {
        return this.Activity;
    }

    public Player getTatzelwurm() { return this.Tatzelwurm; }

    public LinkedList<Touchable> getTouchables() {
        return this.Touchables;
    }

    public GravityState getGravity() { return this.Gravity; }

    public void setGravity(GravityState gravity) { this.Gravity = gravity; }

    public boolean isInvulnerable() { return this.invulnerable; }

    public void setInvulnerable(boolean invulnerability) { this.invulnerable = invulnerability; }

    /**
     * start the actual game
     */
    public void start() {
        // setup player
        this.Tatzelwurm = new Player(this, 16);

        // iterate and add in reverse order for correct z-index of parts
        LinkedList<PlayerPart> PlayerParts = Tatzelwurm.getParts();
        for (int i = PlayerParts.size()-1; i >= 0; i--) {
            this.Activity.getGameView().addView(PlayerParts.get(i).getPartImageView());
        }

        // go!
        this.Tatzelwurm.start();
    }

    /**
     * Will be called, every time everything will be updated after variuous and current the game states
     */
    public void update() {
        this.Tatzelwurm.updateGravity();
    }
}
