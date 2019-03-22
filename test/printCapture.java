
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Bobby
 */
public class printCapture {
    private static final InputStream oringinalIn = System.in;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintStream ps = new PrintStream(baos);
    private PrintStream old = System.out;
    private String result;

    
    public printCapture(String input){
        System.setOut(ps);
        
        String[] args = null;

        input += System.lineSeparator() + "quit"+System.lineSeparator();
        // Input String
        ByteArrayInputStream in1 = new ByteArrayInputStream(input.getBytes());
        System.setIn(in1);
        Main.main(args);
       
        
        this.result = baos.toString();
        System.setIn(oringinalIn);
        System.out.flush();
        System.setOut(old);
    }
    public printCapture(String[] inputs){
        System.setOut(ps);
        InputStream oringinalIn = System.in;
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
        
        
        
        this.result = baos.toString();
        System.setIn(oringinalIn);
        System.out.flush();
        System.setOut(old);

    }
    public boolean contains(String output){
        return this.result.contains(output);
    }
    public boolean contains(String[] outputs){
        boolean bool = true;
        for(String output : outputs){
            if(bool){
                bool = this.result.contains(output);
            }
        }
        return bool;
    }
    @Override
    public String toString(){
        return this.result;
    }
}
