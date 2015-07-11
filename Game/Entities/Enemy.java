package com.devour.all.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
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

    public Enemy(Body body) {

        super(body);
        gather = true;
        flight = false;
        this.setSize(body.getFixtureList().get(0).getShape().getRadius());
        bodies = new HashMap<Double, Body>();
        pathToGo = new ArrayList<Float>();
        index = 0;

    }

    public boolean getGather() { return gather; }
    public void setGather(boolean b) { gather = b; }
    public boolean getFlight() { return flight; }
    public void setFlight(boolean b) { flight = b; }

    public void populateMap(){
        for(int i = 0; i < Play.getFoods().size(); i++){
            double distance;
            Body foodBody = Play.getFoods().get(i).getBody();
            distance = Math.sqrt(Math.pow((double)body.getPosition().x - foodBody.getPosition().x, 2)
                            + Math.pow(body.getPosition().y - foodBody.getPosition().y, 2));
            bodies.put(distance, Play.getFoods().get(i).getBody());
        }
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
        Body foodBody = bodies.get(pathToGo.get(index));
        index++;
        System.out.println(index + " " + body);
        goToPoint(foodBody);

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
