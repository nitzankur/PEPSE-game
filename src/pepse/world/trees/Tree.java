package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Tree {
    private final Function<Integer, Float> groundHeightAt;
    private final int seed;
    private GameObjectCollection gameObjects;
    private int treeLayer;
    private Color BASE_TRUNK_COLOR = new Color(100, 50, 20);

    private static final int RANDOM_TREES = 10;
    private static final int RANDOM_TRUNK = 5;

    private static final int LEAF_POS_START = 2;


    public Tree(GameObjectCollection gameObjects, int treeLayer, Function<Integer, Float> groundHeightAt, int seed) {

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

    public void treesGenerator(int minX, int maxX) {
        for (int i = minX; i < maxX; i += Block.SIZE) {
            //choose random position for trees
            Random random = new Random(Objects.hash(i, seed));
            int rand = random.nextInt(RANDOM_TREES);
            if (rand == 1) {
                //create trunk
                RectangleRenderable trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
                float y = this.groundHeightAt.apply(i);
                gameObjects.addGameObject(new Block(new Vector2(i, y), new Vector2(Block.SIZE, Block.SIZE)
                        , trunkRenderer), treeLayer);
                //random height for trunk
                rand = random.nextInt(RANDOM_TRUNK)+RANDOM_TRUNK;
                for (float j = y - Block.SIZE; j > y - Block.SIZE * rand; j -= Block.SIZE) {
                    trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
                    gameObjects.addGameObject(new Block(new Vector2(i, j), new Vector2(Block.SIZE, Block.SIZE)
                            , trunkRenderer), treeLayer);
                }
                //create leaf
                LeafGenerator leafGenerator = new LeafGenerator(gameObjects, treeLayer + 1, seed);
                leafGenerator.leafGenerator(i, y - Block.SIZE * rand -LEAF_POS_START);
            }
        }
    }

}
