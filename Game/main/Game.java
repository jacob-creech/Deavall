package com.devour.all.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devour.all.handlers.GameStateManager;
import com.devour.all.handlers.PlayerInputProcessor;


public class Game extends ApplicationAdapter {

    public static final String TITLE = "Deavall";
    private static SpriteBatch sb;
    private OrthographicCamera mainCamera;
    private OrthographicCamera hudCamera;

    public static final float STEP = 1/60f;
    private float accum;

    public static SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getMainCamera() { return mainCamera; }
    public OrthographicCamera getHudCamera() { return hudCamera; }

    private GameStateManager gsm;

    @Override
    public void create() {

        Gdx.input.setInputProcessor(new PlayerInputProcessor());

        sb = new SpriteBatch();
        mainCamera = new OrthographicCamera();
        mainCamera.setToOrtho(false);
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false);

        gsm = new GameStateManager(this);

    }

    @Override
    public void render() {

        accum += Gdx.graphics.getDeltaTime();
        while(accum >= STEP){
            accum -= STEP;
            gsm.update(STEP);
            gsm.render();

        }

    }

    public void dispose() {
        sb.dispose();
    }

    public void pause() {}
    public void resume() {}
}
