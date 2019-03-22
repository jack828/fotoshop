package command;

import editor.Editor;

public class QuitCommand extends Command {
  /**
   * Quit. Let the editor know we've finished looping, if no second word is present.
   */
  public void execute(Editor editor) {
    if (this.hasWord(2)) {
      System.out.println(editor.getI18nMap().get("quitWhat"));
    } else {
      editor.setFinished(true);
    }
  }
}
