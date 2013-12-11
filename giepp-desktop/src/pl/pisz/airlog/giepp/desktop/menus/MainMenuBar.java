package pl.pisz.airlog.giepp.desktop.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * @author Rafal
 */
public class MainMenuBar
        extends JMenuBar
        implements ActionListener {

    public static class FileMenu
            extends JMenu {
        
        private static final String NAME = "Plik";
        private static final String ITEM_QUIT = "Wyjdź";
        
        private JMenuItem mQuitItem;
        
        public FileMenu(ActionListener actionListener) {
            super(NAME);
            
            mQuitItem = new JMenuItem(ITEM_QUIT);
            mQuitItem.addActionListener(actionListener);
            
            this.initComponent();
        }
        
        private void initComponent() {
            this.addSeparator();
            this.add(mQuitItem);
        }
        
    }
    
    public static class HelpMenu
        extends JMenu {
    
        private static final String NAME = "Pomoc";
        private static final String ITEM_ABOUT = "O aplikacji";
        
        private JMenuItem mAboutItem;
        
        public HelpMenu(ActionListener actionListener) {
            super(NAME);
            
            mAboutItem = new JMenuItem(ITEM_ABOUT);
            mAboutItem.addActionListener(actionListener);
            
            this.initComponent();
        }
        
        private void initComponent() {
            this.addSeparator();
            this.add(mAboutItem);
        }
    
    }
    
    public static class MainMenuListener {
        
        public void onFileQuit(ActionEvent ae) {
            return;
        }
        
        public void onHelpAbout(ActionEvent ae) {
            return;
        }
        
    }
    
    private FileMenu mFileMenu;
    private HelpMenu mHelpMenu;
    
    private MainMenuListener mMenuListener = null;
    
    /**
     * 
     */
    public MainMenuBar() {
        super();
        
        mFileMenu = new FileMenu(this);
        mHelpMenu = new HelpMenu(this);
        
        this.initComponent();
    }
    
    private void initComponent() {
        this.add(mFileMenu);
        this.add(mHelpMenu);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae);
        if (mMenuListener == null) return;
        
        Object source = ae.getSource();
        if (source == mFileMenu.mQuitItem) mMenuListener.onFileQuit(ae);
        else if (source == mHelpMenu.mAboutItem) mMenuListener.onHelpAbout(ae);
    }
    
    /**
     * @param mml   reference to a listener
     */
    public void setMenuListener(MainMenuListener mml) {
        mMenuListener = mml;
    }
    
}
