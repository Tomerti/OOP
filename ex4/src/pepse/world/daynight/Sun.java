package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;
import pepse.constants.Constants;

import java.awt.*;

/**
 * Sun class, has a predetermined color, size and tag.
 * Uses Transition to cycle in circles through the sky.
 */
public class Sun {
    private static final Color SUN_COLOR = Color.YELLOW;
    private static final float SIZE = 80;
    private static final String TAG = "sun";

    /**
     * Creates a game object that represents sun.
     * Sets tag and transition as well.
     * @param windowDimensions Contains the dimensions of the game window.
     * @param cycleLength Contains the length for a single cycle, used in Transition.
     * @return Game object that represents a sun.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject sun = new GameObject(Vector2.ZERO,
                Vector2.ONES.mult(SIZE),
                new OvalRenderable(SUN_COLOR));

        Vector2 cycleCenter = new Vector2(windowDimensions.x() * Constants.HALF,
                windowDimensions.y() * Constants.TWO_THIRDS);
        Vector2 initialSunCenter = new Vector2(windowDimensions.x() * Constants.HALF,
                windowDimensions.y() * Constants.HALF * Constants.TWO_THIRDS);


        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        sun.setTag(TAG);

        new Transition<Float>(
                sun,
                (Float angle) -> sun.setCenter(initialSunCenter.subtract(cycleCenter) .rotated(angle)
                        .add(cycleCenter)),
                0f,
                360f,
                Transition.LINEAR_INTERPOLATOR_FLOAT,
                cycleLength,
                Transition.TransitionType.TRANSITION_LOOP,
                null);

        return sun;
    }
}