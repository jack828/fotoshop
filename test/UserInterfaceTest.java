
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.concurrent.TimeUnit;

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

    public boolean printCapture(String input, String output){
        String[] args = null;

        
        input = input + System.lineSeparator() + "quit";
        // Input String
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        // Quit Program
        ///in = new ByteArrayInputStream("quit".getBytes());
        ///System.setIn(in);
        Main.main(args);
       
        
        
        String result = baos.toString();
        //System.out.println(result);
        return result.contains(output);
    }
    
    public String printCapture(String input){
        String[] args = null;

        input = input + System.lineSeparator() + "quit";
        // Input String
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.main(args);
       
        
        
        String result = baos.toString();
        return result;
    }
    public String printCapture(String[] inputs){
        String[] args = null;

        String input = "";
        
        for(String cmd: inputs){
            input += cmd + System.lineSeparator();
        }
        input += "quit";
        
        
        // Input String
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Main.main(args);
        
        
        
        String result = baos.toString();
        return result;
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
    @Test
    public void welcomeMessageTest(){
        String output = "Welcome to Fotoshop!\r\nFotoshop is an amazing new, image editing tool.\r\nType 'help' if you need help.\r\n\r\nThe current image is null\r\nFilters applied: "; 
        String input = "quit";
        assertTrue(printCapture(input,output));
    }
   
    @Test
    public void helpMessageTest(){
        String output = "You are using Fotoshop.\r\n\r\nYour command words are:"; 
        String input = "help";
        String result = printCapture(input);
        
        boolean lineOne = result.contains(output);
        output = "open save look mono flipH rot90 help quit";
        boolean lineTwo = result.contains(output);
        assertTrue(lineOne || lineTwo);
    }
    @Test
    public void unknownTest(){
        String output = "I don't know what you mean...";
        String input = "qqqqq";
        assertTrue(printCapture(input,output));
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