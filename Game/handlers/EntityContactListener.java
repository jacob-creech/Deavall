package handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Jacob on 6/28/2015.
 */
public class EntityContactListener implements ContactListener {

    private EventHandler eventHandler;

    public EntityContactListener(EventHandler eventHandler){
        super();

        this.eventHandler = eventHandler;
    }

    @Override
    public void beginContact(Contact contact) {
        /*
        * Called whenever two box2dbodies collide with each other
        * Reference for the box2dBits:
        *   Barrier = 000010
        *   Player  = 000100
        *   Enemy   = 001000
        *   Food    = 010000
        *   Virus   = 100000
        */

        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();
        String objectOne = fixtureA.getUserData().toString();
        String objectTwo = fixtureB.getUserData().toString();

        byte bitA, bitB, bitMap;

        bitA = setBitMap(objectOne, true);
        bitB = setBitMap(objectTwo, false);
        bitMap = (byte) (bitA | bitB);

        checkForEvent(bitMap, fixtureA, fixtureB);

    }

    public byte setBitMap(String contact, boolean fixtureCheck){
        /*
        * This function sets the relevant bitmap for use with collisions.
        * The bitmap returns will be checked to see if the collision
        * requires an event to occur.
         */
        byte bitMap = 0;
        if(contact.equals("2")){
            bitMap = 0x2;
        }
        else if(contact.equals("4")){
            bitMap = 0x4;
        }
        else if(contact.equals("8")){
            if(fixtureCheck){
                bitMap = 0x48;
            }
            else{
                bitMap = 0x8;
            }
        }
        else if(contact.equals("16")){
            bitMap = 0x10;
        }
        else if(contact.equals("32")){
            bitMap = 0x20;
        }

        return bitMap;
    }

    public void checkForEvent(byte bitMap, Fixture fixtureA, Fixture fixtureB){
        /*
        * This function checks for an event and sends any and all events
        * to the event handler.
         */
        if(bitMap == 0x0){
            // TODO: ENEMY CONTACTED ENEMY
            eventHandler.handleEnemyCol(fixtureA, fixtureB);
        }
        if((bitMap & 0x6) == 0x6){
            // TODO: PLAYER CONTACTS BARRIER
            eventHandler.handleBarrierCol(fixtureA, fixtureB);
        }
        if((bitMap & 0xA) == 0xA){
            // TODO: ENEMY CONTACTS BARRIER
            eventHandler.handleBarrierCol(fixtureA, fixtureB);
        }
        if((bitMap & 0xC) == 0xC){
            // TODO: PLAYER CONTACTS ENEMY
            if((bitMap & 0x4C) == 0x4C){
                // Player is fixtureA
                eventHandler.handlePlayerEnemyCol(fixtureA, fixtureB);
            }
            else{
                // Player is fixtureB
                eventHandler.handlePlayerEnemyCol(fixtureB, fixtureA);
            }
        }
        if((bitMap & 0x14) == 0x14){
            // TODO: PLAYER CONTACTS FOOD
            eventHandler.handleFoodCol(fixtureA, fixtureB);
        }
        if((bitMap & 0x18) == 0x18){
            // TODO: ENEMY CONTACTS FOOD
            eventHandler.handleFoodCol(fixtureA, fixtureB);
        }
        if((bitMap & 0x24) == 0x24){
            // TODO: PLAYER CONTACTS VIRUS
            eventHandler.handleVirusCol(fixtureA, fixtureB);
        }
        if((bitMap & 0x28) == 0x28){
            // TODO: ENEMY CONTACTS VIRUS
            eventHandler.handleVirusCol(fixtureA, fixtureB);
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    public void preSolve(Contact contact, Manifold oldManifold) {}
    public void postSolve(Contact contact, ContactImpulse impulse) {}

}
