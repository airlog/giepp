package pl.pisz.airlog.giepp.desktop.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 * @author Rafal
 */
public class MainMenuBar
        extends JMenuBar
        implements ActionListener {

    public static class FileMenu
            extends JMenu {
        
        private static final String NAME = "Plik";
        
        private static final String ITEM_NEW     = "Nowa gra";
        private static final String ITEM_REFRESH = "Odśwież";
        private static final String ITEM_QUIT    = "Wyjdź";
        
        private JMenuItem mNewItem;
        private JMenuItem mRefreshItem;
        private JMenuItem mQuitItem;
        
        public FileMenu(ActionListener actionListener) {
            super(NAME);
            
            mNewItem = new JMenuItem(ITEM_NEW);
            mNewItem.addActionListener(actionListener);
            
            mRefreshItem = new JMenuItem(ITEM_REFRESH);
            mRefreshItem.addActionListener(actionListener);
            mRefreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
            
            mQuitItem = new JMenuItem(ITEM_QUIT);
            mQuitItem.addActionListener(actionListener);
            mQuitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
            
            this.initComponent();
        }
        
        private void initComponent() {
            this.add(mNewItem);
            this.addSeparator();
            this.add(mRefreshItem);
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
        
        public void onFileNew(ActionEvent ae) {
            return;
        }
        
        public void onFileRefresh(ActionEvent ae) {
            return;
        }
        
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
        else if (source == mFileMenu.mNewItem) mMenuListener.onFileNew(ae);
        else if (source == mFileMenu.mRefreshItem) mMenuListener.onFileRefresh(ae);
        else if (source == mHelpMenu.mAboutItem) mMenuListener.onHelpAbout(ae);
    }
    
    /**
     * @param mml   reference to a listener
     */
    public void setMenuListener(MainMenuListener mml) {
        mMenuListener = mml;
    }
    
}
