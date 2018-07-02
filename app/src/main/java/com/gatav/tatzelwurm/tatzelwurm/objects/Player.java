package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.drm.DrmStore;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.gatav.tatzelwurm.tatzelwurm.Game;

import java.util.Collections;
import java.util.LinkedList;

public class Player {
    private Game CurrentGame;
    private LinkedList<PlayerPart> Parts = new LinkedList<>();

    public Player(final Game CurrentGame, final int lifes) {
        this.CurrentGame = CurrentGame;

        for (int i = 0; i < lifes; i++) {
            PlayerPart NewPart;

            // body
            if (i > 1 && i < lifes-1) {
                NewPart = new PlayerPart(CurrentGame,2);
            }
            // head (0), arms (1) or tail (> 1)
            else {
                NewPart = new PlayerPart(CurrentGame, i);
            }

            this.Parts.add(NewPart);
        }
    }

    public PlayerPart getHead() { return this.Parts.getFirst(); }

    public LinkedList<PlayerPart> getParts() {
        return this.Parts;
    }

    public int getLifes() { return this.Parts.size(); }

    public void start() {
        // TODO: review if the numbers will be set here or anywhere else
        int delay = 300;
        int i = delay/this.Parts.size();
        // start position on screen
        float startPosX = this.CurrentGame.getActivity().getScreenWidth()*0.75f;
        for (PlayerPart p : this.Parts) {
            startPosX -= p.getPartImageView().getDrawable().getIntrinsicWidth()*0.5f;
            p.start(startPosX, delay);
            delay += i;
            i += this.Parts.size()/10;
        }
    }

    public void updateGravity() {
        int delayCounter = 0;

        for (PlayerPart part: this.Parts) {
            part.updateGravity(delayCounter++);
        }
    }

    public void hit() {
        for (int i = 0; i < 4; i++) {
            PlayerPart DyingPart = this.Parts.get(0);
            this.Parts.remove(0);
            this.CurrentGame.getActivity().getGameView().removeView(DyingPart.getPartImageView());
            DyingPart.die();
        }
    }
}