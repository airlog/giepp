package pl.pisz.airlog.giepp.desktop.frames;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.CurrentStock;

import pl.pisz.airlog.giepp.desktop.menus.MainMenuBar;
import pl.pisz.airlog.giepp.desktop.panels.RatingsPanel;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;

import pl.pisz.airlog.giepp.game.Game;

/**
 * @author Rafal
 */
public class MainFrame
        extends JFrame {
    
    private static final String FRAME_CAPTION   = "giepp-desktop";
    
    private JTabbedPane mTabbedPane;
    
    public MainFrame(JPanel[] tabs, String[] titles) {
        super(FRAME_CAPTION);        
        if (tabs.length != titles.length) throw new IllegalArgumentException("need the same amount of tabs and titles");
        
        this.initTabs(tabs, titles);
        this.initComponent();
    }
    
    private void initTabs(JPanel[] tabs, String[] titles) {
        mTabbedPane = new JTabbedPane();
        
        for (int i = 0; i < tabs.length; i++) {
            JPanel panel = tabs[i];
            String title = titles[i];
            
            mTabbedPane.addTab(title, panel);
        }
    }
    
    private void initComponent() {
        this.setLayout(new BorderLayout());
        this.add(mTabbedPane, BorderLayout.CENTER);
    }
    
}
