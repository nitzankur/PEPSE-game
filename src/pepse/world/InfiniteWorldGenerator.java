package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.trees.Tree;

public class InfiniteWorldGenerator extends GameObject {
    private GameObjectCollection gameObjects;
    //    private final Camera camera;
    private float curMinX;
    private float curMaxX;
    private float offsetX;
    private float halfDimension;
    private Terrain terrain;
    private Tree tree;
    private Camera avatar;

    public InfiniteWorldGenerator(GameObjectCollection gameObjects, Vector2 windowDimensions, float offsetX,
                                  Terrain terrain, Tree tree, GameObject avatar, Camera camera) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.offsetX = offsetX;
        this.halfDimension = windowDimensions.mult(0.5f).x();
        this.curMinX = 0;
        this.curMaxX = windowDimensions.x() - (windowDimensions.x() % Block.SIZE) + Block.SIZE;
        this.terrain = terrain;
        this.tree = tree;
//        this.avatar = avatar;
        this.avatar = camera;
        this.gameObjects = gameObjects;
        gameObjects.addGameObject(this);
        this.terrain.createInRange((int) curMinX, (int) curMaxX);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

//        System.out.println("avatar: " + (avatar.getCenter().x() - halfDimension));
//        System.out.println("curMinX: " + curMinX);

        if (avatar.getCenter().x() - halfDimension < curMinX) {
            float newMinX = avatar.getCenter().x() - halfDimension;
            newMinX = newMinX < 0 ? newMinX - (avatar.getCenter().x() % Block.SIZE) :
                    newMinX + (avatar.getCenter().x() % Block.SIZE);
            terrain.createInRange((int) newMinX, (int) (curMinX));
            tree.treesGenerator((int) newMinX, (int) (curMinX));
            curMinX = newMinX;
            curMaxX = curMinX + (2 * halfDimension);
        }

        if (avatar.getCenter().x() + halfDimension > curMaxX) {
            float newMaxX = avatar.getCenter().x() + halfDimension;
            newMaxX = newMaxX < 0 ? newMaxX + (avatar.getCenter().x() % Block.SIZE) :
                    newMaxX - (avatar.getCenter().x() % Block.SIZE);
            terrain.createInRange((int) curMaxX, (int) (newMaxX));
            tree.treesGenerator((int) curMaxX, (int) (newMaxX));
            curMaxX = newMaxX;
            curMinX = curMaxX - (2 * halfDimension);
        }
        removeObjects();
    }

    private void removeObjects() {
        for (GameObject gameObject : gameObjects) {
            String tag = gameObject.getTag();

            if (tag.equals(PepseGameManager.TERRAIN_TAG)) {
                if (gameObject.getCenter().x() > curMaxX || gameObject.getCenter().x() < curMinX) {
                    gameObjects.removeGameObject(gameObject, Layer.STATIC_OBJECTS);
                    gameObjects.removeGameObject(gameObject, Layer.STATIC_OBJECTS + 1);
                }
            }
        }
    }
}
