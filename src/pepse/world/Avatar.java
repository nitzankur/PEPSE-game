package pepse.world;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Avatar in pepse game
 */
public class Avatar extends GameObject {
    private static final float VELOCITY_X = 300;
    private static final float VELOCITY_Y = -300;
    private static final float GRAVITY = 300;
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

    /**
     * constructs a game object of avatar, with animations
     * @param topLeftCorner avatar initial position
     * @param dimensions avatar dimensions
     * @param renderable avatar renderable
     * @param inputListener listener of keyboard
     * @param imageReader avatar image reader
     */
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

    /**
     * create an avatar in the game
     * @param gameObjects pepse game objects
     * @param layer avatar layer
     * @param topLeftCorner avatar initial position
     * @param inputListener listener of keyboard
     * @param imageReader avatar image reader
     * @return game object of avatar
     */
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

    /**
     * check if avatar needs to fly, and do so
     * @return true if avatar flew
     */
    private boolean fly() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) &&
                inputListener.isKeyPressed(KeyEvent.VK_SHIFT)
                && currentEnergy > 0) {
            transform().setVelocityY(VELOCITY_Y);
            renderer().setRenderable(flyAnimation);
            currentEnergy -= ENERGY_VALUE;
            return true;
        }
        return false;
    }

    /**
     * check if avatar needs to jump, and do so
     * @return true if avatar jumped
     */
    private boolean jump() {
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0) {
            transform().setVelocityY(VELOCITY_Y);
            return true;
        }
        return false;
    }

    /**
     * check if avatar needs to move sideways, and do so
     * @return true if avatar moved
     */
    private boolean moveSideways() {
        float xVel = 0;
        boolean moved = false;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            xVel -= VELOCITY_X;
            renderer().setRenderable(walkAnimation);
            renderer().setIsFlippedHorizontally(false);
            moved = true;
        }
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            xVel += VELOCITY_X;
            renderer().setRenderable(walkAnimation);
            renderer().setIsFlippedHorizontally(true);
            moved = true;
        }
        transform().setVelocityX(xVel);
        return moved;
    }

    /**
     * update energy if criteria met
     */
    private void updateEnergy() {
        if (getVelocity().y() == 0 && currentEnergy < INITIAL_ENERGY) {
            currentEnergy += ENERGY_VALUE;
        }
    }

    /**
     * update the avatar, check movement and update energy
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
        boolean walked = moveSideways();
        boolean flew = fly();
        boolean jumped = jump();
        updateEnergy();

        if (!walked && !flew && !jumped) {
            renderer().setRenderable(idleAnimation);
        }
    }
}
