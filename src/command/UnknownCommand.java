package command;

import editor.Editor;

public class UnknownCommand extends Command {
  /**
   * Unknown command
   */
  @Override
  public void execute(Editor editor) {
    editor.print("iDontKnow");
  }
}
