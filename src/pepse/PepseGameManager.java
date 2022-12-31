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
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.trees.Tree;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;

import java.awt.*;


public class PepseGameManager extends GameManager {
    public static final String TERRAIN_TAG = "terrain";
    private static final float NIGHT_CYCLE_LENGTH = 10;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int TERRAIN_SEED = 20;

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
//        windowController.setTargetFramerate(20);
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        //initialize sky
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        camera();

        //initialize ground
        Terrain terrain = new Terrain(gameObjects(),
                Layer.STATIC_OBJECTS, windowController.getWindowDimensions(), TERRAIN_SEED);
//        terrain.createInRange(0, (int) (windowController.getWindowDimensions().x() - (windowController.getWindowDimensions().x() % Block.SIZE) + Block.SIZE));

        //initialize trees

        Tree tree = new Tree(gameObjects(), Layer.STATIC_OBJECTS + 2, terrain::groundHeightAt);
        tree.treesGenerator(0,(int) (windowController.getWindowDimensions().x() - (windowController.getWindowDimensions().x() % Block.SIZE) + Block.SIZE));

        Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);

        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND,
                windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), Layer.BACKGROUND + 10, sun, SUN_HALO_COLOR);

        Vector2 initialAvatarLocation =
                new Vector2(windowController.getWindowDimensions().mult(0.5f).x(),
                        terrain.groundHeightAt(
                                windowController.getWindowDimensions().mult(0.5f).x()) - 60);

        GameObject avatar = Avatar.create(gameObjects(), Layer.DEFAULT,
                initialAvatarLocation, inputListener, imageReader);


        setCamera(new Camera(avatar, windowController.getWindowDimensions().mult(0.5f).subtract(initialAvatarLocation), windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

        new InfiniteWorldGenerator(gameObjects(), windowController.getWindowDimensions(),
                50, terrain, tree, avatar, camera());

    }

    public static void main(String[] args){
        new PepseGameManager().run();
    }
}
