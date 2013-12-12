package pl.pisz.airlog.giepp.desktop.util;

import java.awt.GridLayout;

import java.io.File;
import java.io.IOException;

import javax.swing.JPanel;
import javax.swing.JLabel;

import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;

import pl.pisz.airlog.giepp.game.Game;

public class HelperTools {

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
            localStorage = LocalStorage.newInstance(
                    File.createTempFile("owned", ".xml"),
                    File.createTempFile("archive", ".xml"),
                    File.createTempFile("observed", ".xml"),
                    File.createTempFile("stats", ".xml"));
        } catch (IOException e) {
            System.err.println(e);
        }
        return new Game(new GPWDataSource(), new GPWDataParser(), localStorage);
    }
    
}
