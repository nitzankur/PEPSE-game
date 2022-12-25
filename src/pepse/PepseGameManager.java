package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.trees.Tree;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;


public class PepseGameManager extends GameManager {
//    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
//        super(windowTitle, windowDimensions);
//    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        //initialize sky
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        //initialize ground
        Terrain terrain = new Terrain(gameObjects(),
                Layer.STATIC_OBJECTS,windowController.getWindowDimensions(),20);
        terrain.createInRange(0,(int) windowController.getWindowDimensions().x());
        //initialize trees
        Tree tree = new Tree(gameObjects(),Layer.STATIC_OBJECTS,terrain);
        tree.treesGenerator(0,(int) windowController.getWindowDimensions().x());

        Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), 10);
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND, windowController.getWindowDimensions(), 10);
        SunHalo.create(gameObjects(), Layer.BACKGROUND + 10, sun, new Color(255, 255, 0, 20));
        Avatar.create(gameObjects(), Layer.DEFAULT,
                windowController.getWindowDimensions().mult(0.5f), inputListener, imageReader);
    }

    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
