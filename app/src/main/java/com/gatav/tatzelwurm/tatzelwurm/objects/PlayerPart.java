package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;
import com.gatav.tatzelwurm.tatzelwurm.enums.GravityState;

import java.util.Random;

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
        // TODO: create and change drawables // Sorry mache ich noch lol
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
    public void start(float positionX, final int delay, final boolean isLastPart) {
        // references current player part to use later in inner class calls
        final PlayerPart _this = this;

        // the tatzelwurm starts with a jump on start
        ObjectAnimator StartAnimJump = ObjectAnimator.ofFloat(this.PartImageView, "Y", this.PartImageView.getY(), this.PartImageView.getY()-150);
        StartAnimJump.setDuration(500+delay);
        StartAnimJump.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                _this.CurrentGame.setControlLocked(true);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator StartAnimJumpFall = ObjectAnimator.ofFloat(PartImageView, "Y", PartImageView.getY(), maxY);
                StartAnimJumpFall.setDuration(1500);
                StartAnimJumpFall.setInterpolator(new BounceInterpolator());
                StartAnimJumpFall.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if (isLastPart) {
                            _this.CurrentGame.postStart();
                        }
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });

                StartAnimJumpFall.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
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

    /**
     * jumps in random direction and remove itself from view
     *
     */
    public void die() {
        // references current player part to use later in inner class calls
        final PlayerPart _this = this;

        // abort previous animation
        if (this.GravityAnim != null) {
            this.GravityAnim.removeAllListeners();
            this.GravityAnim.cancel();
        }

        // dying jump high is set randomly from Y [100, 300] and X [-600, -200] and [200, 600]
        int dyingJumpY = (100+(int)(Math.random() * 200));
        int dyingJumpX = (200+(int)(Math.random() * 400));

        Random random = new Random();
        dyingJumpX = random.nextBoolean() ? dyingJumpX : -dyingJumpX;
        int dyingRotation = random.nextBoolean() ? 360 : -360;

        // the sum of both is (+some delay to provide a smooth animation) the duration of the dying jump X animation
        final int dyingJumpDuration = 300;
        final int dyingFallDuration = 500;

        ObjectAnimator DyingAnimJumpX = ObjectAnimator.ofFloat(this.PartImageView, "X", this.PartImageView.getX(), this.PartImageView.getX() + dyingJumpX);
        ObjectAnimator DyingAnimJumpY = ObjectAnimator.ofFloat(this.PartImageView, "Y", this.PartImageView.getY(), this.PartImageView.getY() - dyingJumpY);
        ObjectAnimator DyingAnimRotation = ObjectAnimator.ofFloat(this.PartImageView, "Rotation", 0, dyingRotation);

        DyingAnimJumpY.setDuration(dyingJumpDuration);
        DyingAnimJumpX.setDuration(dyingJumpDuration +  dyingFallDuration + 200);
        DyingAnimRotation.setDuration(1800);

        DyingAnimJumpY.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator DyingAnimFall = ObjectAnimator.ofFloat(PartImageView, "Y", PartImageView.getY(), maxY+PartImageView.getHeight());
                DyingAnimFall.setDuration(dyingFallDuration);
                DyingAnimFall.setInterpolator(new AccelerateInterpolator());

                DyingAnimFall.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        _this.CurrentGame.getActivity().getGameView().removeView(_this.PartImageView);
                        _this.PartImageView = null;
                        _this.GravityAnim = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });
                DyingAnimFall.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {}

            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        DyingAnimJumpX.start();
        DyingAnimJumpY.start();
        DyingAnimRotation.start();
    }

    public void growHead() {
        AnimationDrawable growHeadAnim = this.CurrentGame.getActivity().PlayerGrowHeadAnim;
        this.PartImageView.setImageDrawable(this.CurrentGame.getActivity().PlayerGrowHeadAnim);
        growHeadAnim.start();
    }
}
