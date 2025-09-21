package bricker.brick_strategies;

import bricker.gameobjects.Ball;
import bricker.gameobjects.Trackable;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * strategy for camera modifications.
 */
public class SpecialCameraStrategy extends BasicCollisionStrategy {
    private GameManager gameManager;
    private WindowController windowController;
    private Trackable trackable;

    /**
     * Constructor
     * @param gameManager gamemanager
     * @param objectCollection objectcollection
     * @param windowController windowcontroller
     * @param bricks bricks alive
     */
    public SpecialCameraStrategy(GameManager gameManager, GameObjectCollection objectCollection,
                                 WindowController windowController, Counter bricks) {
        super(objectCollection, bricks);
        this.gameManager = gameManager;
        this.windowController = windowController;
    }

    /**
     * overrides oncollision to ajust camera if ball collided.
     * @param thisObj this object.
     * @param otherObj other object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if(gameManager.camera() == null && otherObj.getTag().equals("Ball")){
            trackable = new Trackable((Ball) otherObj,gameManager,objectCollection);
            objectCollection.addGameObject(trackable);
            gameManager.setCamera(new Camera(
                            otherObj, //object to follow
                            Vector2.ZERO, //follow the center of the object
                            windowController.getWindowDimensions().mult(1.2f), //widen the frame a bit
                            windowController.getWindowDimensions() //share the window dimensions
                    )
            );
        }
    }
}