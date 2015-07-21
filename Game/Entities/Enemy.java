package com.devour.all.entities;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.handlers.Box2DVars;
import com.devour.all.main.Game;
import com.devour.all.states.Play;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Enemy extends Entity {

    private boolean gather;
    private boolean flight;
    private List pathToGo;
    private int index;
    private HashMap<Double, Body> bodies;
    float startSize;

    public Enemy(Body body) {

        super(body);
        gather = true;
        flight = false;
        this.setSize(body.getFixtureList().get(0).getShape().getRadius());
        startSize = this.getSize();
        bodies = new HashMap<Double, Body>();
        pathToGo = new ArrayList<Float>();
        index = 0;
        int pixmapRadius = Math.round(this.getSize()*1000);
        this.pixmap = new Pixmap(pixmapRadius,pixmapRadius, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(random(0,255),random(0,255),random(0,255),1f);
        this.pixmap.fillCircle(pixmapRadius/2,pixmapRadius/2,pixmapRadius/2);
        this.pixmapTexture = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
        this.pixmap.dispose();

    }

    public boolean getGather() { return gather; }
    public void setGather(boolean b) { gather = b; }
    public boolean getFlight() { return flight; }
    public void setFlight(boolean b) { flight = b; }
    public List getPath() { return pathToGo; }
    public int getIndex() { return index; }

    public void reset(){
        this.setSize(startSize);
        this.resize(this.body, startSize);
    }

    public void populateMap(){
        for(int i = 0; i < Play.getFoods().size(); i++){
            double distance;
            Body foodBody = Play.getFoods().get(i).getBody();
            distance = Math.sqrt(Math.pow((double)body.getPosition().x - foodBody.getPosition().x, 2)
                    + Math.pow(body.getPosition().y - foodBody.getPosition().y, 2));
            bodies.put(distance, Play.getFoods().get(i).getBody());
        }
    }

    public void addFoodBody(Body foodBody){
        double distance;
        distance = Math.sqrt(Math.pow((double)body.getPosition().x - foodBody.getPosition().x, 2)
                + Math.pow(body.getPosition().y - foodBody.getPosition().y, 2));
        pathToGo.add(distance);
        bodies.put(distance, foodBody);
    }

    public void sortMap(){
        Set<Double> keys = bodies.keySet();
        pathToGo.addAll(keys);
        Collections.sort(pathToGo);
    }

    public void findNextPath(){
        if(pathToGo.isEmpty()){
            populateMap();
            sortMap();
        }
        if(index == bodies.size()){
            index = index % bodies.size();
        }
        Body foodBody = bodies.get(pathToGo.get(0));
        bodies.remove(pathToGo.get(0));
        pathToGo.remove(0);
        index++;
        if(index % Play.getFoods().size() == 0){
            index = index % Play.getFoods().size();
            index = 0;
            sortMap();
        }
        //System.out.println(index + " " + body);
        if(foodBody != null){
            goToPoint(foodBody);
        }

    }

    public void goToPoint(Body otherBody){
        float enemyX = body.getPosition().x;
        float enemyY = body.getPosition().y;
        float otherX = otherBody.getPosition().x;
        float otherY = otherBody.getPosition().y;

        Vector2 direction = new Vector2(otherX - enemyX, otherY - enemyY);
        direction.nor();
        body.setLinearVelocity(direction.scl(this.getSpeed()));
    }

    public void makePixmap(){
        int pixmapRadius = Math.round(this.getSize()*1000);
        this.pixmap = new Pixmap(pixmapRadius,pixmapRadius, Pixmap.Format.RGBA8888);
        this.pixmap.setColor(random(0,255),random(0,255),random(0,255),1f);
        this.pixmap.fillCircle(pixmapRadius/2,pixmapRadius/2,pixmapRadius/2);
        this.pixmapTexture = new Texture(pixmap, Pixmap.Format.RGBA8888, false);
        this.pixmap.dispose();
    }

    @Override
    public void barrierCollision() {
        findNextPath();
    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render() {
        float xPos = body.getPosition().x * Box2DVars.PPM * 2 + 150;
        float yPos = body.getPosition().y * Box2DVars.PPM * 1.5f + 112 + Play.getPlayer().getBody().getPosition().y * Box2DVars.PPM * 2.5f;
        Game.getSpriteBatch().draw(pixmapTexture,
                xPos - this.getSize() * 250,
                yPos - this.getSize() * 250,
                this.getSize() * 600,
                this.getSize() * 600
                );
    }

    @Override
    public void dispose() {
        this.pixmapTexture.dispose();
    }
}
