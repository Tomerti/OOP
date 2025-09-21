package ascii_art;

import ascii_output.ConsoleAsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import image.ProcessImage;

import java.io.IOException;
import java.util.HashSet;

/**
 * Shell Class.
 */
public class Shell {
    private static final int DEFAULT_RESOLUTION = 128;
    private static final String DEFAULT_IMAGE = "cat.jpeg";
    private static final String ADD_FORMAT_ERROR = "Did not add due to incorrect format.";
    private static final String REMOVE_FORMAT_ERROR = "Did not remove due to incorrect format.";
    private static final String COMMAND_ERROR = "Did not execute due to incorrect command.";
    private static final String RESOLUTION_BOUNDARIES_ERROR =
            "Did not change resolution due to exceeding boundaries.";
    private static final String RESOLUTION_FORMAT_ERROR =
            "Did not change resolution due to incorrect format.";
    private static final String OUTPUT_FORMAT_ERROR =
            "Did not change output method due to incorrect format.";
    private static final String CHARSET_ERROR =
            "Did not execute. Charset is empty.";
    private static final String IMAGE_ERROR = "Did not execute due to problem with image file.";
    private static final String DEFAULT_HTML = "./out.html";
    private static final String DEFAULT_FONT = "Courier New";
    private boolean mode = false;
    private Image image;
    private final KeyboardInput keyboardInput;
    private final HashSet<Character> charset = new HashSet<>();
    private int resolution = DEFAULT_RESOLUTION;
    private int imgWidth;
    private int imgHeight;
    private int minCharsInRow;
    private int maxCharsInRow;
    private final ConsoleAsciiOutput consoleAsciiOutput;
    private final HtmlAsciiOutput htmlAsciiOutput;

    /**
     * Constructor for Shell.
     */
    public Shell(){
        keyboardInput = KeyboardInput.getObject();
        initializeCharset();
        consoleAsciiOutput = new ConsoleAsciiOutput();
        htmlAsciiOutput = new HtmlAsciiOutput(DEFAULT_HTML, DEFAULT_FONT);
        try{
            image = new Image(DEFAULT_IMAGE);
            image = ProcessImage.padImage(image);

            imgWidth = image.getWidth();
            imgHeight = image.getHeight();
            minCharsInRow = Math.max(1, imgWidth / imgHeight);
            maxCharsInRow = imgWidth;
        }
        catch(IOException e){
            System.out.println(IMAGE_ERROR);
        }
    }

    /**
     * Running the whole process.
     */
    public void run(){
        int commands = 0;
        String command = "";
        while(!command.equals("exit")){
            try {
                if (command.equals("chars")) {
                    commandChars();
                } else if (command.startsWith("add ")) {
                    try {
                        commandAddHandler(command.substring(4));
                    } catch (ShellExceptions.AddException addException) {
                        System.out.println(addException.getMessage());
                    }
                } else if (command.startsWith("remove ")) {
                    try {
                        commandRemoveHandler(command.substring(7));
                    } catch (ShellExceptions.RemoveException removeException) {
                        System.out.println(removeException.getMessage());
                    }
                } else if (command.equals("res up")) {
                    try {
                        updateResolution(2);
                    } catch (ShellExceptions.ResolutionException resolutionException) {
                        System.out.println(resolutionException.getMessage());
                    }
                } else if (command.equals("res down")) {
                    try {
                        updateResolution(0.5);
                    } catch (ShellExceptions.ResolutionException resolutionException) {
                        System.out.println(resolutionException.getMessage());
                    }
                } else if (command.startsWith("res")) {
                    try {
                        throw new ShellExceptions.ResolutionException(RESOLUTION_FORMAT_ERROR);
                    } catch (ShellExceptions.ResolutionException resolutionException) {
                        System.out.println(resolutionException.getMessage());
                    }
                } else if (command.startsWith("image ")) {
                    image = new Image(command.substring(6));
                    image = ProcessImage.padImage(image);

                    imgWidth = image.getWidth();
                    imgHeight = image.getHeight();
                    minCharsInRow = Math.max(1, imgWidth / imgHeight);
                    maxCharsInRow = imgWidth;
                } else if (command.equals("output html")) {
                    mode = true;
                } else if (command.equals("output console")) {
                    mode = false;
                } else if(command.startsWith("output")){
                    try {
                        throw new ShellExceptions.OutputException(OUTPUT_FORMAT_ERROR);
                    } catch (ShellExceptions.OutputException outputException) {
                        System.out.println(outputException.getMessage());
                    }
                }
                else if (command.equals("asciiArt")) {
                    if (!charset.isEmpty()) {
                        char[] charsetArray = new char[charset.size()];
                        int i = 0;
                        for (Character item : charset) {
                            charsetArray[i] = item;
                            i++;
                        }
                        AsciiArtAlgorithm asciiArtAlgorithm = new AsciiArtAlgorithm(image,resolution,
                                charsetArray);
                        char[][] asciiArt = asciiArtAlgorithm.run();
                        if (mode == true) {
                            htmlAsciiOutput.out(asciiArt);
                        } else {
                            consoleAsciiOutput.out(asciiArt);
                        }
                    } else {
                        try {
                            throw new ShellExceptions.CharsetException(CHARSET_ERROR);
                        } catch (ShellExceptions.CharsetException charsetException) {
                            System.out.println(charsetException.getMessage());
                        }
                    }
                }
                else{
                    if(commands > 0) {
                        try {
                            throw new ShellExceptions.CommandException(COMMAND_ERROR);
                        } catch (ShellExceptions.CommandException commandException) {
                            System.out.println(commandException.getMessage());
                        }
                    }
                }
            }
            catch(IOException exception){
                System.out.println(IMAGE_ERROR);
            }
            commands+=1;
            System.out.print(">>> ");
            command = keyboardInput.readLine();
        }
    }

