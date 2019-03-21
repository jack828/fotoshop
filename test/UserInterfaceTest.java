
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * The test class TestNew.
 *
 * @author  (your name)
 * @version (a version number or a date)
 */
public class UserInterfaceTest
{
    InputStream oringinalIn = System.in;
    PrintStream old = System.out;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintStream ps = new PrintStream(baos);

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
        System.setOut(this.ps);
    }

    /**
     * Tears down the test fixture.
     *
     * Called after every test case method.
     */
    @After
    public void tearDown()
    {
        System.setIn(oringinalIn);
        System.out.flush();
        System.setOut(old);
    }
    public boolean printCapture(String input, String output)
    {
        String[] args = null;
        input += System.lineSeparator() + "quit";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.main(args);
        System.setIn(oringinalIn);
        
        System.out.flush();
        System.setOut(old);
        
        //System.out.println("-->"+baos+"<--");
        String result = baos.toString();
        return result.contains(output);
    }
    @Test
    public void quitTest()
    {
        String input = "open";
        String output = "open what?";
        assertTrue(printCapture(input, output));
    }
    @Test
    public void saveTest()
    {
        String input = "help";
        String output = "Your command words are";
        assertTrue(printCapture(input, output));
    }
}