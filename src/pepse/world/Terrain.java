package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.Random;

public class Terrain {

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windoeDimensions;
    private final Random random;

    private float groundHeightAtX0;
    private static final float HEIGHT_PARAMETER = (float) 2/3;
    private static final Color BASE_GROUND_COLOR = new Color(212,123,74);
    private static final int TERRAIN_DEPTH = 20;
    private int seed;


    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 WindowDimensions, int seed){

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        groundHeightAtX0 = WindowDimensions.y() * HEIGHT_PARAMETER;
        this.seed = seed;
        this.random = new Random(seed);
        this.windoeDimensions = WindowDimensions;
    }

    public double groundHeightAt(float x){
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        return groundHeightAtX0+ Math.sin(4*(x/5))*noiseGenerator.noise(x)*150;
    }

    public void createInRange(int minX, int maxX){
        RectangleRenderable blockRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
        for (int i = minX; i < maxX; i+=Block.SIZE) {
            Vector2 blockPos = calculatePos(i);
            gameObjects.addGameObject(new Block(blockPos,new Vector2(Block.SIZE,TERRAIN_DEPTH),blockRenderer),groundLayer);
            for (float j = blockPos.y()+TERRAIN_DEPTH; j < windoeDimensions.y() ; j+=TERRAIN_DEPTH) {
                Vector2 blockYPos = new Vector2(blockPos.x(),j);
                gameObjects.addGameObject(new Block(blockYPos,new Vector2(Block.SIZE,TERRAIN_DEPTH),blockRenderer),
                        groundLayer);
            }
        }


    }

    private Vector2 calculatePos(int x){
        double y =  Math.floor(groundHeightAt(x)/Block.SIZE * Block.SIZE);
        return new Vector2(x,(float) y);
    }

}
