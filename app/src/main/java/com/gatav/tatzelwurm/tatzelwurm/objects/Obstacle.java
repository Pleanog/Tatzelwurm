package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

import java.util.LinkedList;

public class Obstacle extends Touchable {

    public Obstacle(Game CurrentGame) {
        super(CurrentGame);

        // setup ImageView
        int obstacleType = 1 + (int)(Math.random() * ((3 - 1) + 1));

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
        super.getTouchableImageView().setY(0);
    }

    @Override
    public void onTouch() {
        CurrentGame.hit(this);
    }
}
