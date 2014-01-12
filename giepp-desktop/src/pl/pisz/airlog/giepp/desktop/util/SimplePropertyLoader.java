package pl.pisz.airlog.giepp.desktop.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class SimplePropertyLoader
        implements PropertyLoader {

    private LinkedList<Pair<String, String>> mProperties = new LinkedList<Pair<String, String>>();
    private Pattern mPropertyPattern = Pattern.compile("([A-Za-z.]+)\\s*=\\s*(.+)\\s*");
    
    @Override
    public void parse(BufferedReader reader) throws IOException {
        while (true) {
            String line = reader.readLine();
            if (line == null) break;
            
            Matcher mt = mPropertyPattern.matcher(line);
            if (!mt.matches()) continue;
            mProperties.add(new Pair<String, String>(mt.group(1), mt.group(2)));            
        }
    }

    @Override
    public Iterator<Pair<String, String>> iterator() {
        return mProperties.iterator();
    }

    @Override
    public List<Pair<String, String>> getProperties() {
        return new LinkedList<Pair<String, String>>(mProperties);
    }
    
    public static void main(String[] args) {
        String content =
                "version.minor=1\n" +
                "version.major=0\n" +
                "version.patch=0\n" +
                "version.decorator=beta\n" +
                "\n" +
                "date.compiled=2014-01-12\n" +
                "\n";
        
        PropertyLoader loader = new SimplePropertyLoader();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new StringReader(content));
            loader.parse(reader);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                if (reader != null) reader.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
        
        if (reader != null) {
            for (Pair<String, String> pair : loader) {
                System.out.println(pair.first + ": " + pair.second);
            }
        } else System.err.println("Reader is NULL!");
    }

}
