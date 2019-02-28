
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
    private ArrayList<String> commandWords = new ArrayList<String>();
    private String commandWord;
    private String secondWord;
    private String thirdWord;

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     * @param firstWord The first word of the command. Null if the command
     *                  was not recognised.
     * @param secondWord The second word of the command.
     * @param thirdWord The second word of the command.
     */
    public Command(ArrayList<String> command)
    {
        commandWords = command;

    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @param index position of word in ArrayList
     * @return The command word.
     */
    public String getWord(int index)
    {
        return commandWords.get(index-1);
    }

    /**
     * @param index position of word in ArrayList
     * @return true if this command was not understood.
     */
    public boolean hasWord(int index)
    {
        return (commandWords.size() >= index);
    }
}

