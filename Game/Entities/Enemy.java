package com.devour.all.entities;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.devour.all.states.Play;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Enemy extends Entity {

    private boolean gather;
    private boolean flight;
    private List pathToGo;
    private int index;

    public Enemy(Body body) {

        super(body);
        gather = true;
        flight = false;
        this.setSize(body.getFixtureList().get(0).getShape().getRadius());
        index = 0;

    }

    public boolean getGather() { return gather; }
    public void setGather(boolean b) { gather = b; }
    public boolean getFlight() { return flight; }
    public void setFlight(boolean b) { flight = b; }
    public List getPath() { return pathToGo; }

    public void findShortestPath(){
        if(pathToGo == null) {
            Play.entityGraph.removeVertex(body);
            Play.entityGraph.addVertex(body);
            Play.addBodyToGraph(body);
        }
        ArrayList<Food> foods = Play.getFoods();
        int randomIndex = random(0, foods.size()-1);
        Body foodBody = foods.get(randomIndex).getBody();
        pathToGo = DijkstraShortestPath.findPathBetween(Play.entityGraph, this.body, foodBody);
        index = 0;
        followPath();
    }

    public void followPath(){
        if(pathToGo == null){
            findShortestPath();
            System.out.println("NULL");
        }
        else if(pathToGo.size() > index) {
            DefaultEdge edge = (DefaultWeightedEdge)pathToGo.get(index);
            Body foodBody = (Body)Play.entityGraph.getEdgeTarget(edge);
            if(foodBody != null) {
                goToPoint(foodBody);
                index++;
            }
            else{ findShortestPath(); }
        }
        else{findShortestPath();}

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
        findShortestPath();
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
