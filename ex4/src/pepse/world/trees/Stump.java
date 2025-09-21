package pepse.world.trees;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.RectangleRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Stump class, has a predetermined color, size and tag.
 * Each stump has a random height.
 * Whenever the supplier condition is true, the Stump will change it's color to another shade of STUMP_COLOR.
 */
public class Stump extends GameObject{
    private static final String TAG = "stump";
    private static final Color STUMP_COLOR = new Color(100,50,20);
    private static final float BLOCK_SIZE = 30;
    private static final Random random = new Random();
    private final Supplier<Boolean> supplier;

    /**
     * Stump constructor.
     * @param topLeftCorner Contains Top left corner of the object to be created.
     * @param supplier Contains a boolean supplier to act upon.
     */
    public Stump(Vector2 topLeftCorner, Supplier<Boolean> supplier) {
        super(topLeftCorner, Vector2.of(BLOCK_SIZE, BLOCK_SIZE + (random.nextInt(6) * BLOCK_SIZE)),
                new RectangleRenderable(ColorSupplier.approximateColor(STUMP_COLOR)));
        this.supplier = supplier;
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
        setTag(TAG);
    }

    /**
     * Overriders Game object update method.
     * If the supplier condition is true, the Stump will change its color to another shade of STUMP_COLOR.
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
        if(supplier.get()){
            renderer().setRenderable(new RectangleRenderable(ColorSupplier.approximateColor(STUMP_COLOR)));
        }
    }
}
