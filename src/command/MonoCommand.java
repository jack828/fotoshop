 package command;

import editor.Editor;
import image.ColorImage;
import image.Image;

import java.awt.*;

 public class MonoCommand extends Command {
   /**
    * Convert current image to monochrome,
    * and push a change to the stack
    *
    * @param editor The editor instance
    */
   public void execute(Editor editor) {
     Image image = editor.getImage();
     if (image == null) {
       System.out.println(editor.getI18nMap().get("noImageLoaded"));
       return;
     }

     double redValue = 0.299;
     double greenValue = 0.587;
     double blueValue = 0.114;

     ColorImage tmpImage = new ColorImage(image.getImage());
     int height = tmpImage.getHeight();
     int width = tmpImage.getWidth();

     for (int y = 0; y < height; y++) {
       for (int x = 0; x < width; x++) {
         Color pixel = tmpImage.getPixel(x, y);
         int lum = (int) Math.round(
           redValue * pixel.getRed()
             + greenValue * pixel.getGreen()
             + blueValue * pixel.getBlue());
         tmpImage.setPixel(x, y, new Color(lum, lum, lum));
       }
     }
     // Add the original image to the change stack
     image.addChanges(image.getImage());
     image.setImage(tmpImage);
     image.addFilter("mono");
   }
 }
