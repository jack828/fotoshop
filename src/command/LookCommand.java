package command;

import editor.Editor;
import image.Image;

public class LookCommand extends Command {
  /**
   * Report the status of the work bench
   */
  public void execute(Editor editor) {
    Image image = editor.getImage();
    if (image == null) {
      editor.print("noImageLoaded");
      return;
    }

    editor.print("currentImageIs", image.getName());

    String output = "";
    for (String filter : image.getFilters()) {
      if (filter != null) {
        output += filter + " ";
      }
    }

    editor.print("filtersApplied", output);
  }
}
