
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * A Class for comparing two images
 * @author Robet Bromfield
 */
public class ImageCompare {
    private String imageOne;
    private String imageTwo;
    
    public ImageCompare(String imageOne, String imageTwo){
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
    }

    /**
     * Check to see if imageOne and imageTwo are equal to eachother
     * @return True if images are the same otherwise false
     */
    public boolean same(){
        boolean check = false;
        try {
            check = true;
            
            BufferedImage img1 = ImageIO.read(new File(this.imageOne));
            int height1 = img1.getHeight();
            int width1 = img1.getWidth();

            BufferedImage img2 = ImageIO.read(new File(this.imageTwo));
            int height2 = img2.getHeight();
            int width2 = img2.getWidth();
            
            
            
            if(height1 != height2 && width1 != width2){
                check = false;
            }
            
            
            for (int y = 0; y < height1; y++) {
                for (int x = 0; x < width1; x++) {
                    int pixel1 = img1.getRGB(x, y);
                    Color color1 = new Color(pixel1);
                    
                    int pixel2 = img2.getRGB(x, y);
                    Color color2 = new Color(pixel2);
                    
                    
                    
                    if(     color1.getBlue() != color2.getBlue() ||
                            color1.getGreen() != color2.getGreen() ||
                            color1.getRed() != color2.getRed()          )
                    {
                        System.out.println(x+" - "+y);
                        check = false;
                        break;
                    }
                }
                if(!check){break;}
            }
        } catch (IOException ex) {
            check = false;
            System.out.println(ex);
        }
        return check;
    }
    
    /**
     * This method will delete the image given as the first parameter of the constructor.
     * This is so that it can be deleted after testing as not to cause
     */
    public void delete(){
        File file = new File(imageOne);
        file.delete();
    }
}
