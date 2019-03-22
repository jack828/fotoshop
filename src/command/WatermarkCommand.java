 package command;

import editor.Editor;
import image.ColorImage;
import image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

 public class WatermarkCommand extends Command {
   /**
    * Flip image horizontally
    * @param editor The editor instance
    */
   public void execute(Editor editor) {
     Image image = editor.getImage();
     if (image == null) {
       editor.print("noImageLoaded");
       return;
     }

     try {
       // The intensity which the watermark is to be displayed
       double intensity = 0.5;
       // Watermark file
       String wmFile = "watermark.png";

       // The image that is loaded by the user
       ColorImage tmpImage = new ColorImage(image.getImage());
       int height = tmpImage.getHeight();
       int width = tmpImage.getWidth();

       // The image which will create the watermark
       // Every pixel with a green value over 25o will be ignored
       ColorImage wmImage = new ColorImage(ImageIO.read(new File(wmFile)));
       int wmHeight = wmImage.getHeight();
       int wmWidth = wmImage.getWidth();

       // The ratio determines how much the watermark picture will be scaled up or down
       double ratio;
       if (wmWidth > wmHeight) {
         ratio = (double) width / (double) wmWidth;
       } else {
         ratio = (double) height / (double) wmHeight;
       }

       // The heights of the new watermark image
       int newWmWidth = (int)(wmWidth * ratio);
       int newWmHeight = (int)(wmHeight * ratio);

       // The space from the edge so the the watermark is centered
       int spaceX = (width - newWmWidth) / 2;
       int spaceY = (height - newWmHeight) / 2;

       // These values will be where samples from the watermark image are taken
       int wmX = 0;
       int wmY = 0;

       // Both these loops go through every pixel of the image
       for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
           // Watermark pixel color is initialised
           Color wmPixel;
           double i = 1;

           // If the pixel on the main image is less than the space the
           // intensity is kept at 1
           if (x < spaceX || y < spaceY) {
             i = 1;
           } else {
             // Else the intensity is worked here
             // Which pixels to be selected based on the ratio and spacing
             wmX = (int)((double)(x - spaceX) / ratio);
             wmY = (int)((double)(y - spaceY) / ratio);
             // This if statement is error avoidence so wmX & wmY don't
             // go over the limit of the picture
             if (wmX < wmWidth && wmY < wmHeight) {
               // The pixel is selected
               wmPixel = wmImage.getPixel(wmX, wmY);
               // If it's green then it will stay at 1 and the normal image
               // will be shown for that park
               int wmGreen = wmPixel.getGreen();
               int wmBlue = wmPixel.getBlue();
               int wmRed = wmPixel.getRed();
               i = (wmGreen > 250 && wmBlue < 75 && wmRed  < 75) ? 1 : intensity;
             }
           }
           // The pixels are multiplied by i to make them lighter/darker where
           // the watermark is.
           Color pixel = tmpImage.getPixel(x, y);
           int red = (pixel.getRed() * i > 255) ? 255 : (int)(pixel.getRed() * i);
           int green = (pixel.getGreen() * i > 255) ? 255 : (int)(pixel.getGreen() * i);
           int blue = (pixel.getBlue() * i > 255) ? 255 : (int)(pixel.getBlue() * i);

           tmpImage.setPixel(x, y, new Color(red, green, blue));
         }
       }

       // Add the original image to the change stack
       image.addChanges(image.getImage());
       image.setImage(tmpImage);
       image.addFilter("watermark");
     } catch (IOException ex) {
      editor.print("watermarkError");
     }
   }
 }
