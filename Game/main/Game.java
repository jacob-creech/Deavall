package com.devour.all.main;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.devour.all.handlers.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.devour.all.handlers.GameStateManager;

public class Game extends ApplicationAdapter {

	public static final String TITLE = "Deavall";
    private SpriteBatch sb;
    private OrthographicCamera mainCamera;
    private OrthographicCamera hudCamera;

    public static final float STEP = 1/60f;
    private float accum;

    public SpriteBatch getSpriteBatch() { return sb; }
    public OrthographicCamera getMainCamera() { return mainCamera; }
    public OrthographicCamera getHudCamera() { return hudCamera; }

    private GameStateManager gsm;

	@Override
	public void create() {

        Gdx.input.setInputProcessor(new InputProcessor().returnGestureDetector());

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
