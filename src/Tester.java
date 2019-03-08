
import java.util.ArrayList;

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
   public static void main(String[] args) {
        ArrayList<String> words;
        words = new ArrayList<String>();
        words.add("Open");
        Command command = new Command(words);
        
        System.out.println(command.hasWord(2));
    }
}
