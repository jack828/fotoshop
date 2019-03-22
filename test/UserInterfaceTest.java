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
    /**
     * Default constructor for test class WelcomeTest
     */
    public UserInterfaceTest()
    {
    }
    public void delete(String fileName){
        File file = new File(fileName);
        file.delete();
    }
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @Before
    public void setUp()
    {
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
    }
    
    @Test
    public void welcomeMessageTest(){
        String input = "quit";     
        String[] output = {"Welcome to Fotoshop!",
                           "Fotoshop is an amazing new image editing tool.",
                           "Type 'help' if you need help."
                           }; 

        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    
    @Test
    public void helpMessageTest(){
        String input = "help";
        String[] output = {"You are using Fotoshop.","Your command words are:",
            "open","save", "look", "mono", "flipH", "rot90", "help", "quit"};

        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    @Test
    public void saveNoImageTest(){
        String input = "save";
        String output = "Sorry no image is loaded.";

        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    @Test
    public void saveWhereTest(){
        String input[] = {"open input.jpg","save"};
        String output = "Save where?";

        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    @Test
    public void saveWorkingTest(){
        String input[] = {"open input.jpg","save aaa.jpg"};
        String output = "Image saved to aaa.jpg";
        printCapture o = new printCapture(input);
        delete("aaa.jpg");
        assertTrue(o.contains(output));
    }
    @Test
    public void unknownTest(){
        String output = "I don't know what you mean...";
        String input = "qqqqq";

        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }

    @Test
    public void openNotFoundTest(){
        String output = "Cannot find image file, aaa";
        String input = "open aaa";

        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    @Test
    public void openFound(){
        String output = "Loaded input.jpg";
        String input = "open input.jpg";
        
        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    @Test
    public void lookNoFiltersTest(){
        String output = "No Image is currently loaded at the moment";
        String input = "look";
        
        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    @Test
    public void lookFiltersTest(){
        String[] input = {"open input.jpg",
                          "mono",
                          "flipH",
                          "look"
                        };
        String[] output = {"The current image is input.jpg",
                           "Filters applied:  mono flipH"
                          };
        printCapture o = new printCapture(input);
        assertTrue(o.contains(output));
    }
    
}