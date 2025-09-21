package bricker.gameobjects;

import danogl.GameObject;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Simple numericLife class to show a text of lives left.
 */
public class NumericLife extends GameObject {
    private final int GREEN_LIFE = 3;
    private final int YELLOW_LIFE = 2;
    private final int RED_LIFE = 1;
    private TextRenderable text;
    private Counter lives;
    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param lives lives left
     */
    public NumericLife(Vector2 topLeftCorner, Vector2 dimensions, Counter lives) {
        super(topLeftCorner, dimensions, null);
        this.lives = lives;
        this.text = new TextRenderable(Integer.toString(lives.value()));
        this.text.setColor(Color.green);
        this.renderer().setRenderable(this.text);
    }

    /**
     * overrides update to fit on screen text to real status of lives. updates colors as well.
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
        text.setString(Integer.toString(lives.value()));
        if(lives.value() >= GREEN_LIFE){
            text.setColor(Color.green);
        }
        else if(lives.value() == YELLOW_LIFE){
            text.setColor(Color.yellow);
        }
        else if(lives.value() == RED_LIFE){
            text.setColor(Color.red);
        }
    }
}
