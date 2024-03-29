import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import i18n.I18N;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class I18NJUnitTest
{

    public I18NJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() throws IllegalAccessException
    {
        resetStaticVariables();
        checkStaticVariables();
    }

    @After
    public void tearDown() {
    }

    /**
     * Reset i18n.I18N modules static variables to null
     * @throws java.lang.IllegalAccessException
     */
    public void resetStaticVariables() throws IllegalAccessException
    {
        Class clazz = I18N.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(!field.getName().equals("LANGUAGESETS")){
                field.setAccessible(true);
                field.set(null, null);
            }
        }
    }

    /**
     * Check to Ensure i18n.I18N modules static variables are initialized to null.
     * @throws java.lang.IllegalAccessException
     */
    public void checkStaticVariables() throws IllegalAccessException
    {
        Class clazz = I18N.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            if(!field.getName().equals("LANGUAGESETS")){
                field.setAccessible(true);
                Assert.assertEquals(null, field.get(null));
            }
        }
    }

    /**
     * This Test checks to see if setLanguage Method initializes the i18n.I18N module
     * "default" is passed to the setLanguage method as a argument
     * Test should check to ensure all the i18n.I18N static variables are properly initialized
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testSetLanguageMethodFlow() throws IllegalAccessException
    {
        I18N.setLanguage("default");

        Class clazz = I18N.class;
        Field[] fields = clazz.getDeclaredFields();
        for(Field field : fields){
            field.setAccessible(true);
            switch(field.getName()){
                case "SETS":
                    Map<String, String[]> set = (Map<String, String[]>)field.get(null);
                    Assert.assertArrayEquals(new String[]{"en", "GB", "default"}, set.get("default"));
                    Assert.assertArrayEquals(new String[]{"jp", "JP", "testProperties"}, set.get("japanese"));
                    break;
                case "locale":
                    Assert.assertEquals(new Locale("en", "GB"), (Locale)field.get(null));
                    break;
                case "message":
                    ResourceBundle bundle = (ResourceBundle)field.get(null);
                    Assert.assertEquals("default", bundle.getBaseBundleName());
                    Assert.assertEquals(new Locale("en", "GB"), bundle.getLocale());
                    break;
            }
        }

        Assert.assertEquals("Hello World!%n", I18N.getString("testString1"));
    }

    /**
     * Checks to ensure that if language entered to setLanguage method does not exist
     * That the static variable 'message' does not get initialized
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.NoSuchFieldException
     */
    @Test
    public void testSetLanguageNoLanguage() throws IllegalAccessException, NoSuchFieldException
    {
        I18N.setLanguage("DoesNotExist");
        Class clazz = I18N.class;
        Field field = clazz.getDeclaredField("message");
        field.setAccessible(true);
        Assert.assertEquals(null, field.get(null));
    }

    /**
     * Checks to ensure that if a ResourceBundle does not exist
     * setLanguage method defaults to the default language
     */
    @Test
    public void testSetLanguageNoPropsFile()
    {
        I18N.setLanguage("nonexistent");
        Assert.assertEquals("Hello World!%n", I18N.getString("testString1"));
    }

    /**
     * Checks to ensure the setLanguage method is capable of using another language as intended.
     *
     */
    @Test
    public void testSetLanguageLanguageChangeCapability(){
        I18N.setLanguage("default");
        Assert.assertEquals("Hello World!%n", I18N.getString("testString1"));
        I18N.setLanguage("japanese");
        Assert.assertEquals("おはよ世界!%n", I18N.getString("testString1"));
    }

    /**
     * Checks to ensure that if ResourceBundle does not contain a particular key-pair value
     * A value of null is to be returned;
     */
    @Test
    public void testGetStringNoKey(){
        Assert.assertEquals(null, I18N.getString("ThisDoesNotExist"));
    }

    /**
     * Checks to ensure Correct Locale is returned if i18n.I18N module is properly initialized
     * @throws java.lang.IllegalAccessException
     */
    @Test
    public void testGetCurrentLocaleMethod() throws IllegalAccessException
    {
        Assert.assertEquals(null, I18N.getCurrentLocale());

        Assert.assertEquals("Hello World!%n", I18N.getString("testString1"));
        Assert.assertEquals(new Locale("en", "GB"), I18N.getCurrentLocale());

        resetStaticVariables();
        checkStaticVariables();

        I18N.setLanguage("default");
        Assert.assertEquals(new Locale("en", "GB"), I18N.getCurrentLocale());
    }
}
