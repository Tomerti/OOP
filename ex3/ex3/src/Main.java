import ascii_art.AsciiArtAlgorithm;
import image.Image;
import ascii_output.HtmlAsciiOutput;


import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            // Step 1: Read the image file
            //File imageFile = new File("cat.jpeg");
            //Image image = new Image(imageFile.getPath());
            Image image = new Image("cat.jpeg");

            // Step 2: Define the resolution and valid characters
            int resolution = 128;
            char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

            // Step 3: Create an instance of AsciiArtAlgorithm
            AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image, resolution, validChars);

            // Step 4: Run the algorithm and get the ASCII art
            char[][] asciiArt = asciiArtAlgorithm.run();

            // Step 5: Create an instance of HtmlAsciiOutput and export the ASCII art to an HTML file
            HtmlAsciiOutput htmlAsciiOutput = new HtmlAsciiOutput("output.html", "Courier New");
            htmlAsciiOutput.out(asciiArt);

            System.out.println("HTML file saved successfully: output.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}