package handlers;

/**
 * Created by Jacob on 6/29/2015.
 */
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import main.Game;

/**
 * With much help from the BlockBunny tutorial
 * found on YouTube at the following link:
 * https://www.youtube.com/watch?v=85A1w1iD2oA&list=PL-2t7SM0vDfdYJ5Pq9vxeivblbZuFvGJK
 */

public class Background {

    private TextureRegion image;
    private OrthographicCamera camera;
    private float scale;

    private float x;
    private float y;
    private int numDrawX;
    private int numDrawY;

    private float dx;
    private float dy;

    private float changeX;
    private float changeY;

    public Background(TextureRegion image, OrthographicCamera camera, float scale){
        this.image = image;
        this.camera = camera;
        this.scale = scale;

        changeX = Gdx.graphics.getWidth() / 1920;
        changeY = Gdx.graphics.getHeight() / 1080;

        numDrawX = Gdx.graphics.getWidth() / image.getRegionWidth() + 1;
        numDrawY = Gdx.graphics.getHeight() / image.getRegionHeight() + 1;
    }

    public void setVector(float dx, float dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public void update(float dt) {
        x += (dx * scale) * dt;
        y += (dy * scale) * dt;
    }

    public void render() {

        float x = ((this.x + camera.viewportWidth / 2 - camera.position.x) * scale) % image.getRegionWidth();
        float y = ((this.y + camera.viewportHeight / 2 - camera.position.y) * scale) % image.getRegionHeight();


        int colOffset = x > 0 ? -1 : 0;
        int rowOffset = y > 0 ? -1 : 0;
        for(int row = 0; row <= numDrawY; row++) {
            for(int col = 0; col <= numDrawX; col++) {
                Game.getSpriteBatch().draw(image, x + (col + colOffset) * image.getRegionWidth(), y + (rowOffset + row) * image.getRegionHeight());
            }
        }


    }

}