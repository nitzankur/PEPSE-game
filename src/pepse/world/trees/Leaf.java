package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;
import java.util.function.Consumer;

public class Leaf extends GameObject {

    private static final float FADEOUT_TIME = 5f;
    private static final float LEAF_FALLING_VEL = 40;
    private static final float HORIZONTAL_FALL = 20;
    private static final float ANGLE_RANGE = 10f;
    private static final float MULT_FACTOR = 1.1f;
    private static final int RANDOM_MOVEMENT = 30;
    private static final int RANDOM_FALLING = 50;
    private final Random random;
    private final Vector2 leafPos;
    private Transition<Float> horizontalTransition;
    private Runnable deadManagement;



    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     *
     */
    public Leaf(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Random random) {
        super(topLeftCorner, dimensions, renderable);
        this.random = random;
        this.leafPos = this.getCenter();
        //start cycle with leaf movement
        new ScheduledTask(this, random.nextInt(RANDOM_MOVEMENT),
                false, this::leafMovement);
        //set new life
        this.newLife();
    }


    /**
     * set leaf movement when the leaf on the tree
     */
    private void leafMovement() {
        Consumer<Float> setLeafAngle =
                (angle) -> this.renderer().setRenderableAngle(angle);
        new Transition<Float>(this,
                setLeafAngle,
                -ANGLE_RANGE, ANGLE_RANGE, Transition.CUBIC_INTERPOLATOR_FLOAT,
                2f, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);
        leafSetDimensions();
    }

    /**
     * set leaf dimensions when the leaf on the tree
     */
    private void leafSetDimensions() {
        new Transition<Vector2>(this,
                this::setDimensions, this.getDimensions(), this.getDimensions().mult(MULT_FACTOR),
                Transition.CUBIC_INTERPOLATOR_VECTOR,
                5, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);

    }

    /**
     * Responsible for the process of leaf fall
     *
     */
    private void fallingLeaf() {
        //scheduled the leaf dead
        this.deadManagement =
                () -> new ScheduledTask(
                        this, random.nextInt(5), false, this::newLife);
        this.renderer().fadeOut(FADEOUT_TIME, this.deadManagement);
        //set velocity in y and x for thr falling
        Consumer<Float> leafVelX = (velX) -> this.transform().setVelocityX(velX);
        this.transform().setVelocityY(LEAF_FALLING_VEL);
        //save the transition for remove him when collision occur.
        this.horizontalTransition =
                new Transition<Float>(this, leafVelX, -HORIZONTAL_FALL,
                HORIZONTAL_FALL, Transition.LINEAR_INTERPOLATOR_FLOAT, 1,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * after the leaf falling set him again in the same position and same velocity
     */
    private void newLife() {
        this.setVelocity(Vector2.ZERO);
        this.setCenter(leafPos);
        this.renderer().setOpaqueness(1);
        new ScheduledTask(this,
                random.nextInt(RANDOM_FALLING), false, this::fallingLeaf);
    }

    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        // set velocity to zero
        this.transform().setVelocity(Vector2.ZERO);
        this.removeComponent(this.horizontalTransition);

    }

}
