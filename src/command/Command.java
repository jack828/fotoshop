package command;

import editor.Editor;
import image.Image;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class is an abstract superclass for all commands in the editor.
 * Each command should implement it's own subclass, extending the execute method.
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

public abstract class Command {
    private ArrayList<String> words = new ArrayList<String>();

    private static final String[] editorCommands = {
      "open", "save", "look", "help", "quit", "script", "undo", "put", "get"
    };
    private static final String[] imageCommands = {
      "mono","flipH","flipV", "rot90"
    };

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     * @param words An array of words in the command
     */
    public Command(ArrayList<String> words) {
      this.words = words;
    }

    /**
     * Create a command object.
     */
    public Command() {}

    /**
     * Set the command's words
     *
     * @param words The command's words
     */
    public void setWords (ArrayList<String> words) {
      this.words = words;
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
     * Execute the command
     * @param editor The editor instance
     */
    public abstract void execute(Editor editor);
}

