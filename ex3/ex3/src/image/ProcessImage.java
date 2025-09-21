package image;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that responsible for all algorithmics of Image.
 */
public class ProcessImage {

    // Method for padding an image to the nearest power of 2

    /**
     * Constructor
     * @param image to pad
     * @return padded image with white pixels to near power of 2.
     */
    public static Image padImage(Image image) {
        int targetWidth = getNextPowerOfTwo(image.getWidth());
        int targetHeight = getNextPowerOfTwo(image.getHeight());

        int paddingX = (targetWidth - image.getWidth()) / 2;
        int paddingY = (targetHeight - image.getHeight()) / 2;

        Color[][] paddedPixelArray = new Color[targetHeight][targetWidth];

        // Fill the entire padded array with white pixels
        for (int i = 0; i < targetHeight; i++) {
            for (int j = 0; j < targetWidth; j++) {
                paddedPixelArray[i][j] = Color.WHITE;
            }
        }

        // Copy the original image into the center of the padded array
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                paddedPixelArray[paddingY + y][paddingX + x] = image.getPixel(y, x);
            }
        }

        return new Image(paddedPixelArray, targetWidth, targetHeight);
    }



    /**
     * divides image to sub images and return its list
     * @param image padded image to divide
     * @param resolution required resolution.
     * @return list of sub images.
     */
    public static List<Image> divideImage(Image image, int resolution) {
        List<Image> subImages = new ArrayList<>();
        int subImageSize = image.getWidth() / resolution;
        int imagesPerCol = image.getHeight() / subImageSize;

        for (int i = 0; i < imagesPerCol; i++) {
            for (int j = 0; j < resolution; j++) {
                int startX = i * subImageSize;
                int startY = j * subImageSize;

                Color[][] subPixelArray = new Color[subImageSize][subImageSize];
                for (int x = 0; x < subImageSize; x++) {
                    for (int y = 0; y < subImageSize; y++) {
                        subPixelArray[x][y] = image.getPixel(startX + x, startY + y);
                    }
                }

                subImages.add(new Image(subPixelArray, subImageSize, subImageSize));
            }
        }

        return subImages;
    }

    /**
     * Method for calculating the brightness of an image/sub-image
     */
    public static double calculateBrightness(Image image) {
        int totalPixels = image.getWidth() * image.getHeight();
        double totalBrightness = 0.0;

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = image.getPixel(x, y);
                double grayPixel = color.getRed() * 0.2126 + color.getGreen() * 0.7152 +
                        color.getBlue() * 0.0722;
                totalBrightness += grayPixel / 255.0; // Normalize to be between 0 and 1
            }
        }

        return totalBrightness / totalPixels;
    }

    // Helper method to get the next power of 2 for a given number
    private static int getNextPowerOfTwo(int number) {
        int result = 1;
        while (result < number) {
            result *= 2;
        }
        return result;
    }
}