package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.util.Vector2;
import pepse.ui.EnergyUI;
import pepse.world.Avatar;
import pepse.world.Block;
import pepse.world.Sky;
import pepse.world.Terrain;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Flora;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Game Manager of Pepse game.
 */
public class PepseGameManager extends GameManager {
    private static final float CYCLE_LENGTH = 30;

    private Function<Float, Float> floraFunction;
    private Consumer<Float> floraConsumer;
    private Supplier<Boolean> hasJumped;
    private Supplier<Float> energyUISupplier;

    /**
     * Main function, creates a PepseGameManager object and runs the game.
     * @param args Contains arguments from user.
     */
    public static void main(String[] args){
        new PepseGameManager().run();
    }

    /**
     * Initializes the game.
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
    public void initializeGame(ImageReader imageReader,
                               SoundReader soundReader,
                               UserInputListener inputListener,
                               WindowController windowController){
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        createSky(windowController.getWindowDimensions());
        createTerrain(windowController.getWindowDimensions());
        createAvatar(Vector2.of(10,10), inputListener, imageReader);
        createFlora(windowController.getWindowDimensions(), floraFunction, floraConsumer, hasJumped);
        createNight(windowController.getWindowDimensions());
        createSunWithHalo(windowController.getWindowDimensions());
        createEnergyUI(energyUISupplier);
    }

    private void createSky(Vector2 windowDimensions){
        GameObject sky = Sky.create(windowDimensions);
        gameObjects().addGameObject(sky, Layer.BACKGROUND);
    }

    private void createTerrain(Vector2 windowDimensions){
        Terrain terrain = new Terrain(windowDimensions,0);
        List<Block> blocks = terrain.createInRange(0, (int) windowDimensions.x());
        for(Block block: blocks){
            gameObjects().addGameObject(block, Layer.STATIC_OBJECTS);
        }
        this.floraFunction = terrain::groundHeightAt;
    }

    private void createFlora(Vector2 windowDimensions, Function<Float, Float> function,
                             Consumer<Float> consumer, Supplier<Boolean> hasJumped){
        Flora flora = new Flora(function, consumer, hasJumped);
        List<GameObject> floraObjects = flora.createInRange(0, (int) windowDimensions.x());
        for(GameObject floraObject: floraObjects){
            gameObjects().addGameObject(floraObject, Layer.STATIC_OBJECTS);
        }
    }

    private void createNight(Vector2 windowDimensions){
        GameObject night = Night.create(windowDimensions,CYCLE_LENGTH);
        gameObjects().addGameObject(night, Layer.BACKGROUND);
    }

    private void createSunWithHalo(Vector2 windowDimensions){
        GameObject sun = Sun.create(windowDimensions,CYCLE_LENGTH);
        gameObjects().addGameObject(sun, Layer.BACKGROUND);
        GameObject halo = SunHalo.create(sun);
        gameObjects().addGameObject(halo, Layer.BACKGROUND);
    }

    private void createAvatar(Vector2 startingPosition,
                              UserInputListener userInputListener,
                              ImageReader imageReader){
        Avatar avatar= new Avatar(startingPosition, userInputListener, imageReader);
        gameObjects().addGameObject(avatar,Layer.DEFAULT);
        this.energyUISupplier = avatar::getEnergy;
        this.floraConsumer = avatar::addEnergy;
        this.hasJumped = avatar::hasJumped;
    }

    private void createEnergyUI(Supplier<Float> energyUISupplier) {
        EnergyUI energyUI = new EnergyUI(energyUISupplier);
        gameObjects().addGameObject(energyUI, Layer.UI);
    }
}


