package command;

import editor.Editor;

public class QuitCommand extends Command {
  /**
   * Quit. Let the editor know we've finished looping, if no second word is present.
   */
  @Override
  public void execute(Editor editor) {
    if (this.hasWord(2)) {
      editor.print("quitWhat");
    } else {
      editor.setFinished(true);
    }
  }
}
