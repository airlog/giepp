package pl.pisz.airlog.giepp.data;

import java.io.IOException;

public interface DataSource {

    public abstract String retrieveCurrentData() throws IOException;
    
    public abstract String retrieveArchiveData(int day, int month, int year) throws IOException, BadDate;
    
}
