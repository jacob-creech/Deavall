package com.devour.all.entities;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Player extends Entity{

    public Player(Body body) {
        super(body);
    }

    public Body getBody(){ return body; }

    @Override
    public void barrierCollision() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {

    }

    @Override
    public void dispose() {

    }
}
