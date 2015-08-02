package com.devour.all.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.handlers.Box2DVars;
import com.devour.all.main.Game;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Player extends Entity{

    private ArrayList<Body> playerBodies;
    private Vector2 coords;
    private int highscore;

    public Player(Body body) {
        super(body);
        this.setSize(.1f);

        coords = new Vector2();
        playerBodies = new ArrayList<Body>();
        playerBodies.add(body);

        highscore = 0;

        this.pixmap = new Pixmap(300,300, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(random(0,255),random(0,255),random(0,255),1f);
        this.pixmap.fillCircle(150,150,150);
        this.pixmapTexture = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
        this.pixmap.dispose();
    }

    public Vector2 getCoords(){

        float xPos, yPos;
        xPos = 0;
        yPos = 0;
        for(int i = 0; i < playerBodies.size(); i++){
            xPos += playerBodies.get(i).getPosition().x;
            yPos += playerBodies.get(i).getPosition().y;
        }
        xPos = xPos / playerBodies.size();
        yPos = yPos / playerBodies.size();
        coords.x = xPos;
        coords.y = yPos;
        return coords;
    }

    public int returnHighscore(){ return highscore; }

    @Override
    public void barrierCollision() {

    }

    public void split(float xPos, float yPos){

        for(int i = 0; i < playerBodies.size(); i++){
            playerBodies.remove(i);
        }
    }

    @Override
    public void update(float dt) {
        if(this.getScoreInt() > highscore){
            highscore = this.getScoreInt();
        }
    }

    @Override
    public void render() {
        float xPos = Gdx.graphics.getWidth()/2 - this.getSize() * Box2DVars.PPM/2;
        float yPos = Gdx.graphics.getHeight()/2 - this.getSize() * Box2DVars.PPM/2;
        Game.getSpriteBatch().draw(pixmapTexture,
                xPos - this.getSize() * 250,
                yPos - this.getSize() * 250,
                this.getSize() * 500,
                this.getSize() * 500
        );
    }

    @Override
    public void dispose() {
        this.pixmapTexture.dispose();
    }
}
