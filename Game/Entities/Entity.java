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
    public abstract void barrierCollision();

    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
