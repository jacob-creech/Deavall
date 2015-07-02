package com.devour.all.handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.devour.all.entities.Enemy;
import com.devour.all.entities.Entity;
import com.devour.all.entities.Food;
import com.devour.all.entities.Player;
import com.devour.all.states.Play;

import java.util.ArrayList;

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
    public boolean getGameOver() { return gameover; }

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
