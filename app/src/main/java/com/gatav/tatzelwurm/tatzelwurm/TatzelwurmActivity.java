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

import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;

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
    public Drawable PlayerPreTail;
    public Drawable PlayerTail;
    public AnimationDrawable PlayerGrowHeadAnim;
    //All the obstacles
    public Drawable ObstacleBig;
    public Drawable ObstacleMedium;
    public Drawable ObstacleSmall;

    public Drawable Archer1;
    public Drawable halleys_comet;
    public Drawable horsemen12;
    public Drawable pointing_guy2;
    public Drawable bird3;
    public Drawable guy_on_pole;
    public Drawable guy_on_pole_hanging;
    public Drawable tower4;
    public Drawable dead_warrior2;



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

        this.PlayerHead = getResources().getDrawable(R.drawable.head_5);
        this.PlayerBody = getResources().getDrawable(R.drawable.body_5);
        this.PlayerPreTail = getResources().getDrawable(R.drawable.pretail);
        this.PlayerTail = getResources().getDrawable(R.drawable.tail_v1);
        this.PlayerGrowHeadAnim = (AnimationDrawable)getResources().getDrawable(R.drawable.growhead);

        // All the different obstacles are defined here, so that they are already loaded into the activity from the start
        this.ObstacleBig = getResources().getDrawable(R.drawable.obstacle_big);
        this.ObstacleMedium = getResources().getDrawable(R.drawable.obstacle_medium);
        this.ObstacleSmall = getResources().getDrawable(R.drawable.obstacle_small);

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
     /**
     * Use first half of screen to decrease gravity, other half to increase gravity
     * no input causes normal gravity
     * @param event MotionEvent
     * @return true
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        int eventAction = event.getAction();
        int eventPosX = (int)event.getX();

        switch (eventAction & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_POINTER_DOWN:
                // overwrite latest pointer position at multitouch
                eventPosX = (int)event.getX(event.getPointerCount()-1);
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
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

        return true;
    }

    public int getScreenWidth()  { return this.screenWidth; }

    public int getScreenHeight() {
        return this.screenHeight;
    }

    public ConstraintLayout getGameView() {
        return this.GameView;
    }
}
