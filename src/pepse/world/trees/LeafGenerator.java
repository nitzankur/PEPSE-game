package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

/**
 * generates leaves in pepse game
 */
public class LeafGenerator {

    private static final int TREE_WIDTH = 3;
    private static final int TREE_HEIGHT = 9;
    private static final int RANDOM_LEAF = 20;
    private final Color BASE_LEAF_COLOR = new Color(50, 200, 30);
    private final GameObjectCollection gameObjects;
    private final int treeLayer;
    private final int seed;


    public LeafGenerator(GameObjectCollection gameObjects, int treeLayer, int seed) {

        this.gameObjects = gameObjects;
        this.treeLayer = treeLayer;
        this.seed = seed;
    }

    /**
     * create leafs for a given trunk
     * @param x the position of the x trunk
     * @param y the position of the y trunk
     */
    public void leafGenerator(int x, float y) {
        for (int i = x - Block.SIZE * TREE_WIDTH; i < x + Block.SIZE * TREE_WIDTH; i++) {
            //create random position for leaf
            Random random = new Random(Objects.hash(i, seed));
            for (float j = y; j > y - Block.SIZE * TREE_HEIGHT; j -= Block.SIZE) {
                int rand = random.nextInt(RANDOM_LEAF);
                if (rand == 1) {
                    RectangleRenderable leafRenderer =
                            new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
                    Leaf leaf = (new Leaf(new Vector2(i, j), new Vector2(Block.SIZE, Block.SIZE)
                            , leafRenderer, random));
                    leaf.setTag(PepseGameManager.LEAF_TAG);
                    gameObjects.addGameObject(leaf, treeLayer);
                }
            }
        }
        //make leaf collide with terrain
        gameObjects.layers().shouldLayersCollide(treeLayer, Layer.STATIC_OBJECTS, true);

    }
}
