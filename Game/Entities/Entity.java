package com.devour.all.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.devour.all.handlers.Box2DVars;
import com.devour.all.main.Game;
import com.devour.all.states.Play;

/**
 * Created by Jacob on 6/28/2015.
 */
public abstract class Entity {
    protected Body body;
    private float size;
    int score;
    Pixmap pixmap;
    Texture pixmapTexture;

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
        setSize(this.getSize()*.99f);
        resize(body, this.getSize()*.99f);
    }

    public boolean isOnScreen(float xPos, float yPos){
        float playerX = Play.getPlayer().getBody().getPosition().x;
        float playerY = Play.getPlayer().getBody().getPosition().y;

        float leftScreen = playerX * Box2DVars.PPM * 2 - (Gdx.graphics.getWidth() * Game.getMainCamera().zoom);
        float rightScreen = playerX * Box2DVars.PPM * 2 + (Gdx.graphics.getWidth() * Game.getMainCamera().zoom);
        float topScreen = playerY * Box2DVars.PPM * 2 + (Gdx.graphics.getHeight() * Game.getMainCamera().zoom);
        float bottomScreen = playerY * Box2DVars.PPM * 2 - (Gdx.graphics.getHeight() * Game.getMainCamera().zoom);

        boolean xDir = (leftScreen < xPos) && (xPos < rightScreen);
        boolean yDir = (yPos > bottomScreen) && (yPos < topScreen);
        return (xDir && yDir);
    }

    public abstract void barrierCollision();

    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
