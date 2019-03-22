 package command;

import editor.Editor;
import image.ColorImage;
import image.Image;

import java.awt.*;

 public class FlipHCommand extends Command {
   /**
    * Flip image horizontally
    * @param editor The editor instance
    */
   @Override
   public void execute(Editor editor) {
       Image image = editor.getImage();
       if (image == null) {
         editor.print("noImageLoaded");
         return;
       }

       int height = image.getImage().getHeight();
       int width = image.getImage().getWidth();
       ColorImage tmpImage = new ColorImage(width, height);

       for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
           Color pixel = image.getImage().getPixel(x, y);
           tmpImage.setPixel(width - x - 1, y, pixel);
         }
       }
       // Add the original image to the change stack
       image.addChanges(image.getImage());
       image.setImage(tmpImage);
       image.addFilter("flipH");
   }
 }
