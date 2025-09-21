package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;

/**
 * Simple paddle class.
 */
public class Paddle extends GameObject {
    private static final float MOVEMENT_SPEED = 400;
    private float paddleGap;
    private UserInputListener inputListener;
    private Vector2 windowDimensions;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner    Position of the object, in window coordinates (pixels).
     *                         Note that (0,0) is the top-left corner of the window.
     * @param dimensions       Width and height in window coordinates.
     * @param renderable       The renderable representing the object. Can be null, in which case
     * @param inputListener UserInputListener
     * @param windowDimensions windowDimensions
     * @param paddleGap x axis gap.
     */
    public Paddle(Vector2 topLeftCorner, Vector2 dimensions,
                  Renderable renderable, UserInputListener inputListener, Vector2 windowDimensions,
                  float paddleGap) {
        super(topLeftCorner, dimensions, renderable);
        this.inputListener = inputListener;
        this.windowDimensions = windowDimensions;
        this.paddleGap = paddleGap;
    }

    /**
     * overrides update to keep paddle in x axis limitations.
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
        Vector2 movementDir = Vector2.ZERO;
        if(inputListener.isKeyPressed(KeyEvent.VK_LEFT)) {
            if(getTopLeftCorner().x() - paddleGap > 0) {
                movementDir = movementDir.add(Vector2.LEFT);
            }
        }
        if(inputListener.isKeyPressed(KeyEvent.VK_RIGHT)) {
            if(getTopLeftCorner().x() + getDimensions().x() + paddleGap < windowDimensions.x()) {
                movementDir = movementDir.add(Vector2.RIGHT);
            }
        }
        setVelocity(movementDir.mult(MOVEMENT_SPEED));
    }
}
