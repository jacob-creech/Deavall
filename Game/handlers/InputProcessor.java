package com.devour.all.handlers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.states.Play;

/**
 * Created by Jacob on 6/28/2015.
 */
public class InputProcessor implements GestureListener {

    private GestureDetector gd = new GestureDetector(this);
    public GestureDetector returnGestureDetector() { return gd; }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        Body body = Play.getPlayer().getBody();

        // calculte the normalized direction from the body to the touch position
        Vector2 direction = new Vector2(Gdx.input.getX() / 100f, Gdx.input.getY() / 100f);
        direction.sub(body.getPosition());
        direction.nor();
        System.out.println(body.getPosition().x + " " + body.getPosition().y);
        System.out.println(Gdx.input.getX() / 10f + " " + Gdx.input.getY() / 10f);

        float speed = 1;
        body.setLinearVelocity(direction.scl(speed));
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }
}
