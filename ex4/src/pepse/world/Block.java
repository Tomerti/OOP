package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

/**
 * Block class, has a predetermined size, immovable.
 */
public class Block extends GameObject {
    /**
     * BLOCK SIZE
     */
    public static final int SIZE = 30; // block size

    /**
     * Block constructor.
     * @param topLeftCorner Contains coordinates of top left corner.
     * @param renderable Contains the renderable of the object, can be null.
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }
}

