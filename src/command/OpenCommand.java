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
  @Override
  public void execute(Editor editor) {
    if (!this.hasWord(2)) {
      // if there is no second word, we don't know what to open...
      editor.print("openWhat");
      return;
    }

    String inputName = this.getWord(2);
    ColorImage img = null;

    try {
      img = new ColorImage(ImageIO.read(new File(inputName)));
    } catch (IOException e) {
      editor.print("cannotFindImageFile", inputName);

      editor.print("cwdIs", System.getProperty("user.dir"));
    }

    if (img != null) {
      Image image = new Image(inputName, img);

      editor.setImage(image);

      editor.print("loaded", inputName);
    }
  }
}
