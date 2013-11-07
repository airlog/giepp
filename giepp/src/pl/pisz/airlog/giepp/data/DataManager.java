package pl.pisz.airlog.giepp.data;

import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;

import java.util.ArrayList;
import java.io.*;

public class DataManager {
	
	private DataParser parser;
	private DataSource source;

	public DataManager(){
		parser = new GPWDataParser();
		source = new GPWDataSource();
	}
	
	public ArrayList<ArchivedStock> getArchival(int day, int month, int year) throws BadDate, IOException {
		ArrayList<ArchivedStock> archived = parser.parseArchive(source.retrieveArchiveData(day,month,year));
        String dayS = day+"";
        if (day < 10) {
            dayS = "0"+day;
        }
        String monthS = month+"";
        if (month < 10) {
            monthS = "0"+month;
        }
		String date = year+"-"+monthS+"-"+dayS;

		for (int i = 0; i < archived.size(); i++)
			archived.get(i).setDate(date);
		return archived;
	}
	
	public ArrayList<CurrentStock> getCurrent() throws IOException {
		return parser.parseCurrent(source.retrieveCurrentData());
	}

}

