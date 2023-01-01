package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * sky in pepse game
 */
public class Sky {
    private static final Color BASIC_COLOR_SKY = Color.decode("#80C6E5");
    private static final String SKY_TAG = "sky";

    /**
     * creates sky
     * @param gameObjects pepse game objects
     * @param windowDimensions pepse window dimensions
     * @param skyLayer layer of sky in pepse
     * @return game object of sky
     */
    public static GameObject create(GameObjectCollection gameObjects, Vector2 windowDimensions,
                                    int skyLayer) {
        GameObject sky = new GameObject(Vector2.ZERO, windowDimensions,
                new RectangleRenderable(BASIC_COLOR_SKY));
        sky.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sky, skyLayer);
        sky.setTag(SKY_TAG);
        return sky;
    }


}
