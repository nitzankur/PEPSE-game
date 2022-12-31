package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.Random;

public class Terrain {

    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
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
        this.windowDimensions = WindowDimensions;
    }

    public float groundHeightAt(float x){
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        return  groundHeightAtX0+ (float) Math.sin(100 * x) * (float) noiseGenerator.noise(x)*150;
    }

    public void createInRange(int minX, int maxX){
        for (int i = minX; i < maxX; i+=Block.SIZE) {
            RectangleRenderable blockRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
            Vector2 blockPos = calculatePos(i);
            Block block = new Block(blockPos,new Vector2(Block.SIZE,TERRAIN_DEPTH),blockRenderer);
            block.setTag(PepseGameManager.TERRAIN_TAG);
            gameObjects.addGameObject(block,groundLayer);
            for (float j = blockPos.y()+TERRAIN_DEPTH; j < windowDimensions.y() ; j+=TERRAIN_DEPTH) {
                Vector2 blockYPos = new Vector2(blockPos.x(),j);
                blockRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                block = new Block(blockYPos,new Vector2(Block.SIZE,TERRAIN_DEPTH),blockRenderer);
                block.setTag(PepseGameManager.TERRAIN_TAG);
                gameObjects.addGameObject(block,
                        groundLayer + 1);
            }
        }
    }

    private Vector2 calculatePos(int x){
        double y =  Math.floor(groundHeightAt(x)/Block.SIZE * Block.SIZE);
        return new Vector2(x,(float) y);
    }

    private void removeBlock(Block block) {
        Avatar avatar = null;
        for (GameObject gameObject : gameObjects.objectsInLayer(Layer.DEFAULT)) {
            if(gameObject instanceof Avatar)
                avatar = (Avatar) gameObject;
        }

        if (avatar != null) {
            float x = block.getTopLeftCorner().x();
            if (x < avatar.getCenter().x() - windowDimensions.mult(0.5f).x() - 100 ||
                    x > avatar.getCenter().x() + windowDimensions.mult(0.5f).x() + 100) {
                System.out.println("removed");
                gameObjects.removeGameObject(block);
            }
        }
    }

}
