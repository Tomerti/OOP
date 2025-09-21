package bricker.brick_strategies;

import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * specialpuckstrategy class.
 */
public class SpecialPuckStrategy extends BasicCollisionStrategy{
    private final Renderable renderable;
    private final Sound collisionSound;
    private Vector2 dimensions;
    private Vector2 velocity;
    private Puck[] pucks;

    /**
     *
     * @param objectCollection objectCollection
     * @param renderable image of puck.
     * @param collisionSound sound of puck on collision
     * @param dimensions dimensions of puck
     * @param velocity velocity of puck
     * @param bricks bricks alive
     * @param amount amount of pucks to make
     */
    public SpecialPuckStrategy(GameObjectCollection objectCollection,
                               Renderable renderable, Sound collisionSound, Vector2 dimensions,
                               Vector2 velocity, Counter bricks, int amount) {
        super(objectCollection, bricks);
        this.renderable = renderable;
        this.collisionSound = collisionSound;
        this.dimensions = dimensions;
        this.velocity = velocity;
        this.pucks = new Puck[amount];
    }

    /**
     * overrides on collision to create pucks
     * @param thisObj this object.
     * @param otherObj other object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        for(int i=0; i< pucks.length; i++){
            pucks[i] = new Puck(thisObj.getTopLeftCorner(),dimensions,renderable,collisionSound, velocity);
            pucks[i].setCenter(thisObj.getCenter());
            pucks[i].setTag("Puck");
            super.objectCollection.addGameObject(pucks[i]);
        }
    }
}