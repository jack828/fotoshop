package editor;

import command.*;
import image.ColorImage;
import image.Image;
import parser.Parser;
import i18n.I18N;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
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
    private boolean finished;

    private HashMap<String, Image> imageCache;
    private HashMap<String, String> i18nWordsMapping;

    private HashMap<String, Command> commands;

    private static String[] EDITORTEXTSKEY = {
        "welcome",
        "goodbye",
        "iDontKnow",
        "youAreUsingFotoshop",
        "cannotFindImageFile",
        "cwdIs",
        "openWhat",
        "putWhat",
        "loaded",
        "saveWhere",
        "imageSavedTo",
        "currentImageIs",
        "filtersApplied",
        "whichScript",
        "cannotFind",
        "cannotFindKey",
        "panic",
        "quitWhat",
        "noImageLoaded",
        "noSuchMethod",
        "redoMethodPrompt",
        "imageRevertedPrompt",
        "nothingToUndoPrompt",
        "imageInCache",
        "watermarkError"
    };

    /**
     * Create the editor and initialise its parser.
     */
    public Editor() {
        this.parser = new Parser();
        this.i18nWordsMapping = returnLanguageHashMap("default");
        this.imageCache = new HashMap<>();
        this.finished = false;

        this.commands = new HashMap<String, Command>();

        // Editor commands
        commands.put("quit", new QuitCommand());
        commands.put("open", new OpenCommand());
        commands.put("save", new SaveCommand());
        commands.put("help", new HelpCommand());
        commands.put("look", new LookCommand());
        commands.put("script", new ScriptCommand());
        commands.put("undo", new UndoCommand());
        commands.put("put", new PutCommand());
        commands.put("get", new GetCommand());

        // Image commands
        commands.put("mono", new MonoCommand());
        commands.put("rot90", new Rot90Command());
        commands.put("flipH", new FlipHCommand());
        commands.put("flipV", new FlipVCommand());
        commands.put("watermark", new WatermarkCommand());
    }

    /**
     * Get the state of the main loop
     * @return If the program should exit or not
     */
    public boolean getFinished() {
      return this.finished;
    }

    /**
     * Set the state of the main loop
     * @param finished If the program should exit or not 
     */
    public void setFinished(boolean finished) {
      this.finished = finished;
    }

    /**
     * Sets the current working image
     * @param image Image type which will be set as current image
     */
    public void setImage(Image image) {
      this.currentImage = image;
    }

    /**
     * Gets the current working image
     */
    public Image getImage() {
      return this.currentImage;
    }

    /**
     * Get the I18N word map
     * @return I18N HashMap which holds the messages which will be printed to 
     * the user
     */
    public HashMap<String, String> getI18nMap() {
      return this.i18nWordsMapping;
    }

    /**
     * Put an image to the cache
     * @param key What the user will type to load the image
     * @param image The image that will be added to the cache
     */
    public void addToImageCache(String key, Image image) {
      this.imageCache.put(key, image);
    }

    /**
     * Get an image from the cache
     * @param key What the image is called in the cache
     * @return An Image which the user previously saved under the key name
     */
    public Image getFromImageCache(String key) {
      return this.imageCache.get(key);
    }

    /**
     * Get a string representation of the available commands
     * @return A string representation of the commands which can be used
     */
    public String getCommands() {
      StringBuilder output = new StringBuilder();
      for (String key : this.commands.keySet()) {
        output.append(key);
        output.append(' ');
      }
      return output.toString();
    }

    /**
     *
     * @param i18nKey i18n key for i18nWordsMapping HashMap
     * @param formats Strings to be added into placeholders 
     */
    public void print(String i18nKey, String ... formats) {
      System.out.printf(i18nWordsMapping.get(i18nKey), formats);
    }

    /**
     * Returns a HashMap containing the mapping of keywords to specific words
     * of the specific language the i18n.I18N module is set to.
     * @param language E.g. "default" or "japanese" et cetera
     * @return HashMap<String, String> consisting of the key-pair mapping
     * of specific key values to language specific words
     */
    public HashMap<String, String> returnLanguageHashMap(String language){
        HashMap<String, String> languageHashMap = new HashMap();
        I18N.setLanguage(language);

        for (String key : EDITORTEXTSKEY) {
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
            ArrayList<String> commandWords = this.parser.getCommand(false);
            processCommand(commandWords);
        }
        this.print("goodbye");
    }

    /**
     * Print out the opening message for the user.
     */
    private void printWelcome() {
        this.print("welcome");
    }

    /**
     * Given a command, edit (that is: execute) the command.
     *
     * @param commandWords The command to be processed.
     */
    public void processCommand(ArrayList<String> commandWords) {
        Command command = this.commands.get(commandWords.get(0));

        if (command == null) {
          command = new UnknownCommand();
        }

        command.setWords(commandWords);

        command.execute(this);
    }
}
