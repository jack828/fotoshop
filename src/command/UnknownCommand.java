package command;

import editor.Editor;

public class UnknownCommand extends Command {
  /**
   * Quit. Let the editor know we've finished looping, if no second word is present.
   */
  public void execute(Editor editor) {
      System.out.println(editor.getI18nMap().get("iDontKnow"));
  }
}
