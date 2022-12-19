package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import pepse.world.Terrain;

import java.awt.*;
import java.util.Random;

public class Tree {
    private GameObjectCollection gameObjects;
    private Random random;
    private int treeLayer;
    private Terrain terrain;
    private Color BASE_TRUNK_COLOR = new Color(100,50,20);
    private Color BASE_LEAF_COLOR = new Color(50,200,30);

    public Tree(GameObjectCollection gameObjects, int treeLayer, Terrain terrain){

        this.gameObjects = gameObjects;
        this.random = new Random(20);
        this.treeLayer = treeLayer;
        this.terrain = terrain;
    }

    public void treesGenerator(int minX,int minY){
        for (int i = minX; i < minY ; i+= Block.SIZE) {
            int rand = random.nextInt(7);
            if(rand==1){
            RectangleRenderable trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
            float y = terrain.groundHeightAt(i);
            gameObjects.addGameObject(new Block(new Vector2(i,y),new Vector2(Block.SIZE,Block.SIZE)
                    ,trunkRenderer),treeLayer);
            for (float j =y-Block.SIZE ; j >y-Block.SIZE * 10 ; j-=Block.SIZE) {
                trunkRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_TRUNK_COLOR));
                gameObjects.addGameObject(new GameObject(new Vector2(i,j),new Vector2(Block.SIZE,Block.SIZE)
                        ,trunkRenderer),treeLayer);
            }
            leafGenerator(i,y-Block.SIZE * 7);
          }
        }
    }

    private void leafGenerator(int x,float y){
        for (int i = x-Block.SIZE * 3; i < x+Block.SIZE *3 ; i++) {
           int rand = random.nextInt(20);
           if(rand==1){
               RectangleRenderable leafRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
               gameObjects.addGameObject(new GameObject(new Vector2(i,y),new Vector2(Block.SIZE,Block.SIZE)
                       ,leafRenderer),treeLayer);
           }
            for (float j = y-Block.SIZE; j > y-Block.SIZE*8 ; j-=Block.SIZE) {
                 rand = random.nextInt(20);
                 if(rand==1){
                     RectangleRenderable leafRenderer = new RectangleRenderable(ColorSupplier.approximateColor(BASE_LEAF_COLOR));
                     gameObjects.addGameObject(new GameObject(new Vector2(i,j),new Vector2(Block.SIZE,Block.SIZE)
                             ,leafRenderer),treeLayer);
                 }
            }
        }

    }
}
