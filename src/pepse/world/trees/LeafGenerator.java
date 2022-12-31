package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;

public class LeafGenerator {

    private Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    private final GameObjectCollection gameObjects;
    private final Random random;
    private final int treeLayer;
    private final int seed;
    private static final int TREE_WIDTH = 3;
    private static final int TREE_HEIGHT = 9;
    private static final int RANDOM_LEAF = 20;


    public LeafGenerator(GameObjectCollection gameObjects, int treeLayer, int seed) {

        this.gameObjects = gameObjects;
        this.random = new Random(seed);
        this.treeLayer = treeLayer;
        this.seed = seed;
    }

    public void leafGenerator(int x, float y) {
        for (int i = x - Block.SIZE * TREE_WIDTH; i < x + Block.SIZE * TREE_WIDTH; i++) {
            for (float j = y; j > y - Block.SIZE * TREE_HEIGHT; j -= Block.SIZE) {
                int rand = random.nextInt(RANDOM_LEAF);
                if (rand == 1) {
                    RectangleRenderable leafRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
                    Leaf leaf = (new Leaf(new Vector2(i, j), new Vector2(Block.SIZE, Block.SIZE)
                            , leafRenderer, random));
                    gameObjects.addGameObject(leaf, treeLayer);
                }
            }
        }

        gameObjects.layers().shouldLayersCollide(treeLayer, Layer.STATIC_OBJECTS, true);

    }
}
