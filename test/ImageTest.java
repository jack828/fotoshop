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
    public ImageTest() {
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
        ImageCompare imageComare = new ImageCompare("rot90.jpg","test_image/rot90.jpg");
        boolean check = imageComare.same();
        imageComare.delete();
        assertTrue(check);
    }
    @Test
    public void flipHTest() {
        String[] input = {"open input.jpg","flipH","save flipH.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageComare = new ImageCompare("flipH.jpg","test_image/flipH.jpg");
        boolean check = imageComare.same();
        imageComare.delete();
        assertTrue(check);
    }
    @Test
    public void flipVTest() {
        String[] input = {"open input.jpg","flipV","save flipV.jpg"};
        printCapture o = new printCapture(input);
        ImageCompare imageComare = new ImageCompare("flipV.jpg","test_image/flipV.jpg");
        boolean check = imageComare.same();
        imageComare.delete();
        assertTrue(check);
    }
}
