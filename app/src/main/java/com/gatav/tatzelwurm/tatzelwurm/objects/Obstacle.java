package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;
import com.gatav.tatzelwurm.tatzelwurm.enums.ObstaclePosition;
import com.gatav.tatzelwurm.tatzelwurm.handler.OneShotTimerHandler;

import java.util.LinkedList;
import java.util.Random;

public class Obstacle extends Touchable {

    public Obstacle(Game CurrentGame) {
        super(CurrentGame);

        /* List of all obstacles so far:
        * Archer1; halleys_comet; horsemen12; pointing_guy2; bird3; guy_on_pole; tower4; dead_warrior2;
        */

        // setup ImageView
        // Type will be selected as a number between 0 ..(X-1)
        int obstacleType =(int)(Math.random() * (8+1));

        /* Possible ObstaclePositionAlignments for the Objects
         * top, middele, bottom, topMiddel, middelBottom, topMiddelBottom,
         */

        // Sets standartposition to bottom
        ObstaclePosition obstaclePossiblePositions = ObstaclePosition.bottom;

        Drawable currentDrawable;
        switch (obstacleType) {
            case 0:
                currentDrawable = this.CurrentGame.getActivity().Archer1;
                break;
            case 1:
                currentDrawable = this.CurrentGame.getActivity().halleys_comet;
                obstaclePossiblePositions = ObstaclePosition.topMiddelBottom;
                break;
            case 2:
                currentDrawable = this.CurrentGame.getActivity().horsemen12;
                break;
            case 3:
                currentDrawable = this.CurrentGame.getActivity().pointing_guy2;
                obstaclePossiblePositions = ObstaclePosition.topMiddel;
                break;
            case 4:
                currentDrawable = this.CurrentGame.getActivity().bird3;
                obstaclePossiblePositions = ObstaclePosition.topMiddel;
                break;
            case 5:
                currentDrawable = this.CurrentGame.getActivity().guy_on_pole;
                break;
            case 6:
                currentDrawable = this.CurrentGame.getActivity().guy_on_pole_hanging;
                obstaclePossiblePositions = ObstaclePosition.top;
                break;
            case 7:
                currentDrawable = this.CurrentGame.getActivity().tower4;
                break;
            case 8:
                currentDrawable = this.CurrentGame.getActivity().dead_warrior2;
                obstaclePossiblePositions = ObstaclePosition.topMiddel;
                break;
            default:
                // default is the bird, because it can have every obstaclePossiblePositions
                currentDrawable = this.CurrentGame.getActivity().bird3;
                obstaclePossiblePositions = ObstaclePosition.topMiddelBottom;
                break;
        }

        super.getTouchableImageView().setImageDrawable(currentDrawable);

        /*Sets the Alignment of the currentDrawable to the
         * top = 0,
         * middle = screenHeight/2 - currentDrawableHeight + random ShiftValue,
         * bottom = screenHeight - currentDrawableHeight of the screen
         */

        /* Possible ObstaclePositionAlignments for the Objects
         * top, middele, bottom, topMiddel, middelBottom, topMiddelBottom,
         */
        int oapTop = 0;
        int oapMiddle = ((this.CurrentGame.getActivity().getScreenHeight())/2) - (currentDrawable.getIntrinsicHeight()) + ((int)(((Math.random() * (20))-10)*10));
        int oapBottom = (this.CurrentGame.getActivity().getScreenHeight()) - (currentDrawable.getIntrinsicHeight());


        int obstacleAlignmentPosition = oapBottom;

        Random RandomPosition = new Random();

        switch (obstaclePossiblePositions) {
            case top:
                obstacleAlignmentPosition = oapTop;
                break;
            case middele:
                break;
            case bottom:
                break;
            case topMiddel:
                if (RandomPosition.nextBoolean()){
                    obstacleAlignmentPosition = oapTop;
                } else {
                    obstacleAlignmentPosition = oapMiddle;
                }
                break;
            case middelBottom:
                if (RandomPosition.nextBoolean()){
                    obstacleAlignmentPosition = oapMiddle;
                } else {
                    obstacleAlignmentPosition = oapBottom;
                }
                break;
            case topMiddelBottom:
                if (RandomPosition.nextBoolean()){
                    obstacleAlignmentPosition = oapTop;
                } else {
                    if (RandomPosition.nextBoolean()){
                        obstacleAlignmentPosition = oapMiddle;
                    } else {
                        obstacleAlignmentPosition = oapBottom;
                    }
                }
                break;
        }

        System.out.println("Obstacle-Y-Position  " + obstaclePossiblePositions + "Value" + obstacleAlignmentPosition);
        super.getTouchableImageView().setY(obstacleAlignmentPosition);

        // setup & start animation to move from left to right
        float fromX = this.CurrentGame.getActivity().getScreenWidth();
        float toX = -currentDrawable.getIntrinsicWidth();

        super.move(fromX, toX);
    }


    /*
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
