package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

public class StatusBar
        extends JPanel {

    private JProgressBar mArchiveProgressBar = new JProgressBar();
    private JProgressBar mCurrentProgressBar = new JProgressBar();
    
    public StatusBar() {
        super(new FlowLayout(FlowLayout.RIGHT));
        
        mArchiveProgressBar.setIndeterminate(true);
        mArchiveProgressBar.setVisible(false);
        
        mCurrentProgressBar.setIndeterminate(true);
        mCurrentProgressBar.setVisible(false);
        
        this.add(mArchiveProgressBar);
        this.add(mCurrentProgressBar);
    }
    
    protected void triggerProgressBar(JProgressBar progressBar, String caption) {
        if (progressBar.isVisible()) {
            progressBar.setVisible(false);
            return;
        }
        
        if (caption != null) {
            progressBar.setString(caption);
            progressBar.setStringPainted(true);
        }
        else progressBar.setStringPainted(false);
        
        progressBar.setVisible(true);
    }
    
    public void triggerArchiveProgressBar(String caption) {
        this.triggerProgressBar(mArchiveProgressBar, caption);
    }
    
    public void triggerCurrentProgressBar(String caption) {
        this.triggerProgressBar(mCurrentProgressBar, caption);
    }
    
}
