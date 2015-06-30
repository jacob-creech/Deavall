package entities;

import com.badlogic.gdx.physics.box2d.Body;

/**
 * Created by Jacob on 6/28/2015.
 */
public abstract class Entity {
    protected Body body;

    public Entity(Body body){
        this.body = body;

    }

    public abstract void barrierCollision();

    public abstract void update(float dt);
    public abstract void render();
    public abstract void dispose();

}
