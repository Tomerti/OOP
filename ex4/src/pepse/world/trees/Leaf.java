package pepse.world.trees;

import danogl.GameObject;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.util.ColorSupplier;

import java.awt.*;
import java.util.Random;
import java.util.function.Supplier;

/**
 * Leaf class, has a predetermined color, size, tag, and shaking transitions.
 * Whenever the supplier condition is true, the leaves will rotate in 90 degrees.
 */
public class Leaf extends GameObject {
    private static final Color LEAF_COLOR = new Color(50,200,30);
    private static final float SHAKING_ANGLE_START = -5;
    private static final float SHAKING_ANGLE_END = 5;
    private static final float TRANSITION_DURATION = 1.5f;
    private static final String TAG = "leaf";
    private static final int SIZE = 30;
    private static final int SHAKING_SIZE = 33;
    private final Supplier<Boolean> supplier;

    /**
     * Creates a Leaf object.
     * @param topLeftCorner Contains Top left corner of the object to be created.
     * @param supplier Contains a boolean supplier to act upon.
     */
    public Leaf(Vector2 topLeftCorner, Supplier<Boolean> supplier) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE),
                new RectangleRenderable(ColorSupplier.approximateColor(
                        ColorSupplier.approximateColor(LEAF_COLOR))));
        this.supplier = supplier;
        setTag(TAG);
        Random random = new Random();

        new ScheduledTask(
                this,
                random.nextFloat(1),
                false, // Do not repeat
                () -> createLeafTransition(
                        this,
                        SHAKING_ANGLE_START,
                        SHAKING_ANGLE_END,
                        Vector2.of(SIZE, SIZE),
                        Vector2.of(SHAKING_SIZE, SHAKING_SIZE),
                        TRANSITION_DURATION,
                        Transition.LINEAR_INTERPOLATOR_FLOAT,
                        Transition.LINEAR_INTERPOLATOR_VECTOR
                )
        );
    }

    private void createLeafTransition(GameObject leaf,
                                      float angleStart,
                                      float angleEnd,
                                     Vector2 dimensionStart,
                                      Vector2 dimensionEnd,
                                      float duration,
                                      Transition.Interpolator<Float> angleInterpolator,
                                      Transition.Interpolator<Vector2> dimensionInterpolator) {
        new Transition<Float>(
                leaf,
                leaf.renderer()::setRenderableAngle,
                angleStart,
                angleEnd,
                angleInterpolator,
                duration,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );

        new Transition<Vector2>(
                leaf,
                leaf::setDimensions,
                dimensionStart,
                dimensionEnd,
                dimensionInterpolator,
                duration,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null
        );
    }

    /**
     * Overriders Game object update method.
     * If the supplier condition is true, the leaf will rotate in 90 degrees.
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
            new Transition<Float>(
                    this,
                    angle -> this.renderer().setRenderableAngle(angle),
                    0f,
                    90f,
                    Transition.LINEAR_INTERPOLATOR_FLOAT,
                    1.5f,
                    Transition.TransitionType.TRANSITION_ONCE,
                    null
            );
        }
    }

    /**
     * Overrides Game object shouldCollideWith method.
     * The leaves should not collide with anything, so will always return false.
     * @param other The other GameObject.
     * @return false, always.
     */
    @Override
    public boolean shouldCollideWith(GameObject other) {
        return false;
    }
}

