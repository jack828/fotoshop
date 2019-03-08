
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

    private Parser parser;
    private Image currentImage;
    private String name;

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
      // TODO: move to class variable?
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
      // TODO: print current working directory
        System.out.println("Welcome to Fotoshop!\nFotoshop is an amazing new image editing tool.\n" +
          "Type 'help' if you need help.\n\nThe current image is: " + name + "\nFilters applied: none");
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
        System.out.println("I don't know what you mean..");
        return wantToQuit;
      }

      String commandWord = command.getWord(1);

      String commandClass = command.getCommandClass();

      try {
          Class c = Class.forName(commandClass);
          Class[] cArgs = { Command.class };
          Method method;
          Boolean result = false;

          if (commandClass.equals("Editor")) {
            method = c.getDeclaredMethod(commandWord.trim().toLowerCase(), cArgs);
            result = (Boolean) method.invoke(this, command);
          } else {
            method = c.getDeclaredMethod(commandWord.trim().toLowerCase());
            method.invoke(this.currentImage);
          }
          // TODO remove
          if (result == null) wantToQuit = false;
          else wantToQuit = result;
      } catch (ClassNotFoundException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          System.out.println(e); //<--- DELETE
          return wantToQuit;
      } catch (NoSuchMethodException e) {
          System.out.println("No such method: \"" + commandWord + "\"");
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
    private void help(Command command) {
      System.out.println("You are using Fotoshop.\n" + "Your command words are:\n" + Command.getCommands());
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
    private boolean open(Command command) {
        int fileName = 2;
        if (!command.hasWord(fileName)) {
            // if there is no second word, we don't know what to open...
            System.out.println("open what?");
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
            System.out.println("Loaded " + name);
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
            System.out.println("save where?");
            return false;
        }

        String outputName = command.getWord(2);
        try {
            File outputFile = new File(outputName);
            ImageIO.write(currentImage.getImage(), "jpg", outputFile);
            System.out.println("Image saved to " + outputName);
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
          System.out.println("No image loaded");
          return false;
        }
        System.out.println("The current image is " + name);
        System.out.print("Filters applied: ");

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
