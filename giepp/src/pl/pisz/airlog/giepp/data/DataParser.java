package pl.pisz.airlog.giepp.data;

import java.util.ArrayList;

public interface DataParser {

    public abstract ArrayList<ArchivedStock> parseArchive(String data) throws BadDate;
    
    public abstract ArrayList<CurrentStock> parseCurrent(String data);
    
}
