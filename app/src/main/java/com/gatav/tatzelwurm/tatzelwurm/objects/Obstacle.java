package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;
import com.gatav.tatzelwurm.tatzelwurm.handler.OneShotTimerHandler;

import java.util.LinkedList;
import java.util.Random;

public class Obstacle extends Touchable {

    public Obstacle(Game CurrentGame) {
        super(CurrentGame);

        // setup ImageView
        // Type will be selected as a number between 0..(X-1)
        int obstacleType = 1 + (int)(Math.random() * 3);

        Drawable currentDrawable;
        switch (obstacleType) {
            case 1:
                currentDrawable = this.CurrentGame.getActivity().ObstacleBig;
                break;
            case 2:
                currentDrawable = this.CurrentGame.getActivity().ObstacleMedium;
                break;
            default:
                currentDrawable = this.CurrentGame.getActivity().ObstacleSmall;
                break;
        }

        super.getTouchableImageView().setImageDrawable(currentDrawable);

        /**Sets the Alignment of the currentDrawable to the top#
         *  = 0
        * or the bottom = screenHeight - currentDrawableHeight of the screen
        */
         Random topOrBottom = new Random();
         //TODO Make slight changes in the positions of the obstacles to make the levels even more random
        int obstacleAlignmentPosition = 0;
        if (topOrBottom.nextBoolean()){
            obstacleAlignmentPosition = (this.CurrentGame.getActivity().getScreenHeight()) - (currentDrawable.getIntrinsicHeight());
        }
        
        super.getTouchableImageView().setY(obstacleAlignmentPosition);

        // setup & start animation to move from left to right
        float fromX = this.CurrentGame.getActivity().getScreenWidth();
        float toX = -currentDrawable.getIntrinsicWidth();

        super.move(fromX, toX);
    }


    /**
     *  if the player gets hit by an obstacles, he loses lifes and is invulnerable for two seconds
     *  loosing lifes = getting shorter in physical size
     */
    @Override
    public void onTouch() {
        if (!this.CurrentGame.isInvulnerable()) {
            this.CurrentGame.getTatzelwurm().hit();
            this.CurrentGame.setInvulnerable(true);
            new OneShotTimerHandler(new Runnable() {
                @Override
                public void run() {
                    CurrentGame.setInvulnerable(false);
                }
            }, 2000);
        }
    }
}
