package pl.pisz.airlog.giepp.desktop.util;

import java.io.BufferedReader;
import java.io.IOException;

import java.util.Iterator;
import java.util.List;

public interface PropertyLoader
        extends Iterable<Pair<String, String>> {

    public abstract void parse(BufferedReader reader) throws IOException;
    
    public abstract Iterator<Pair<String, String>> iterator();
    
    public abstract List<Pair<String, String>> getProperties();
        
}
