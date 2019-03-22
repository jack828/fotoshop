package command;

import editor.Editor;

public class HelpCommand extends Command {
  /**
   * Print out some help information. Here we print some useless, cryptic
   * message and a list of the command words.
   */
  public void execute(Editor editor) {
    System.out.printf(editor.getI18nMap().get("youAreUsingFotoshop"), editor.getCommands());
  }
}
