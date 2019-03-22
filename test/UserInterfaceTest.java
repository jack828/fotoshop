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
                           "Fotoshop is an amazing new, image editing tool.",
                           "Type 'help' if you need help.",
                           "The current image is:",
                           "Filters applied:"
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
    public void saveTest(){
        String input = "save";
        String output = "Sorry no image is loaded.";

        printCapture o = new printCapture(input);
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
}