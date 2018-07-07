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
            if (i > 3 && i < lifes - 2) {
                NewPart = new PlayerPart(CurrentGame,2);
            } else if (i == lifes - 2) {
                NewPart = new PlayerPart(CurrentGame, 3);
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
        int delay = 300;
        int i = delay/this.Parts.size();
        // start position on screen on 75% of screen
        float startPosX = this.CurrentGame.getActivity().getScreenWidth()*0.5f;
        for (PlayerPart p : this.Parts) {
            // player parts X positions are 50% in itself
            startPosX -= p.getPartImageView().getDrawable().getIntrinsicWidth()*0.3f;
            // start player start animation
            p.start(startPosX, delay, p == this.Parts.getLast());
            // increase delay for smooth jump of each player part
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
        int i;
        for (i = 0; i < 4; i++) {
            PlayerPart DyingPart = this.Parts.get(0);
            this.Parts.remove(0);

            DyingPart.die();
        }
        this.Parts.get(0).growHead();
    }
}