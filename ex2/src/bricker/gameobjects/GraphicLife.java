package bricker.gameobjects;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.rendering.Renderable;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.*;

/**
 * Responsible to show hearts to the user (UI).
 */
public class GraphicLife extends GameObject {

    private Renderable renderable;
    private Counter lives;
    private GameObject[] hearts;
    private GameObjectCollection objectCollection;
    private Vector2 windowDimensions;
    private Vector2 graphicDimensions;
    private float width;
    private float graphic_pad_x;
    private float graphic_pad_y;
    private int livesindex;
    private int maxlife;

    /**
     * GraphicLife constructor.
     * @param topLeftCorner top left corner.
     * @param dimensions dimensions of a single heart.
     * @param windowDimensions dimensions of the window.
     * @param renderable image to show.
     * @param lives how many lives left.
     * @param maxlife maximum lives in game.
     * @param width width
     * @param graphic_pad_x padding of x axis
     * @param graphic_pad_y padding of y axis
     * @param objectCollection object collection.
     */
    public GraphicLife(Vector2 topLeftCorner,Vector2 dimensions, Vector2 windowDimensions,
                       Renderable renderable, Counter lives, int maxlife, float width, float graphic_pad_x,
                       float graphic_pad_y, GameObjectCollection objectCollection) {
        super(Vector2.ZERO, Vector2.ZERO, null);
        this.objectCollection = objectCollection;
        this.windowDimensions = windowDimensions;
        this.graphicDimensions = dimensions;
        this.lives = lives;
        this.width = width;
        this.graphic_pad_x = graphic_pad_x;
        this.graphic_pad_y = graphic_pad_y;
        this.livesindex = lives.value();
        this.renderable = renderable;
        this.maxlife = maxlife;

        this.hearts = new GameObject[maxlife];
        for(int i=0; i<this.lives.value(); i++){
            hearts[i] = new GameObject(Vector2.ZERO, graphicDimensions, this.renderable);
            hearts[i].setTopLeftCorner(Vector2.of(this.width + graphicDimensions.x() +
                            i * (graphicDimensions.x() + graphic_pad_x),
                    windowDimensions.y() - graphic_pad_y));
            this.objectCollection.addGameObject(hearts[i],Layer.UI);
        }
    }

    /**
     * Overrides update function to set new heart/delete existing heart to fit current lives status.
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
        if(livesindex > lives.value()){
            livesindex--;
            objectCollection.removeGameObject(hearts[livesindex],Layer.UI);
        }
        else if (livesindex < lives.value() && livesindex < maxlife){
            hearts[livesindex] = new GameObject(Vector2.ZERO, graphicDimensions, this.renderable);
            hearts[livesindex].setTopLeftCorner(Vector2.of(this.width + graphicDimensions.x() +
                            livesindex * (graphicDimensions.x() + graphic_pad_x),
                    windowDimensions.y() - graphic_pad_y));
            this.objectCollection.addGameObject(hearts[livesindex],Layer.UI);
            livesindex++;
        }
    }
}
