package bricker.brick_strategies;

import bricker.gameobjects.ExtraPaddle;
import bricker.gameobjects.Puck;
import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

/**
 * SpecialPaddleStrategy class.
 */
public class SpecialPaddleStrategy extends BasicCollisionStrategy{
    private final Renderable renderable;
    private final UserInputListener userInputListener;
    private Vector2 windowDimensions;
    private Vector2 dimensions;
    private Counter hits_left;
    private float paddleGap;

    /**
     * constructor for specialpaddlestrategy.
     * @param objectCollection objectCollection
     * @param userInputListener userInputListener
     * @param renderable image of extra paddle
     * @param windowDimensions windowDimensions
     * @param dimensions dimensions of extra paddle
     * @param max_hits max hits of extra paddle
     * @param bricks bricks alive
     * @param paddleGap gap from x-axis.
     */
    public SpecialPaddleStrategy(GameObjectCollection objectCollection, UserInputListener userInputListener,
                                 Renderable renderable,
                                 Vector2 windowDimensions,
                                 Vector2 dimensions, Counter max_hits, Counter bricks, float paddleGap) {
        super(objectCollection, bricks);
        this.renderable = renderable;
        this.userInputListener = userInputListener;
        this.windowDimensions = windowDimensions;
        this.dimensions = dimensions;
        this.paddleGap = paddleGap;
        this.hits_left = max_hits;
    }

    /**
     * overrides on collision to create extra paddle if no extra paddle is alive.
     * @param thisObj this object.
     * @param otherObj other object.
     */
    @Override
    public void onCollision(GameObject thisObj, GameObject otherObj) {
        super.onCollision(thisObj, otherObj);
        if (this.hits_left.value() == 0) {
            ExtraPaddle extraPaddle = new ExtraPaddle(Vector2.ZERO, dimensions, renderable, userInputListener,
                    windowDimensions, paddleGap, hits_left, super.objectCollection);

            extraPaddle.setCenter(
                    new Vector2(windowDimensions.x() / 2, windowDimensions.y() / 2));
            super.objectCollection.addGameObject(extraPaddle);
            hits_left.increaseBy(4);
        }
    }
}