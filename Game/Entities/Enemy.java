package com.devour.all.entities;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Enemy extends Entity {

    private boolean gather;
    private boolean flight;

    public Enemy(Body body) {

        super(body);
        gather = true;
        flight = false;
        this.setSize(body.getFixtureList().get(0).getShape().getRadius());

    }

    public boolean getGather() { return gather; }
    public void setGather(boolean b) { gather = b; }
    public boolean getFlight() { return flight; }
    public void setFlight(boolean b) { flight = b; }

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
