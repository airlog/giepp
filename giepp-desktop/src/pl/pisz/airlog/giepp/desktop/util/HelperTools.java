package pl.pisz.airlog.giepp.desktop.util;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class HelperTools {

    public static JPanel newTextPanel(String text) {
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        
        return panel;
    }
    
}
