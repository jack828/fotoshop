package command;

import editor.Editor;
import image.Image;

import java.util.ArrayList;

/**
 * This class is an abstract superclass for all commands in the editor.
 * Each command should implement it's own subclass, extending the execute method.
 *
 * Calling the implementing method requires no knowledge about the implementation.
 *
 * @author  Jack Burgess
 * @version 2019.03.22
 */

public abstract class Command {
    private ArrayList<String> words = new ArrayList<String>();
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

