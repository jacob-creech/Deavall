package com.devour.all.handlers;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.devour.all.entities.Enemy;
import com.devour.all.entities.Entity;
import com.devour.all.entities.Food;
import com.devour.all.entities.Player;
import com.devour.all.states.Play;

import java.util.ArrayList;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Jacob on 6/28/2015.
 */
public class EventHandler {

    private boolean gameover;
    private boolean win;
    private ArrayList<Body> bodiesToRemove;

    public EventHandler(){
        super();
        gameover = false;
        win = false;
        bodiesToRemove = new ArrayList<Body>();
    }

    public boolean getWin() { return win; }
    public void setWin(Boolean b) { win = b; }
    public boolean getGameOver() { return gameover; }
    public void setGameover(Boolean b) { gameover = b; }

    public void handleBarrierCol(Fixture fixtureA, Fixture fixtureB) {
        if(fixtureA.getBody().getUserData().equals("Barrier")){
            changeAnimation(fixtureB.getBody());
        }
        else{
            changeAnimation(fixtureA.getBody());
        }
    }
    public void handlePlayerEnemyCol(Fixture player, Fixture enemy) {}
    public void handleEnemyCol(Fixture enemyA, Fixture enemyB) {}
    public void handleFoodCol(Fixture fixtureA, Fixture fixtureB) {
        /*
        * This method properly resizes a body whenever it
        * comes into contact with food. This will affect
        * whether or not the player or enemy can eat one
        * another on contact.
         */

        if(fixtureA.getBody().getUserData() instanceof Enemy){
            resizeEnemy((Enemy)fixtureA.getBody().getUserData());
        }
        else if(fixtureA.getBody().getUserData() instanceof Player){
            Player player = Play.getPlayer();
            resizePlayer(player);
        }
        else if(fixtureB.getBody().getUserData() instanceof Enemy){
            resizeEnemy((Enemy)fixtureA.getBody().getUserData());
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

    public void handleVirusCol(Fixture fixtureA, Fixture fixtureB) {}

    public void handleAIPlayer(Fixture fixtureA, Fixture fixtureB){
        Enemy enemy = returnEnemy(fixtureA,fixtureB);
        Player player = Play.getPlayer();

        float enemyX = enemy.getBody().getPosition().x;
        float enemyY = enemy.getBody().getPosition().y;
        float playerX = player.getBody().getPosition().x;
        float playerY = player.getBody().getPosition().y;

        float randomPercentage = random(0,100) / 100f;

        if(player.getSize() > enemy.getSize() && randomPercentage > .8){
            // Avoid player
            enemy.setGather(false);
            enemy.setFlight(true);
            Vector2 direction = new Vector2(playerX - enemyX, playerY - enemyY);
            enemy.getBody().setLinearVelocity(direction.scl(enemy.getSpeed()));
        }
        else{
            // Do nothing, carry on
        }
    }

    public void handleAIEnemy(Fixture fixtureA, Fixture fixtureB){

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
        if(fixtureA.getBody().getUserData() instanceof Enemy){
            return (Food)fixtureA.getBody().getUserData();
        }
        return (Food)fixtureB.getBody().getUserData();
    }

    public void resizePlayer(Player player){
        float size = player.getSize();
        Body body = player.getBody();
        player.setSize((float)(size + (size * .01)));
        player.resize(body, (float)(size + (size * .01)));
    }
    public void resizeEnemy(Enemy enemy){
        float size = enemy.getSize();
        Body body = enemy.getBody();
        enemy.setSize((float)(size + (size * .01)));
        enemy.resize(body, (float)(size + (size * .01)));
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
