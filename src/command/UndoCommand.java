package command;

import editor.Editor;
import image.Image;

public class UndoCommand extends Command {
  /**
   * Reverts the current image to its most recent state
   * @param editor The editor instance
   */
  public void execute(Editor editor) {
    Image image = editor.getImage();

    if (this.hasWord(2)) {
      System.out.println(editor.getI18nMap().get("redoMethodPrompt"));
      return;
    }

    if (!image.getChanges().isEmpty()){
      image.setImage(image.getChanges().pop());

      image.undoFilter();

      editor.setImage(image);
      System.out.println(editor.getI18nMap().get("imageRevertedPrompt"));
    } else {
      System.out.println(editor.getI18nMap().get("nothingToUndoPrompt"));
    }
  }
}