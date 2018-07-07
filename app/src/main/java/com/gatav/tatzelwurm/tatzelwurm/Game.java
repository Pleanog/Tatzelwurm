package com.gatav.tatzelwurm.tatzelwurm;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.renderscript.Sampler;
import android.widget.ImageView;
import android.widget.TextView;

import com.gatav.tatzelwurm.tatzelwurm.enums.GameState;
import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;
import com.gatav.tatzelwurm.tatzelwurm.handler.CollisionDetectionHandler;
import com.gatav.tatzelwurm.tatzelwurm.handler.PeriodicTimerHandler;
import com.gatav.tatzelwurm.tatzelwurm.objects.Egg;
import com.gatav.tatzelwurm.tatzelwurm.objects.Obstacle;
import com.gatav.tatzelwurm.tatzelwurm.objects.Player;
import com.gatav.tatzelwurm.tatzelwurm.objects.PlayerPart;
import com.gatav.tatzelwurm.tatzelwurm.objects.Touchable;

import java.util.Iterator;
import java.util.LinkedList;

public class Game {
    private TatzelwurmActivity Activity;
    private Egg IntroEgg;
    private Player Tatzelwurm;

    // game parameters
    private GameState State = GameState.INTRO;
    private GravityState Gravity = GravityState.NORMAL;
    private boolean controlLocked = true;
    private boolean invulnerable = false;
    private int difficulty = 1;

    // all touchable objects - list will be updated asynchronous by multiple PeriodicTimerHandlers
    private LinkedList<Touchable> Touchables = new LinkedList<>();
    private PeriodicTimerHandler ObstacleTimeHandler;
    private PeriodicTimerHandler DifficultyTimeHandler;
    private PeriodicTimerHandler CollisionTimeHandler;

    /**
     * starts new game
     */
    public Game(TatzelwurmActivity activity) {
        // use the Activity to get resources and the game view
        this.Activity = activity;
        // references current game to use later in inner class calls
        final Game _this = this;

        // remove all inner views, except the background, if the game restarts
        this.Activity.getGameView().removeViews(2, this.Activity.getGameView().getChildCount()-2);
        // fade in the main view, if the game restarts
        ObjectAnimator FadeOutViewAnim = ObjectAnimator.ofFloat(this.Activity.getGameView(), "Alpha", this.Activity.getGameView().getAlpha(), 1.f);
        FadeOutViewAnim.setDuration(1500);
        FadeOutViewAnim.start();

        // go!
        intro();
    }

    public TatzelwurmActivity getActivity() { return this.Activity; }

    public Egg getIntroEgg() { return this.IntroEgg; }

    public Player getTatzelwurm() { return this.Tatzelwurm;   }

    public LinkedList<Touchable> getTouchables() { return this.Touchables; }

    public GameState getState() { return this.State; }

    public void setState(GameState state) { this.State = state; }

    public GravityState getGravity() { return this.Gravity; }

    public void setGravity(GravityState gravity) { this.Gravity = gravity; }

    public boolean isControlLocked() { return this.controlLocked; }

    public void setControlLocked(boolean controlLock) { this.controlLocked = controlLock; }

    public boolean isInvulnerable() { return this.invulnerable; }

    public void setInvulnerable(boolean invulnerability) { this.invulnerable = invulnerability; }

    /**
     * pre-game screen. The player cracks open an egg to release the tatzelwurm
     */
    public void intro() {
        // start message
        message(false);
        // setup egg
        this.IntroEgg = new Egg(this);
        this.Activity.getGameView().addView(this.IntroEgg.getTopImageView());
        this.Activity.getGameView().addView(this.IntroEgg.getBottomImageView());
    }

