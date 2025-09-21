package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.Sound;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Simple puck class.
 */
public class Puck extends Ball {
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     * @param collisionSound collisions sound.
     * @param velocity velocity.
     */
    public Puck(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable,
                Sound collisionSound, Vector2 velocity) {
        super(topLeftCorner, dimensions, renderable, collisionSound);
        Random random = new Random();
        double angle = random.nextDouble() * Math.PI;
        setVelocity(Vector2.of((float)Math.cos(angle) * velocity.x(),
                (float)Math.sin(angle) * velocity.y()));
    }

    /**
     * overrides oncollisionenter - just because we have to.
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
    }
}
