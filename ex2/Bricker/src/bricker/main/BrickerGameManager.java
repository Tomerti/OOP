package bricker.main;

import bricker.brick_strategies.*;
import bricker.gameobjects.*;
import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.*;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;

import java.awt.event.KeyEvent;
import java.util.Random;

/**
 * Manager class of Bricker Game. using DanoGameLab.
 */
public class BrickerGameManager extends GameManager{

    /*Game constants*/
    private static final String WINDOW_TITLE = "Bricker Game";
    private static final float WINDOW_WIDTH = 700;
    private static final float WINDOW_HEIGHT = 500;
    private static final int MAX_LIVES = 4;
    private static final int DEFAULT_LIVES = 3;
    private static final int STRATEGIES = 6;
    private static final int PUCKS_AMOUNT = 2;

    /*Background constants*/
    private static final String BACKGROUND_IMAGE = "assets/DARK_BG2_small.jpeg";

    /*Border constants*/
    private static final float BORDER_WIDTH = 21;

    /*Bricks constants*/
    private static final String BRICK_IMAGE = "assets/brick.png";
    private static final int DEFAULT_BRICK_ROWS = 7;
    private static final int DEFAULT_BRICK_COLS = 8;
    private static final float BRICK_HEIGHT = 15;
    private static final float BRICK_EXTRA_PADDING_X = 4;
    private static final float BRICK_EXTRA_PADDING_Y = 5;

    /*Paddle constants*/
    private static final String PADDLE_IMAGE = "assets/paddle.png";
    private static final float PADDLE_GAP = 45;
    private static final float PADDLE_HEIGHT = 15;
    private static final float PADDLE_WIDTH = 100;

    /*Ball constants*/
    private static final String BALL_IMAGE = "assets/ball.png";
    private static final String BALL_SOUND = "assets/blop_cut_silenced.wav";
    private static final float BALL_RADIUS = 20;
    private static final float BALL_SPEED = 300;

    /*Puck constants*/
    private static final String PUCK_IMAGE = "assets/mockBall.png";
    private static final String PUCK_SOUND = "assets/blop_cut_silenced.wav";
    private static final float PUCK_RADIUS = BALL_RADIUS * 0.75f;
    private static final float PUCK_SPEED = 300;

    /*Heart Constants*/
    private static final String HEART_IMAGE = "assets/heart.png";
    private static final float HEART_WIDTH = 25;
    private static final float HEART_HEIGHT = 25;
    private static final Vector2 HEART_VELOCITY = Vector2.of(0,100);


    /*NumericLife Constants*/
    private static final float NUMERIC_WIDTH = 25;
    private static final float NUMERIC_HEIGHT = 25;
    private static final float NUMERIC_PAD = 30;

    /*GraphicLife Constants*/
    private static final float GRAPHIC_WIDTH = 25;
    private static final float GRAPHIC_HEIGHT = 25;
    private static final float GRAPHIC_PAD_X = 8;
    private static final float GRAPHIC_PAD_Y = 30;

    /*Game variables*/
    private RandomStrategy randomStrategy;
    private Ball ball;
    private Counter lives;
    private Counter paddle_lives;
    private Counter bricks;
    private final Vector2 windowDimensions;
    private WindowController windowController;
    private ImageReader imageReader;
    private SoundReader soundReader;
    private UserInputListener userInputListener;
    private final int brick_cols;
    private final int brick_rows;

    /**
     * BrickerGameManager constructor.
     * @param windowTitle Title of the game window.
     * @param windowDimensions Dimensions of the game window.
     * @param brick_cols Desired amount of bricks (columns)
     * @param brick_rows Desired amount of bricks (rows)
     */
    public BrickerGameManager(String windowTitle, Vector2 windowDimensions,
                              int brick_cols, int brick_rows){
        super(windowTitle, windowDimensions);
        this.windowDimensions = windowDimensions;
        this.brick_cols = brick_cols;
        this.brick_rows = brick_rows;
    }

    /**
     * Simple main function, creates an instance of BrickerGameManager and runs a single round.
     * @param args command line arguments, first one is for desired cols of bricks in game
     *             second one is for desired rows of bricks in game.
     *             if no arguments received, the game will use default values (8 cols, 7 rows).
     */
    public static void main(String[] args){
        BrickerGameManager game;
        int brick_cols = DEFAULT_BRICK_COLS;
        int brick_rows = DEFAULT_BRICK_ROWS;
        if(args.length == 2) {
            brick_cols = Integer.parseInt(args[0]);
            brick_rows = Integer.parseInt(args[1]);
        }
        game = new BrickerGameManager(WINDOW_TITLE, Vector2.of(WINDOW_WIDTH,WINDOW_HEIGHT),
                brick_cols, brick_rows);
        game.run();
    }

