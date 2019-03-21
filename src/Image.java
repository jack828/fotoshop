import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * This class contains a reference to the current image, and allows
 * actions to be performed on it.
 *
 * @author Jack Burgess
 * @version 2019.3.8
 */

public class Image {

 private ColorImage image;
 private ArrayList < String > filters;
 private Stack < ColorImage > changes;

 public Image(ColorImage image) {
  this.image = image;
  this.filters = new ArrayList < > ();
  this.changes = new Stack();
 }

 /**
  * Return the image reference
  * @return The image reference
  */
 public ColorImage getImage() {
  return this.image;
 }

 /**
  * Return the currently applied filters
  * @return An array of the currently applied filters
  */
 // TODO: _should_ this return a string representation?
 public ArrayList < String > getFilters() {
  return filters;
 }

 /**
  * Setter to allow a ColorImage to be saved as an Image
  * @param image The ColorImage to be saved
  */
 public void setImage(ColorImage image) {
  this.image = image;
 }

 /**
  * Retrieves a list of changes made to the Image.
  * @return a Stack of Changes made
  */
 public Stack < ColorImage > getChanges() {
  return this.changes;
 }

 /**
  * Adds a new change to the list of Image changes
  * @param snapshot the image snapshot to be added to the list
  */
 public void addChanges(ColorImage snapshot) {
  this.changes.push(snapshot);
 }

 /**
  * Convert the current image to monochrome,
  * and update internal image reference
  *
  */
 public void mono() {

  double redValue = 0.299;
  double greenValue = 0.587;
  double blueValue = 0.114;

  ColorImage tmpImage = new ColorImage(this.image);
  int height = tmpImage.getHeight();
  int width = tmpImage.getWidth();

  for (int y = 0; y < height; y++) {
   for (int x = 0; x < width; x++) {
    Color pixel = tmpImage.getPixel(x, y);
    int lum = (int) Math.round(
     redValue * pixel.getRed() +
     greenValue * pixel.getGreen() +
     blueValue * pixel.getBlue());
    tmpImage.setPixel(x, y, new Color(lum, lum, lum));
   }
  }
  this.image = tmpImage;

  filters.add("mono");
 }

 /**
  * Adds a watermark from the file "watermark.png"
  * All green is filtered out of watermark image
  *
  */
 public void watermark() {
  try {
   // The intensity which the watermark is to be displayed
   double intensity = 0.5;
   // Watermark file
   String wmFile = "watermark.png";

   // The image that is loaded by the user
   ColorImage tmpImage = new ColorImage(this.image);
   int height = tmpImage.getHeight();
   int width = tmpImage.getWidth();

   // The image which will create the watermark
   // Every pixel with a green value over 25o will be ignored
   ColorImage wmImage = new ColorImage(ImageIO.read(new File(wmFile)));
   int wmHeight = wmImage.getHeight();
   int wmWidth = wmImage.getWidth();

   // The ratio determins how much the watermark picture will be scaled up or down
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
     // The pixels are timesed by i to make them lighter/darker where
     // the watermark is.
     Color pixel = tmpImage.getPixel(x, y);
     int red = (pixel.getRed() * i > 255) ? 255 : (int)(pixel.getRed() * i);
     int green = (pixel.getGreen() * i > 255) ? 255 : (int)(pixel.getGreen() * i);
     int blue = (pixel.getBlue() * i > 255) ? 255 : (int)(pixel.getBlue() * i);

     tmpImage.setPixel(x, y, new Color(red, green, blue));
    }
   }
   // Then this image is added to the field variables
   this.image = tmpImage;
   // The string "watermark" is then added to filters so that the user can see
   // this was done to the image
   filters.add("watermark");
  } catch (IOException ex) {
   System.out.println(ex);
  }
 }

 /**
  * Rotate the current image 90 degrees,
  * and update the internal image reference.
  *
  */
 public void rot90() {

  // R90 = [0 -1, 1 0] rotates around origin
  // (x,y) -> (-y,x)
  // then transate -> (height-y, x)
  int height = this.image.getHeight();
  int width = this.image.getWidth();
  ColorImage rotImage = new ColorImage(height, width);

  for (int y = 0; y < height; y++) { // in the rotated image
   for (int x = 0; x < width; x++) {
    Color pixel = this.image.getPixel(x, y);
    rotImage.setPixel(height - y - 1, x, pixel);
   }
  }
  this.image = rotImage;
  filters.add("rot90");
 }

 /**
  * Flips image Horizontally
  * and update the internal image reference.
  *
  */
 public void flipH() {
  int height = this.image.getHeight();
  int width = this.image.getWidth();
  ColorImage rotImage = new ColorImage(width, height);

  for (int y = 0; y < height; y++) { // in the flipped image
   for (int x = 0; x < width; x++) {
    Color pixel = this.image.getPixel(x, y);
    rotImage.setPixel(width - x - 1, y, pixel);
   }
  }
  this.image = rotImage;

  filters.add("flipH");
 }
 /**
  * Flips image Vertically
  * and update the internal image reference.
  *
  */
 public void flipV() {
  int height = this.image.getHeight();
  int width = this.image.getWidth();
  ColorImage rotImage = new ColorImage(width, height);

  for (int y = 0; y < height; y++) { // in the flipped image
   for (int x = 0; x < width; x++) {
    Color pixel = this.image.getPixel(x, y);
    rotImage.setPixel(x, height - y - 1, pixel);
   }
  }
  this.image = rotImage;

  filters.add("flipV");
 }
}