package com.devour.all.handlers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.devour.all.main.Game;

/**
 * Created by Jacob on 7/11/2015.
 */
public class Loading extends ApplicationAdapter {

    Texture screen;
    Texture loadingBar;
    int percent = 0;

    public Loading(){
        screen = new Texture(Gdx.files.internal("android/assets/glassPanel_Projection.png"));
        loadingBar = new Texture(Gdx.files.internal("android/assets/buttonLong_grey.png"));
    }

    public int getPercent() { return percent; }
    public void incPercent() { percent++; }

    @Override
    public void render(){
        Game.getSpriteBatch().draw(screen, 0, 0,
                screen.getWidth() * (Gdx.graphics.getWidth() / 100f),
                screen.getHeight() * (Gdx.graphics.getHeight() / 100f));
        //Game.getSpriteBatch().draw();
    }

    public void dispose(){
        screen.dispose();
        loadingBar.dispose();
    }
}
