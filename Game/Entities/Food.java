package com.devour.all.entities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.handlers.Box2DVars;
import com.devour.all.main.Game;
import com.devour.all.states.Play;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Food extends Entity {

    public Food(Body body) {
        super(body);
        this.pixmap = new Pixmap(30,30, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(random(0,255),random(0,255),random(0,255),1f);
        this.pixmap.fillCircle(15,15,15);
        this.pixmapTexture = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
        this.pixmap.dispose();
    }

    public void barrierCollision() {}

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        float xpos = body.getPosition().x * Box2DVars.PPM * 2 + 150;
        float ypos = body.getPosition().y * Box2DVars.PPM * 1.5f + 112 + Play.getPlayer().getBody().getPosition().y * Box2DVars.PPM * 2.5f;
        Game.getSpriteBatch().draw(pixmapTexture, xpos, ypos);
    }

    @Override
    public void dispose() {
        this.pixmapTexture.dispose();
    }

}
