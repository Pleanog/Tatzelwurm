package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

import java.util.LinkedList;

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
        // TODO: Set possible Y positions
        super.getTouchableImageView().setY(0);

        // setup & start animation to move from left to right
        float fromX = this.CurrentGame.getActivity().getScreenWidth();
        float toX = -currentDrawable.getIntrinsicWidth();
        super.move(fromX, toX);
    }

    @Override
    public void onTouch() {
        CurrentGame.hit(this);
    }
}
