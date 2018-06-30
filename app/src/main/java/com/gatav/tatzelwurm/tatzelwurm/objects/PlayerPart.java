package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

public class PlayerPart {
    private Game CurrentGame;
    // view
    private ImageView PartImageView;
    // animation
    private ObjectAnimator PartAnimation;
    // position boundaries, in what range is the player able to move vertically
    private int minY = 0;
    private int maxY;

    /**
     * One Part of the Players Body. It can either be the head (0), body (1) or tail (> 1 or < 0)
     * @param CurrentGame Current Game Context
     * @param type Sets the drawable. 0 for head, 1 for body > 1 or < 0 for tail
     */
    public PlayerPart(Game CurrentGame, int type) {
        this.CurrentGame = CurrentGame;

        this.PartImageView = new ImageView(CurrentGame.getActivity());
        // set drawable
        // head
        if (type == 0) {
            this.PartImageView.setImageDrawable(CurrentGame.getActivity().PlayerHead);
        }
        // body
        else if (type == 1) {
            this.PartImageView.setImageDrawable(CurrentGame.getActivity().PlayerBody);
        }
        // tail
        else {
            this.PartImageView.setImageDrawable(CurrentGame.getActivity().PlayerBody);
        }

        // set start positions
        // the player starts within its egg. After the actual game starts, the tatzelwurm arrange itself to the game position
        int startPosX = CurrentGame.getActivity().getScreenWidth()/2 - this.PartImageView.getDrawable().getIntrinsicWidth()/2;
        // the player starts on the bottom of the screen
        int startPosY = CurrentGame.getActivity().getScreenHeight() - this.PartImageView.getDrawable().getIntrinsicHeight();
        this.PartImageView.setX(startPosX);
        this.PartImageView.setY(startPosY);

        // set position boundaries
        this.maxY = startPosY;
    }

    public ImageView getPartImageView() {
        return PartImageView;
    }

    /**
     * Arrange the position of a single player body part. This method will be used e.g. for the game start animation.
     * @param positionX go to x position
     * @param delay sets a delay , which will be added to the default duration
     */
    public void start(float positionX, int delay) {
        // the tatzelwurm starts with a jump on start
        ObjectAnimator StartAnimJump = ObjectAnimator.ofFloat(this.PartImageView, "Y", this.PartImageView.getY(), this.PartImageView.getY()-150);
        StartAnimJump.setDuration(500+delay);
        StartAnimJump.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                // TODO: Lock touch control
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator StartAnimJumpFall = ObjectAnimator.ofFloat(PartImageView, "Y", PartImageView.getY(), maxY);
                StartAnimJumpFall.setDuration(1500);
                StartAnimJumpFall.setInterpolator(new BounceInterpolator());
                StartAnimJumpFall.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        // TODO: Unlock touch control
                        // TODO: make new method for post start animation game start and and start this method there
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });

                StartAnimJumpFall.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        // the tatzelwurm will arrange on screen on start
        ObjectAnimator StartAnimArrangeX = ObjectAnimator.ofFloat(this.PartImageView, "X", this.PartImageView.getX(), positionX);
        StartAnimArrangeX.setDuration(400 + delay);

        // go!
        StartAnimArrangeX.start();
        StartAnimJump.start();
    }
}
