package pepse.ui;

import java.util.function.Supplier;
import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Energy UI class, has a predetermined size and colors for different energy scenarios.
 */
public class EnergyUI extends GameObject {
    private static final int SIZE = 40;
    private static final String TAG = "EnergyUI";
    private static final int GREEN_ENERGY = 80;
    private static final int YELLOW_ENERGY = 50;
    private static final int RED_ENERGY = 20;
    private final Supplier<Float> supplier;
    private float currentEnergy;
    private final TextRenderable text;

    /**
     * Creates a game object that represents Energy UI, returns it.
     * @param supplier Contains the supplier function for the UI.
     * @return EnergyUI class object.
     */
    public EnergyUI(Supplier<Float> supplier) {
        super(Vector2.ZERO, Vector2.of(SIZE, SIZE), null);
        super.setTag(TAG);
        this.supplier = supplier;
        this.currentEnergy = supplier.get();
        this.text = new TextRenderable(Float.toString(currentEnergy));
        updateColor(currentEnergy);
        this.renderer().setRenderable(this.text);
    }

    /**
     * Updates EnergyUI with the supplier function.
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
        currentEnergy = supplier.get();
        text.setString(Float.toString(currentEnergy));
        updateColor(currentEnergy);
    }

    private void updateColor(float energy) {
        if (energy > GREEN_ENERGY) {
            text.setColor(Color.GREEN);
        } else if (energy > YELLOW_ENERGY) {
            text.setColor(Color.YELLOW);
        } else if (energy <= RED_ENERGY) {
            text.setColor(Color.RED);
        }
    }
}