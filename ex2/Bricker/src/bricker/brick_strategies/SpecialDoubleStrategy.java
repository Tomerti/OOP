package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.util.Counter;

import java.util.Random;

/**
 * class for special double ups.
 */
public class SpecialDoubleStrategy extends BasicCollisionStrategy{
    private static final int MAXIMUM_STRATEGIES = 3;
    private int actual_strategies = 0;
    private CollisionStrategy strategies[];

    /**
     * constructor
     * @param objectCollection objectCollection
     * @param randomStrategy randomStrategy (factory)
     * @param bricks bricks alive
     */
    public SpecialDoubleStrategy(GameObjectCollection objectCollection, RandomStrategy randomStrategy,
                                 Counter bricks) {
        super(objectCollection, bricks);
        Random random = new Random();
        strategies = new CollisionStrategy[MAXIMUM_STRATEGIES * 2];
        for(int i=0; i<strategies.length; i++){
            strategies[i] = randomStrategy.getSpecialNoDouble();
        }
        for(int i=0;i<MAXIMUM_STRATEGIES;i++){
            int x = random.nextInt(5);
            if(x == 0){
                actual_strategies++;
            }
            actual_strategies++;
        }
    }

    /**
     * overrides to call all strategies in array.
     * @param thisObj this object.
     * @param otherObj other object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        for(int i=0;i<actual_strategies;i++){
            strategies[i].onCollision(thisObj,otherObj);
        }
    }
}