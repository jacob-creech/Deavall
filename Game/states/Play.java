package com.devour.all.states;

import com.badlogic.gdx.Gdx;

import static com.badlogic.gdx.math.MathUtils.random;
import static com.badlogic.gdx.math.MathUtils.randomSign;
import static com.devour.all.handlers.Box2DVars.*;
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
import com.devour.all.entities.Enemy;
import com.devour.all.entities.Entity;
import com.devour.all.entities.Food;
import com.devour.all.entities.Player;
import com.devour.all.handlers.Background;
import com.devour.all.handlers.EntityContactListener;
import com.devour.all.handlers.EventHandler;
import com.devour.all.handlers.GameStateManager;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;


/**
 * Created by Jacob on 6/28/2015.
 */
public class Play extends GameState {

    private static World world;
    private Box2DDebugRenderer b2dr;
    private static final float WIDTH = Gdx.graphics.getWidth();
    private static final float HEIGHT = Gdx.graphics.getHeight();

    private static ArrayList<Enemy> enemies;
    private static ArrayList<Food> foods;
    private static Player player;
    private EventHandler eventHandler;

    public static SimpleWeightedGraph entityGraph;

    public static Player getPlayer() { return player; }
    public static ArrayList<Food> getFoods() { return foods; }

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

        // Initialize arrayLists
        enemies = new ArrayList<Enemy>();
        foods = new ArrayList<Food>();
        entityGraph = new SimpleWeightedGraph<Body, DefaultWeightedEdge>(DefaultWeightedEdge.class);

        // Create the area for the entities
        createArea();

        // Create the Player
        createPlayer();

        // Create food
        for(int i = 0; i < 500; i++){
            createFood();
        }

        // Create enemies
        for(int i = 0; i < 8; i++){
            createEnemy();
        }

        // Create Background
        Texture texture = new Texture(Gdx.files.internal("android/assets/grid.png"));
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
        bodyDef.position.set(WIDTH / 4 / PPM - 10 / PPM, HEIGHT / 2 / PPM - 10 / PPM);
        bodyDef.type = BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(10 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = BIT_PLAYER;
        fixtureDef.filter.maskBits = BIT_BARRIER | BIT_ENEMY | BIT_FOOD | BIT_VIRUS | BIT_ENEMY_FILTER;
        body.setLinearDamping(1f);
        body.createFixture(fixtureDef).setUserData(BIT_PLAYER);

        player = new Player(body);
        body.setUserData(player);

        circle.dispose();
    }

    public void createEnemy(){
        /*
        * This function randomly generates enemies within the
        * play area with varying sizes.
         */
        BodyDef bodyDef = new BodyDef();
        Random rand = new Random();
        int maxX = (int) (4*WIDTH);
        int maxY = (int) (4*HEIGHT);
        int randomX = rand.nextInt(maxX+1);
        int randomY = rand.nextInt(maxY+1);

        // Creating random coordinates between barriers
        randomX -= (int) (2*WIDTH);
        randomY -= (int) (2*HEIGHT);

        bodyDef.position.set(randomX / PPM, randomY / PPM);
        bodyDef.type = BodyType.DynamicBody;
        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();

        /*
        * In order to create both a challenge and possibility to
        * improve, enemies are generated at 0 - 50% larger and
        * smaller than the current player.
        * There is also a rare chance of a very large percentage
        * difference between a player and an enemy. We are
        * setting this percentage to .5% for now.
         */
        float percentageDiff = random(0,50) / 100f;

        int randomNum = rand.nextInt( (200-0) + 1);
        if(randomNum == 1) {
            percentageDiff = random(125,175) / 100f;
        }

        float radius = player.getSize() + randomSign() * player.getSize() * percentageDiff;
        circle.setRadius(radius);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = BIT_ENEMY;
        fixtureDef.filter.maskBits = BIT_BARRIER | BIT_PLAYER | BIT_FOOD | BIT_VIRUS | BIT_ENEMY_FILTER | BIT_ENEMY;
        body.createFixture(fixtureDef).setUserData(BIT_ENEMY);

        // Creating a filter for AI decisions
        circle.setRadius(radius + .15f);
        fixtureDef.shape = circle;
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = BIT_ENEMY_FILTER;
        fixtureDef.filter.maskBits = BIT_PLAYER | BIT_FOOD | BIT_VIRUS | BIT_ENEMY;
        body.createFixture(fixtureDef).setUserData(BIT_ENEMY_FILTER);

        Enemy enemy = new Enemy(body);
        body.setUserData(enemy);
        circle.dispose();

        entityGraph.addVertex(body);
        addBodyToGraph(body);
        enemy.findShortestPath();
        enemies.add(enemy);
    }


