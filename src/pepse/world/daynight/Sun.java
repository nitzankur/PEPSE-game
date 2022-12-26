package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;
import java.util.function.Consumer;

public class Sun {
    private static final Float RADIUS_FROM_CENTER_X = 400f;
    private static final Float RADIUS_FROM_CENTER_Y = 600f;
    private static final Vector2 SUN_DIMENSIONS = new Vector2(200, 200);
    private static final String SUN_TAG = "sun";

    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength) {
        GameObject sun = new GameObject(Vector2.ZERO, SUN_DIMENSIONS,
                new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun, layer);
        sun.setTag(SUN_TAG);

        Consumer<Float> setSunCenter =
                (angleInSky) -> sun.setCenter(calcSunPosition(windowDimensions, angleInSky));

        new Transition<Float>(sun, setSunCenter,
                0f, (float) Math.PI * 2, Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength * 2, Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }

    public static Vector2 calcSunPosition(Vector2 windowDimensions, float angleInSky) {
        Vector2 center = windowDimensions.mult(0.5f);
        float posX = ((float) Math.sin(-angleInSky) * RADIUS_FROM_CENTER_Y) + center.x();
        float posY = ((float) -Math.cos(angleInSky) * RADIUS_FROM_CENTER_X) + center.y();
        return new Vector2(posX, posY);
    }
}
