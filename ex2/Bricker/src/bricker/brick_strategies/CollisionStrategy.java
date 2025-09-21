package bricker.brick_strategies;

import danogl.GameObject;

/**
 * CollisionStrategy inteface.
 */
public interface CollisionStrategy {

    /**
     * on Collision function.
     * @param thisObj this object.
     * @param otherObj other object.
     */
    public void onCollision(GameObject thisObj, GameObject otherObj);
}
