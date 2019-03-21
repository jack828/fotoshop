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
  private ArrayList<String> filters;
  private Stack<ColorImage> changes;

  public Image(ColorImage image) {
    this.image = image;
    this.filters = new ArrayList<>();
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
  public ArrayList<String> getFilters() {
    return filters;
  }

  /**
   * Setter to allow a ColorImage to be saved as an Image
   * @param image The ColorImage to be saved
   */
  public void setImage(ColorImage image) {this.image = image;}

  /**
   * Retrieves a list of changes made to the Image.
   * @return a Stack of Changes made
   */
  public Stack<ColorImage> getChanges() {return this.changes;}

  /**
   * Adds a new change to the list of Image changes
   * @param snapshot the image snapshot to be added to the list
   */
  public void addChanges(ColorImage snapshot) {this.changes.push(snapshot);}

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
                 redValue * pixel.getRed()
                + greenValue * pixel.getGreen()
                + blueValue * pixel.getBlue());
        tmpImage.setPixel(x, y, new Color(lum, lum, lum));
      }
    }
    this.image = tmpImage;

    filters.add("mono");
  }
  
  
  
  
  
  
  public void watermark() {
      try {
          double intensity = 0.5;
          
          ColorImage tmpImage = new ColorImage(this.image);
          int height = tmpImage.getHeight();
          int width = tmpImage.getWidth();
          
          ColorImage wmImage = new ColorImage(ImageIO.read(new File("watermark.png")));
          int wmHeight = wmImage.getHeight();
          int wmWidth = wmImage.getWidth();
          
          double ratio;
          
          if(wmWidth > wmHeight){
              ratio = (double)width/(double)wmWidth;
          }else{
              ratio = (double)height/(double)wmHeight;
          }
          int newWmWidth = (int) (wmWidth*ratio);
          int newWmHeight = (int) (wmHeight*ratio);
          
          int spaceX = (width - newWmWidth)/2;
          int spaceY = (height - newWmHeight)/2;
          
          int wmX = 0;
          int wmY = 0;
          for (int y = 0; y < height; y++) {
              for (int x = 0; x < width; x++) {
                  Color wmPixel;
                  double i = 1;
                  
                  if(x<spaceX || y<spaceY){
                      i = 1;
                  }else{
                        wmX = (int)((double)(x-spaceX) / ratio);
                        wmY = (int)((double)(y-spaceY) / ratio);
                        if((wmX+spaceX) < wmWidth && (wmY+spaceY) < wmHeight){
                            wmPixel = wmImage.getPixel(wmX, wmY);
                            int opacity = wmPixel.getGreen();   
                            i = (opacity > 250) ? 1 : intensity;
                        }
                  }
                  
                  Color pixel = tmpImage.getPixel(x, y);
                  int red = (pixel.getRed()*i> 255) ? 255 : (int)(pixel.getRed()*i);
                  int green = (pixel.getGreen()*i> 255) ? 255 : (int)(pixel.getGreen()*i);
                  int blue = (pixel.getBlue()*i> 255) ? 255 : (int)(pixel.getBlue()*i);
                  
                  tmpImage.setPixel(x, y, new Color(red,green,blue));
              }
          }
          this.image = tmpImage;
          
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
        rotImage.setPixel(width - x -1, y, pixel);
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
        rotImage.setPixel(x,height - y - 1, pixel);
      }
    }
    this.image = rotImage;

    filters.add("flipV");
  }
}
