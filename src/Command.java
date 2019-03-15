
import java.util.ArrayList;

/**
 * This class is taken from the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 *
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */

public class Command
{
    private ArrayList<String> words = new ArrayList<String>();

    private static final String[] editorCommands = {
      "open", "save", "look", "help", "quit", "script"
    };
    private static final String[] imageCommands = {
      "mono", "rot90"
    };

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     * @param command An array of words in the command
     */
    public Command(ArrayList<String> command) {
      this.words = command;
    }

    /**
     * Get the class to which the command is referring to
     * @return Either "Editor" or "Image", depending on
     *         which class the command corresponds to
     */
    public String getCommandClass() {
      for (String command : editorCommands) {
        if (command.equals(this.words.get(0))) {
          return "Editor";
        }
      }

      for (String command : imageCommands) {
        if (command.equals(this.words.get(0))) {
          return "Image";
        }
      }
      return null;
    }
    /**
     * Validate the command
     *
     * @return whether or not this command is valid or not
     */
    public boolean isValid () {
      return this.getCommandClass() != null;
    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @param index position of word in ArrayList
     * @return The command word.
     */
    public String getWord(int index) {
      return this.words.get(index-1);
    }

    /**
     * @param index position of word in ArrayList
     * @return true if this command was not understood.
     */
    public boolean hasWord(int index) {
      return this.words.size() >= index;
    }

    /**
     * Returns a string representation of the command words
     *
     * @return A string representation of the command words
     */

    public static String getCommands() {
      String output = "";

      for (String command : editorCommands) {
        output += command + ' ';
      }

      for (String command : imageCommands) {
        output += command + ' ';
      }

      return output;
    }
}

