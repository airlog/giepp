package pl.pisz.airlog.giepp.desktop.frames;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author Rafal
 */
public class MainFrame
        extends JFrame {
    
    private static final String FRAME_CAPTION   = "giepp-desktop";
    
    private JTabbedPane mTabbedPane;
    
    public MainFrame(JPanel[] tabs, String[] titles, ImageIcon[] icons) {
        super(FRAME_CAPTION);        
        if (tabs.length != titles.length) throw new IllegalArgumentException("need the same amount of tabs and titles");
        
        this.initTabs(tabs, titles, icons);
        this.initComponent();
    }
    
    private void initTabs(JPanel[] tabs, String[] titles, ImageIcon[] icons) {
        mTabbedPane = new JTabbedPane();
        
        for (int i = 0; i < tabs.length; i++) {
            JPanel panel = tabs[i];
            String title = titles[i];
            
            mTabbedPane.addTab(title, panel);
            mTabbedPane.setIconAt(i, icons[i]);
        }
    }
    
    private void initComponent() {
        this.setLayout(new BorderLayout());
        this.add(mTabbedPane, BorderLayout.CENTER);
    }
    
}
