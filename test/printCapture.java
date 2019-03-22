
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

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
    
    private String output;
        
    public static String out(String input){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        PrintStream old = System.out;
        
        System.setOut(ps);
        
        String[] args = null;

        input += System.lineSeparator() + "quit"+System.lineSeparator();
        // Input String
        ByteArrayInputStream in1 = new ByteArrayInputStream(input.getBytes());
        System.setIn(in1);
        Main.main(args);
       
        
        String result = baos.toString();
        System.setIn(oringinalIn);
        System.out.flush();
        System.setOut(old);
        return result;
    }
//    public static String printCapture(String[] inputs){
//        System.setOut(ps);
//        InputStream oringinalIn = System.in;
//        String[] args = null;
//
//        String input = "";
//        
//        for(String cmd: inputs){
//            input += cmd + System.lineSeparator();
//        }
//        input += "quit";
//        
//        // Input String
//        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
//        System.setIn(in);
//        Main.main(args);
//        
//        
//        
//        String result = baos.toString();
//        System.setIn(oringinalIn);
//        System.out.flush();
//        System.setOut(old);
//        return result;
//    }
    public String getOutput(){
        return this.output;
    }
}
