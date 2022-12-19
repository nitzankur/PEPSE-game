package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.util.Vector2;
import pepse.util.NoiseGenerator;

import java.util.Random;

public class Terrain {

    private final GameObjectCollection gameObjects;
    private final int groundLayer;

    private float groundHeightAtX0;
    private static final float HEIGHT_PARAMETER =(float) 1/3;
    private int seed;


    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 WindowDimensions, int seed){

        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        groundHeightAtX0 = WindowDimensions.y() * HEIGHT_PARAMETER;
        this.seed = seed;
    }

    public float groundHeightAt(float x){
        NoiseGenerator noiseGenerator = new NoiseGenerator(seed);
        return groundHeightAtX0+(float) noiseGenerator.noise(x);
    }

}
