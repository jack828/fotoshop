package command;

import editor.Editor;

public class HelpCommand extends Command {
  /**
   * Print out some help information. Here we print some useless, cryptic
   * message and a list of the command words.
   */
  @Override
  public void execute(Editor editor) {
    editor.print("youAreUsingFotoshop", editor.getCommands());
  }
}
