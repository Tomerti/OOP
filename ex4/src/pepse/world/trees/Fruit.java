package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.ScheduledTask;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Fruit class, has a predetermined color, size and tag.
 */
public class Fruit extends GameObject {
    private static final Color FRUIT_COLOR = new Color(255,125,140);
    private static final String TAG = "fruit";
    private static final String AVATAR_TAG = "avatar";
    private static Random random = new Random();
    private static final int SIZE = 30;
    private static final int DISAPPEARANCE_LENGTH = 30;
    private static final float ENERGY_ADDITION = 10;
    private final Consumer<Float> consumer;
    private final Supplier<Boolean> supplier;

    /**
     * Fruit class generator.
     * @param topLeftCorner Contains top left corner of object.
     * @param consumer Contains consumer function to act with.
     * @param supplier Contains supplier function for conditions.
     */
    public Fruit(Vector2 topLeftCorner, Consumer<Float> consumer, Supplier<Boolean> supplier){
        super(topLeftCorner, Vector2.ONES.mult(SIZE),
                new OvalRenderable(ColorSupplier.approximateColor(
                        ColorSupplier.approximateColor(FRUIT_COLOR))));
        this.consumer = consumer;
        this.supplier = supplier;
        setTag(TAG);
    }

    /**
     * Overrides Game object shouldCollideWith method.
     * @param other Contains The other GameObject.
     * @return true if other is avatar, and the fruit is shown on screen, false otherwise.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return(renderer().getRenderable() != null && other.getTag().equals(AVATAR_TAG));
    }

    /**
     * Overrides Game object onCollisionEnter to make the object invisible for DISAPPEARANCE_LENGTH.
     * will also activate the consumer method, changes the shade of the fruit
     * @param other The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     *                  setVelocity(getVelocity().flipped(collision.getNormal()));
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        renderer().setRenderable(null);
        consumer.accept(ENERGY_ADDITION);
        new ScheduledTask(this,
                DISAPPEARANCE_LENGTH,
                false,
                () -> renderer().setRenderable(new OvalRenderable(ColorSupplier.approximateColor
                        (ColorSupplier.approximateColor(FRUIT_COLOR))))
        );
    }

    /**
     * Overrides Game object update method.
     * If the object is shown and the supplier condition is true, change the fruit color to another shade.
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
        if(supplier.get() && renderer().getRenderable() != null){
            renderer().setRenderable(new OvalRenderable(ColorSupplier.approximateColor(FRUIT_COLOR)));
        }
    }
}
