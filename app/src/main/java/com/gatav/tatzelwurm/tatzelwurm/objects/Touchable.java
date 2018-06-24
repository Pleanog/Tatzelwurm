package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

public abstract class Touchable {
    protected Game CurrentGame;
    private ImageView TouchableImageView;
    private ObjectAnimator RighToLeftAnim;

    public Touchable(final Game CurrentGame) {
        this.CurrentGame = CurrentGame;
        this.TouchableImageView = new ImageView(this.CurrentGame.getActivity());

        // references current game to use later in inner class calls
        final Touchable _this = this;

        // setup animation to move from left to right
        float fromX = this.CurrentGame.getActivity().getScreenWidth();
        float toX = -this.TouchableImageView.getDrawable().getIntrinsicWidth();
        RighToLeftAnim = ObjectAnimator.ofFloat(this.TouchableImageView, "X", fromX, toX);
        // TODO: set duration to individual values of child class, depending of current difficulty
        RighToLeftAnim.setDuration(3000);
        // TODO: create delay value randomly, depending of current difficulty
        RighToLeftAnim.setStartDelay(0);

        RighToLeftAnim.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                // remove from List
                _this.CurrentGame.getTouchables().removeFirst();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
    }

    public abstract void onTouch();

    public ImageView getTouchableImageView() {
        return this.TouchableImageView;
    }
}
