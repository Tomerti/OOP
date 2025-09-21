package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Simple ExtraPaddle class.
 */
public class ExtraPaddle extends Paddle{
    private Counter lives;
    private GameObjectCollection objectCollection;

    /**
     *
     * @param topLeftCorner top left corner of object
     * @param dimensions dimensions of object
     * @param renderable image of object (can be null)
     * @param inputListener UserInputListener
     * @param windowDimensions game window dimensions.
     * @param paddleGap Gap of x axis.
     * @param lives how many lives this paddle has.
     * @param ObjectCollection GameManager's object collection.
     */
    public ExtraPaddle(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                       UserInputListener inputListener, Vector2 windowDimensions, float paddleGap,
                       Counter lives, GameObjectCollection ObjectCollection) {
        super(topLeftCorner, dimensions, renderable, inputListener, windowDimensions, paddleGap);
        this.lives = lives;
        this.objectCollection = ObjectCollection;
    }

    /**
     * overrides should collidewith, allows only with puck or ball.
     * @param other The other GameObject.
     * @return true if can collide, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        if(super.shouldCollideWith(other) && (other.getTag().equals("Ball") ||
                other.getTag().equals("Puck"))){
            return true;
        }
        return false;
    }

    /**
     * overrides onCollisionEnter to decrease lives by 1.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        lives.increaseBy(-1);
    }

    /**
     * overrides update to remove the extra paddle in case no more lives.
     * @param deltaTime The time elapsed, in seconds, since the last frame. Can
     *                  be used to determine a new position/velocity by multiplying
     *                  this delta with the velocity/acceleration respectively
     *                  and adding to the position/velocity:
     *                  velocity += deltaTime*acceleration
     *                  pos += deltaTime*velocity
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(lives.value() == 0){
            objectCollection.removeGameObject(this);
        }
    }
}