    /**
     * overrides GameManager initializegame, resets game parameters for a new round.
     * @param imageReader Contains a single method: readImage, which reads an image from disk.
     *                 See its documentation for help.
     * @param soundReader Contains a single method: readSound, which reads a wav file from
     *                    disk. See its documentation for help.
     * @param inputListener Contains a single method: isKeyPressed, which returns whether
     *                      a given key is currently pressed by the user or not. See its
     *                      documentation.
     * @param windowController Contains an array of helpful, self explanatory methods
     *                         concerning the window.
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader,
                               UserInputListener inputListener, WindowController windowController){
        super.initializeGame(imageReader, soundReader, inputListener, windowController);

        this.imageReader = imageReader;
        this.soundReader = soundReader;
        this.userInputListener = inputListener;
        this.windowController = windowController;
        this.lives = new Counter(DEFAULT_LIVES);
        this.paddle_lives = new Counter(0);
        this.bricks = new Counter(brick_cols * brick_rows);

        //Create Background
        createBackground(imageReader);

        //Create NumericLife
        createNumericLife();

        //Create GraphicLife
        createGraphicLife(imageReader);

        //Create RandomStrategy
        createRandomStrategy();

        //Create Brick
        createBrick();

        //Create Ball
        createBall(imageReader, soundReader);

        //Create Paddle
        createPaddle(imageReader, userInputListener);

        //Create Borders
        createBorders();
    }

    /**
     * overrides GameManger update function, checks how many lives user has left,
     * checks and deletes every GameOject (except ball) that crossed game y-axis borders (checks positive
     * values), ends the game in case of win/loss, lets the player play another round or terminate the
     * program.
     * @param deltaTime The time, in seconds, that passed since the last invocation
     *                  of this method (i.e., since the last frame). This is useful
     *                  for either accumulating the total time that passed since some
     *                  event, or for physics integration (i.e., multiply this by
     *                  the acceleration to get an estimate of the added velocity or
     *                  by the velocity to get an estimate of the difference in position).
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        this.checkLives();
        this.checkOutside();
        this.checkForGameEnd();
    }

    private void checkLives(){
        if(ball.getCenter().y() > windowDimensions.y()) {
            //player lost a heart
            this.lives.increaseBy(-1);
            resetBall(ball);
        }
    }

    private void checkOutside(){
        //removes all game objects outside of border (y-axis), won't remove ball because of ball priority
        //in update.
        for(GameObject gameObject:gameObjects()){
            if(gameObject.getCenter().y() > windowDimensions.y()){
                super.gameObjects().removeGameObject(gameObject);
            }
        }
    }


    private void checkForGameEnd() {
        String prompt = "";
        if(this.bricks.value() == 0 || this.userInputListener.isKeyPressed(KeyEvent.VK_W)) {
            //we won
            prompt = "You win!";
        }
        if(lives.value() <= 0) {
            //we lost
            prompt = "You Lose!";
        }
        if(!prompt.isEmpty()) {
            prompt += " Play again?";
            if(windowController.openYesNoDialog(prompt))
                windowController.resetGame();
            else
                windowController.closeWindow();
        }
    }

    private void createBackground(ImageReader imageReader) {
        Renderable backgroundImage =
                imageReader.readImage(BACKGROUND_IMAGE, false);
        GameObject background = new GameObject(
                Vector2.ZERO, windowDimensions, backgroundImage);
        background.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects().addGameObject(background, Layer.BACKGROUND);
    }

    private void createRandomStrategy(){
        Renderable heartImage = imageReader.readImage(HEART_IMAGE, true);
        Renderable paddleImage = imageReader.readImage(PADDLE_IMAGE, false);
        Renderable puckImage = imageReader.readImage(PUCK_IMAGE, true);
        Sound puckSound = soundReader.readSound(BALL_SOUND);
        this.randomStrategy = new RandomStrategy(this,gameObjects(),windowController,
                userInputListener,
                heartImage,paddleImage,puckImage,puckSound,Vector2.of(PADDLE_WIDTH,PADDLE_HEIGHT),
                Vector2.of(HEART_WIDTH,HEART_HEIGHT),Vector2.of(PUCK_RADIUS,
                PUCK_RADIUS),
                HEART_VELOCITY,Vector2.of(PUCK_SPEED,
                PUCK_SPEED),bricks,
                lives,
                paddle_lives,STRATEGIES,PUCKS_AMOUNT,MAX_LIVES,BORDER_WIDTH);
    }

    private void createBrick() {
        Renderable brickImage =
                imageReader.readImage(BRICK_IMAGE, false);
        int brick_width = (int)(((windowDimensions.x() - (2 * (BORDER_WIDTH + 2))) / brick_cols) -
                BRICK_EXTRA_PADDING_X);
        for(int i=0; i<brick_rows; i++){
            for(int k=0; k<brick_cols; k++){
                Brick brick = new Brick(Vector2.ZERO, Vector2.of(brick_width, BRICK_HEIGHT), brickImage,
                        randomStrategy.getAnyStrategy(), bricks);
                brick.setTopLeftCorner(Vector2.of(BORDER_WIDTH + 1 + k *(brick_width + BRICK_EXTRA_PADDING_Y),
                        BORDER_WIDTH + i*(BRICK_HEIGHT + BRICK_EXTRA_PADDING_Y)));
                gameObjects().addGameObject(brick,Layer.STATIC_OBJECTS);
            }
        }
    }

    private void createBall(ImageReader imageReader, SoundReader soundReader) {
        Renderable ballImage =
                imageReader.readImage(BALL_IMAGE, true);
        Sound collisionSound = soundReader.readSound(BALL_SOUND);


        ball = new Ball(
                Vector2.ZERO, new Vector2(BALL_RADIUS, BALL_RADIUS), ballImage, collisionSound);

        ball.setTag("Ball");
        gameObjects().addGameObject(ball);
        resetBall(ball);
    }

    private void createNumericLife(){
        NumericLife numericLife = new NumericLife(Vector2.of(BORDER_WIDTH,
                windowDimensions.y() - NUMERIC_PAD),
                Vector2.of(NUMERIC_WIDTH, NUMERIC_HEIGHT),
                lives);
        gameObjects().addGameObject(numericLife,Layer.UI);
    }

    private void createGraphicLife(ImageReader imageReader){
        Renderable heartImage =
                imageReader.readImage(HEART_IMAGE, true);
        GraphicLife graphicLife = new GraphicLife(Vector2.ZERO, Vector2.of(GRAPHIC_WIDTH, GRAPHIC_HEIGHT),
                windowDimensions,
                heartImage,
                lives,
                MAX_LIVES,
                BORDER_WIDTH,
                GRAPHIC_PAD_X,
                GRAPHIC_PAD_Y,
                gameObjects());
        gameObjects().addGameObject(graphicLife,Layer.UI);
    }

    private void resetBall(Ball ball){
        Random rand = new Random();
        ball.setCenter(windowDimensions.mult(0.5f));
        float ballVelX = BALL_SPEED;
        float ballVelY = BALL_SPEED;

        if(rand.nextBoolean())
            ballVelX *= -1;
        if(rand.nextBoolean())
            ballVelY *= -1;
        ball.setVelocity(new Vector2(ballVelX, ballVelY));
    }

    private void createBorders() {
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.ZERO,
                        Vector2.of(BORDER_WIDTH, windowDimensions.y()),
                        null)
        );
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.of(windowDimensions.x()-BORDER_WIDTH, Vector2.ZERO.y()),
                        Vector2.of(BORDER_WIDTH, windowDimensions.y()),
                        null)
        );
        gameObjects().addGameObject(
                new GameObject(
                        Vector2.of(BORDER_WIDTH,Vector2.ZERO.y()),
                        Vector2.of(windowDimensions.x() - 2 * BORDER_WIDTH, BORDER_WIDTH),
                        null)
        );
    }

    private void createPaddle(ImageReader imageReader, UserInputListener inputListener) {
        Renderable paddleImage = imageReader.readImage(
                PADDLE_IMAGE, true);
        GameObject paddle = new Paddle(
                Vector2.ZERO,
                Vector2.of(PADDLE_WIDTH, PADDLE_HEIGHT), paddleImage, inputListener, windowDimensions,
                BORDER_WIDTH);

        paddle.setCenter(
                new Vector2(windowDimensions.x() / 2, windowDimensions.y() - PADDLE_GAP));
        paddle.setTag("Paddle");
        gameObjects().addGameObject(paddle);
    }
}
