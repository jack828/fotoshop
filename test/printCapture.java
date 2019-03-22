
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

/**
 * A class for capturing the output of the Main class and comparing it with given Strings
 * @author Robert Bromfield
 */
public class printCapture {
    private static final InputStream oringinalIn = System.in;
    private ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private PrintStream ps = new PrintStream(baos);
    private PrintStream old = System.out;
    private String result;
    private String[] inputs;

    
    public printCapture(String input){
        String[] in = {input};
        this.inputs = in;
    }
    public printCapture(String[] inputs){
        this.inputs = inputs;
    }
    public String getOutput(){
        // System.out is captured so prinnted line will be saved under 'baos'
        System.setOut(ps);
        // Here "quit" is added to the end of inputs so the program will self-terminate     
        String input = "";
        for(String cmd: this.inputs){
            input += cmd + System.lineSeparator();
        }
        input += "quit";
        
        // The input is turned into a Byte Array and added to System.setIn() 
        // So it will act as user input
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        
        // Here the Main method is called and the program is run
        String[] args = null;
        Main.main(args);
        
        // System.in is returned to normal
        System.setIn(oringinalIn);
        // System.out is reset
        System.out.flush();
        System.setOut(old);
        // The captured output is turned into a string
        this.result = baos.toString();
        return this.result;
    }

    /**
     * @param output A String that is expected to be printed from given command
     * @return A boolean which is true if output is printed. False otherwise.
     */
    public boolean contains(String output){
        return this.result.contains(output);
    }

    /**
     * @param outputs An array of string that are expected to be printed from given command
     * @return A boolean which is true if output is printed. False otherwise.
     */
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
