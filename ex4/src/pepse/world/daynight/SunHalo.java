package pepse.world.daynight;

import danogl.GameObject;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * SunHalo class, has a predetermined color, size and tag.
 * Follows the sun.
 */public class SunHalo {
    private static final Color HALO_COLOR = new Color(255, 255, 0, 20);
    private static final float SIZE = 100;
    private static final String TAG = "halo";

    /**
     * Creates a game object that represents sun halo.
     * Sets tag and follows the sun.
     * @param sun Contains GameObject of sun.
     * @return Game Object that represents the halo of a sun.
     */
    public static GameObject create(GameObject sun){
        GameObject halo = new GameObject(sun.getTopLeftCorner(),
                Vector2.ONES.mult(SIZE),
                new OvalRenderable(HALO_COLOR));

        halo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        halo.setTag(TAG);
        halo.addComponent(deltaTime -> halo.setCenter(sun.getCenter()));
        return halo;
    }
}
