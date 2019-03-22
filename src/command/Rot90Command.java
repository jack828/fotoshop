 package command;

import editor.Editor;
import image.ColorImage;
import image.Image;

import java.awt.*;

 public class Rot90Command extends Command {
   /**
    * Rotate the image 90 degrees
    * @param editor The editor instance
    */
   public void execute(Editor editor) {
       Image image = editor.getImage();
       if (image == null) {
          System.out.println(editor.getI18nMap().get("noImageLoaded"));
          return;
       }
       // R90 = [0 -1, 1 0] rotates around origin
       // (x, y) -> (-y, x)
       // then translate -> (height - y, x)
       int height = image.getImage().getHeight();
       int width = image.getImage().getWidth();
       ColorImage tmpImage = new ColorImage(height, width);

       for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
           Color pixel = image.getImage().getPixel(x, y);
           tmpImage.setPixel(height - y - 1, x, pixel);
         }
       }
       // Add the original image to the change stack
       image.addChanges(image.getImage());
       image.setImage(tmpImage);
       image.addFilter("rot90");
   }
 }
