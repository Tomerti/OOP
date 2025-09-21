package bricker.brick_strategies;

import bricker.gameobjects.Heart;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * SpecialHeartStrategy class
 */
public class SpecialHeartStrategy extends BasicCollisionStrategy {
    private Renderable renderable;
    private Vector2 dimensions;
    private Vector2 velocity;
    private Counter lives;
    private int max_life;

    /**
     * constructor for heart strategy
     * @param objectCollection objectCollection
     * @param renderable image of heart
     * @param dimensions dimensions of heart
     * @param velocity velocity of heart
     * @param bricks bricks alive
     * @param lives lives left
     * @param max_life max lives in game
     */
    public SpecialHeartStrategy(GameObjectCollection objectCollection,
                                Renderable renderable, Vector2 dimensions, Vector2 velocity,
                                Counter bricks, Counter lives, int max_life) {
        super(objectCollection, bricks);
        this.renderable = renderable;
        this.dimensions = dimensions;
        this.velocity = velocity;
        this.lives = lives;
        this.max_life = max_life;
    }

    /**
     * overrides collision to create heart.
     * @param thisObj this object, brick
     * @param otherObj other object collided.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        Heart heart = new Heart(Vector2.ZERO,dimensions,renderable,lives,velocity,objectCollection,max_life);
        heart.setCenter(thisObj.getCenter());
        objectCollection.addGameObject(heart);
    }
}