package pl.pisz.airlog.giepp.desktop.menus;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/** Pasek głównego menu aplikacji.
 * Klasa implementuje {@link ActionListener} i sama nasłuchuje na kliknięcia odpowiednich obiektów
 * w menu. Następnie rozpoznaje kliknęty obiekt i wywołuje odpowiednią metodę.
 * 
 * @author Rafal
 */
public class MainMenuBar extends JMenuBar
        implements ActionListener {

    private static class FileMenu
            extends JMenu {
        
        private static final String NAME = "Plik";
        
        private static final String ITEM_NEW    = "Nowa gra";
        private static final String ITEM_SAVE   = "Zapisz grę";
        private static final String ITEM_QUIT   = "Wyjdź";
        
        private JMenuItem mNewItem;
        private JMenuItem mSaveItem;
        private JMenuItem mQuitItem;
        
        public FileMenu(ActionListener actionListener) {
            super(NAME);
            
            mNewItem = new JMenuItem(ITEM_NEW);
            mNewItem.addActionListener(actionListener);
            
            mSaveItem = new JMenuItem(ITEM_SAVE);
            mSaveItem.addActionListener(actionListener);
            mSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
            
            mQuitItem = new JMenuItem(ITEM_QUIT);
            mQuitItem.addActionListener(actionListener);
            mQuitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
            
            this.initComponent();
        }
        
        private void initComponent() {
            this.add(mNewItem);
            this.add(mSaveItem);
            this.addSeparator();
            this.add(mQuitItem);
        }
        
    }
    
    private static class ToolsMenu        
            extends JMenu {
        
        private static final String NAME = "Narzędzia";
        
        private static final String ITEM_REFRESH = "Odśwież";
        private static final String ITEM_ARCHIVE_DOWNLOAD = "Odśwież archiwum";
        
        private JMenuItem mRefreshItem;
        private JMenuItem mArchiveDownload;
        
        public ToolsMenu(ActionListener actionListener) {
            super(NAME);
            
            mRefreshItem = new JMenuItem(ITEM_REFRESH);
            mRefreshItem.addActionListener(actionListener);
            mRefreshItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0));
            
            mArchiveDownload = new JMenuItem(ITEM_ARCHIVE_DOWNLOAD);
            mArchiveDownload.addActionListener(actionListener);
     
            this.initComponent();
        }
        
        private void initComponent() {
            this.add(mRefreshItem);
            this.addSeparator();
            this.add(mArchiveDownload);
        }
        
    }
    
    private static class HelpMenu
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
    
    /** Klasa z metodami wywołanymi po kliknęciu odpowiednich obiektów w menu.
     * Metody oferowane przez klasę są puste - wymagają implemetacji.
     *
     * @author Rafal
     */
    public static class MainMenuListener {
        
        /** Zakończenie obecnej i rozpoczęcie nowej rozgrywki.
         * @param ae
         */
        public void onFileNew(ActionEvent ae) {}
        
        /** Zapisanie stanu obecnej rozgrywki.
         * @param ae
         */
        public void onSaveGame(ActionEvent ae) {}
        
        /** Wyjście z aplikacji.
         * @param ae
         */
        public void onFileQuit(ActionEvent ae) {}
        
        /** Odświeżenie notowań ciągłych.
         * @param ae
         */
        public void onRefresh(ActionEvent ae) {}
        
        /** Odświeżenie notowań archiwalnych.
         * @param ae
         */
        public void onArchiveDownload(ActionEvent ae) {}
        
        /** Wyświetlenie okna dialogowego z informacjami o aplikacji.
         * @param ae
         */
        public void onHelpAbout(ActionEvent ae) {}
                
    }
    
    private FileMenu    mFileMenu;
    private ToolsMenu   mToolsMenu;
    private HelpMenu    mHelpMenu;
    
    private MainMenuListener mMenuListener = null;
    
    /** Tworzy nowy obiekt.
     * 
     */
    public MainMenuBar() {
        super();
        
        mFileMenu = new FileMenu(this);
        mToolsMenu = new ToolsMenu(this);
        mHelpMenu = new HelpMenu(this);
        
        this.initComponent();
    }
    
    private void initComponent() {
        this.add(mFileMenu);
        this.add(mToolsMenu);
        this.add(mHelpMenu);
    }

    /** Wywołuje odpowiednie metody zależnie od źródła sygnału.
     * @param ae
     * @see MainMenuListener
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (mMenuListener == null) return;
        
        Object source = ae.getSource();
        if (source == mFileMenu.mQuitItem) mMenuListener.onFileQuit(ae);
        else if (source == mFileMenu.mNewItem) mMenuListener.onFileNew(ae);
        else if (source == mFileMenu.mSaveItem) mMenuListener.onSaveGame(ae);
        else if (source == mToolsMenu.mRefreshItem) mMenuListener.onRefresh(ae);
        else if (source == mToolsMenu.mArchiveDownload) mMenuListener.onArchiveDownload(ae);
        else if (source == mHelpMenu.mAboutItem) mMenuListener.onHelpAbout(ae);
    }
    
    /** Ustawia klasę przetwarzającą sygnały.
     * @param mml   reference to a listener
     */
    public void setMenuListener(MainMenuListener mml) {
        mMenuListener = mml;
    }
    
}
