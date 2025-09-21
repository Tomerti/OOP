package ascii_art;

import image.Image;
import image.ProcessImage;
import image_char_matching.SubImgCharMatcher;

import java.util.List;

/**
 * Constructor of AsciiArtAlgorithm
 */
public class AsciiArtAlgorithm {

    private final Image originalImage;
    private final int resolution;
    private final char[] charset;

    /**
     * AsciiArtAlgorithm Constructor
     * @param originalImage before padding
     * @param resolution resolution
     * @param charset charset
     */
    public AsciiArtAlgorithm(Image originalImage, int resolution, char[] charset) {
        this.originalImage = originalImage;
        this.resolution = resolution;
        this.charset = charset;
    }

    /**
     * Public method to run the algorithm and return the ASCII art
     * @return complete asciiart!
     */
    public char[][] run() {
        // Step 1: Pad the original image
        Image paddedImage = ProcessImage.padImage(originalImage);

        // Step 2: Divide the padded image into sub-images based on resolution
        List<Image> subImages = ProcessImage.divideImage(paddedImage, resolution);

        // Step 3: Create a SubImgCharMatcher with the provided charset
        SubImgCharMatcher charMatcher = new SubImgCharMatcher(charset);

        // Step 4: Create a 2D array to store the resulting ASCII art
        int imagesPerCol = paddedImage.getHeight() / (paddedImage.getWidth() / resolution);
        char[][] asciiArt = new char[imagesPerCol][resolution];

        // Step 5: Replace each sub-image with the closest character in brightness
        for (int i = 0; i < imagesPerCol; i++) {
            for (int j = 0; j < resolution; j++) {
                int index = i * resolution + j;
                Image subImage = subImages.get(index);
                double brightness = ProcessImage.calculateBrightness(subImage);
                char closestChar = charMatcher.getCharByImageBrightness(brightness);
                asciiArt[i][j] = closestChar;
            }
        }
        return asciiArt;
    }
}