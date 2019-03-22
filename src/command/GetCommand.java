 package command;

import editor.Editor;
import image.Image;

public class GetCommand extends Command {
  /**
   * Replaces the current working image one from the image cache, using a string to identify it.
   * @param editor The editor instance
   */
  @Override
  public void execute(Editor editor) {
      if (!this.hasWord(2)) {
        editor.print("getWhat");
        return;
      }

      Image imageToSet = editor.getFromImageCache(this.getWord(2));

      if (imageToSet == null) {
        editor.print("cannotFindKey", this.getWord(2));
        return;
      }

      editor.setImage(imageToSet);
      editor.print("currentImageIs", this.getWord(2));
  }
}
