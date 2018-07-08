package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

public class Egg {
    private Game CurrentGame;

    private ImageView TopImageView;
    private ImageView BottomImageView;
    private int cracks = 0;

    public Egg(Game CurrentGame) {
        this.CurrentGame = CurrentGame;

        this.TopImageView = new ImageView(this.CurrentGame.getActivity());
        this.BottomImageView = new ImageView(this.CurrentGame.getActivity());

        this.TopImageView.setImageDrawable(this.CurrentGame.getActivity().getEggTop());
        this.BottomImageView.setImageDrawable(this.CurrentGame.getActivity().getEggBottom());

        int startTopPosX = this.CurrentGame.getActivity().getScreenWidth()/2 - this.TopImageView.getDrawable().getIntrinsicWidth()/2;
        int startBottomPosX = this.CurrentGame.getActivity().getScreenWidth()/2 - this.BottomImageView.getDrawable().getIntrinsicWidth()/2;
        int startTopPosY = this.CurrentGame.getActivity().getScreenHeight() - this.TopImageView.getDrawable().getIntrinsicHeight() - this.BottomImageView.getDrawable().getIntrinsicHeight() + 20;
        int startBottomPosY = this.CurrentGame.getActivity().getScreenHeight() - this.TopImageView.getDrawable().getIntrinsicHeight();

        this.TopImageView.setX(startTopPosX);
        this.BottomImageView.setX(startBottomPosX);
        this.TopImageView.setY(startTopPosY);
        this.BottomImageView.setY(startBottomPosY);
    }

    public ImageView getTopImageView() { return this.TopImageView; }

    public ImageView getBottomImageView() { return this.BottomImageView; }

    public void crack() {
        // references current egg to use later in inner class calls
        final Egg _this = this;

        // shake egg every time
        ObjectAnimator RotTopAnim = ObjectAnimator.ofFloat(this.TopImageView, "Rotation", 4, -4);
        RotTopAnim.setDuration(75);
        RotTopAnim.setRepeatCount(5);
        RotTopAnim.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator RotBottomAnim = ObjectAnimator.ofFloat(this.BottomImageView, "Rotation", -4, 4);
        RotBottomAnim.setDuration(75);
        RotBottomAnim.setRepeatCount(5);
        RotBottomAnim.setRepeatMode(ValueAnimator.REVERSE);

        // TODO: Replace with Egg drawables
        switch (this.cracks) {
            case 0:
                this.CurrentGame.getActivity().crack1.start();
                RotTopAnim.start();
                RotBottomAnim.start();
                break;
            case 1:
                this.CurrentGame.getActivity().crack2.start();
                RotTopAnim.start();
                RotBottomAnim.start();
                break;
            case 2:
                this.CurrentGame.getActivity().crack3.start();
                RotTopAnim.start();
                RotBottomAnim.start();
                // fake player movement with moving egg and let the top jump away
                ObjectAnimator MoveTopAnim = ObjectAnimator.ofFloat(this.TopImageView, "X", this.TopImageView.getX(), -this.TopImageView.getX()-800);
                ObjectAnimator JumpTopAnim = ObjectAnimator.ofFloat(this.TopImageView, "Y", this.TopImageView.getY(), this.TopImageView.getY()-70);
                ObjectAnimator RotateTopAnim = ObjectAnimator.ofFloat(this.TopImageView, "Rotation", 0, -360);

                ObjectAnimator MoveBottomAnim = ObjectAnimator.ofFloat(this.BottomImageView, "X", this.BottomImageView.getX(), -this.BottomImageView.getX());

                MoveTopAnim.setDuration(1000);
                JumpTopAnim.setDuration(150);
                JumpTopAnim.setInterpolator(new DecelerateInterpolator());
                RotateTopAnim.setDuration(1500);
                MoveBottomAnim.setDuration(1200);

                JumpTopAnim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {}

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ObjectAnimator JumpFallTopAnim = ObjectAnimator.ofFloat(_this.TopImageView, "Y", _this.TopImageView.getY(), _this.CurrentGame.getActivity().getScreenHeight() - _this.TopImageView.getDrawable().getIntrinsicHeight());
                        JumpFallTopAnim.setDuration(600);
                        JumpFallTopAnim.setInterpolator(new BounceInterpolator());
                        JumpFallTopAnim.start();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {}

                    @Override
                    public void onAnimationRepeat(Animator animation) {}
                });

                MoveTopAnim.start();
                JumpTopAnim.start();
                RotateTopAnim.start();
                MoveBottomAnim.start();

                // go! the game starts.
                this.CurrentGame.start();
                break;
        }

        this.cracks++;
    }
}