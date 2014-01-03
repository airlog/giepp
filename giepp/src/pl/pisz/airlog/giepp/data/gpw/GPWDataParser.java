package pl.pisz.airlog.giepp.data.gpw;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.DataParser;
import pl.pisz.airlog.giepp.data.BadDate;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.ArrayList;

public class GPWDataParser implements DataParser {
	
	private Pattern patternArchive;
	private Pattern patternCurrent;

	public GPWDataParser() {
		patternArchive = Pattern.compile(".*<td [^<>]*>([^<>]*)</td>[^<>]*<td [^<>]*>[^<>]*</td>[^<>]*<td>[^<>]*</td>[^<>]*<td>[^<>]*</td>[^<>]*<td>([^<>]*)</td>[^<>]*<td>([^<>]*)</td>[^<>]*<td>([^<>]*)</td>[^<>]*<td>[^<>]*</td>[^<>]*<td>[^<>]*</td>[^<>]*<td>[^<>]*</td>[^<>]*<td>[^<>]*</td>");
		patternCurrent = Pattern.compile(".*<td>.*</td><td class=\"left nowrap\"><a[^<>]*>([^<>]*)</a></td><td class=\"left\">[^<>]*</td><td class=\"left\">PLN</td><td>([^<>]*)</td><td>([^<>]*)</td><td>[^<>]*</td><td>([^<>]*)</td><td>([^<>]*)</td><td>([^<>]*)</td><td>([^<>]*)</td><td>([^<>]*)</td><td>[^<>]*</td><td>[^<>]*</td>");
	}

	/** Zwraca listę notowań archiwalnych z podanego dnia. Jeśli dla danego dnia nie ma wyników to wyrzuca wyjątek BadDate **/
	public ArrayList<ArchivedStock> parseArchive(String site) throws BadDate {
		String table = findTable(site);
		String[] lines = splitToLines(table);
		
		ArrayList<ArchivedStock> all = new ArrayList<ArchivedStock>();
		
		for (int i = 0 ; i < lines.length ; i++) {
			ArchivedStock single = matchSingleStockArchive(lines[i]);
			if (single != null) {
				all.add(single);
			}
		}
				
		if (all.size()==0) {
			throw new BadDate();
		}
		else {
			return all;
		}
	}
	
	/** Zwraca aktualną listę notowań **/
	public ArrayList<CurrentStock> parseCurrent(String site) {
		String table = findTable(site);
		String[] lines = splitToLines(table);	
 		ArrayList<CurrentStock> all = new ArrayList<CurrentStock>();

		for (int i = 0 ; i < lines.length ; i++) {
			CurrentStock single = matchSingleStockCurrent(lines[i]);
			if (single != null) {
				all.add(single);
			}
		}
		
		return all;
	}
	
	private String findTable(String site) {
		Pattern p = Pattern.compile("<table class=\"tab03\">(.*)</table>");
		Matcher m = p.matcher(site);
		String table = "";
		if ( m.find() ){
			table = m.group(1);
		}
		return table;
	}
	
	private String[] splitToLines(String table) {
		return table.split("</tr>");
	}
	
	private ArchivedStock matchSingleStockArchive(String line) {
		
		Matcher m = patternArchive.matcher(line);
		int max = 0;
		int min = 0;
		int end = 0;
			 
		if (m.find()) {
			try{
				max = Integer.parseInt(m.group(2).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			try{
				min = Integer.parseInt(m.group(3).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			try{
				end = Integer.parseInt(m.group(4).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			
			if (max == 0.0f) {
				max = end;
				min = end;
			}

		//	System.out.print(m.group(1) + "\t"+ max +  "\t"+ min + "\n");
			return new ArchivedStock(m.group(1),max,min);
		}
		return null;
	}

	private CurrentStock matchSingleStockCurrent(String line) {
		Matcher m = patternCurrent.matcher(line);
		int odn = 0;
		int start = 0;
		int max = 0;
		int min = 0;
		int end = 0;
		float change = 0.0f;

		if (m.find()) {
			try{
				odn = Integer.parseInt(m.group(3).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			try{
				start = Integer.parseInt(m.group(4).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			try{
				min = Integer.parseInt(m.group(5).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			try{
				max = Integer.parseInt(m.group(6).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}	
			try{
				end = Integer.parseInt(m.group(7).replaceAll(",","").replaceAll("&nbsp;", ""));
			}catch (Exception e) {}
			try{
				change = Float.parseFloat(m.group(8).replaceAll(",","."));
			}catch (Exception e) {}
			if (max == 0 && end == 0) {
				end = odn;
				start = odn;
				max = odn;
				min = odn;
			}
			//		System.out.print(m.group(1) + " " + m.group(2) +" " + start +" " + min +" " + max +" " + end + " " + change +"\n");
			return new CurrentStock(m.group(1),m.group(2),start,min,max,end,change);
		}		
		return null;
	}	
}
