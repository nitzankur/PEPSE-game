package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.trees.Tree;

public class InfiniteWorldGenerator extends GameObject {
    private static final int BLOCK_OFFSET_FACTOR = 3;
    private GameObjectCollection gameObjects;
    private float curMinX;
    private float curMaxX;
    private float halfDimension;
    private Terrain terrain;
    private Tree tree;
    private Camera camera;

    public InfiniteWorldGenerator(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                  Terrain terrain, Tree tree, Camera camera) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.halfDimension = windowDimensions.mult(0.5f).x() -
                (windowDimensions.mult(0.5f).x() % Block.SIZE) + (Block.SIZE * BLOCK_OFFSET_FACTOR);
        this.curMinX = 0;
        this.curMaxX = this.curMinX + (2 * this.halfDimension) + Block.SIZE;
        this.terrain = terrain;
        this.tree = tree;
        this.camera = camera;
        this.gameObjects = gameObjects;
        gameObjects.addGameObject(this);
        this.terrain.createInRange((int) curMinX, (int) curMaxX);
        this.tree.treesGenerator((int) curMinX, (int) curMaxX);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        createObjects();
        removeObjects();
    }

    private void createObjects() {
        if (camera.getCenter().x() - halfDimension < curMinX) {
            float newMinX = camera.getCenter().x() - halfDimension;
            newMinX = newMinX - (newMinX % Block.SIZE) - Block.SIZE;
            terrain.createInRange((int) newMinX, (int) (curMinX));
            tree.treesGenerator((int) newMinX, (int) (curMinX));
            curMinX = newMinX;
            curMaxX = curMinX + (2 * halfDimension) + Block.SIZE;
        }

        else if (camera.getCenter().x() + halfDimension > curMaxX) {
            float newMaxX = camera.getCenter().x() + halfDimension;
            newMaxX = newMaxX - (newMaxX % Block.SIZE) + Block.SIZE;
            terrain.createInRange((int) curMaxX, (int) (newMaxX));
            tree.treesGenerator((int) curMaxX, (int) (newMaxX));
            curMaxX = newMaxX;
            curMinX = curMaxX - (2 * halfDimension) - Block.SIZE;
        }
    }

    private void removeObjects() {
        for (GameObject gameObject : gameObjects) {
            String tag = gameObject.getTag();

            if (tag.equals(PepseGameManager.TERRAIN_TAG)) {
                if (gameObject.getCenter().x() > curMaxX || gameObject.getCenter().x() < curMinX) {
                    gameObjects.removeGameObject(gameObject, PepseGameManager.TERRAIN_LAYER);
                    gameObjects.removeGameObject(gameObject, PepseGameManager.TERRAIN_LAYER + 1);
                }
            }

            if (tag.equals(PepseGameManager.TREE_TAG)) {
                if (gameObject.getCenter().x() > curMaxX || gameObject.getCenter().x() < curMinX) {
                    gameObjects.removeGameObject(gameObject, PepseGameManager.TREE_LAYER);
                }
            }

            if (tag.equals(PepseGameManager.LEAF_TAG)) {
                if (gameObject.getCenter().x() > curMaxX + (Block.SIZE * 3) ||
                        gameObject.getCenter().x() < curMinX  - (Block.SIZE * 3)) {
                    gameObjects.removeGameObject(gameObject, PepseGameManager.TREE_LAYER + 1);
                }
            }
        }
    }
}
