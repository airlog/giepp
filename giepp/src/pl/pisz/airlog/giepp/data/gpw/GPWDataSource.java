package pl.pisz.airlog.giepp.data.gpw;

import pl.pisz.airlog.giepp.data.DataSource;

import java.net.*;
import java.io.*;

/** Klasa służąca do pobierania danych ze strony gpw.pl.
 */
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
    
	/** Ściąga stronę z notowaniami archiwalnymi z podanego dnia 
		@param dayI - dzień, z którego mają być notowania 
		@param monthI - numer miesiąca, z którego mają być notowania
		@param yearI - rok, z którego mają być notowania
		@return tekst ściągniętej strony
		@throws IOException
	**/
    public String retrieveArchiveData(int dayI, int monthI, int yearI) throws IOException {
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
	
	/** Ściąga stronę z notowaniami aktualnymi 
		@return tekst ściągniętej strony
		@throws IOException
	**/
    public String retrieveCurrentData() throws IOException {
        String address = "http://www.gpw.pl//ajaxindex.php?action=GPWQuotations&start=showTable&tab=all&lang=PL&default_order=";
        URL url = new URL(address);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        
        return this.getData(in);
    }

}
