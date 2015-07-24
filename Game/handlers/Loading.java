package com.devour.all.handlers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.devour.all.main.Game;

/**
 * Created by Jacob on 7/11/2015.
 */
public class Loading extends ApplicationAdapter {

    Texture screen;
    Texture loadingBar;
    BitmapFont font;
    float percent;
    int BodiesDone;
    final int totalBodies = 550;
    float WIDTH = Gdx.graphics.getWidth();
    float HEIGHT = Gdx.graphics.getHeight();

    public Loading(){
        screen = Game.res.getTexture("loadBackground");
        loadingBar = Game.res.getTexture("loadingBar");
        font = Game.res.getFont("mainFont");
        BodiesDone = 0;
        percent = 0;
    }

    public int getBodiesDone() { return BodiesDone; }
    public void incBodiesDone() { BodiesDone++; }
    public float getPercent() {
        percent = (float)BodiesDone / totalBodies;
        return percent;
    }

    @Override
    public void render(){
        Game.getSpriteBatch().draw(screen, 0, 0,
                screen.getWidth() * (Gdx.graphics.getWidth() / 100f),
                screen.getHeight() * (Gdx.graphics.getHeight() / 100f)
        );
        Game.getSpriteBatch().draw(loadingBar,
                WIDTH/4,
                3*HEIGHT/8,
                WIDTH/2 * getPercent(),
                HEIGHT/10
        );
        if(getBodiesDone() < 500) {
            font.draw(Game.getSpriteBatch(), "Loading food", WIDTH / 2 - 96, 21 * HEIGHT / 40);
        }
        else if(getBodiesDone() < 550) {
            font.draw(Game.getSpriteBatch(), "Loading enemies", WIDTH / 2 - 120, 21 * HEIGHT / 40);
        }
    }

    public void dispose(){ }
}
