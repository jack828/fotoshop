package image;

import java.util.ArrayList;
import java.util.Stack;

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
  private String name = "";

  public Image(String name, ColorImage image) {
    this.image = image;
    this.filters = new ArrayList<>();
    this.changes = new Stack();
    this.name = name;
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
  public ArrayList<String> getFilters() {
    return filters;
  }

    /**
     * Get the name of this image.
     * @return Name of the image.
     */
  public String getName() { return this.name; }

    /**
     * Sets the name of this image.
     * @param name the name to be assigned.
     */
  public void setName(String name) { this.name = name; }

  /**
   * Setter to allow a image to be saved as an image.
   * @param image The image to be saved
   */
  public void setImage(ColorImage image) {
    this.image = image;
  }

  /**
   * Retrieves a list of changes made to the image.
   * @return a Stack of Changes made
   */
  public Stack<ColorImage> getChanges() {
    return this.changes;
  }

  /**
   * Adds a new change to the list of image changes
   * @param snapshot the image snapshot to be added to the list
   */
  public void addChanges(ColorImage snapshot) {
    this.changes.push(snapshot);
  }

  /**
   * Adds a new filter to the image filter list
   * @param filter the filter applied
   */
  public void addFilter(String filter) {
    this.filters.add(filter);
  }
}