    /**
     * main
     * @param args arguments.
     */
    public static void main(String[]args){
        Shell shell = new Shell();
        shell.run();
    }

    private void initializeCharset(){
        for(int i=0;i<10;i++){
            charset.add((char)(48+i));
        }
    }

    private void commandChars(){
        for(Character item : charset){
            System.out.print(item + " ");
        }
        System.out.println();
    }

    private boolean isValidChar(char c){
        return c >= 32 && c <= 126;
    }

    private boolean isValidTrio(char start, char separator, char end){
        return (isValidChar(start) && separator == 45 && isValidChar(end));
    }

    private void commandAddSingle(char c){
        charset.add(c);
    }

    private void commandAddTrio(char start, char end){
        int minimal = Math.min(start, end);
        int maximal = Math.max(start, end);
        for(int i=minimal;i<=maximal;i++){
            charset.add((char)(i));
        }
    }

    private void commandAddAll(){
        for(int i=32;i<=126;i++){
            charset.add((char)(i));
        }
    }

    private void commandRemoveSingle(char c){
        charset.remove(c);
    }

    private void commandRemoveTrio(char start, char end){
        int minimal = Math.min(start, end);
        int maximal = Math.max(start, end);
        for(int i=minimal;i<=maximal;i++){
            charset.remove((char)(i));
        }
    }

    private void commandRemoveAll(){
        for(int i=32;i<=126;i++){
            charset.remove((char)(i));
        }
    }

    private void commandAddHandler(String essence) throws ShellExceptions.AddException {
        char c, start, separator, end;
        if (essence.length() == 1) {
            c = essence.charAt(0);
            if (!isValidChar(c)) {
                throw new ShellExceptions.AddException(ADD_FORMAT_ERROR);
            }
            commandAddSingle(c);
        }else if (essence.equals("space")) {
            commandAddSingle(' ');
        } else if (essence.equals("all")) {
            commandAddAll();
        } else if (essence.length() == 3) {
            start = essence.charAt(0);
            separator = essence.charAt(1);
            end = essence.charAt(2);
            if (!isValidTrio(start, separator, end)) {
                throw new ShellExceptions.AddException(ADD_FORMAT_ERROR);
            }
            commandAddTrio(start, end);
        } else {
            throw new ShellExceptions.AddException(ADD_FORMAT_ERROR);
        }
    }

    private void commandRemoveHandler(String essence) throws ShellExceptions.RemoveException {
        char c, start, separator, end;
        if (essence.length() == 1) {
            c = essence.charAt(0);
            if (!isValidChar(c)) {
                throw new ShellExceptions.RemoveException(REMOVE_FORMAT_ERROR);
            }
            commandRemoveSingle(c);
        }else if (essence.equals("space")) {
            commandRemoveSingle(' ');
        } else if (essence.equals("all")) {
            commandRemoveAll();
        } else if (essence.length() == 3) {
            start = essence.charAt(0);
            separator = essence.charAt(1);
            end = essence.charAt(2);
            if (!isValidTrio(start, separator, end)) {
                throw new ShellExceptions.RemoveException(REMOVE_FORMAT_ERROR);
            }
            commandRemoveTrio(start, end);
        } else {
            throw new ShellExceptions.RemoveException(REMOVE_FORMAT_ERROR);
        }
    }

    private void updateResolution(double multiplier) throws ShellExceptions.ResolutionException {
        if(resolution * multiplier > maxCharsInRow || resolution * multiplier < minCharsInRow){
            throw new ShellExceptions.ResolutionException(RESOLUTION_BOUNDARIES_ERROR);
        }
        resolution *= multiplier;
        System.out.println("Resolution set to " + resolution + ".");
    }

}
