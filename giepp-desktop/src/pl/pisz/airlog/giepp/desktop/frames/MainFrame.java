package pl.pisz.airlog.giepp.desktop.frames;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/** Implementacja głównego okna aplikacji.
 * Główne okno posiada standardowy {@link BorderLayout}, w którego centrum znajduje się {@link JTabbedPane}.
 * W południowej cześci layoutu umieszczony został pasek statusu (zwykły {@link JPanel}).
 * 
 * @author Rafal
 */
public class MainFrame extends JFrame {
    
    private static final String FRAME_CAPTION   = "giepp-desktop";
    
    private JTabbedPane mTabbedPane;
    private JPanel      mStatusBar;
    
    /** Tworzy nowy obiekt.
     * Podając argumenty trzeba pamiętać o umieszczeniu odpowiednich obiektów na tych samych pozycjach
     * w tablicach.
     * 
     * @param tabs  obiekty klasy {@link JPanel} zawierające poszczególne zakładki
     * @param titles    tytuły kolejnych zakładek
     * @param icons ikony odpowiednich zakładek
     */
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
    
    /** Ustawia pasek statusu aplikacji.
     * @param statusBar panel implementujący pasek statusu
     */
    public void setStatusBar(JPanel statusBar) {
        mStatusBar = statusBar;
        this.add(mStatusBar, BorderLayout.SOUTH);
    }
    
}
