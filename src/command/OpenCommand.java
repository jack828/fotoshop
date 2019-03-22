package command;

import editor.Editor;
import image.ColorImage;
import image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class OpenCommand extends Command {
  /**
   * Open the file given as the second word of the command
   * and use as the current image.
   * @param editor the editor instance
   */
  public void execute(Editor editor) {
    if (!this.hasWord(2)) {
      // if there is no second word, we don't know what to open...
      System.out.println(editor.getI18nMap().get("openWhat"));
      return;
    }

    String inputName = this.getWord(2);
    ColorImage img = null;

    try {
      img = new ColorImage(ImageIO.read(new File(inputName)));
    } catch (IOException e) {
      System.out.printf(editor.getI18nMap().get("cannotFindImageFile"), inputName);

      System.out.printf(editor.getI18nMap().get("cwdIs"), System.getProperty("user.dir"));
    }

    if (img != null) {
      Image image = new Image(inputName, img);

      editor.setImage(image);

      System.out.printf(editor.getI18nMap().get("loaded"), inputName);
      System.out.println();
    }
  }
}
