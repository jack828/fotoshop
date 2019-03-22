package command;

import editor.Editor;

public class PutCommand extends Command {
  /**
   * Places a copy of the current working image into the image cache, using a string to identify it.
   * @param editor The editor instance
   */
  @Override
  public void execute(Editor editor) {
      if (!this.hasWord(2)) {
        editor.print("putWhat");
        return;
      }

      editor.addToImageCache(this.getWord(2), editor.getImage());
      editor.print("imageInCache");
  }
}
