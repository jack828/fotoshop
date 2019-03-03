import java.util.*;
import java.util.HashMap;

/**
 * The I18N module has only 3 public static methods that needs to be taken note of:
 * 1) setLanguage(String language) - This can be used both to initialize the module or change the language being used.
 * 2) getString(String wordtoget) - This is used to get a key-pair value corresponding to the function argument from the properties file stated for the language currently set.
 *    if setLanguage() or getString() method was not run before, the I18N module is initialized to using the 'default' language as a default setting.
 * 3) getCurrentLocale() - Returns null if neither setLanguage() or getString() method was run before,
 *    & returns the Locale of the currently set language if the I18N module has been configured beforehand by the previous 2 methods.
 * 
 * For an example of how to use the I18N module, refer to I18NJUnitTest.java testSetLanguageLanguageChangeCapability() method.
 */

public class I18N {
    
    private static Map<String, String[]> SETS;
    private static Locale locale;
    private static ResourceBundle message;
    
    /** Constructor is set to private to prevent an instance 
     * of it from being created, which is highly recommended not to do so 
     */
    private I18N(){}
    
    /* Used Internally to initialize the I18N module */
    private static void Init(){
        SETS = new HashMap<>();
        
        /* Language settings should be inserted below for initiaization purposes 
           Ideally all of this values below should be moved to a properties file as well to facilitate testing */
        SETS.put("default", new String[]{"en", "US", "default"});
        SETS.put("japanese", new String[]{"jp", "JP", "testProperties"});
        SETS.put("nonexistent", new String[]{"en", "US", "nonexistent"});
    }
         
    /** Sets the I18N module to the desired language 
     *  that is available as specified by the Init() method 
     *  @param options Parameter passed containing the language to set the I18N module to
     *  (E.g. 'japanese')
     */
    public static void setLanguage(String options){
        
        if(SETS == null)
            Init();
       
        String[] set = SETS.get(options);
        locale = (set!=null) ? new Locale(set[0], set[1]) : null;
        
        if(set == null){
            System.out.println("\nLanguage entered does not exist!");
            return;
        }

        try 
        {
           message = ResourceBundle.getBundle(set[2], locale);
        }
        catch(NullPointerException | MissingResourceException e)
        {
            System.out.printf("%nError: %s%nReturning to default language", e);
            setLanguage("default");
        }
    }
    
    /** External modules call this method to 
     *  @param words references the key inside a properties file that contains the string we wish to return
     *  @return Returns a string that corresponds to key-pair value that exists inside the currently set Locale. Else return null if the key-pair does not exist.
     */    
    public static String getString(String words){
        /* This means if setLanguage() method has not been invoked before we should proceed to do so. */
        if (message == null)
            setLanguage("default");
        
        try
        {
            return message.getString(words);
        }
        catch(MissingResourceException | NullPointerException e){
            System.out.printf("Error: %s", e);
            return null;
        }
    }
    
    /** 
     * This method can be used by external modules to check the currently set language/locale
     * @return Returns a Locale Object that corresponds to the current Locale that the I18N module is set to.
     */
    public static Locale getCurrentLocale(){
        return locale;
    }
}
