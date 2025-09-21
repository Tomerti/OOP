package pepse.world;

import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.constants.Constants;
import pepse.util.ColorSupplier;
import pepse.util.NoiseGenerator;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * Terrain class, responsible to add all required Blocks (ground) for the game.
 * Allows other objects to know the height of the ground in specific X coordinate.
 */
public class Terrain {
    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final String TAG = "ground";
    private static final int BLOCK_SIZE = 30;
    private static final int TERRAIN_DEPTH = 20;
    private static final float NOISE_FACTOR = BLOCK_SIZE * 7;
    private final NoiseGenerator noiseGenerator;
    private int groundHeightAtX0;


    /**
     * Terrain constructor.
     * @param windowDimensions Contains the dimensions of the game window.
     * @param seed Contains the seed for randomizer, Optional.
     */
    public Terrain(Vector2 windowDimensions, int seed){
        this.groundHeightAtX0 = (int) (windowDimensions.y() * Constants.TWO_THIRDS);
        this.noiseGenerator = new NoiseGenerator(new Random().nextGaussian(),groundHeightAtX0);
    }

    /**
     * Gets a specific X coordinate, returns the height of the ground for that coordinate.
     * @param x Contains the X coordinate to calculate its ground height.
     * @return The height of the ground for that X coordinate.
     */
    public float groundHeightAt(float x) {
        float noise = (float) noiseGenerator.noise(x, NOISE_FACTOR);
        return groundHeightAtX0 + noise;
    }

    /**
     * Gets minimal and maximal X coordinates, returns a list of blocks accordingly.
     * @param minX Contains minimal X coordinate to fill terrain with.
     * @param maxX Contains maximal X coordinate to fill terrain with.
     * @return List of blocks, it is guaranteed that when added
     * to the GameObjectCollection, continuous ground will be made.
     */
    public List<Block> createInRange(int minX, int maxX){
        List<Block> blocks= new ArrayList<>();
        int currentX = correctedX(minX);
        int newMax = correctedX(maxX);
        int currentY;
        while(currentX <= newMax){
            currentY = (int)Math.floor(groundHeightAt(currentX) / Block.SIZE);
            currentY *= BLOCK_SIZE;
            for(int i=0; i<TERRAIN_DEPTH;i++) {
                currentY += BLOCK_SIZE;
                Block block = new Block(Vector2.of(currentX, currentY),
                        new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                block.setTag(TAG);
                blocks.add(block);
            }
            currentX += BLOCK_SIZE;
        }
        return blocks;
    }

    private int correctedX(int number) {
        if (number % BLOCK_SIZE == 0) {
            return number;
        }
        if (number > 0) {
            return number - (number % BLOCK_SIZE);
        }
        return number - (BLOCK_SIZE + (number % BLOCK_SIZE));
    }
}
