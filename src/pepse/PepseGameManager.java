package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;

public class PepseGameManager extends GameManager {
//    public PepseGameManager(String windowTitle, Vector2 windowDimensions) {
//        super(windowTitle, windowDimensions);
//    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        //initialize sky
        Sky.create(gameObjects(), windowController.getWindowDimensions(), 0);

        //initialize ground
        Terrain terrain = new Terrain(gameObjects(),1,windowController.getWindowDimensions(),3);

        Night.create(gameObjects(), Layer.FOREGROUND, windowController.getWindowDimensions(), 30);
    }

    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
