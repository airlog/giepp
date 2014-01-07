package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StatusBar
        extends JPanel {

    private JProgressBar mProgressBar = new JProgressBar();
    
    public StatusBar() {
        super(new FlowLayout(FlowLayout.RIGHT));
        
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisible(false);
        
        this.add(mProgressBar);
    }
    
    public void triggerProgressBar(String caption) {
        if (mProgressBar.isVisible()) {
            mProgressBar.setVisible(false);
            return;
        }
        
        if (caption != null) {
            mProgressBar.setString(caption);
            mProgressBar.setStringPainted(true);
        }
        else mProgressBar.setStringPainted(false);
        mProgressBar.setVisible(true);
    }
    
}
