
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.imageio.ImageIO;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

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

    //private Parser parser;
    private ColorImage currentImage;
    private String name;
    private ArrayList<String> filters = new ArrayList<>();
    private Scanner reader;

    private HashMap<String, String> i18nWordsMapping;
    private static String[] EDITORTEXTSKEY = {
        "welcome",
        "goodbye",
        "iDontKnow",
        "youAreUsingFotoshop",
        "cannotFindImageFile",
        "cwdIs",
        "openWhat",
        "loaded",
        "saveWhere",
        "imageSavedTo",
        "currentImageIs",
        "filtersApplied",
        "whichScript",
        "cannotFind",
        "panic",
        "quitWhat"
    };
    
    /**
     * Create the editor and initialise its parser.
     */
    public Editor() {
        //parser = new Parser();
        reader = new Scanner(System.in);
        i18nWordsMapping = returnLanguageHashMap("default");
    }
    public void setInputStream(FileInputStream str) { 
        reader = new Scanner(str);
    }
    /**
     * Returns a HashMap containing the mapping of keywords to specific words 
     * of the specific language the I18N module is set to.
     * @param language E.g. "default" or "japanese" et cetera
     * @return HashMap<String, String> consisting of the key-pair mapping
     * of specific key values to language specific words
     */
    public HashMap<String, String> returnLanguageHashMap(String language){
        HashMap<String, String> languageHashMap = new HashMap();
        I18N.setLanguage(language);

        for(String key : EDITORTEXTSKEY){
            languageHashMap.put(key, I18N.getString(key));
        }
        
        return languageHashMap;
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
            Command command = getCommand();
            finished = processCommand(command);
        }
        // Says: "Thank you for using Fotoshop.  Good bye."
        System.out.println(i18nWordsMapping.get("goodbye"));
    }
    /**
     * @return The next command from the user.
     */
    public Command getCommand()
    {
        String inputLine;   // will hold the full input line
        ArrayList<String> words = new ArrayList<>();

        System.out.print("> ");     // print prompt

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);

        while(tokenizer.hasNext()){
            words.add(tokenizer.next());
        }

        return new Command(words);
    }
    /**
     * Print out the opening message for the user.
     */
    private void printWelcome() {
	//Says:
        //"Welcome to Fotoshop!"
        //"Fotoshop is an amazing new, image editing tool."
        //"Type 'help' if you need help."
        //
        //"The current image is " + name       
        System.out.printf(i18nWordsMapping.get("welcome"), name);
        System.out.println("");

        for(String filter : filters){
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
        // If word inputted is not in list of Command words
        if (!command.isValid()) {
            //Says: "I don't know what you mean..."
            System.out.println(i18nWordsMapping.get("iDontKnow"));
            return false;
        }

        String commandWord = command.getWord(1);

        // As "help" command is different than method name
        // And help() has no parameters it is in its own if statement
        if(commandWord.equals("help")){
            printHelp();
            return false;
        }

        //Here reflection is used to open the classes via the string inputted by user
        try {
            Class c = Class.forName("Editor");
            Class[] cArgs = new Class[1];
            cArgs[0] = Command.class;
            Method method = c.getDeclaredMethod(commandWord.trim().toLowerCase(), cArgs);
            wantToQuit = (boolean)method.invoke(this, command);
        } catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.out.println(e); //<--- DELETE
            return false;
        } catch (NoSuchMethodException e){
            // Says: "I don't know what you mean..."
            System.out.println(i18nWordsMapping.get("iDontKnow"));
        }

        // This is important for quit() and script()
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
        // Says:
        //"You are using Fotoshop."
        //
        //"Your command words are:"
        //"   open save look mono flipH rot90 help quit"
        System.out.printf(i18nWordsMapping.get("youAreUsingFotoshop"), Command.getCommands());
        System.out.println("");
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
            // Says: "Cannot find image file, "
            System.out.printf(i18nWordsMapping.get("cannotFindImageFile"), name);
            System.out.println("");
            // Says: "cwd is " + System.getProperty("user.dir")
            System.out.printf(i18nWordsMapping.get("cwdIs"), System.getProperty("user.dir"));
            System.out.println("");            
        }

        return img;
    }


    /**
     * "open" was entered. Open the file given as the second word of the command
     * and use as the current image.
     * @param command the command given.
     */
    private boolean open(Command command) {
        int fileName = 2;
        if (!command.hasWord(fileName)) {
            // if there is no second word, we don't know what to open...
            // Says: "open what?"
            System.out.println(i18nWordsMapping.get("openWhat"));
            return false;
        }

        String inputName = command.getWord(2);
        ColorImage img = loadImage(inputName);

        if (img == null) {
            printHelp();
        } else {
            currentImage = img;
            name = inputName;
            // Says: "Loaded "
            System.out.printf(i18nWordsMapping.get("loaded"), name);
            System.out.println("");
        }

        return false;
    }

    /**
     * "save" was entered. Save the current image to the file given as the
     * second word of the command.
     * @param command the command given
     */
    private boolean save(Command command) {
        if (currentImage == null) {
            printHelp();
            return false;
        }

        if (!command.hasWord(2)) {
            // if there is no second word, we don't know where to save...
            // Says: "save where?"
            System.out.println(i18nWordsMapping.get("saveWhere"));
            return false;
        }

        String outputName = command.getWord(2);
        try {
            File outputFile = new File(outputName);
            ImageIO.write(currentImage, "jpg", outputFile);
            // Says: "Image saved to " + outputName
            System.out.printf(i18nWordsMapping.get("imageSavedTo"), outputName);
            System.out.println("");
        } catch (IOException e) {
            System.out.println(e.getMessage());
            printHelp();
        }

        return false;
    }

    /**
     * "look" was entered. Report the status of the work bench.
     * @param command the command given.
     */
    private boolean look(Command command) {
        //Says: "The current image is " + name
        System.out.printf(i18nWordsMapping.get("currentImageIs"), name);
        System.out.println("");
        //Says: "Filters applied: "
        System.out.println(i18nWordsMapping.get("filtersApplied") + " ");

        for(String filter: filters){
            if (filter != null) {
                System.out.print(filter + " ");
            }
        }

        System.out.println();
        return false;
    }

    /**
     * "mono" was entered. Convert the current image to monochrome.
     * @param command the command given.
     */
    private void mono(Command command) {

        double redValue = 0.299;
        double greenValue = 0.587;
        double blueValue = 0.114;

        ColorImage tmpImage = new ColorImage(currentImage);
        //Graphics2D g2 = currentImage.createGraphics();
        int height = tmpImage.getHeight();
        int width = tmpImage.getWidth();

        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color pix = tmpImage.getPixel(x, y);
                int lum = (int) Math.round(redValue  *pix.getRed()
                                         + greenValue*pix.getGreen()
                                         + blueValue *pix.getBlue());
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
            // Says: "which script"
            System.out.println(i18nWordsMapping.get("whichScript"));
            return false;
        }

        String scriptName = command.getWord(2);

        try (FileInputStream inputStream = new FileInputStream(scriptName)) {
            setInputStream(inputStream);
            boolean finished = false;
            while (!finished) {
                try {
                    Command cmd = getCommand();
                    finished = processCommand(cmd);
                } catch (Exception ex) {
                    return finished;
                }
            }
            return finished;
        }
        catch (FileNotFoundException ex) {
            // Says: "Cannot find " + scriptName
            System.out.printf(i18nWordsMapping.get("cannotFind"), scriptName);
            System.out.println("");
            return false;
        }
        catch (IOException ex) {
            // Says: "Panic: script barfed!"
            throw new RuntimeException(i18nWordsMapping.get("panic"));
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
            System.out.println(i18nWordsMapping.get("quitWhat"));
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }
}
