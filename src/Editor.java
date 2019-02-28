
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import java.lang.reflect.Method;
import java.util.ArrayList;


/**
 * This class is the main processing class of the Fotoshop application. 
 * Fotoshop is a very simple image editing tool. Users can apply a number of
 * filters to an image. That's all. It should really be extended to make it more
 * useful!
 *
 * To edit an image, create an instance of this class and call the "edit"
 * method.
 *
 * This main class creates and initialises all the others: it creates the parser
 * and  evaluates and executes the commands that the parser returns.
 *
 * @author Joseph Williams
 * @version 2018.12.12
 */

public class Editor {

    Parser parser;
    ColorImage currentImage;
    String name;
    ArrayList<String> filters = new ArrayList<>();

   
    /**
     * Create the editor and initialise its parser.
     */
    public Editor() {
        parser = new Parser();
    }

    /**
     * Main edit routine. Loops until the end of the editing session.
     */
    public void edit() {
        printWelcome();

        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the editing session is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        System.out.println("Thank you for using Fotoshop.  Good bye.");
    }

    /**
     * Print out the opening message for the user.
     */
    private void printWelcome() {
        System.out.println();
        System.out.println("Welcome to Fotoshop!");
        System.out.println("Fotoshop is an amazing new, image editing tool.");
        System.out.println("Type 'help' if you need help.");
        System.out.println();
        System.out.println("The current image is " + name);
        System.out.print("Filters applied: ");
        
        for(String filter: filters){
            if (filter != null) {
                System.out.print(filter + " ");
            }
        }
        System.out.println();
    
    }
    /**
     * Given a command, edit (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the editing session, false otherwise.
     */
    private boolean processCommand(Command command) {
        boolean wantToQuit = false;
        
        if (command.hasWord(1)) {
            System.out.println("I don't know what you mean...");
            return false;
        }
        
        String commandWord = command.getWord(1);
        
        if(commandWord.equals("help"))
            commandWord = "printHelp";
        
        try {
            Class c = Class.forName("Editor");
            @SuppressWarnings("unchecked")
            Method method = c.getDeclaredMethod(commandWord);
            wantToQuit = (boolean) method.invoke(this);         
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) { 
            System.out.println("I don't know what you mean...");
            return false;
        }
        
        return wantToQuit;
    }

//----------------------------------
// Implementations of user commands:
//----------------------------------
    
    /**
     * Print out some help information. Here we print some useless, cryptic
     * message and a list of the command words.
     */
    private void printHelp() {
        System.out.println("You are using Fotoshop.");
        System.out.println();
        System.out.println("Your command words are:");
        System.out.println("   open save look mono flipH rot90 help quit");
    }

    /**
     * Load an image from a file.
     * @param name The name of the image file
     * @return a ColorImage containing the image
     */
    private ColorImage loadImage(String name) {
        ColorImage img = null;
        try {
            img = new ColorImage(ImageIO.read(new File(name)));
        } catch (IOException e) {
            System.out.println("Cannot find image file, " + name);
            System.out.println("cwd is " + System.getProperty("user.dir"));
        }
        return img;
    }


    /**
     * "open" was entered. Open the file given as the second word of the command
     * and use as the current image. 
     * @param command the command given.
     */
    private void open(Command command) {
        if (!command.hasWord(2)) {
            // if there is no second word, we don't know what to open...
            System.out.println("open what?");
            return ;
        }
  
        String inputName = command.getWord(2);
        ColorImage img = loadImage(inputName);
        if (img == null) {
            printHelp();
        } else {
            currentImage = img;
            name = inputName;
            System.out.println("Loaded " + name);
        }
    }

    /**
     * "save" was entered. Save the current image to the file given as the 
     * second word of the command. 
     * @param command the command given
     */
    private void save(Command command) {
        if (currentImage == null) {
            printHelp();
            return;
        }
        if (!command.hasWord(2)) {
            // if there is no second word, we don't know where to save...
            System.out.println("save where?");
            return ;
        }
  
        String outputName = command.getWord(2);
        try {
            File outputFile = new File(outputName);
            ImageIO.write(currentImage, "jpg", outputFile);
            System.out.println("Image saved to " + outputName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            printHelp();
        }
    }

    /**
     * "look" was entered. Report the status of the work bench. 
     * @param command the command given.
     */
    private void look(Command command) {
        System.out.println("The current image is " + name);
        System.out.print("Filters applied: ");
        
        for(String filter: filters){
            if (filter != null) {
                System.out.print(filter + " ");
            }
        }
        System.out.println();
    }

    /**
     * "mono" was entered. Convert the current image to monochrome. 
     * @param command the command given.
     */
    private void mono(Command command) {
        
        ColorImage tmpImage = new ColorImage(currentImage);
        //Graphics2D g2 = currentImage.createGraphics();
        int height = tmpImage.getHeight();
        int width = tmpImage.getWidth();
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color pix = tmpImage.getPixel(x, y);
                int lum = (int) Math.round(0.299*pix.getRed()
                                         + 0.587*pix.getGreen()
                                         + 0.114*pix.getBlue());
                tmpImage.setPixel(x, y, new Color(lum, lum, lum));
            }
        }
        currentImage = tmpImage;

        filters.add("mono");

    }
    
    /**
     * "rot90" was entered. Rotate the current image 90 degrees. 
     * @param command the command given.
     */
    private void rot90(Command command) {
        
        // R90 = [0 -1, 1 0] rotates around origin
        // (x,y) -> (-y,x)
        // then transate -> (height-y, x)
        int height = currentImage.getHeight();
        int width = currentImage.getWidth();
        ColorImage rotImage = new ColorImage(height, width);
        for (int y=0; y<height; y++) { // in the rotated image
            for (int x=0; x<width; x++) {
                Color pix = currentImage.getPixel(x,y);
                rotImage.setPixel(height-y-1,x, pix);
            }
        }
        currentImage = rotImage;

        filters.add("flipH");
    }
    
    /**
     * The 'script' command runs a sequence of commands from a
     * text file.
     * 
     * IT IS IMPORTANT THAT THIS COMMAND WORKS AS IT CAN BE USED FOR TESTING
     * 
     * @param command the script command, second word of which is the name of 
     * the script file.
     * @return whether to quit.
     */
    private boolean script(Command command) {
        if (!command.hasWord(2)) {
            // if there is no second word, we don't know what to open...
            System.out.println("which script"); 
            return false;
        }
  
        String scriptName = command.getWord(2);
        Parser scriptParser = new Parser();
        try (FileInputStream inputStream = new FileInputStream(scriptName)) {
            scriptParser.setInputStream(inputStream);
            boolean finished = false;
            while (!finished) {
                try {
                    Command cmd = scriptParser.getCommand();
                    finished = processCommand(cmd);
                } catch (Exception ex) {
                    return finished;
                }               
            }
            return finished;
        } 
        catch (FileNotFoundException ex) {
            System.out.println("Cannot find " + scriptName);
            return false;
        }
        catch (IOException ex) {
            throw new RuntimeException("Panic: script barfed!");
        }
    }
    
    /**
     * "Quit" was entered. Check the rest of the command to see whether we
     * really quit the editor.
     * @param command the command given.
     * @return true, if this command quits the editor, false otherwise.
     */
    private boolean quit(Command command) {
        if (command.hasWord(2)) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }
}
