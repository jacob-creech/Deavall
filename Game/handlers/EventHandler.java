package com.devour.all.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.devour.all.entities.Enemy;
import com.devour.all.entities.Food;
import com.devour.all.entities.Player;
import com.devour.all.states.Play;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.awt.geom.Point2D;
import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class EventHandler {

    private boolean gameover;
    private boolean win;
    private int box2dZoom;
    private int mainCamZoom;
    private ArrayList<Body> bodiesToRemove;

    public EventHandler(){
        super();
        gameover = false;
        win = false;
        bodiesToRemove = new ArrayList<Body>();
        box2dZoom = 0;
        mainCamZoom = 0;
    }

    public int getBox2dZoom() { return box2dZoom; }
    public void setBox2dZoom(int zoom) { box2dZoom = zoom; }
    public int getMainCamZoom() { return mainCamZoom; }
    public void setMainCamZoom(int zoom) { mainCamZoom = zoom; }

    public boolean getWin() { return win; }
    public void setWin(Boolean b) { win = b; }
    public boolean getGameOver() { return gameover; }
    public void setGameover(Boolean b) { gameover = b; }

    public void handleBarrierCol(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getUserData().equals("Barrier")){
            changeAnimation(fixtureB.getBody());
            if(fixtureB.getBody().getUserData() instanceof Enemy){
                Enemy enemy = (Enemy)fixtureB.getBody().getUserData();
                enemy.barrierCollision();
            }
        }
        else{
            changeAnimation(fixtureA.getBody());
            if(fixtureA.getBody().getUserData() instanceof Enemy){
                Enemy enemy = (Enemy)fixtureA.getBody().getUserData();
                enemy.barrierCollision();
            }
        }
    }
    public void handlePlayerEnemyCol(Fixture fixtureA, Fixture fixtureB) {
        /*
        * This method properly handles whenever a player body
        * comes into contact with an enemy body. If the enemy
        * body is 25% less than the player, the enemy is removed.
        * Else, if the player is 25% less, the game ends and the
        * gameover switch is set to true.
         */
        Player player = Play.getPlayer();
        Enemy enemy = returnEnemy(fixtureA, fixtureB);

        if(enemy.getSize() > (player.getSize()*1.25f)){
            setGameover(true);
        }
        else if(player.getSize() > (enemy.getSize()*1.25f)){
            player.resize(player.getBody(), player.getSize() + (enemy.getSize() * .3f));
            player.setSize(player.getSize() + (enemy.getSize() * .3f));
            addToRemove(enemy.getBody());
            int newZoom = (int)Math.ceil(enemy.getSize() / .001f);
            box2dZoom+= (newZoom);
            mainCamZoom+= (newZoom);
        }
    }
    public void handleEnemyCol(Fixture fixtureA, Fixture fixtureB) {
        /*
        * This method properly handles whenever an enemy body
        * comes in contact with another enemy body. The proper
        * procedure is to check whether or not one of the bodies
        * is 25% bigger than the other, then remove the smaller
        * and increase the size of the larger.
         */
        Enemy enemyA = (Enemy)fixtureA.getBody().getUserData();
        Enemy enemyB = (Enemy)fixtureB.getBody().getUserData();
        if(enemyA.getSize() > (enemyB.getSize()*1.25f)){
            enemyA.resize(enemyA.getBody(), enemyA.getSize() + (enemyB.getSize()*.1f));
            enemyA.setSize(enemyA.getSize() + (enemyB.getSize()*.1f));
            addToRemove(enemyB.getBody());
            followNextPath(enemyA);
        }
        else if(enemyB.getSize() > (enemyA.getSize()*1.25f)){
            enemyB.resize(enemyB.getBody(), enemyB.getSize() + (enemyA.getSize()*.1f));
            enemyB.setSize(enemyB.getSize() + (enemyA.getSize()*.1f));
            addToRemove(enemyA.getBody());
            followNextPath(enemyB);
        }
        else{
            followNextPath(enemyA);
            followNextPath(enemyB);
        }

    }
    public void handleFoodCol(Fixture fixtureA, Fixture fixtureB) {
        /*
        * This method properly resizes a body whenever it
        * comes into contact with food. This will affect
        * whether or not the player or enemy can eat one
        * another on contact.
         */
        //System.out.println("FOOD COLLISION");
        if(fixtureA.getBody().getUserData() instanceof Enemy){
            Enemy enemy = (Enemy)fixtureA.getBody().getUserData();
            resizeEnemy(enemy);
            followNextPath(enemy);
        }
        else if(fixtureA.getBody().getUserData() instanceof Player){
            Player player = Play.getPlayer();
            resizePlayer(player);
        }
        else if(fixtureB.getBody().getUserData() instanceof Enemy){
            Enemy enemy = (Enemy)fixtureB.getBody().getUserData();
            resizeEnemy(enemy);
            followNextPath(enemy);
        }
        else if(fixtureB.getBody().getUserData() instanceof Player){
            Player player = Play.getPlayer();
            resizePlayer(player);
        }

        if(fixtureA.getBody().getUserData() instanceof Food){
            addToRemove(fixtureA.getBody());
        }
        else if(fixtureB.getBody().getUserData() instanceof Food){
            addToRemove(fixtureB.getBody());
        }
    }

    public void followNextPath(Enemy enemy){ enemy.findNextPath(); }

    public void handleVirusCol(Fixture fixtureA, Fixture fixtureB) {}

    public void handleAIPlayer(Fixture fixtureA, Fixture fixtureB){
        Enemy enemy = returnEnemy(fixtureA,fixtureB);
        Player player = Play.getPlayer();

        float enemyX = enemy.getBody().getPosition().x;
        float enemyY = enemy.getBody().getPosition().y;
        float playerX = player.getBody().getPosition().x;
        float playerY = player.getBody().getPosition().y;

        float randomPercentage = random(0,100) / 100f;

        if(player.getSize() > enemy.getSize() && randomPercentage < .8f){
            // Avoid player
            System.out.println("Avoiding Player");
            enemy.setGather(false);
            enemy.setFlight(true);
            Vector2 direction = new Vector2(-(playerX - enemyX), -(playerY - enemyY));
            direction.nor();
            enemy.getBody().setLinearVelocity(direction.scl(enemy.getSpeed()));
        }
        else{
            enemy.setGather(true);
            // Do nothing, carry on
        }
    }

    public void handleAIEnemy(Fixture fixtureA, Fixture fixtureB){
        Enemy enemyA = (Enemy)fixtureA.getBody().getUserData();
        Enemy enemyB = (Enemy)fixtureB.getBody().getUserData();

        float enemyAX = enemyA.getBody().getPosition().x;
        float enemyAY = enemyA.getBody().getPosition().y;
        float enemyBX = enemyB.getBody().getPosition().x;
        float enemyBY = enemyB.getBody().getPosition().y;

        float randomPercentageA = random(0,100) / 100f;
        float randomPercentageB = random(0,100) / 100f;

        if(enemyB.getSize() > enemyA.getSize() && randomPercentageA < .6f){
            enemyA.setGather(false);
            enemyA.setFlight(true);
            Vector2 direction = new Vector2(-(enemyBX - enemyAX), -(enemyBY - enemyAY));
            direction.nor();
            enemyA.getBody().setLinearVelocity(direction.scl(enemyA.getSpeed()));
        }
        else if(enemyA.getSize() > enemyB.getSize() && randomPercentageB < .6f){
            enemyB.setGather(false);
            enemyB.setFlight(true);
            Vector2 direction = new Vector2(-(enemyAX - enemyBX), -(enemyAY - enemyBY));
            direction.nor();
            enemyB.getBody().setLinearVelocity(direction.scl(enemyB.getSpeed()));
        }
        else{
            Vector2 directionA = new Vector2(-(enemyBX - enemyAX), -(enemyBY - enemyAY));
            directionA.nor();
            enemyB.getBody().setLinearVelocity(directionA.scl(enemyB.getSpeed()));

            Vector2 directionB = new Vector2(-(enemyAX - enemyBX), -(enemyAY - enemyBY));
            directionB.nor();
            enemyA.getBody().setLinearVelocity(directionB.scl(enemyA.getSpeed()));
        }
    }

    public void handleAIFood(Fixture fixtureA, Fixture fixtureB){
        Enemy enemy = returnEnemy(fixtureA,fixtureB);
        Food food = returnFood(fixtureA,fixtureB);
        Body enemyBody = enemy.getBody();

        float enemyX = enemy.getBody().getPosition().x;
        float enemyY = enemy.getBody().getPosition().y;
        float foodX = food.getBody().getPosition().x;
        float foodY = food.getBody().getPosition().y;
        // Make sure that enemy is set to gather
        if(enemy.getGather()){
            Vector2 direction = new Vector2(foodX - enemyX, foodY - enemyY);
            direction.nor();
            enemyBody.setLinearVelocity(direction.scl(enemy.getSpeed()));
        }
    }

    public void handleAIVirus(Fixture fixtureA, Fixture fixtureB){}

    public Enemy returnEnemy(Fixture fixtureA, Fixture fixtureB){
        if(fixtureA.getBody().getUserData() instanceof Enemy){
            return (Enemy)fixtureA.getBody().getUserData();
        }
        return (Enemy)fixtureB.getBody().getUserData();
    }

    public Food returnFood(Fixture fixtureA, Fixture fixtureB){
        if(fixtureA.getBody().getUserData() instanceof Food){
            return (Food)fixtureA.getBody().getUserData();
        }
        return (Food)fixtureB.getBody().getUserData();
    }

    public void resizePlayer(Player player){
        float size = player.getSize();
        Body body = player.getBody();
        player.setSize(size + .001f);
        player.resize(body, size + .001f);
        box2dZoom++;
        mainCamZoom++;
    }
    public void resizeEnemy(Enemy enemy){
        float size = enemy.getSize();
        Body body = enemy.getBody();
        enemy.setSize(size + .001f);
        enemy.resize(body, size + .001f);
    }

    public void addToRemove(Body body){ bodiesToRemove.add(body); }
    public ArrayList<Body> getBodies() { return bodiesToRemove; }


    public void changeAnimation(Body body){
        /*
        * This method will change an entity's animation
        * based on where it is on the map, and how it is
        * touching the barriers
         */

        if(body.getUserData() instanceof Enemy) {
            Enemy enemy = (Enemy)body.getUserData();
            enemy.barrierCollision();
        }
        else{
            Player player = (Player)body.getUserData();
            player.barrierCollision();
        }

    }

}
