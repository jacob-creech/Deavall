package com.devour.all.entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.devour.all.main.Game;

/**
 * Created by Jacob on 7/8/2015.
 */
public class HUD extends ApplicationAdapter{

    BitmapFont font;
    Player player;

    public HUD(Player player){
        font = new BitmapFont(Gdx.files.internal("android/assets/visitor.fnt"));
        this.player = player;
    }

    public void render(){
        String score = player.getScore();
        font.draw(Game.getSpriteBatch(), score, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 20);
    }
    public void dispose(){ font.dispose(); }


}
