package command;

import editor.Editor;
import image.Image;

public class LookCommand extends Command {
  /**
   * Report the status of the work bench
   */
  @Override
  public void execute(Editor editor) {
    Image image = editor.getImage();
    if (image == null) {
      editor.print("noImageLoaded");
      return;
    }

    editor.print("currentImageIs", image.getName());

    StringBuilder output = new StringBuilder();
    for (String filter : image.getFilters()) {
      if (filter != null) {
        output.append(filter);
        output.append(' ');
      }
    }

    editor.print("filtersApplied", output.toString());
  }
}
