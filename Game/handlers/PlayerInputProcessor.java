package com.devour.all.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.states.Play;

/**
 * Created by Jacob on 6/29/2015.
 */
public class PlayerInputProcessor implements InputProcessor {

    private float xPos, yPos;

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        Body body = Play.getPlayer().getBody();
        xPos = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2f);
        yPos = (Gdx.input.getY() - Gdx.graphics.getHeight() / 2f);

        // calculate the normalized direction from the body to the touch position
        Vector2 direction = new Vector2(xPos, -yPos);
        direction.sub(body.getPosition());
        direction.nor();

        float speed = Play.getPlayer().getSpeed();
        body.setLinearVelocity(direction.scl(2f));
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
