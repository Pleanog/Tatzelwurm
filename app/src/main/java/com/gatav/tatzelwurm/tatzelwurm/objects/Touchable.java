package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

public abstract class Touchable {
    protected Game CurrentGame;
    private ImageView TouchableImageView;
    private ObjectAnimator RighToLeftAnim;

    public Touchable(final Game CurrentGame) {
        this.CurrentGame = CurrentGame;
        // setup basic ImageView, positions will be set in child classes
        this.TouchableImageView = new ImageView(this.CurrentGame.getActivity());
    }

    public void move(float fromX, float toX) {
        // references current game to use later in inner class calls
        final Touchable _this = this;

        this.RighToLeftAnim = ObjectAnimator.ofFloat(this.TouchableImageView, "X", fromX, toX);
        this.RighToLeftAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                // remove from List
                _this.CurrentGame.getTouchables().removeFirst();
                CurrentGame.getActivity().getGameView().removeView(TouchableImageView);
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        // TODO: set duration to individual values of child class, depending of current difficulty
        this.RighToLeftAnim.setDuration(3000);
        // TODO: create delay value randomly, depending of current difficulty
        this.RighToLeftAnim.setStartDelay(0);
        this.RighToLeftAnim.setInterpolator(new LinearInterpolator());

        RighToLeftAnim.start();
    }

    public abstract void onTouch();

    public ImageView getTouchableImageView() {
        return this.TouchableImageView;
    }
}
