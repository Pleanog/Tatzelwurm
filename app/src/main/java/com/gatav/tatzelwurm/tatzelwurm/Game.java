package com.gatav.tatzelwurm.tatzelwurm;

import android.animation.ObjectAnimator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;
import com.gatav.tatzelwurm.tatzelwurm.handler.CollisionDetectionHandler;
import com.gatav.tatzelwurm.tatzelwurm.handler.OneShotTimerHandler;
import com.gatav.tatzelwurm.tatzelwurm.handler.PeriodicTimerHandler;
import com.gatav.tatzelwurm.tatzelwurm.objects.Obstacle;
import com.gatav.tatzelwurm.tatzelwurm.objects.Player;
import com.gatav.tatzelwurm.tatzelwurm.objects.PlayerPart;
import com.gatav.tatzelwurm.tatzelwurm.objects.Touchable;

import java.util.Iterator;
import java.util.LinkedList;

public class Game {
    private TatzelwurmActivity Activity;
    private Player Tatzelwurm;

    // game parameters
    private GravityState Gravity = GravityState.NORMAL;
    private boolean controlLocked = true;
    private boolean invulnerable = false;
    private int difficulty = 1;

    // all touchable objects - list will be updated asynchronous by multiple PeriodicTimerHandlers
    private LinkedList<Touchable> Touchables = new LinkedList<>();
    private PeriodicTimerHandler obstacleTimeHandler;
    private PeriodicTimerHandler difficultyTimeHandler;
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

    public boolean isControlLocked() { return this.controlLocked; }

    public void setControlLocked(boolean controlLock) { this.controlLocked = controlLock; }

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
     * here begins the actual game for the player with obstacles and such. this method will be called after start() finished with its animations.
     */
    public void postStart() {
        // references current game to use later in inner class calls
        final Game _this = this;

        // unlocks the control lock
        this.controlLocked = false;

        // now the obstacles will be created
        obstacleTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                // get last obstacle to avoid collision, which would make the game impossible
                ImageView LastObstacleIV = null;
                for(Iterator it = _this.Touchables.descendingIterator(); it.hasNext();) {
                    Object t = it.next();
                    if (t instanceof Obstacle) {
                        LastObstacleIV = ((Obstacle) t).getTouchableImageView();
                        break;
                    }
                }

                // calc minimum gap and actual gap to make the game possible to beat
                boolean gapOk = false;
                if (LastObstacleIV != null) {
                    int minGap = LastObstacleIV.getWidth() + Tatzelwurm.getHead().getPartImageView().getWidth() + 50;
                    float gap = _this.getActivity().getScreenWidth() - LastObstacleIV.getX();
                    gapOk = minGap < gap;
                }

                if (LastObstacleIV == null || gapOk) {
                    // create new obstacle with  running animation. The parameters will be initialised in class.
                    Obstacle NewObstacle = new Obstacle(_this);
                    // add to general touchable list
                    Touchables.add(NewObstacle);
                    // add ImageView to the activities game view
                    _this.Activity.getGameView().addView(NewObstacle.getTouchableImageView());
                }
            }
        }, 2000);

        // the difficulty will be increased every x ms
        _this.difficultyTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                _this.incDifficulty();
            }
        }, 4000);
    }

    /**
     * will be called, every time everything will be updated after variuous and current the game states
     */
    public void update() {
        if (!this.controlLocked) {
            this.Tatzelwurm.updateGravity();
        }
    }

    /**
     * increases the difficulty and shortens the periodic time for obstacles
     */
    public void incDifficulty() {
        this.difficulty++;
        int newDifficulty = difficulty*100;

        if (newDifficulty < 1200) {
           this.obstacleTimeHandler.schedule(2000 - newDifficulty);
        }
    }
}