package com.gatav.tatzelwurm.tatzelwurm.objects;

import android.graphics.drawable.Drawable;

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
            if (i > 0 && i < lifes-1) {
                NewPart = new PlayerPart(CurrentGame,1);
            }
            // head (0) or tail (> 1)
            else {
                NewPart = new PlayerPart(CurrentGame, i);
            }

            this.Parts.add(NewPart);
        }
    }

    public LinkedList<PlayerPart> getParts() {
        return this.Parts;
    }

    public void start() {
        // TODO: review if the numbers will be set here or anywhere else
        int delay = 200;
        int i = delay/this.Parts.size();
        float startPosX = this.CurrentGame.getActivity().getScreenWidth()*0.75f;
        for (PlayerPart p : this.Parts) {
            startPosX -= p.getPartImageView().getDrawable().getIntrinsicWidth()*0.7f;
            p.start(startPosX, delay);
            delay += i;
            i += this.Parts.size();
        }
    }
}