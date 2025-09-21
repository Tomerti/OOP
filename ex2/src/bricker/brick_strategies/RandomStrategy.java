package bricker.brick_strategies;

import danogl.GameManager;
import danogl.collisions.GameObjectCollection;
import danogl.gui.Sound;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.util.Random;

/**
 * Factory to create strategies in random ways.
 */
public class RandomStrategy {
    private final GameManager gameManager;
    private final GameObjectCollection objectCollection;
    private final WindowController windowController;
    private final UserInputListener userInputListener;
    private final Renderable heart_image;
    private final Renderable extra_paddle_image;
    private final Renderable puck_image;
    private final Sound puck_sound;
    private final Vector2 heart_dimensions;
    private final Vector2 extra_paddle_dimensions;
    private final Vector2 puck_dimensions;
    private final Vector2 heart_velocity;
    private final Vector2 puck_velocity;
    private Counter bricks;
    private Counter lives;
    private Counter max_extra_hits;
    private final Random random;
    private final int strategies;
    private final int puck_amount;
    private final int max_life;
    private final float extra_paddle_gap;

    /**
     *
     * @param gameManager game manager
     * @param objectCollection object collection
     * @param windowController window controller
     * @param userInputListener userinputlistener
     * @param heart_image image of heart
     * @param extra_paddle_image image of extra paddle
     * @param puck_image image of puck
     * @param puck_sound sound of puck when collides
     * @param extra_paddle_dimensions dimensions of extra paddle
     * @param heart_dimensions dimensions of heart
     * @param puck_dimensions dimensions of puck
     * @param heart_velocity velocity of heart
     * @param puck_velocity velocity of puck
     * @param bricks how many bricks left
     * @param lives how many lives left
     * @param max_extra_hits maximum hits for extra paddle
     * @param strategies strategies number in total
     * @param puck_amount amount of pucks to create
     * @param max_life max lives in game.
     * @param extra_paddle_gap gap of extra paddle
     */
    public RandomStrategy(GameManager gameManager, GameObjectCollection objectCollection,
                          WindowController windowController, UserInputListener userInputListener,
                          Renderable heart_image, Renderable extra_paddle_image, Renderable puck_image,
                          Sound puck_sound, Vector2 extra_paddle_dimensions,
                          Vector2 heart_dimensions, Vector2 puck_dimensions, Vector2 heart_velocity,
                          Vector2 puck_velocity, Counter bricks, Counter lives, Counter max_extra_hits,
                          int strategies, int puck_amount, int max_life, float extra_paddle_gap){
        random = new Random();
        this.gameManager = gameManager;
        this.objectCollection = objectCollection;
        this.windowController = windowController;
        this.userInputListener = userInputListener;
        this.heart_image = heart_image;
        this.extra_paddle_image = extra_paddle_image;
        this.puck_image = puck_image;
        this.puck_sound = puck_sound;
        this.heart_dimensions = heart_dimensions;
        this.extra_paddle_dimensions = extra_paddle_dimensions;
        this.puck_dimensions = puck_dimensions;
        this.heart_velocity = heart_velocity;
        this.puck_velocity = puck_velocity;
        this.bricks = bricks;
        this.lives = lives;
        this.max_extra_hits = max_extra_hits;
        this.strategies = strategies;
        this.puck_amount = puck_amount;
        this.max_life = max_life;
        this.extra_paddle_gap = extra_paddle_gap;
    }

    /**
     * returns a random strategy at 1/2 for basic, rest is divided 1/5 by all other options.
     * @return a random collision strategy.
     */
    public CollisionStrategy getAnyStrategy() {
        int randomIndex = random.nextInt(strategies);
        if (randomIndex == 0) {
            return new BasicCollisionStrategy(objectCollection, bricks);
        }
        return getSpecialStrategy();
    }

    /**
     * returns a random special strategy
     * @return a random special strategy
     */
    public CollisionStrategy getSpecialStrategy(){
        int randomIndex = random.nextInt(strategies - 1);
        if(randomIndex == 0){
            return new SpecialCameraStrategy(gameManager,objectCollection,windowController, bricks);
        }
        else if(randomIndex == 1){
            return new SpecialPaddleStrategy(objectCollection,userInputListener, extra_paddle_image,
                    windowController.getWindowDimensions(), extra_paddle_dimensions,
                    max_extra_hits, bricks,extra_paddle_gap);
        }

        else if(randomIndex == 2){
            return new SpecialHeartStrategy(objectCollection, heart_image, heart_dimensions, heart_velocity
                    ,bricks, lives,max_life);
        }
        else if(randomIndex == 3){
            return new SpecialPuckStrategy(objectCollection, puck_image, puck_sound, puck_dimensions,
                    puck_velocity, bricks, puck_amount);
        }
        else{
            return new SpecialDoubleStrategy(objectCollection, this, bricks);
        }
    }

    /**
     * returns a special strategy without double.
     * @return a special strategy without double.
     */
    public CollisionStrategy getSpecialNoDouble(){
        int randomIndex = random.nextInt(strategies - 2);
        if(randomIndex == 0){
            return new SpecialCameraStrategy(gameManager,objectCollection,windowController, bricks);
        }
        else if(randomIndex == 1){
            return new SpecialPaddleStrategy(objectCollection,userInputListener, extra_paddle_image,
                    windowController.getWindowDimensions(), extra_paddle_dimensions,
                    max_extra_hits, bricks,extra_paddle_gap);
        }

        else if(randomIndex == 2){
            return new SpecialHeartStrategy(objectCollection, heart_image, heart_dimensions, heart_velocity
                    ,bricks, lives,max_life);
        }
        else{
            return new SpecialPuckStrategy(objectCollection, puck_image, puck_sound, puck_dimensions,
                    puck_velocity, bricks, puck_amount);
        }
    }
}
