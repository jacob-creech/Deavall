package com.devour.all.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.devour.all.handlers.Box2DVars;
import com.devour.all.main.Game;
import com.devour.all.states.Play;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.devour.all.handlers.Box2DVars.BIT_BARRIER;
import static com.devour.all.handlers.Box2DVars.BIT_ENEMY;
import static com.devour.all.handlers.Box2DVars.BIT_ENEMY_FILTER;
import static com.devour.all.handlers.Box2DVars.BIT_FOOD;
import static com.devour.all.handlers.Box2DVars.BIT_PLAYER;
import static com.devour.all.handlers.Box2DVars.BIT_VIRUS;
import static com.devour.all.handlers.Box2DVars.PPM;

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

    public void split(){
        float xPos = (Gdx.input.getX() - Gdx.graphics.getWidth() / 2f);
        float yPos = (Gdx.input.getY() - Gdx.graphics.getHeight() / 2f);
        if(this.getSize() > 150 || playerBodies.size() < 9){
            float currentSize = playerBodies.size();

            for(int i = 0; i < playerBodies.size(); i++){
                playerBodies.remove(i);
            }

            createNewBody((float)(this.getSize() * .6 * currentSize / 2));
        }
    }

    public void createNewBody(float size){
        BodyDef bodyDef = new BodyDef();
        //bodyDef.position.set(WIDTH / 4 / PPM - 10 / PPM, HEIGHT / 2 / PPM - 10 / PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //Body body = Play.world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(10 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_BARRIER | BIT_ENEMY | BIT_FOOD | BIT_VIRUS | BIT_ENEMY_FILTER;
        body.setLinearDamping(1f);
        body.createFixture(fixtureDef).setUserData(BIT_PLAYER);
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
