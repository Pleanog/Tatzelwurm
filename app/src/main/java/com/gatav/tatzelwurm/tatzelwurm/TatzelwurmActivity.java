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
        this.ObstacleBig = getResources().getDrawable(R.drawable.obstacle_big);
        this.ObstacleMedium = getResources().getDrawable(R.drawable.obstacle_medium);
        this.ObstacleSmall = getResources().getDrawable(R.drawable.obstacle_small);

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
