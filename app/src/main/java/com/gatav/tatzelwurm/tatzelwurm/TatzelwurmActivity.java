package com.gatav.tatzelwurm.tatzelwurm;

import android.graphics.Point;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.gatav.tatzelwurm.tatzelwurm.enums.GameState;
import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;

import java.util.Random;

public class TatzelwurmActivity extends AppCompatActivity {
    // Game
    private Game CurrentGame;

    // Layout
    private ConstraintLayout GameView;

    // Screen information
    private int screenWidth;
    private int screenHeight;
    // screen half will be used for player controls
    private int screenHalf;

    // TODO: final public for temporary convenient
    // Drawables
    public Drawable PlayerHead;
    public Drawable PlayerBody;
    public AnimationDrawable PlayerGrowHeadAnim;
    public Drawable ObstacleBig;
    public Drawable ObstacleMedium;
    public Drawable ObstacleSmall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tazelwurm);

        // references current game to use later in inner class calls
        final TatzelwurmActivity _this = this;

        // get screen width & height
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;
        this.screenHalf = this.screenWidth/2;

        this.PlayerHead = getResources().getDrawable(R.drawable.head);
        this.PlayerBody = getResources().getDrawable(R.drawable.body);
        this.PlayerGrowHeadAnim = (AnimationDrawable)getResources().getDrawable(R.drawable.growhead);

        // All the different obstacles are defined here, so that they are already loaded into the activity from the start
        this.Archer1 = getResources().getDrawable(R.drawable.archer1);
        this.halleys_comet = getResources().getDrawable(R.drawable.halleys_comet);
        this.horsemen12 = getResources().getDrawable(R.drawable.horsemen12);
        this.pointing_guy2 = getResources().getDrawable(R.drawable.pointing_guy2);
        this.bird3 = getResources().getDrawable(R.drawable.bird3);
        this.guy_on_pole = getResources().getDrawable(R.drawable.guy_on_pole);
        this.guy_on_pole_hanging = getResources().getDrawable(R.drawable.guy_on_pole_hanging);
        this.tower4 = getResources().getDrawable(R.drawable.tower4);
        this.dead_warrior2 = getResources().getDrawable(R.drawable.dead_warrior2);


        // get game view and call post render method
        GameView = findViewById(R.id.gameView);

        GameView.post(new Runnable() {
            @Override
            public void run() {
                _this.CurrentGame = new Game(_this);
            }
        });
    }


    /**
     * Get start message for game start
     * @return
     */
    public String getStartMessage() {
        return getResources().getString(R.string.start_message);
    }

    /**
     * Get random game message
     * @return Message
     */
    public String getMessage() {
        String[] messages = getResources().getStringArray(R.array.messages);
        return messages[new Random().nextInt(messages.length)];
    }

    // lazy loads
    public Animation getFadeOutInOut() { return AnimationUtils.loadAnimation(this, R.anim.fadeoutinout); }

    public Drawable getEggTop() { return getResources().getDrawable(R.drawable.eggtop); }

    public Drawable getEggBottom() { return getResources().getDrawable(R.drawable.eggbottom); }

    public int getScreenWidth()  { return this.screenWidth; }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public ConstraintLayout getGameView() {
        return this.GameView;
    }

    public TextView getMessageView() {
        return (TextView)findViewById(R.id.messageTextView);
    }

    /**
     * Use first half of screen to decrease gravity, other half to increase gravity
     * no input causes normal gravity
     * @param event MotionEvent
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        switch (this.CurrentGame.getState()) {
            case INTRO:
                introControl(event, event.getAction());
                break;
            case GAME:
                gravityControl(event, event.getAction());
                break;
            case GAMEOVER:
                break;
        }

        return true;
    }

    /**
     * Controls for the Intro
     * @param event MotionEvent
     * @param eventAction eventAction
     */
    public void introControl(MotionEvent event, int eventAction) {
        switch (eventAction & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                this.CurrentGame.getIntroEgg().crack();
                break;
        }
    }

    /**
     * Control during the game for gravity
     * @param event MotionEvent
     * @param eventAction eventAction
     */
    public void gravityControl(MotionEvent event, int eventAction) {
        int eventPosX = (int)event.getX();

        switch (eventAction & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if (eventPosX < this.screenHalf) {
                    this.CurrentGame.setGravity(GravityState.DECREASING);
                } else {
                    this.CurrentGame.setGravity(GravityState.INCREASING);
                }
                this.CurrentGame.update();
                break;
            case MotionEvent.ACTION_UP:
                this.CurrentGame.setGravity(GravityState.NORMAL);
                this.CurrentGame.update();
                break;
        }
    }

    /**
     * Restarts the whole game. All Views and TimerHandlers will be reset
     */
    public void restart() {
        this.CurrentGame = new Game(this);
    }
}
