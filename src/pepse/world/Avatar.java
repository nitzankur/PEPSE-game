package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Avatar extends GameObject {
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 500;
    private static final float INITIAL_ENERGY = 100;
    private static final float ENERGY_VALUE = 0.5f;
    private static final String IDLE_ANIMATION_NAME = "assets/link_idle_";
    private static final String WALKING_ANIMATION_NAME = "assets/link_walking_";
    private static final String FLYING_ANIMATION_NAME = "assets/link_flying_up_";
    private static final float IDLE_TIME_BETWEEN = 0.2f;
    private static final float WALKING_TIME_BETWEEN = 0.1f;
    private static final float FLYING_TIME_BETWEEN = 0.1f;
    private static final String PNG_FILE_SUFFIX = ".png";
    private static final float AVATAR_SIZE = 50;
    private static final String AVATAR_TAG = "avatar";
    private final UserInputListener inputListener;
    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable walkAnimation;
    private final AnimationRenderable flyAnimation;
    private float currentEnergy;
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                  UserInputListener inputListener, ImageReader imageReader) {
        super(topLeftCorner, dimensions, renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        currentEnergy = INITIAL_ENERGY;

        idleAnimation = (AnimationRenderable) renderable;

        String[] walkAnimationPaths = new String[10];
        for (int i = 0; i < 10; i++) {
            walkAnimationPaths[i] = WALKING_ANIMATION_NAME + (i + 1) + PNG_FILE_SUFFIX;
        }

        walkAnimation = new AnimationRenderable(walkAnimationPaths, imageReader,
                true, WALKING_TIME_BETWEEN);

        String[] flyAnimationPaths = new String[2];
        for (int i = 0; i < 2; i++) {
            flyAnimationPaths[i] = FLYING_ANIMATION_NAME + (i + 1) + PNG_FILE_SUFFIX;
        }

        flyAnimation = new AnimationRenderable(flyAnimationPaths, imageReader,
                true, FLYING_TIME_BETWEEN);

    }

    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 topLeftCorner, UserInputListener inputListener,
                                    ImageReader imageReader) {
        String[] idleAnimationPaths = new String[3];
        for (int i = 0; i < 3; i++) {
            idleAnimationPaths[i] = IDLE_ANIMATION_NAME + (i + 1) + PNG_FILE_SUFFIX;
        }

        Renderable idleAnimation = new AnimationRenderable(idleAnimationPaths, imageReader,
                true, IDLE_TIME_BETWEEN);

        Avatar avatar = new Avatar(topLeftCorner, Vector2.ONES.mult(AVATAR_SIZE),
                idleAnimation, inputListener, imageReader);
        gameObjects.addGameObject(avatar, layer);
        avatar.setTag(AVATAR_TAG);

        return avatar;
    }

    private boolean fly() {
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT)
                && currentEnergy > 0) {
            transform().setVelocityY(VELOCITY_Y);
            renderer().setRenderable(flyAnimation);
            currentEnergy -= ENERGY_VALUE;
            return true;
        }
        return false;
    }

    private boolean jump() {
        if(inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y);
            return true;
        }
        return false;
    }
    private boolean moveSideways() {
        float xVel = 0;
        boolean moved = false;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setRenderable(walkAnimation);
            renderer().setIsFlippedHorizontally(false);
            moved = true;
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            renderer().setRenderable(walkAnimation);
            renderer().setIsFlippedHorizontally(true);
            moved = true;
        }
        transform().setVelocityX(xVel);
        return moved;
    }

    private void updateEnergy() {
        if(getVelocity().y() == 0 && currentEnergy < INITIAL_ENERGY) {
            currentEnergy += ENERGY_VALUE;
        }
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        boolean walked = moveSideways();
        boolean flew = fly();
        boolean jumped = jump();
        updateEnergy();

        if (!walked && !flew && !jumped) {
            renderer().setRenderable(idleAnimation);
        }
    }
}
