package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.constants.Constants;

import java.awt.*;

/**
 * Night class, has a predetermined color, tag and opacity.
 * Uses Transition to fade in or out of the game.
 */
public class Night {
    private static final Color NIGHT_COLOR = Color.BLACK;
    private static final String TAG = "night";
    private static final Float MIDNIGHT_OPACITY = 0.5f;

    /**
     * Creates a game object that represents night.
     * Sets tag and cycle as well.
     * @param windowDimensions Contains the dimensions of the game window.
     * @param cycleLength Contains the length for a single cycle, used in Transition.
     * @return Game object that represents the night.
     */
    public static GameObject create(Vector2 windowDimensions, float cycleLength){
        GameObject night = new GameObject(Vector2.ZERO,
                windowDimensions,
                new RectangleRenderable(NIGHT_COLOR));

        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        night.setTag(TAG);

        new Transition<Float>(
                night, // the game object being changed
                night.renderer()::setOpaqueness, // the method to call
                0f, // initial transition value
                MIDNIGHT_OPACITY, // final transition value
                Transition.CUBIC_INTERPOLATOR_FLOAT,// use a cubic interpolator
                cycleLength * Constants.HALF, // transition fully over half a day
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, // Choose appropriate ENUM value
                null);// nothing further to execute upon reaching final value

        return night;
    }
}
