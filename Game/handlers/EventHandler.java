package handlers;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import entities.Enemy;
import entities.Entity;
import entities.Player;

/**
 * Created by Jacob on 6/28/2015.
 */
public class EventHandler {

    private boolean gameover;
    private boolean win;

    public EventHandler(){
        super();
        gameover = false;
        win = false;
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
    public void handleFoodCol(Fixture fixtureA, Fixture fixtureB) {}
    public void handleVirusCol(Fixture fixtureA, Fixture fixtureB) {}

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
