/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Bobby
 */
public class NewEmptyJUnitTest {
    
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintStream ps = new PrintStream(baos);
    PrintStream old = System.out;
    
    public boolean printCapture(String input, String output){
        String[] args = null;
        
        Main.main(args);
        // Input String
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        // Quit Program
        in = new ByteArrayInputStream("quit".getBytes());
        System.setIn(in);
        
        // Put things back
        System.out.flush();
        System.setOut(old);
        System.out.print(baos);
        
        String result = baos.toString();
        return result.contains(output);
    }
    public NewEmptyJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
     @Test
     public void hello() {
        String output = "Welcome to Fotoshop!\r\nFotoshop is an amazing new, image editing tool.\r\nType 'help' if you need help.\r\n\r\nThe current image is null\r\nFilters applied: "; 
        String input = "quit";

        boolean actual = printCapture(input, output);
        assertTrue(actual);
     }
}
