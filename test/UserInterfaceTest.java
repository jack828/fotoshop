import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class WelcomeTest.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class UserInterfaceTest
{
    @Test
    public void welcomeMessageTest(){
        String input = "quit";     
        String[] output = {"Welcome to Fotoshop!",
                           "Fotoshop is an amazing new image editing tool.",
                           "Type 'help' if you need help."
                           }; 

        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    
    @Test
    public void helpMessageTest(){
        String input = "help";
        String[] output = {"You are using Fotoshop.","Your command words are:",
            "open","save", "look", "mono", "flipH", "rot90", "help", "quit"};

        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    @Test
    public void saveNoImageTest(){
        String input = "save";
        String output = "No Image is currently loaded at the moment";

        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    @Test
    public void saveWhereTest(){
        String input[] = {"open input.jpg","save"};
        String output = "Save where?";

        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    @Test
    public void unknownTest(){
        String output = "I don't know what you mean...";
        String input = "qqqqq";

        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }

    @Test
    public void openNotFoundTest(){
        String output = "Cannot find image file, aaa";
        String input = "open aaa";

        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    @Test
    public void openFound(){
        String output = "Loaded input.jpg";
        String input = "open input.jpg";
        
        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    @Test
    public void lookNoFiltersTest(){
        String output = "No Image is currently loaded at the moment";
        String input = "look";
        
        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.getOutput().contains(output));
    }
    @Test
    public void lookFiltersTest(){
        String[] input = {"open input.jpg",
                          "mono",
                          "flipH",
                          "look"
                        };
        String[] output = {"The current image is input.jpg",
                           "Filters applied:",
                           "mono",
                           "flipH"
                          };
        printCapture o = new printCapture(input);
        o.getOutput();
        assertTrue(o.contains(output));
    }
    
}