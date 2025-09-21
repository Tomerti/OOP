package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Simple heart class.
 */
public class Heart extends GameObject {
    private final GameObjectCollection objectCollection;
    private final Counter lives;
    private final int max_life;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     *                      the GameObject will not be rendered.
     * @param lives lives left
     * @param velocity velocity of falling heart
     * @param objectCollection objectCollection.
     * @param max_life maximum lives allowed in game
     */
    public Heart(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Counter lives,
                 Vector2 velocity, GameObjectCollection objectCollection,int max_life) {
        super(topLeftCorner, dimensions, renderable);
        this.objectCollection = objectCollection;
        this.lives = lives;
        this.max_life = max_life;
        super.setVelocity(velocity);
    }

    /**
     * overrides OnCollisionEnter to increase lives by 1, removes the heart from screen.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if(this.lives.value() < max_life){
            lives.increaseBy(1);
        }
        objectCollection.removeGameObject(this);
    }

    /**
     * overrides shouldCollideWith to return true only if other object is paddle.
     * @param other The other GameObject.
     * @return true if can collide with other object, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(super.shouldCollideWith(other) && other.getTag().equals("Paddle")){
            return true;
        }
        return false;
    }
}
