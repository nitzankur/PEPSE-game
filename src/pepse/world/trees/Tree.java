package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/**
 * Tree class to create tress in pepse game
 */
public class Tree {
    private static final int RANDOM_TREES = 10;
    private static final int RANDOM_TRUNK = 5;
    private static final int LEAF_POS_START = 2;
    private final Color BASE_TRUNK_COLOR = new Color(100, 50, 20);
    private final Function<Integer, Float> groundHeightAt;
    private final int seed;
    private final GameObjectCollection gameObjects;
    private final int treeLayer;

    public Tree(GameObjectCollection gameObjects, int treeLayer,
                Function<Integer, Float> groundHeightAt, int seed) {
        this.gameObjects = gameObjects;
        this.treeLayer = treeLayer;
        this.groundHeightAt = groundHeightAt;
        this.seed = seed;
    }

    /**
     * create trees in random position in given x range
     * @param minX - start of range
     * @param maxX - end of range
     */
    public void createInRange(int minX, int maxX) {
        for (int i = minX; i < maxX; i += Block.SIZE) {
            //choose random position for trees
            Random random = new Random(Objects.hash(i, seed));
            int rand = random.nextInt(RANDOM_TREES);

            if (rand == 1) {
                float y = createTrunk(random,i);
                //create leaf
                LeafGenerator leafGenerator =
                        new LeafGenerator(gameObjects, PepseGameManager.LEAF_LAYER, seed);
                leafGenerator.leafGenerator(i,y - LEAF_POS_START);
            }
        }
    }

    /**
     *create trunk of tree in random height
     */

    private float createTrunk(Random random,int i){
        //create trunk
        RectangleRenderable trunkRenderer =
                new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
        float y = this.groundHeightAt.apply(i);
        Block block = new Block(new Vector2(i, y), trunkRenderer);
        block.setTag(PepseGameManager.TREE_TAG);
        gameObjects.addGameObject(block, treeLayer);

        //random height for trunk
        int rand = random.nextInt(RANDOM_TRUNK)+RANDOM_TRUNK;
        for (float j = y - Block.SIZE; j > y - Block.SIZE * rand; j -= Block.SIZE) {
            trunkRenderer =
                    new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
            block = new Block(new Vector2(i, j), trunkRenderer);
            block.setTag(PepseGameManager.TREE_TAG);
            gameObjects.addGameObject(block, treeLayer);
        }
        return y - Block.SIZE * rand;
    }

}
