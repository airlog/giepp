package pl.pisz.airlog.giepp.desktop;

import java.util.TreeMap;

import pl.pisz.airlog.giepp.desktop.util.Pair;

public class GieppProperties {  
    
    public static final String KEY_VERSION              = "version.full";
    public static final String KEY_VERSION_MAJOR        = "version.major";
    public static final String KEY_VERSION_MINOR        = "version.minor";
    public static final String KEY_VERSION_PATCH        = "version.patch";
    public static final String KEY_VERSION_DECORATOR    = "version.decorator";
    public static final String KEY_DATE_COMPILE         = "date.compiled";
    
    private TreeMap<String, String> mProperties = new TreeMap<String, String>();
    
    public GieppProperties(Iterable<Pair<String, String>> pairs) {
        for (Pair<String, String> pair : pairs) mProperties.put(pair.first, pair.second); 
    
        this.assertVersion();
        this.assertCompileDate();
    }   
    
    private void assertVersion() {
        if (this.getProperty(KEY_VERSION) == null) mProperties.put(KEY_VERSION, "0");
        if (this.getProperty(KEY_VERSION_MAJOR) == null) mProperties.put(KEY_VERSION_MAJOR, "0");
        if (this.getProperty(KEY_VERSION_MINOR) == null) mProperties.put(KEY_VERSION_MINOR, "0");
        if (this.getProperty(KEY_VERSION_PATCH) == null) mProperties.put(KEY_VERSION_PATCH, "0");
        if (this.getProperty(KEY_VERSION_DECORATOR) == null) mProperties.put(KEY_VERSION_DECORATOR, "undefined");        
    }
    
    private void assertCompileDate() {
        if (this.getProperty(KEY_VERSION) == null) mProperties.put(KEY_DATE_COMPILE, "0000-00-00");
    }
    
    public String getProperty(String key) {
        return mProperties.get(key);
    }
    
}
