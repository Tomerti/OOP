package bricker.brick_strategies;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.util.Counter;

/**
 * class that implements CollisionStrategy
 * objectCollection will be passed to all other strategies that extend it.
 *
 */
public class BasicCollisionStrategy implements CollisionStrategy{
    /**
     * objectCollection will be passed to all other strategies that extend it.
     */
    protected GameObjectCollection objectCollection; // protected so we can send it to others.
    private Counter bricks; // bricks alive

    /**
     * constructor of basic collision strategy.
     * @param objectCollection objectCollection.
     * @param bricks bricks alive.
     */
    public BasicCollisionStrategy(GameObjectCollection objectCollection, Counter bricks){
        this.objectCollection = objectCollection;
        this.bricks = bricks;
    }

    /**
     * removes any static object from game.
     * @param thisObj this object.
     * @param otherObj other object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        if(objectCollection.removeGameObject(thisObj, Layer.STATIC_OBJECTS)){
            bricks.increaseBy(-1);
        }
    }
}
