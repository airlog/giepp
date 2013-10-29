package pl.pisz.airlog.giepp.data.gpw;

import pl.pisz.airlog.giepp.data.DataSource;

import java.net.*;
import java.io.*;

public class GPWDataSource implements DataSource {


	/** Zwraca stronę z notowaniami archiwalnymi z podanego dnia **/
    public String retrieveArchiveData(int dayI, int monthI, int yearI) throws IOException{

        String data = "";
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

        String line;
        while ( (line = in.readLine()) != null) {
            data+=line;
        }
        in.close();

        return data;
    }
	
	/** Zwraca stronę z notowaniami aktualnymi**/
    public String retrieveCurrentData() throws IOException{

        String data = "";

        String address = "http://www.gpw.pl//ajaxindex.php?action=GPWQuotations&start=showTable&tab=all&lang=PL&default_order=";
        URL url = new URL(address);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String line;
        while ( (line = in.readLine()) != null) {
            data+=line;
        }
        in.close();

        return data;
    }

}
