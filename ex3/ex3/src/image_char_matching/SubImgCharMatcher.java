package image_char_matching;
import java.util.HashMap;
/**
 * Class that's responsible to match ASCII char to sub image with specific brightness.
 */
public class SubImgCharMatcher {
    private static final int DEFAULT_MATRIX_SIZE = 16;
    private final HashMap<Character, Double[]> charset = new HashMap<>();
    /**
     * Constructor based on given charset.
     * @param charset valid chars for the matcher to pick from.
     */
    public SubImgCharMatcher(char[] charset) {
        for(Character item:charset){
            this.charset.put(item, calculateBrightness(item));
        }
        normalizeBrightness();
    }

    /**
     * returns the char with the minimal brightness delta to given brightness.
     * @param brightness value of sub image.
     * @return
     */
    public char getCharByImageBrightness(double brightness) {
        double minimalDelta = 2; //making sure it'll work.
        double delta;
        char minimalChar = 0;
        for (Character key : charset.keySet()) {
            delta = Math.abs(charset.get(key)[1] - brightness);
            if(delta < minimalDelta) {
                minimalChar = key;
                minimalDelta = delta;
            }
            else if(delta <= minimalDelta){
                if((int)key < (int)minimalChar){
                    minimalChar = key;
                }
            }
        }
        return minimalChar;
    }

    /**
     * Gets a char, adds it to the charset.
     * @param c char to add.
     */
    public void addChar(char c){
        if(charset.containsKey(c)){
            return;
        }
        Double[] charBrightnessArray = calculateBrightness(c);
        Double minBrightness = getMinBrightness();
        Double maxBrightness = getMaxBrightness();
        if(charBrightnessArray[0] < minBrightness || charBrightnessArray[0] > maxBrightness){
            charset.put(c, charBrightnessArray);
            normalizeBrightness();
        }
        else{
            charBrightnessArray[1] = ((charBrightnessArray[0] - minBrightness) /
                    (maxBrightness - minBrightness));
            charset.put(c, charBrightnessArray);
        }
    }

    /**
     * Gets a char, removes it from the charset.
     * @param c char to remove.
     */
    public void removeChar(char c){
        if(!charset.containsKey(c)){
            return;
        }
        Double[] charBrightnessArray = charset.get(c);
        Double minBrightness = getMinBrightness();
        Double maxBrightness = getMaxBrightness();
        charset.remove(c);
        if(charBrightnessArray[0] < minBrightness || charBrightnessArray[0] > maxBrightness){
            normalizeBrightness();
        }
    }

    /**
     * Calculates brightness of a single char.
     * @param c char to calculate.
     * @return brightness of char.
     */
    private Double[] calculateBrightness(char c){
        boolean[][] charMatrix = CharConverter.convertToBoolArray(c);
        Double[] charBrightnessArray = new Double[2];
        int whitePixels = 0;
        for(int i=0; i<DEFAULT_MATRIX_SIZE; i++){
            for(int k=0; k<DEFAULT_MATRIX_SIZE; k++){
                if(charMatrix[i][k]){
                    whitePixels++;
                }
            }
        }
        charBrightnessArray[0] = (double) whitePixels / (DEFAULT_MATRIX_SIZE * DEFAULT_MATRIX_SIZE);
        charBrightnessArray[1] = (double) 0;
        return charBrightnessArray;
    }

    /**
     * Normalizes the brightness of a charset.
     */
    private void normalizeBrightness(){
        Double[] newCharBrightnessArray;
        double minBrightness = getMinBrightness();
        double maxBrightness = getMaxBrightness();
        for(Character key: charset.keySet()){
            newCharBrightnessArray = new Double[2];
            newCharBrightnessArray[0] = charset.get(key)[0];
            newCharBrightnessArray[1] = ((charset.get(key)[0] - minBrightness) /
                    (maxBrightness - minBrightness));
            charset.put(key,newCharBrightnessArray);
        }
    }


    /**
     * Iterates over charset brightness, returns the minimal brightness.
     * @return minimal brightness value.
     */
    private double getMinBrightness(){
        double minBrightness = 1;
        for(Double[] charBrightnessArray:charset.values()){
            if(charBrightnessArray[0] < minBrightness){
                minBrightness = charBrightnessArray[0];
            }
        }
        return minBrightness;
    }

    /**
     * Iterates over charset brightness, returns the maximal brightness.
     * @return maximal brightness value.
     */
    private double getMaxBrightness(){
        double maxBrightness = 0;
        for(Double[] charBrightnessArray:charset.values()){
            if(charBrightnessArray[0] > maxBrightness){
                maxBrightness = charBrightnessArray[0];
            }
        }
        return maxBrightness;
    }
}
