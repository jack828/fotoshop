package command;

import editor.Editor;

public class PutCommand extends Command {
  /**
   * Places a copy of the current working image into the image cache, using a string to identify it.
   * @param editor The editor instance
   */
  public void execute(Editor editor) {
      if (!this.hasWord(2)) {
        System.out.println(editor.getI18nMap().get("putWhat"));
        return;
      }

      editor.addToImageCache(this.getWord(2), editor.getImage());
      System.out.println("Placed current working image into cache");
  }
}
