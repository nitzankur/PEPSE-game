package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
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
    private static final float NIGHT_CYCLE_LENGTH = 10;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int TERRAIN_SEED = 20;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        //initialize sky
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);

        //initialize ground
        Terrain terrain = new Terrain(gameObjects(),
                Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), TERRAIN_SEED);
        terrain.createInRange(0, (int) windowController.getWindowDimensions().x());

        //initialize trees
        Tree tree = new Tree(gameObjects(),Layer.STATIC_OBJECTS,terrain);
        tree.treesGenerator(0, (int) windowController.getWindowDimensions().x());

        Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);

        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND,
                windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), Layer.BACKGROUND + 10, sun, SUN_HALO_COLOR);

        GameObject avatar = Avatar.create(gameObjects(), Layer.DEFAULT,
                windowController.getWindowDimensions().mult(0.5f), inputListener, imageReader);

        setCamera(new Camera(avatar, Vector2.ZERO, windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));
    }

    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
