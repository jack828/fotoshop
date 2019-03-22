package command;

import editor.Editor;
import image.Image;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class SaveCommand extends Command {
  /**
   * Save the current image to the file given as the
   * second word of the command.
   */
  public void execute(Editor editor) {
    Image image = editor.getImage();
    if (image == null) {
      System.out.println(editor.getI18nMap().get("noImageLoaded"));
      return;
    }

    if (!this.hasWord(2)) {
      System.out.println(editor.getI18nMap().get("saveWhere"));
      return;
    }

    String outputName = this.getWord(2);
    try {
      File outputFile = new File(outputName);
      ImageIO.write(image.getImage(), "jpg", outputFile);
      System.out.printf(editor.getI18nMap().get("imageSavedTo"), outputName);
      System.out.println();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
