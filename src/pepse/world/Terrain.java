package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;

/**
 * Terrain class to calculate and create terrain positions in pepse game
 */
public class Terrain {

    private static final float HEIGHT_PARAMETER = (float) 2 / 3;
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final int TERRAIN_FACTOR_SIN = 100;
    private static final int TERRAIN_FACTOR = 150;
    private static final int TERRAIN_HEIGHT = 10;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final float groundHeightAtX0;
    private final int seed;


    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 WindowDimensions, int seed) {

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        groundHeightAtX0 = WindowDimensions.y() * HEIGHT_PARAMETER;
        this.seed = seed;
        this.windowDimensions = WindowDimensions;
    }

    /**
     * calculate ground height
     * @param x - position in world
     * @return the height in given x
     */
    public float groundHeightAt(float x) {
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);

        return groundHeightAtX0 + (float) Math.sin(TERRAIN_FACTOR_SIN * x) *
                (float) noiseGenerator.noise(x) * TERRAIN_FACTOR;

    }

    /**
     * create terrain n given range
     * @param minX - start position
     * @param maxX - end position
     */

    public void createInRange(int minX, int maxX) {
        for (int i = minX; i < maxX; i += Block.SIZE) {
            // create new block - along width in given range
            RectangleRenderable blockRenderer =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
            Vector2 blockPos = calculatePos(i);
            Block block = new Block(blockPos, new Vector2(Block.SIZE, TERRAIN_DEPTH), blockRenderer);
            block.setTag(PepseGameManager.TERRAIN_TAG);
            gameObjects.addGameObject(block, groundLayer);

            for (float j = blockPos.y() + TERRAIN_DEPTH;
                 j < windowDimensions.y() + TERRAIN_DEPTH * TERRAIN_HEIGHT; j += TERRAIN_DEPTH) {
                //creates block in column
                Vector2 blockYPos = new Vector2(blockPos.x(), j);
                blockRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR));
                block = new Block(blockYPos, new Vector2(Block.SIZE, TERRAIN_DEPTH), blockRenderer);
                block.setTag(PepseGameManager.BOTTOM_TERRAIN_TAG);
                gameObjects.addGameObject(block, PepseGameManager.BOTTOM_TERRAIN_LAYER);
            }
        }
    }

    /**
     * calculate terrain position
     * @param x - return the given x
     * @return the position of y position
     */
    private Vector2 calculatePos(int x) {
        double y = Math.floor(groundHeightAt(x) / Block.SIZE * Block.SIZE);
        return new Vector2(x, (float) y);
    }
}
