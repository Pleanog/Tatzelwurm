package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;
import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;

public class PlayerPart {
    private Game CurrentGame;
    // view
    private ImageView PartImageView;
    // continuous animations
    private ObjectAnimator GravityAnim;
    // position boundaries, in what range is the player able to move vertically
    private int minY = 0;
    private int maxY;

    /**
     * One Part of the players body. It can either be the head (0), body (1) or tail (> 1 or < 0)
     * @param CurrentGame Current Game Context
     * @param type Sets the drawable. 0 for head, 1 for body > 1 or < 0 for tail
     */
    public PlayerPart(Game CurrentGame, int type) {
        this.CurrentGame = CurrentGame;

        this.PartImageView = new ImageView(CurrentGame.getActivity());
        // set drawable
        // head
        // TODO: create and change drawables
        if (type == 0) {
            this.PartImageView.setImageDrawable(CurrentGame.getActivity().PlayerHead);
        }
        // body
        else if (type == 1) {
            this.PartImageView.setImageDrawable(CurrentGame.getActivity().PlayerBody);
        }
        // arms
        else if (type == 2) {
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
    public void start(float positionX, final int delay) {
        // the tatzelwurm starts with a jump on start
        ObjectAnimator StartAnimJump = ObjectAnimator.ofFloat(this.PartImageView, "Y", this.PartImageView.getY(), this.PartImageView.getY()-150);
        // TODO: review if the numbers will be set here or anywhere else
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
        // TODO: review if the numbers will be set here or anywhere else
        StartAnimArrangeX.setDuration(250 + delay);

        // go!
        StartAnimArrangeX.start();
        StartAnimJump.start();
    }

    /**
     * Updates the players moving direction according the current game gravity
     * @param delay delay needed ro reach the estimated min/max Y-position
     */
    public void updateGravity(int delay) {
        GravityState CurrentGravity = this.CurrentGame.getGravity();
        // is the gravity decreasing (tatzelwurm is moving up) or normal/increasing (tatzelwurm is moving down fast)?
        int toPosition = this.CurrentGame.getGravity() == GravityState.DECREASING ? minY : maxY;
        // TODO: review if the numbers will be set here or anywhere else
        int duration = (this.CurrentGame.getGravity() == GravityState.INCREASING ? 900 : 2000)+((delay*delay)*5);

        // abort previous animation
        if (this.GravityAnim != null) {
            this.GravityAnim.removeAllListeners();
            this.GravityAnim.cancel();
        }

        // build up new animation
        this.GravityAnim = ObjectAnimator.ofFloat(this.PartImageView, "Y", this.PartImageView.getY(), toPosition);
        this.GravityAnim.setDuration(duration);
        this.GravityAnim.setInterpolator(new BounceInterpolator());

        this.GravityAnim.start();
    }

    public void die() {
        // TODO: Death Animation
        this.PartImageView = null;
        this.GravityAnim = null;
    }
}
