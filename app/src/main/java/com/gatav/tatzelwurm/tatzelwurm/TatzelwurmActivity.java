package com.gatav.tatzelwurm.tatzelwurm;

import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class TatzelwurmActivity extends AppCompatActivity {
    // Game
    private Game CurrentGame;

    // Layout
    private ConstraintLayout GameView;

    // Screen Informations
    private int screenWidth;
    private int screenHeight;

    // TODO: final public for temporary convenient
    // Drawables
    public Drawable PlayerHead;
    public Drawable PlayerBody;
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

        this.PlayerHead = getResources().getDrawable(R.drawable.head);
        this.PlayerBody = getResources().getDrawable(R.drawable.body);
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

    public int getScreenWidth() {
        return this.screenWidth;
    }

    public int getScreenHeight() {
        return this.screenHeight;
    }


    public ConstraintLayout getGameView() {
        return this.GameView;
    }
}
