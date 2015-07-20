package com.devour.all.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.handlers.Box2DVars;
import com.devour.all.main.Game;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Player extends Entity{

    public Player(Body body) {
        super(body);
        this.setSize(.1f);
        this.pixmap = new Pixmap(300,300, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(random(0,255),random(0,255),random(0,255),1f);
        this.pixmap.fillCircle(150,150,150);
        this.pixmapTexture = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
        this.pixmap.dispose();
    }

    @Override
    public void barrierCollision() {

    }

    @Override
    public void update(float dt) {

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
