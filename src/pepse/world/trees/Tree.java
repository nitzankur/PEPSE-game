package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Tree {
    private final Function<Integer,Float> groundHeightAt;
    private GameObjectCollection gameObjects;
    private Random random;
    private int treeLayer;
    private Color BASE_TRUNK_COLOR = new Color(100,50,20);


    public Tree(GameObjectCollection gameObjects, int treeLayer, Function<Integer,Float> groundHeightAt ){

        this.gameObjects = gameObjects;
        this.random = new Random(20);
        this.treeLayer = treeLayer;
        this.groundHeightAt = groundHeightAt;
    }

    public void treesGenerator(int minX,int minY){
        for (int i = minX; i < minY ; i+= Block.SIZE) {
            int rand = random.nextInt(7);
            if(rand==1){
            RectangleRenderable trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
            float y = this.groundHeightAt.apply(i);
            gameObjects.addGameObject(new Block(new Vector2(i,y),new Vector2(Block.SIZE,Block.SIZE)
                    ,trunkRenderer),treeLayer);
            LeafGenerator leafGenerator =new LeafGenerator(gameObjects,treeLayer + 1);
            for (float j =y-Block.SIZE ; j >y-Block.SIZE * 10 ; j-=Block.SIZE) {
                trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
                gameObjects.addGameObject(new GameObject(new Vector2(i,j),new Vector2(Block.SIZE,Block.SIZE)
                        ,trunkRenderer),treeLayer);
            }
            leafGenerator.leafGenerator(i,y-Block.SIZE * 7);
          }
        }
    }

}
