package com.devour.all.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Created by Jacob on 6/28/2015.
 */
public abstract class Entity {
    protected Body body;
    private float size;
    int score;

    public Entity(Body body){
        this.body = body;
    }

    public Body getBody(){ return body; }
    public float getSize() { return size; }
    public void setSize(float size) { this.size = size; }

    public float getSpeed(){
        float speed;
        speed = (2f-size);
        return speed;
    }

    public void resize(Body body, float newRadius){
        Shape shape;
        for(int i = 0; i < body.getFixtureList().size; i++){
            shape = body.getFixtureList().get(i).getShape();
            if(body.getFixtureList().get(i).isSensor()){
                shape.setRadius(.15f + newRadius);
            }
            else{
                shape.setRadius(newRadius);
            }
        }
    }

    public String getScoreString(){
        score = (int)Math.ceil(this.getSize() * 1000f - 99);
        return Integer.toString(score);
    }

    public int getScoreInt(){
        score = (int)Math.ceil(this.getSize() * 1000f - 99);
        return score;
    }

    public void shrink(Body body){
        System.out.println(this.getSize()*.99f);
        setSize(this.getSize()*.99f);
        resize(body, this.getSize()*.99f);
    }

    public abstract void barrierCollision();

    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
