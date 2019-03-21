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

    private Parser parser;
    private Image currentImage;
    private String name;
    private boolean finished;

    private Scanner reader;
    private static Editor editor;

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
        "quitWhat",
        "noImageLoaded",
        "noSuchMethod"
    };

    /**
     * Create the editor and initialise its parser.
     */
    private Editor() {
        this.parser = new Parser();
        this.reader = new Scanner(System.in);
        this.i18nWordsMapping = returnLanguageHashMap("default");
        this.finished = false;
    }

    public static Editor getInstence(){
        if(editor == null){
            Editor.editor = new Editor();
        }
        return editor;
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
        while (!this.finished) {
            Command command = this.parser.getCommand(false);
            processCommand(command);
        }
        // Says: "Thank you for using Fotoshop.  Good bye."
        System.out.println(i18nWordsMapping.get("goodbye"));
    }
     /**
     * A method for calling other methods based on an inputted String and Command
     *
     * @param o The Object the methods will be called from
     * @param command The command that will be passed to the method
     */
    public void callMethod(Object o, Command command) {
        // This word should be the name of the method being called
        String commandWord = command.getWord(1).trim();

        try {
            // Name of the class the Object belongs to
            String commandClass = o.getClass().getSimpleName();
            // Class datatype of this class which will hold details of this class
            Class c = Class.forName(commandClass);

            // Method data type is initiated
            Method method;

            if(commandClass.equals("Editor")){
                // An array of the of the Class datatype is made so that getDeclaredMethod
                // will know the datatypes of the arguments passed to it
                Class[] cArgs = { Command.class };
                // The method is created by passing the methods name with the data type of its parameter
                method = c.getDeclaredMethod(commandWord, cArgs);
                // Command is passed to method
                method.invoke(o, command);
            }else{
                // This part is for methods that do not have arguments
                method = c.getDeclaredMethod(commandWord);
                method.invoke(o);
            }

        } catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.out.println(e); //<--- DELETE
        } catch (NoSuchMethodException e) {
            System.out.printf("%s: %s%s%s", i18nWordsMapping.get("noSuchMethod"), "\"", commandWord, "\"");
        }
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
        System.out.println();
    }

    /**
     * Given a command, edit (that is: execute) the command.
     *
     * @param command The command to be processed.
     */
    private void processCommand(Command command) {
        // If word inputted is not in list of Command words
        if (!command.isValid()) {
            System.out.println(i18nWordsMapping.get("iDontKnow"));
            return;
        }

        if ((command.getCommandClass()).equals("Editor")) {
            this.callMethod(this, command);
        } else if ((command.getCommandClass()).equals("Image")) {
            if (this.currentImage != null) {
                this.currentImage.addChanges( this.currentImage.getImage() );
                callMethod(this.currentImage, command);
            } else {
                System.out.println("No Image is currently loaded at the moment");
                //System.out.printf(i18nWordsMapping.get("noImage"));
            }
        }
    }

//----------------------------------
// Implementations of user commands:
//----------------------------------

    /**
     * Print out some help information. Here we print some useless, cryptic
     * message and a list of the command words.
     */
    private void help(Command command) {
        System.out.printf(i18nWordsMapping.get("youAreUsingFotoshop"), Command.getCommands());
        System.out.println();
        return;
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
            System.out.println();
            // Says: "cwd is " + System.getProperty("user.dir")
            System.out.printf(i18nWordsMapping.get("cwdIs"), System.getProperty("user.dir"));
            System.out.println();
        }

        return img;
    }


    /**
     * "open" was entered. Open the file given as the second word of the command
     * and use as the current image.
     * @param command the command given.
     */
    private void open(Command command) {
        int fileName = 2;
        if (!command.hasWord(fileName)) {
            // if there is no second word, we don't know what to open...
            System.out.println(i18nWordsMapping.get("openWhat"));
            return;
        }

        String inputName = command.getWord(2);
        ColorImage img = loadImage(inputName);

        if (img == null) {
            help(command);
        } else {
            currentImage = new Image(img);
            // TODO: put image name in the Image class
            name = inputName;
            // Says: "Loaded "
            System.out.printf(i18nWordsMapping.get("loaded"), name);
            System.out.println();
        }
    }

    /**
     * "save" was entered. Save the current image to the file given as the
     * second word of the command.
     * @param command the command given
     */
    private void save(Command command) {
        if (currentImage == null) {
            help(command);
            return;
        }

        if (!command.hasWord(2)) {
            // if there is no second word, we don't know where to save...
            // Says: "save where?"
            System.out.println(i18nWordsMapping.get("saveWhere"));
            return;
        }

        String outputName = command.getWord(2);
        try {
            File outputFile = new File(outputName);
            ImageIO.write(currentImage.getImage(), "jpg", outputFile);
            System.out.printf(i18nWordsMapping.get("imageSavedTo"), outputName);
            System.out.println();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            help(command);
        }
    }

    /**
     * "look" was entered. Report the status of the work bench.
     */
    private void look(Command command) {
        if (currentImage == null) {
          System.out.println(i18nWordsMapping.get("noImageLoaded"));
          return;
        }

        System.out.printf(i18nWordsMapping.get("currentImageIs"), name);
        System.out.println();
        System.out.print(i18nWordsMapping.get("filtersApplied") + " ");
        System.out.println();

        for (String filter: currentImage.getFilters()) {
            if (filter != null) {
                System.out.print(filter + " ");
            }
        }

        System.out.println();
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
    private void script(Command command) {
        if (!command.hasWord(2)) {
            // if there is no second word, we don't know what to open...
            System.out.println(i18nWordsMapping.get("whichScript"));
            return;
        }

        String scriptName = command.getWord(2);
        Parser scriptParser = new Parser();

        try (FileInputStream inputStream = new FileInputStream(scriptName)) {
            scriptParser.setInputStream(inputStream);
            while (!this.finished) {
                try {
                    Command cmd = scriptParser.getCommand(true);
                    processCommand(cmd);
                } catch (Exception ex) {
                    return;
                }
            }
            // Reset the internal state of execution - the _script_ has finished, not the program
            this.finished = false;
            return;
        } catch (FileNotFoundException ex) {
            System.out.printf(i18nWordsMapping.get("cannotFind"), scriptName);
            System.out.println();
            return;
        } catch (IOException ex) {
            throw new RuntimeException(i18nWordsMapping.get("panic"));
        }
    }

    /**
     * "Quit" was entered. Check the rest of the command to see whether we
     * really quit the editor.
     * @param command the command given.
     * @return true, if this command quits the editor, false otherwise.
     */
    private void quit(Command command) {
        if (command.hasWord(2)) {
            System.out.println(i18nWordsMapping.get("quitWhat"));
            return;
        } else {
          this.finished = true;
        }
    }

  /**
   * "undo" was entered. Reverts the current image to its most recent state
   * @param command the command given
   * @return true, if this command quits the editor, false otherwise.
   */
    private void undo(Command command) {
        if (command.hasWord(2)) {
          System.out.println("'redo' method accepts 0 parameters.");
          return;
        }

      if (!this.currentImage.getChanges().isEmpty()){
          this.currentImage.setImage(this.currentImage.getChanges().pop());
          System.out.println("Image reverted to previous state.");
      } else {
          System.out.println("There is nothing to undo!");
      }
      return;
    }
}
