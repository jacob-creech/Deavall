package com.devour.all.entities;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.devour.all.main.Game;

import com.badlogic.gdx.scenes.scene2d.Stage;

/**
 * Created by Jacob on 7/8/2015.
 */
public class HUD extends ApplicationAdapter{

    BitmapFont font;
    private static Player player;
    private Stage stage;
    private TextButton splitButton;
    private TextButton.TextButtonStyle splitStyle;
    Skin skin;
    TextureAtlas buttonAtlas;

    public HUD(Player player){
        font = Game.res.getFont("mainFont");
        this.player = player;
        buttonAtlas = Game.res.getTextureAtlas("defaultButton");
        stage = new Stage();
        skin = new Skin();
        splitStyle = new TextButton.TextButtonStyle();
        skin.addRegions(buttonAtlas);

        splitStyle.font = font;
        splitStyle.up = skin.getDrawable("buttonLong_blue");
        splitStyle.down = skin.getDrawable("buttonLong_blue_pressed");
        splitButton = new TextButton("", splitStyle);

        float height = skin.getDrawable("buttonLong_blue").getMinHeight();
        float width = skin.getDrawable("buttonLong_blue").getMinWidth();
        splitButton.setPosition(7*Gdx.graphics.getWidth()/8 - (width/2), Gdx.graphics.getHeight()/8 - (height/2));
        stage.addActor(splitButton);
    }

    public static void resetPlayer(Player newPlayer){
        player = newPlayer;
    }

    public void update(float dt){
        if(splitButton.isChecked()){
            // split player
            splitButton.setChecked(false);
        }
    }

    public void render(){
        String score = player.getScoreString();
        font.draw(Game.getSpriteBatch(), score, Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 20);
        stage.draw();
    }
    public void dispose(){ }


}
