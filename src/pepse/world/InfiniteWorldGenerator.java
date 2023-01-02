package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.trees.Tree;

/**
 * create an infinite world
 */
public class InfiniteWorldGenerator extends GameObject {
    private static final int BLOCK_OFFSET_FACTOR = 3;
    private final GameObjectCollection gameObjects;
    private float curMinX;
    private float curMaxX;
    private final float halfDimension;
    private final Terrain terrain;
    private final Tree tree;
    private final Camera camera;

    /**
     * infinite world constructor
     * @param gameObjects game objects
     * @param windowDimensions pepse dimensions
     * @param terrain Terrain object to create terrain in
     * @param tree Tree object to create trees and leaves in
     * @param camera Camera object of pepse game
     */
    public InfiniteWorldGenerator(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                  Terrain terrain, Tree tree, Camera camera) {
        super(Vector2.ZERO, Vector2.ZERO,  null);
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

    /**
     * updates the world, adding objects in range and removing objects out of range
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        createObjects();
        removeObjects();
    }

    /**
     * create objects in range (terrain, trees and leaves)
     */
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

    /**
     * remove objects out of range (terrain, trees and leaves)
     */
    private void removeObjects() {
        for (GameObject gameObject : gameObjects) {
            String tag = gameObject.getTag();

            if (PepseGameManager.TAG_LAYER_MAP.containsKey(tag)) {
                if (gameObject.getCenter().x() > curMaxX + (Block.SIZE * 3) ||
                        gameObject.getCenter().x() < curMinX  - (Block.SIZE * 3))
                    gameObjects.removeGameObject(gameObject, PepseGameManager.TAG_LAYER_MAP.get(tag));
            }
        }
    }
}
