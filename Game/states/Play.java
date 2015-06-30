package states;

import com.badlogic.gdx.Gdx;
import static handlers.Box2DVars.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import java.util.ArrayList;

import entities.Enemy;
import entities.Food;
import entities.Player;
import handlers.Background;
import handlers.EntityContactListener;
import handlers.EventHandler;
import handlers.GameStateManager;

/**
 * Created by Jacob on 6/28/2015.
 */
public class Play extends GameState {

    private World world;
    private Box2DDebugRenderer b2dr;
    private final float WIDTH = Gdx.graphics.getWidth();
    private final float HEIGHT = Gdx.graphics.getHeight();

    private ArrayList<Enemy> enemies;
    private ArrayList<Food> food;
    private static Player player;
    private EventHandler eventHandler;

    public static Player getPlayer() { return player; }

    private OrthographicCamera b2dcam;
    private Background background;

    public Play(GameStateManager gsm){
        super(gsm);

        // Create the world with no gravity
        world = new World(new Vector2(0, 0), true);
        eventHandler = new EventHandler();
        world.setContactListener(new EntityContactListener(eventHandler));

        b2dr = new Box2DDebugRenderer();
        b2dcam = new OrthographicCamera();
        b2dcam.setToOrtho(false, (WIDTH/2) / PPM, (WIDTH/2) / PPM);

        // Create the area for the entities
        createArea();

        // Create the Player
        createPlayer();

        // Create Background
        Texture texture = new Texture(Gdx.files.internal("grid.png"));
        TextureRegion textureRegion = new TextureRegion(texture, 0, 0, 49, 49);
        background = new Background(textureRegion, mainCamera, .31f);

    }

    public void createArea(){
        /*
        * The area is the room that which each body can move around in.
        * Barriers on all four sides shall be invisible, as well as show
        * an animation when in collision with an entity.
         */

        // Bottom Left corner
        createBarrier(WIDTH*(-2), HEIGHT * (-2), true);
        createBarrier(WIDTH*(-2), HEIGHT * (-2), false);
        // Top Left corner
        createBarrier(WIDTH*(-2), HEIGHT * (2), true);
        // Bottom Right corner
        createBarrier(WIDTH*(2), HEIGHT * (-2), false);


    }

    public void createBarrier(float xPos, float yPos, boolean direction){
        /*
        * Helper function for creating the barriers in createArea.
        * Direction drawn is decided by the boolean:
        *   True = Draw Right
        *   False = Draw Up
         */

        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(xPos / PPM, yPos / PPM);
        bodyDef.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        if(direction) {
            shape.setAsBox(WIDTH*4 / PPM, 2 / PPM);
        }
        else{
            shape.setAsBox(2 / PPM, WIDTH*4 / PPM);
        }
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = BIT_BARRIER;
        fixtureDef.filter.maskBits = BIT_ENEMY | BIT_PLAYER;
        body.createFixture(fixtureDef).setUserData(BIT_BARRIER);
        body.setUserData("Barrier");

        shape.dispose();

    }

    public void createPlayer(){
        /*
        * This function creates the player and places him onto the
        * area.
         */
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(WIDTH / 4 / PPM - 2.5f / PPM, HEIGHT / 2 / PPM - 2.5f / PPM);
        bodyDef.type = BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(5 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_BARRIER | BIT_ENEMY | BIT_FOOD | BIT_VIRUS;
        body.createFixture(fixtureDef).setUserData(BIT_PLAYER);

        player = new Player(body);
        body.setUserData(player);

        circle.dispose();
    }

    @Override
    public void handleInput() {  }

    @Override
    public void update(float dt) {
        //System.out.println(player.getBody().getPosition().x + " " + player.getBody().getPosition().y);
        handleInput();
        world.step(dt, 6, 2);

    }

    @Override
    public void render() {

        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        sb.begin();

        sb.setProjectionMatrix(hudCamera.combined);
        background.render();

        sb.setProjectionMatrix(mainCamera.combined);
        mainCamera.position.set(
                player.getBody().getPosition().x * PPM * 2 + WIDTH / 4,
                player.getBody().getPosition().y * PPM * 4 + HEIGHT / 4,
                0
        );
        mainCamera.update();
        sb.end();

        // Draw box2d world
        b2dcam.position.set(
                player.getBody().getPosition().x ,//+ WIDTH / 8 / PPM ,
                player.getBody().getPosition().y,
                0
        );
        b2dcam.update();
        b2dr.render(world, b2dcam.combined);

    }

    @Override
    public void dispose() {
        world.dispose();
    }
}
