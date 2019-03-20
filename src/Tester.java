
import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.After;
import static org.junit.Assert.assertTrue;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bobby
 */
public class Tester {
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

        
        String result = baos.toString();
        return result.contains(output);
    }
   public static void main(String[] args) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
       
        String output = "Welcome to Fotoshop!\r\nFotoshop is an amazing new, image editing tool.\r\nType 'help' if you need help.\r\n\r\nThe current image is null\r\nFilters applied: "; 
        String input = "quit";

        //String[] args = null;
        
        Main.main(args);
        // Input String
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        // Quit Program
        in = new ByteArrayInputStream("quit".getBytes());
        System.setIn(in);
        
        // Put things back
        System.out.flush();

        
        String result = baos.toString();
        
    }
}
