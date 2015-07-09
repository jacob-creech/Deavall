package com.devour.all.entities;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Player extends Entity{

    int score;

    public Player(Body body) {
        super(body);
        this.setSize(.1f);
        this.score = 0;
    }

    @Override
    public void barrierCollision() {

    }

    public String getScore(){
        score = (int)Math.ceil(this.getSize() * 100f);
        return Integer.toString(score);
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
