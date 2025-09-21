package pepse.world;

import danogl.GameObject;
import danogl.gui.ImageReader;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.util.Vector2;
import pepse.constants.Constants;


import java.awt.event.KeyEvent;

/**
 * Avatar class, has predetermined tag, animations, size, speed, gravity and refresh rate.
 * Uses some cool animations.
 */
public class Avatar extends GameObject {
    private static final String DEFAULT_IMAGE_PATH = "assets/idle_0.png";
    private static final String TAG = "avatar";
    private static final String[] IDLE_PATHS = {"assets/idle_0.png",
            "assets/idle_1.png",
            "assets/idle_2.png",
            "assets/idle_3.png"};
    private static final String[] JUMP_PATHS = {"assets/jump_0.png",
            "assets/jump_1.png",
            "assets/jump_2.png",
            "assets/jump_3.png"};
    private static final String[] RUN_PATHS = {"assets/run_0.png",
            "assets/run_1.png",
            "assets/run_2.png",
            "assets/run_3.png",
            "assets/run_4.png",
            "assets/run_5.png"};

    private static final float SIZE = 30;
    private static final float VELOCITY_X = 350;
    private static final float VELOCITY_Y = -350;
    private static final float GRAVITY = 300;
    private static final float MAX_ENERGY = 100;
    private static final double REFRESH_RATE = 0.2;
    private final AnimationRenderable idleAnimation;
    private final AnimationRenderable jumpAnimation;
    private final AnimationRenderable runAnimation;
    private boolean jumped;
    private float energy;
    private final UserInputListener inputListener;

    /**
     * Avatar constructor.
     * @param pos Contains the spawn coordinates.
     * @param inputListener Contains the user input listener.
     * @param imageReader Contains the Image reader.
     */
    public Avatar (Vector2 pos, UserInputListener inputListener, ImageReader imageReader){
        super(pos, Vector2.ONES.mult(SIZE), imageReader.readImage(DEFAULT_IMAGE_PATH,
                true));
        super.setTag(TAG);

        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        transform().setAccelerationY(GRAVITY);
        this.inputListener = inputListener;
        this.energy = MAX_ENERGY;
        this.idleAnimation = new AnimationRenderable(IDLE_PATHS,imageReader,
                true, REFRESH_RATE);
        this.jumpAnimation = new AnimationRenderable(JUMP_PATHS,imageReader,
                true, REFRESH_RATE);
        this.runAnimation = new AnimationRenderable(RUN_PATHS,imageReader,
                true, REFRESH_RATE);
    }

    /**
     * Simple getter function whether the avatar just jumped, used in Leaf, Stump and fruit as the supplier
     * function.
     * @return energy of the avatar.
     */
    public Boolean hasJumped(){
        return jumped;
    }

    /**
     * Simple getter function for energy, used in EnergyUI as the supplier function.
     * @return energy of the avatar.
     */
    public Float getEnergy(){
        return energy;
    }

    /**
     * Simple setter function whether the avatar just collided with a fruit, used in Fruit as the consumer
     * function.
     * @return energy of the avatar.
     */
    public void addEnergy(float number){
        energy = Math.min(MAX_ENERGY, energy + number);
    }

    /**
     * Updates Avatar class with current energy, velocity and animations.
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
        handleJump();
        handleHorizontalMovement();
        updateAnimation();
        regenerateEnergyIfNeeded();
    }

    private void handleHorizontalMovement() {
        float xVel = 0;
        if (inputListener.isKeyPressed(KeyEvent.VK_LEFT))
            xVel -= VELOCITY_X;
        if (inputListener.isKeyPressed(KeyEvent.VK_RIGHT))
            xVel += VELOCITY_X;

        if (xVel != 0 && energy >= Constants.HALF) {
            transform().setVelocityX(xVel);
            energy -= Constants.HALF;
            renderer().setIsFlippedHorizontally(xVel < 0);
        } else {
            transform().setVelocityX(0);
        }
    }

    private void handleJump() {
        jumped = false;
        if (inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 &&
                energy >= Constants.TEN) {
            jumped = true;
            transform().setVelocityY(VELOCITY_Y);
            energy -= Constants.TEN;
        }
    }

    private void updateAnimation() {
        if(getVelocity().x() == 0 && getVelocity().y() == 0){
            renderer().setRenderable(idleAnimation);
        }
        if (getVelocity().y() != 0) {
            renderer().setRenderable(jumpAnimation);
        }
        if (getVelocity().x() != 0){
            renderer().setRenderable(runAnimation);
        }
    }

    private void regenerateEnergyIfNeeded() {
        if (getVelocity().x() == 0 && getVelocity().y() == 0) {
            energy = Math.min(100, energy + Constants.HALF);
        }
    }
}