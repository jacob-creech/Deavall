package com.devour.all.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.devour.all.handlers.GameStateManager;
import com.devour.all.main.Game;

import static com.devour.all.main.Game.*;

/**
 * Created by Jacob on 7/22/2015.
 */
public class Menu extends GameState {

    private TextButton playButton;
    private Stage stage;
    private TextButton.TextButtonStyle buttonStyle;
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;
    private Texture background;


    public Menu(GameStateManager gsm){
        super(gsm);

        background = Game.res.getTexture("background");
        background.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        skin = new Skin();
        buttonStyle = new TextButton.TextButtonStyle();
        buttonAtlas = Game.res.getTextureAtlas("defaultButton");

        skin.addRegions(buttonAtlas);
        font = Game.res.getFont("mainFont");
        buttonStyle.font = font;
        buttonStyle.up = skin.getDrawable("buttonLong_blue");
        buttonStyle.down = skin.getDrawable("buttonLong_blue_pressed");
        playButton = new TextButton("Play", buttonStyle);
        float h = skin.getDrawable("buttonLong_blue").getMinHeight();
        float w = skin.getDrawable("buttonLong_blue").getMinWidth();
        playButton.setPosition(Gdx.graphics.getWidth()/2 - (w/2), Gdx.graphics.getHeight()/2 - (h/2));
        stage.addActor(playButton);
    }

    @Override
    public void handleInput() {
        if(playButton.isPressed()){
            gsm.setState(GameStateManager.PLAY);
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();
        sb.draw(background,0,0,WIDTH,HEIGHT);
        sb.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
