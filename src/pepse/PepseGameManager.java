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
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;

import java.awt.*;
import java.util.HashMap;


public class PepseGameManager extends GameManager {
    public static final String TERRAIN_TAG = "terrain";
    public static final String BOTTOM_TERRAIN_TAG=  "bottom_terrain";
    public static final String TREE_TAG = "tree";
    public static final String LEAF_TAG = "leaf";
    public static final int TERRAIN_LAYER = Layer.STATIC_OBJECTS;
    public static final int BOTTOM_TERRAIN_LAYER = TERRAIN_LAYER + 1;
    public static final int TREE_LAYER = BOTTOM_TERRAIN_LAYER + 1;
    public static final int LEAF_LAYER = TREE_LAYER + 1;
    public static final HashMap<String, Integer> TAG_LAYER_MAP = new HashMap<String, Integer>();
    private static final float NIGHT_CYCLE_LENGTH = 10;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    private static final int TERRAIN_SEED = 20;
    private static final int SUN_ADDITIVE_LAYER = 10;
    private static final int AVATAR_ADDITIVE_LOCATION = 60;

    private static final float AVATAR_FACTOR_LOCATION = 0.5f;

    private void createTagLayerMap() {
        TAG_LAYER_MAP.put(TERRAIN_TAG, TERRAIN_LAYER);
        TAG_LAYER_MAP.put(BOTTOM_TERRAIN_TAG, BOTTOM_TERRAIN_LAYER);
        TAG_LAYER_MAP.put(TREE_TAG, TREE_LAYER);
        TAG_LAYER_MAP.put(LEAF_TAG, LEAF_LAYER);
    }

    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController) {
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        initializeDayNightAndSky(windowController);

        //initialize ground
        Terrain terrain = new Terrain(gameObjects(),
                TERRAIN_LAYER, windowController.getWindowDimensions(), TERRAIN_SEED);

        //initialize trees
        Tree tree = new Tree(gameObjects(), TREE_LAYER,
                terrain::groundHeightAt, TERRAIN_SEED);


        //initialize Infinite World
        new InfiniteWorldGenerator(gameObjects(), windowController.getWindowDimensions(),
                terrain, tree, camera());

        initializeAvatar(windowController,inputListener,imageReader,terrain);

    }


    /**
     *  initialize sky, night and sun
     */

    private void initializeDayNightAndSky(WindowController windowController){
        //initialize sky
        Sky.create(gameObjects(), windowController.getWindowDimensions(), Layer.BACKGROUND);
        //initialize night
        Night.create(gameObjects(), Layer.FOREGROUND,
                windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
        //initialize sun
        GameObject sun = Sun.create(gameObjects(), Layer.BACKGROUND,
                windowController.getWindowDimensions(), NIGHT_CYCLE_LENGTH);
        SunHalo.create(gameObjects(), Layer.BACKGROUND + SUN_ADDITIVE_LAYER, sun, SUN_HALO_COLOR);
    }

    /**
     * initialize avatar and set the camera to follow after him.
     */

    private void initializeAvatar(WindowController windowController,UserInputListener inputListener, ImageReader
                                  imageReader,Terrain terrain){
        //initialize avatar
        Vector2 initialAvatarLocation =
                new Vector2(windowController.getWindowDimensions().mult(AVATAR_FACTOR_LOCATION).x(),
                        terrain.groundHeightAt(
                                windowController.getWindowDimensions().mult(AVATAR_FACTOR_LOCATION).x()) -
                                AVATAR_ADDITIVE_LOCATION);

        GameObject avatar = Avatar.create(gameObjects(), Layer.DEFAULT,
                initialAvatarLocation, inputListener, imageReader);

        //initialize camera
        setCamera(new Camera(avatar,
                windowController.getWindowDimensions().mult(AVATAR_FACTOR_LOCATION).subtract(initialAvatarLocation),
                windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

        createTagLayerMap();

        // set avatar collide with tree trunks and bottom terrain layer
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT,
                PepseGameManager.TREE_LAYER, true);
        gameObjects().layers().shouldLayersCollide(Layer.DEFAULT,
                PepseGameManager.BOTTOM_TERRAIN_LAYER, true);
    }

    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}
