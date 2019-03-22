 package command;

import editor.Editor;
import image.Image;

public class GetCommand extends Command {
  /**
   * Replaces the current working image one from the image cache, using a string to identify it.
   * @param editor The editor instance
   */
  public void execute(Editor editor) {
      if (!this.hasWord(2)) {
        System.out.println(editor.getI18nMap().get("getWhat"));
        return;
      }

      Image imageToSet = editor.getFromImageCache(this.getWord(2));

      if (imageToSet == null) {
        System.out.printf(editor.getI18nMap().get("cannotFindKey"), this.getWord(2));
        return;
      }

      editor.setImage(imageToSet);
      System.out.printf(editor.getI18nMap().get("currentImageIs"), this.getWord(2));
  }
}
