package parser;

import command.Command;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * This class is taken from the "World of Zuul" application.
 * "World of Zuul" is a very simple, text based adventure game.
 *
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a three word command. It returns the command
 * as an object of class command.Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 *
 * @author  Michael Kolling and David J. Barnes
 * @version 2006.03.30
 */
public class Parser
{
    private Scanner reader;         // source of command input

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser()
    {
        reader = new Scanner(System.in);
    }

    public void setInputStream(FileInputStream str) {
        reader = new Scanner(str);
    }
    /**
     * Get an array of words from the user
     * @param hidePrompt whether or not to show the prompt
     * @return The next words from the input.
     */
    public ArrayList<String> getCommand(boolean hidePrompt)
    {
        String inputLine;   // will hold the full input line
        ArrayList<String> words = new ArrayList<>();

        if (!hidePrompt) {
          System.out.print("> ");
        }

        inputLine = reader.nextLine();

        // Find up to two words on the line.
        Scanner tokenizer = new Scanner(inputLine);

        while(tokenizer.hasNext()){
            words.add(tokenizer.next());
        }

        return words;
    }
}
