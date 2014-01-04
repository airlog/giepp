package pl.pisz.airlog.giepp.desktop.util;

import java.awt.GridLayout;

import java.io.File;
import java.io.IOException;

import java.text.DecimalFormat;

import java.util.Comparator;

import javax.swing.JPanel;
import javax.swing.JLabel;

import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;

import pl.pisz.airlog.giepp.game.Game;

public class HelperTools {

    private static final DecimalFormat PRICE_FORMAT = new DecimalFormat("#0.00");
    
    public static JPanel newTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        
        return panel;
    }
    
    public static Game newGame() {
        LocalStorage localStorage = null;        
        try {
            File files[] = new File[] {
                    File.createTempFile("owned", ".xml"),
                    File.createTempFile("archive", ".xml"),
                    File.createTempFile("observed", ".xml"),
                    File.createTempFile("stats", ".xml")
                };
            for (File file : files) file.deleteOnExit();
            
            localStorage = LocalStorage.newInstance(files[0], files[1], files[2], files[3]);
        } catch (IOException e) {
            System.err.println(e);
        }
        return new Game(new GPWDataSource(), new GPWDataParser(), localStorage);
    }
    
    public static <T> Comparator<T> getReverseComparator(Comparator<T> comparator) {
        final Comparator<T> comp = comparator;
        return new Comparator<T>() {
            @Override
            public int compare(T a, T b) {
                return -comp.compare(a, b); 
            }
        };
    }
    
    public static DecimalFormat getPriceFormat() {
        return PRICE_FORMAT;
    }
    
}
