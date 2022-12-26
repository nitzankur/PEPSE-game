package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    private static final float HALO_FACTOR = 2;
    private static final String SUN_HALO_TAG = "sunHalo";
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    GameObject sun, Color color) {
        GameObject sunHalo = new GameObject(Vector2.ZERO, sun.getDimensions().mult(HALO_FACTOR),
                new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo, layer);
        sunHalo.setTag(SUN_HALO_TAG);

        sunHalo.addComponent(deltaTime -> sunHalo.setCenter(sun.getCenter()));

        return sunHalo;
    }
}
