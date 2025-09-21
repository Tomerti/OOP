package pepse.world.trees;

import danogl.GameObject;
import danogl.util.Vector2;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Flora class, responsible to add all required trees (including leaves and fruits) to the game.
 * passes Consumer and Supplier to relevant clients (Stump, Fruit, Leaf) objects, to act in the desired
 * way (change colors, add lives to avatar, rotate).
 */
public class Flora {
    private static final int BLOCK_SIZE = 30;
    private static final int MAX_LEAVES_PER_AXIS = 2;
    private static final double TREE_PROBABILITY = 0.2;
    private static final double LEAF_PROBABILITY = 0.5;
    private static final double FRUIT_PROBABILITY = 0.2;
    private final Function<Float, Float> groundHeightAt;
    private final Consumer<Float> addEnergy;
    private final Supplier<Boolean> hasJumped;
    private final Random random;

    /**
     * Flora constructor.
     * @param groundHeightAt Contains groundHeightAt function.
     * @param addEnergy Contains add energy to avatar.
     * @param hasJumped Contains has jumped method.
     */
    public Flora(Function<Float, Float> groundHeightAt, Consumer<Float> addEnergy,
                 Supplier<Boolean> hasJumped){
        this.addEnergy = addEnergy;
        this.hasJumped = hasJumped;
        this.random = new Random();
        this.groundHeightAt = groundHeightAt;
    }


    /**
     * Gets minimal and maximal X coordinates, returns a list of blocks accordingly.
     * @param minX Contains minimal X coordinate to fill terrain with.
     * @param maxX Contains maximal X coordinate to fill terrain with.
     * @return list of game objects.
     */
    public List<GameObject> createInRange(int minX, int maxX){
        List<GameObject> stumps = createStumps(minX, maxX);
        List<GameObject> total = new ArrayList<>(stumps);
        for(GameObject stump: stumps){
            total.addAll(createTree(stump));
        }
        return total;
    }

    private List<GameObject> createStumps(int minX, int maxX){
        List<GameObject> stumps= new ArrayList<>();
        float currentX = correctedX(minX);
        float newMax = correctedX(maxX);
        while(currentX <= newMax)
        {
            if(shouldPlant(TREE_PROBABILITY)){
                float currentY = groundHeightAt.apply(currentX);
                Stump stump = new Stump(Vector2.of(currentX, currentY),hasJumped);
                stump.setTopLeftCorner(Vector2.of(stump.getTopLeftCorner().x(),
                        stump.getTopLeftCorner().y() - stump.getDimensions().y() + BLOCK_SIZE));
                stumps.add(stump);
            }
            currentX+=BLOCK_SIZE;
        }
        return stumps;
    }

    private List<GameObject> createTree(GameObject stump) {
        List<GameObject> tree= new ArrayList<>();
        float currentX;
        float currentY;
        for(int i = - MAX_LEAVES_PER_AXIS; i < MAX_LEAVES_PER_AXIS ; i++){
            for(int j = - MAX_LEAVES_PER_AXIS; j < MAX_LEAVES_PER_AXIS ; j++){
                if(shouldPlant(LEAF_PROBABILITY)){
                    currentX = stump.getTopLeftCorner().x() + i * BLOCK_SIZE;
                    currentY = stump.getTopLeftCorner().y() + j * BLOCK_SIZE;
                    if(groundHeightAt.apply(currentX) >= currentY) {
                        tree.add(new Leaf(Vector2.of(currentX,
                                currentY),hasJumped));
                    }
                }
                if(shouldPlant(FRUIT_PROBABILITY) && i != 0){
                    currentX = stump.getTopLeftCorner().x() + i * BLOCK_SIZE;
                    currentY = stump.getTopLeftCorner().y() + j * BLOCK_SIZE;
                    if(groundHeightAt.apply(currentX) >= currentY) {
                        tree.add(new Fruit(Vector2.of(currentX,
                                currentY), addEnergy, hasJumped));
                    }
                }
            }
        }
        return tree;
    }

    private boolean shouldPlant(double probability){
        double chance = random.nextDouble();
        return chance < probability;
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
