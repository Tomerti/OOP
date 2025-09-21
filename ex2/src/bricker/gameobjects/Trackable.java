package bricker.gameobjects;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * Class that allows to track a ball, perfect for camera adjustments.
 */
public class Trackable extends GameObject{
    private Ball ball;
    private int collisions_began;
    private GameManager gameManager;
    private GameObjectCollection objectCollection;

    /**
     * Constructor of Trackable class.
     * @param ball ball to track
     * @param gameManager Manager of the game to set the camera.
     * @param objectCollection objectCollection to remove the tracking if needed.
     */
    public Trackable(Ball ball, GameManager gameManager, GameObjectCollection objectCollection) {
        super(Vector2.ZERO,Vector2.ZERO,null);
        this.gameManager = gameManager;
        this.objectCollection = objectCollection;
        this.ball = ball;
        collisions_began = ball.getCollisionCounter() + 1;
    }

    /**
     * overrides update to return camera to default conditions and remove the tracking from object once
     * required amount of hits are made.
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
        if(ball.getCollisionCounter() - collisions_began == 4){
            this.gameManager.setCamera(null);
            objectCollection.removeGameObject(this);
        }
    }
}