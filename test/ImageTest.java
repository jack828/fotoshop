/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
public class ImageTest {
    @Test
    public void saveWorkingTest(){
        String input[] = {"open input.jpg","save aaa.jpg"};
        String output = "Image saved to aaa.jpg";
        
        ImageCompare imageComare = new ImageCompare("input.jpg","aaa.jpg");
        boolean check = imageComare.same();
        imageComare.delete();
        assertTrue(check);
    }
    @Test
    public void monoTest() {
        String[] input = {"open input.jpg","mono","save mono.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageComare = new ImageCompare("mono.jpg","test_image/mono.jpg");
        boolean check = imageComare.same();
        imageComare.delete();
        assertTrue(check);
    }
    @Test
    public void rot90Test() {
        String[] input = {"open input.jpg","rot90","save rot90.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageCompare = new ImageCompare("rot90.jpg","test_image/rot90.jpg");
        boolean check = imageCompare.same();
        imageCompare.delete();
        assertTrue(check);
    }
    @Test
    public void flipHTest() {
        String[] input = {"open input.jpg","flipH","save flipH.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageCompare = new ImageCompare("flipH.jpg","test_image/flipH.jpg");
        boolean check = imageCompare.same();
        imageCompare.delete();
        assertTrue(check);
    }
    @Test
    public void flipVTest() {
        String[] input = {"open input.jpg","flipV","save flipV.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageCompare = new ImageCompare("flipV.jpg","test_image/flipV.jpg");
        boolean check = imageCompare.same();
        imageCompare.delete();
        assertTrue(check);
    }
    @Test
    public void watermarkTest() {
        String[] input = {"open input.jpg","watermark","save watermark.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageCompare = new ImageCompare("watermark.jpg","test_image/watermark.jpg");
        boolean check = imageCompare.same();
        imageCompare.delete();
        assertTrue(check);
    }
    @Test
    public void undoTest() {
        String[] input = {"open input.jpg","mono","flipH","undo","save undo.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageCompare = new ImageCompare("undo.jpg","test_image/mono.jpg");
        boolean check = imageCompare.same();
        imageCompare.delete();
        assertTrue(check);
    }
    public void putGetTest() {
        String[] input = {"open input.jpg","flipH","put fff","mono","get fff","save fff.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageCompare = new ImageCompare("fff.jpg","test_image/flipH.jpg");
        boolean check = imageCompare.same();
        imageCompare.delete();
        assertTrue(check);
    }
}
