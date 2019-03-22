package command;

import editor.Editor;
import image.Image;

public class UndoCommand extends Command {
  /**
   * Reverts the current image to its most recent state
   * @param editor The editor instance
   */
  @Override
  public void execute(Editor editor) {
    Image image = editor.getImage();

    if (this.hasWord(2)) {
      editor.print("redoMethodPrompt");
      return;
    }
    if(image == null){
        editor.print("noImageLoaded");
        return;
    }
    if (!image.getChanges().isEmpty()){
      image.setImage(image.getChanges().pop());

      image.undoFilter();

      editor.setImage(image);
      editor.print("imageRevertedPrompt");
    } else {
      editor.print("nothingToUndoPrompt");
    }
  }
}
