

import java.awt.*;
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
import java.util.Stack;

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
        "quitWhat",
        "noImageLoaded",
        "noSuchMethod"
    };

    /**
     * Create the editor and initialise its parser.
     */
    public Editor() {
        parser = new Parser();
        reader = new Scanner(System.in);
        i18nWordsMapping = returnLanguageHashMap("default");
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
      // TODO: move to class variable?
        boolean finished = false;
        while (!finished) {
            Command command = getCommand();
            finished = processCommand(command);
        }
        // Says: "Thank you for using Fotoshop.  Good bye."
        System.out.println(i18nWordsMapping.get("goodbye"));
    }
     /**
     * A method for calling other methods based on an inputted String and Command
     * 
     * @param o The Object the methods will be called from
     * @param command The command that will be passed to the method
     * @return A Boolean either given by the method called or False
     */
    public Boolean callMethod(Object o, Command command) {
        // This word should be the name of the method being called
        String commandWord = command.getWord(1).trim();
        Boolean result = false;
        
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
                return (Boolean) method.invoke(o, command);
            }else{
                method = c.getDeclaredMethod(commandWord);
                method.invoke(o);
            }

        } catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            System.out.println(e); //<--- DELETE
        } catch (NoSuchMethodException e) {
            System.out.printf("%s: %s%s%s", i18nWordsMapping.get("noSuchMethod"), "\"", commandWord, "\"");
        }
        return result; 
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
        System.out.printf(i18nWordsMapping.get("welcome"), name);
        System.out.println();
    }

    /**
     * Given a command, edit (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the editing session, false otherwise.
     */
    private boolean processCommand(Command command) {

        Boolean wantToQuit = false;
        // If word inputted is not in list of Command words
        if (!command.isValid()) {
            System.out.println(i18nWordsMapping.get("iDontKnow"));
            return wantToQuit;
        }

        if((command.getCommandClass()).equals("Editor")){
            wantToQuit = callMethod(this,command);
        }else if((command.getCommandClass()).equals("Image")){
            if(this.currentImage != null){
                //System.out.println("aaaa");
                this.currentImage.addChanges( this.currentImage.getImage() );
                wantToQuit = callMethod(this.currentImage,command);
            }else{
                System.out.println("No Image is currently loaded at the moment");
                //System.out.printf(i18nWordsMapping.get("noImage"));
            }
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
    private boolean help(Command command) {
        System.out.printf(i18nWordsMapping.get("youAreUsingFotoshop"), Command.getCommands());
        return false;
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
            help(command);
        } else {
            currentImage = new Image(img);
            // TODO: put image name in the Image class
            name = inputName;
            // Says: "Loaded "
            System.out.printf(i18nWordsMapping.get("loaded"), name);
            System.out.println();
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
            help(command);
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
            ImageIO.write(currentImage.getImage(), "jpg", outputFile);
            System.out.printf(i18nWordsMapping.get("imageSavedTo"), outputName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            help(command);
        }

        return false;
    }

    /**
     * "look" was entered. Report the status of the work bench.
     */
    private boolean look(Command command) {
        if (currentImage == null) {
          System.out.println(i18nWordsMapping.get("noImageLoaded"));
          return false;
        }
        System.out.printf(i18nWordsMapping.get("currentImageIs"), name);
        System.out.print(i18nWordsMapping.get("filtersApplied") + " ");

        for(String filter: currentImage.getFilters()){
            if (filter != null) {
                System.out.print(filter + " ");
            }
        }

        System.out.println();
        return false;
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
            // Says: "Cannot find " + scriptName
            System.out.printf(i18nWordsMapping.get("cannotFind"), scriptName);
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

  /**
   * "undo" was entered. Reverts the current image to its most recent state
   * @param command the command given
   * @return true, if this command quits the editor, false otherwise.
   */
    private boolean undo(Command command) {
      if (command.hasWord(2)) {
        System.out.println("'redo' method accepts 0 parameters.");
        return false;
      }

      if (!this.currentImage.getChanges().isEmpty()){
          this.currentImage.setImage(this.currentImage.getChanges().pop());
          System.out.println("Image reverted to previous state.");

          return false;
        }

      System.out.println("There is nothing to undo!");
      return false;
    }
}