    /**
     * start the actual game
     */
    public void start() {
        // references current game to use later in inner class calls
        final Game _this = this;

        randomMessage(true);

        // setup player
        this.Tatzelwurm = new Player(this, 16);
        this.State = GameState.GAME;

        // iterate and add in reverse order for correct z-index of parts
        LinkedList<PlayerPart> PlayerParts = Tatzelwurm.getParts();
        for (int i = PlayerParts.size()-1; i >= 0; i--) {
            this.Activity.getGameView().addView(PlayerParts.get(i).getPartImageView());
        }
        this.IntroEgg.getTopImageView().bringToFront();
        this.IntroEgg.getBottomImageView().bringToFront();

        // Collision Detection, every 42 ms, because (1/24fps)*1000=41.67 ms
        CollisionTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                if (_this.Tatzelwurm.getLifes() > 0) {
                    for (Touchable t : _this.Touchables) {
                        ImageView HeadIV = Tatzelwurm.getHead().getPartImageView();
                        ImageView HitTouchableIV = t.getTouchableImageView();

                        if (CollisionDetectionHandler.isCollisionDetected(HeadIV,
                                                                        (int)HeadIV.getX(),
                                                                        (int)HeadIV.getY(),
                                                                        HitTouchableIV,
                                                                        (int)HitTouchableIV.getX(),
                                                                        (int)HitTouchableIV.getY())) {
                            t.onTouch();
                        }
                    }
                }
            }
        }, 42);

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
        ObstacleTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                // get last obstacle to avoid collision, which would make the game impossible
                ImageView LastObstacleImageView = null;
                for(Iterator it = _this.Touchables.descendingIterator(); it.hasNext();) {
                    Object t = it.next();
                    if (t instanceof Obstacle) {
                        LastObstacleImageView = ((Obstacle) t).getTouchableImageView();
                        break;
                    }
                }

                // calc minimum gap and actual gap to make the game possible to beat
                boolean gapOk = false;
                if (LastObstacleImageView != null) {
                    int headWidth = _this.Tatzelwurm.getLifes() > 0 ?  Tatzelwurm.getHead().getPartImageView().getWidth() + 20 : 20;
                    int minGap = LastObstacleImageView.getWidth() + headWidth;
                    float gap = _this.getActivity().getScreenWidth() - LastObstacleImageView.getX();
                    gapOk = minGap < gap;
                }

                if (LastObstacleImageView == null || gapOk) {
                    // create new obstacle with  running animation. The parameters will be initialised in class.
                    Obstacle NewObstacle = new Obstacle(_this);
                    // add to general touchable list
                    Touchables.add(NewObstacle);
                    // add ImageView to the activities game view
                    _this.Activity.getGameView().addView(NewObstacle.getTouchableImageView());
                }
            }
        }, 2000, 3000);

        // the difficulty will be increased every x ms
        _this.DifficultyTimeHandler = new PeriodicTimerHandler(new Runnable() {
            @Override
            public void run() {
                _this.incDifficulty();
            }
        }, 4000);
    }

    /**
     * will be called, when no tatzelwurmpart is left and restarts the whole game
     */
    public void gameover() {
        // references current game to use later in inner class calls
        final Game _this = this;

        // cancel all Timer Handlers
        this.ObstacleTimeHandler.cancel();
        this.DifficultyTimeHandler.cancel();
        this.CollisionTimeHandler.cancel();

        // fade out the game view. The app has a black background, so it looks like a black fade out
        ObjectAnimator FadeOutViewAnim = ObjectAnimator.ofFloat(this.Activity.getGameView(), "Alpha", 1.0f, 0.f);
        FadeOutViewAnim.setDuration(1500);
        FadeOutViewAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                _this.Activity.restart();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        FadeOutViewAnim.start();
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
           this.ObstacleTimeHandler.schedule(2000 - newDifficulty, 2000);
        }
    }

    public void message(boolean discard) {
        this.message(this.Activity.getStartMessage(), discard);
    }

    public void randomMessage(boolean discard) {
        this.message(this.Activity.getMessage(), discard);
    }

    public void message(String Message, boolean discard) {
        // references current game to use later in inner class calls
        Game _this = this;

        TextView MessageView = this.Activity.getMessageView();
        this.Activity.getMessageView().setText(Message);

        ObjectAnimator FadeInViewAnim = ObjectAnimator.ofFloat(MessageView, "Alpha", 0.0f, 1.f);
        FadeInViewAnim.setDuration(1000);
        if (discard) {
            FadeInViewAnim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {}

                @Override
                public void onAnimationEnd(Animator animation) {
                    discardMessage();
                }

                @Override
                public void onAnimationCancel(Animator animation) {}

                @Override
                public void onAnimationRepeat(Animator animation) {}
            });
        }
        FadeInViewAnim.start();
    }

    private void discardMessage() {
        TextView MessageView = this.Activity.getMessageView();
        ObjectAnimator FadeOutViewAnim = ObjectAnimator.ofFloat(MessageView, "Alpha", MessageView.getAlpha(), 0.f);
        FadeOutViewAnim.setDuration(1000);
        FadeOutViewAnim.setStartDelay(1000);
        FadeOutViewAnim.start();
    }
}