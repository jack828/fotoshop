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
      System.out.println(editor.getI18nMap().get("noImageLoaded"));
      return;
    }

    System.out.printf(editor.getI18nMap().get("currentImageIs"), image.getName());
    System.out.println();
    System.out.print(editor.getI18nMap().get("filtersApplied") + " ");
    System.out.println();

    for (String filter : image.getFilters()) {
      if (filter != null) {
        System.out.print(filter + " ");
      }
    }

    System.out.println();
  }
}
