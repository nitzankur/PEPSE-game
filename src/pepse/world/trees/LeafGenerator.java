package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;

public class LeafGenerator {
    private static final float FADEOUT_TIME = 2f;
    private final GameObjectCollection gameObjects;
    private final Random random;
    private final int treeLayer;

    private Color BASE_LEAF_COLOR = new Color(50,200,30);

    public LeafGenerator(GameObjectCollection gameObjects, int treeLayer){

        this.gameObjects = gameObjects;
        this.random = new Random(20);
        this.treeLayer = treeLayer;
    }

    public void leafGenerator(int x,float y){
        gameObjects.layers().shouldLayersCollide(treeLayer, Layer.BACKGROUND,true);
        for (int i = x- Block.SIZE * 3; i < x+Block.SIZE *3 ; i++) {
            for (float j = y; j > y-Block.SIZE*9 ; j-=Block.SIZE) {
                int rand = random.nextInt(17);
                if(rand==1){
                    RectangleRenderable leafRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
                    Leaf leaf = (new Leaf(new Vector2(i,j),new Vector2(Block.SIZE,Block.SIZE)
                            ,leafRenderer));
                    gameObjects.addGameObject(leaf,treeLayer);

                    Consumer<Float> setLeafAngle =
                            (angle) -> leaf.renderer().setRenderableAngle(angle);
                    rand = random.nextInt(200);
                    new ScheduledTask(leaf,rand,false,() -> new Transition<Float>(leaf, setLeafAngle,
                            0f, 90f, Transition.CUBIC_INTERPOLATOR_FLOAT,
                            10f, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null));


                    rand = random.nextInt(200);
                    new ScheduledTask(leaf, (float) rand,false,()-> new Transition<Vector2>(leaf,
                            leaf::setDimensions, leaf.getDimensions(), leaf.getDimensions().mult(1.25f),
                            Transition.CUBIC_INTERPOLATOR_VECTOR,
                            12f, Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                            null));

                    int lifeTime = random.nextInt(50);
                    Vector2 leafPos = leaf.getCenter();
                    Runnable newBorn = ()->new Leaf(leafPos,new Vector2(Block.SIZE,Block.SIZE),leafRenderer);
                    Runnable leafFadeOut = ()-> leaf.renderer().fadeOut(FADEOUT_TIME,newBorn);
                    Runnable leafVelY = ()-> leaf.transform().setVelocityY(100);
                    Consumer<Float> leafVelX = (velX)-> leaf.transform().setVelocityX(velX);
//                    Transition<Float> horizontalMove = new Transition<Float>(leaf,leafVelX,-100f,
//                            100f,Transition.LINEAR_INTERPOLATOR_FLOAT,4,
//                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null);
                    new ScheduledTask(leaf,lifeTime,false,()->new Transition<Float>(leaf,leafVelX,-100f,
                            100f,Transition.LINEAR_INTERPOLATOR_FLOAT,4,
                            Transition.TransitionType.TRANSITION_BACK_AND_FORTH,null));
                    new ScheduledTask(leaf,lifeTime+2,false,leafFadeOut);
                    new ScheduledTask(leaf,lifeTime,false,leafVelY);


                }
            }
        }

    }
}
