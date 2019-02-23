import java.util.*;
import java.util.HashMap;

public class Internationalization {
    
    private static final Map<String, String[]> SETS = initSets();
            
    private static Locale locale = returnLocale("default");
    
    private static ResourceBundle message = ResourceBundle.getBundle(SETS.get("default")[2], locale);
    
    private Internationalization(){}
    
    private static Map<String, String[]> initSets(){
        Map<String, String[]> sets = new HashMap<>();
        
        /* Language settings should be inserted below for initiaization purposes */
        sets.put("default", new String[]{"en", "US", "default"});
        sets.put("japanese", new String[]{"jp", "JP", "testProperties"});
        
        return sets;
    }
         
    public static void setLanguage(String options){
                
        String[] set = SETS.get(options);
        
        if(set==null){
            System.out.println("\nLanguage entered does not exist!");
            return;
        }

        locale = returnLocale(options);
        
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
        
    public static String getString(String words){
        return message.getString(words);
    }
    
    public static Locale returnLocale(String options){
        String[] arr = SETS.get(options);
        return (arr!=null) ? new Locale(arr[0], arr[1]) : null ;
    }
}
