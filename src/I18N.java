import java.util.*;
import java.util.HashMap;

public class I18N {
    
    private static Map<String, String[]> SETS;
    private static Locale locale;
    private static ResourceBundle message;
    
    /* Constructor is set to private to prevent an instance of it from being created, 
       which is highly recommended not to do so */
    private I18N(){}
    
    /* Used Internally to initialize the I18N module */
    private static void Init(){
        SETS = new HashMap<>();
        
        /* Language settings should be inserted below for initiaization purposes 
           Ideally all of this values below should be moved to a properties file as well to facilitate testing */
        SETS.put("default", new String[]{"en", "US", "default"});
        SETS.put("japanese", new String[]{"jp", "JP", "testProperties"});
    }
         
    /* Sets the I18N module to the desired language that is 
       available as specified by the Init() method */
    public static void setLanguage(String options){
        
        if(SETS == null)
            Init();
                
        String[] set = SETS.get(options);
        locale = returnLocale(options);
        
        if(set == null || locale == null){
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
    
    /* External modules call this method to */    
    public static String getString(String words){
        
        if(SETS == null)
            Init();
        
        /* This means setLanguage() method has not been invoked before & we should proceed to do so. */
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
    
    /* This is used internally to create Locale Objects*/
    private static Locale returnLocale(String options){
        String[] arr = SETS.get(options);
        return (arr!=null) ? new Locale(arr[0], arr[1]) : null ;
    }
    
    
    /* This method can be used by external modules to check the currently set language/locale */
    public static Locale getCurrentLocale(){
        return locale;
    }
}
