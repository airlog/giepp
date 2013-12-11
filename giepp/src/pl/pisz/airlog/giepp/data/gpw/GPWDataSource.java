package pl.pisz.airlog.giepp.data.gpw;

import pl.pisz.airlog.giepp.data.DataSource;

import java.net.*;
import java.io.*;

public class GPWDataSource implements DataSource {

    protected String getData(BufferedReader in)
            throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println(String.format("[%d] Downloading started", startTime));
        
        String data = new String("");
        String line;
        while ( (line = in.readLine()) != null ) {
            data += line;
        }
        in.close();
        
        long endTime = System.currentTimeMillis();
        System.out.println(String.format("[%d] Downloading finished (in %d ms)", endTime, endTime - startTime));

        return data;
    }
    
	/** Zwraca stronę z notowaniami archiwalnymi z podanego dnia **/
    public String retrieveArchiveData(int dayI, int monthI, int yearI) throws IOException{
        String dayS = dayI+"";
        if (dayI < 10) {
            dayS = "0"+dayI;
        }

        String monthS = monthI+"";
        if (monthI < 10) {
            monthS = "0"+monthI;
        }
		
        String address = "http://www.gpw.pl/notowania_archiwalne_full?type=10&date="+yearI+"-"+monthS+"-"+dayS;
        URL url = new URL(address);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        
        return this.getData(in);
    }
	
	/** Zwraca stronę z notowaniami aktualnymi**/
    public String retrieveCurrentData() throws IOException{
        String address = "http://www.gpw.pl//ajaxindex.php?action=GPWQuotations&start=showTable&tab=all&lang=PL&default_order=";
        URL url = new URL(address);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        
        return this.getData(in);
    }

}
