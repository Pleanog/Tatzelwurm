package com.gatav.tatzelwurm.tatzelwurm;

import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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

import java.io.IOException;
import java.util.Random;

public class TatzelwurmActivity extends AppCompatActivity {
    // Game
    private Game CurrentGame;
    private boolean paused = false;

    // Layout
    private ConstraintLayout GameView;
    // Message
    private int messageIndex = 0;

    // Screen information
    private int screenWidth;
    private int screenHeight;
    // screen half will be used for player controls
    private int screenHalf;

    // Drawables
    public Drawable PlayerHead;
    public Drawable PlayerPreTail;
    public Drawable PlayerTail;
    public Drawable PlayerBody;
    public AnimationDrawable PlayerGrowHeadAnim;
    //All the obstacles
    public Drawable Archer1;
    public Drawable halleys_comet;
    public Drawable horsemen12;
    public Drawable pointing_guy2;
    public Drawable bird3;
    public Drawable bird2;
    public Drawable guy_on_pole;
    public Drawable guy_on_pole_hanging;
    public Drawable tower4;
    public Drawable dead_warrior2;

    //Sounds
    public MediaPlayer hit;
    public MediaPlayer soundtrack;
    public MediaPlayer crack1;
    public MediaPlayer crack2;
    public MediaPlayer crack3;


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
        this.PlayerTail = getResources().getDrawable(R.drawable.tail);
        this.PlayerPreTail = getResources().getDrawable(R.drawable.pretail);
        this.PlayerGrowHeadAnim = (AnimationDrawable)getResources().getDrawable(R.drawable.growhead);

        // All the different obstacles are defined here, so that they are already loaded into the activity from the start
        this.Archer1 = getResources().getDrawable(R.drawable.archer1);
        this.halleys_comet = getResources().getDrawable(R.drawable.halleys_comet);
        this.horsemen12 = getResources().getDrawable(R.drawable.horsemen12);
        this.pointing_guy2 = getResources().getDrawable(R.drawable.pointing_guy2);
        this.bird3 = getResources().getDrawable(R.drawable.bird3);
        this.bird2 = getResources().getDrawable(R.drawable.bird2);
        this.guy_on_pole = getResources().getDrawable(R.drawable.guy_on_pole);
        this.guy_on_pole_hanging = getResources().getDrawable(R.drawable.guy_on_pole_hanging);
        this.tower4 = getResources().getDrawable(R.drawable.tower4);
        this.dead_warrior2 = getResources().getDrawable(R.drawable.dead_warrior2);


        // Sounds
        // Hit Sound
         this.hit = MediaPlayer.create(this,R.raw.hit);
         this.hit.setLooping(false);
         this.hit.stop();
         try {
             hit.prepare();
         }catch(IOException e) {
            System.out.println("Konnte die Datei hit nicht abspielen");
             e.printStackTrace();
         } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // Soundtrack
        // https://www.youtube.com/watch?v=_YQpacAuhX8
        // Credits to: Brandon Fiechter
        // TODO: change Music before publishing
        this.soundtrack = MediaPlayer.create(this,R.raw.soundtrack);
        this.soundtrack.setLooping(true);
        this.soundtrack.stop();
        try {
            soundtrack.prepare();
        }catch(IOException e) {
            System.out.println("Konnte die Datei soundtrack nicht abspielen");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        // cracking sounds for the egg
        //crack1, crack2, crack3
        this.crack1 = MediaPlayer.create(this,R.raw.crack1);
        this.crack1.setLooping(false);
        this.crack1.stop();
        //crack2
        this.crack2 = MediaPlayer.create(this,R.raw.crack2);
        this.crack2.setLooping(false);
        this.crack2.stop();
        //crack3
        this.crack3 = MediaPlayer.create(this,R.raw.crack3);
        this.crack3.setLooping(false);
        this.crack3.stop();
        try {
            crack1.prepare();
            crack2.prepare();
            crack3.prepare();
        }catch(IOException e) {
            System.out.println("Konnte die Datei crack1,2 oder 3 nicht abspielen");
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


        // get game view and call post render method
        GameView = findViewById(R.id.gameView);
        Typeface BayeuxType = Typeface.createFromAsset(getAssets(),"fonts/bayeux.ttf");
        Typeface AugustaType = Typeface.createFromAsset(getAssets(),"fonts/augusta.ttf");
        this.getMessageView().setTypeface(BayeuxType);
        this.getScoreView().setTypeface(AugustaType);

        GameView.post(new Runnable() {
            @Override
            public void run() {
                _this.CurrentGame = new Game(_this);
            }
        });
    }

    @Override
    public void onPause() {
        if( soundtrack.isPlaying() ) {
            soundtrack.pause();
            this.paused = true;
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (this.paused == true) {
            soundtrack.start();
            this.paused = false;
        }
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
        return messages[messageIndex++ % messages.length];
        //return messages[new Random().nextInt(messages.length)];
    }

    // lazy loads
    public Animation getFadeOut() { return AnimationUtils.loadAnimation(this, R.anim.fadeout); }

    public Animation getFadeIn() { return AnimationUtils.loadAnimation(this, R.anim.fadein); }

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

    public TextView getScoreView() {
        return (TextView)findViewById(R.id.scoreTextView);
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