    public static void createFood(){
        /*
        * This function randomly creates food within the
        * play area.
         */
        BodyDef bodyDef = new BodyDef();
        Random rand = new Random();
        int maxX = (int) (4*WIDTH);
        int maxY = (int) (4*HEIGHT);
        int randomX = rand.nextInt(maxX+1);
        int randomY = rand.nextInt(maxY+1);

        // Creating random coordinates between barriers
        randomX -= (int) (2*WIDTH);
        randomY -= (int) (2*HEIGHT);

        bodyDef.position.set(randomX / PPM, randomY / PPM);
        bodyDef.type = BodyType.StaticBody;
        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(5 / PPM);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = BIT_FOOD;
        fixtureDef.filter.maskBits = BIT_ENEMY | BIT_PLAYER | BIT_ENEMY_FILTER;
        body.createFixture(fixtureDef).setUserData(BIT_FOOD);

        Food food = new Food(body);
        body.setUserData(food);
        circle.dispose();

        entityGraph.addVertex(body);
        foods.add(food);
        addBodyToGraph(body);
    }

    public static void addBodyToGraph(Body body){
        /*
        * This function creates a graph of all the entities.
        * This graph will be used for pathfinding with enemies.
        * Each food is only connected to 2 other food entities,
        * the 2 entities being the shortest distance away upon
        * creation of the original entity.
         */
        DefaultWeightedEdge minEdge = new DefaultWeightedEdge();
        Body minBody = foods.get(0).getBody();
        DefaultWeightedEdge edge;
        double currentWeight = 100;
        for(int i = 0; i < foods.size(); i++){
            //edge = (DefaultWeightedEdge)entityGraph.addEdge(foods.get(i).getBody(), body);
            double weight;
            double xDiff = foods.get(i).getBody().getPosition().x - body.getPosition().x;
            double yDiff = foods.get(i).getBody().getPosition().y - body.getPosition().y;
            weight = Math.sqrt(Math.pow(xDiff, 2) + Math.pow(yDiff, 2));
            minBody = foods.get(i).getBody();
            if(minBody != body) {
                minEdge = (DefaultWeightedEdge) entityGraph.addEdge(minBody, body);
                entityGraph.setEdgeWeight(minEdge, currentWeight);
            }
            if(weight < currentWeight){
                currentWeight = weight;
            }
        }
        /*if(minBody != body) {
            minEdge = (DefaultWeightedEdge) entityGraph.addEdge(minBody, body);
            entityGraph.setEdgeWeight(minEdge, currentWeight);
        }*/
    }

    @Override
    public void handleInput() {  }

    int enemyCounter = 0;
    Vector2 zeroSpeed = new Vector2(1,1);
    @Override
    public void update(float dt) {
        handleInput();
        world.step(dt, 6, 2);

        ArrayList<Body> bodies = eventHandler.getBodies();
        for(int i = 0; i < bodies.size(); i++) {
            Body b = bodies.get(i);
            entityGraph.removeVertex(b);
            foods.remove(b.getUserData());
            world.destroyBody(bodies.get(i));
            Play.createFood();
        }
        bodies.clear();

        for(int i = 0; i < enemies.size(); i++){
            if(enemies.get(i).getBody().getLinearVelocity().isZero()){
                enemies.get(i).followPath();
            }
        }

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
