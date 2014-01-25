package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;

/** Panel implementujący funkcjonalność paska statusu aplikacji.
 * 
 * Pasek statusu powinien móc wyświetlać i ukrywać paski postępu dla pobierania danych archiwalnych
 * oraz notowań ciągłych.
 * 
 * @author Rafal
 */
public class StatusBar extends JPanel {

    private JProgressBar mArchiveProgressBar = new JProgressBar();
    private JProgressBar mCurrentProgressBar = new JProgressBar();
    
    /** Tworzy nowy obiekt.
     * 
     */
    public StatusBar() {
        super(new FlowLayout(FlowLayout.RIGHT));
        
        mArchiveProgressBar.setIndeterminate(true);
        mArchiveProgressBar.setVisible(false);
        
        mCurrentProgressBar.setIndeterminate(true);
        mCurrentProgressBar.setVisible(false);
        
        this.add(mArchiveProgressBar);
        this.add(mCurrentProgressBar);
    }
    
    /** Wyświetlenie lub schowanie określonego paska postępu.
     * Pasek postępu jest wyświetlany albo ukrywany zależnie od jego aktualnego stanu.
     *
     * @param progressBar   pasek postępu
     * @param caption       tekst wyświetlany na pasku postępu
     */
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
    
    /** Wyświetla lub chowa pasek postępu pobierania danych archiwalnych.
     * @param caption   tekst wyświetlany na pasku postępu
     * @see StatusBar#triggerProgressBar(JProgressBar, String)
     */
    public void triggerArchiveProgressBar(String caption) {
        this.triggerProgressBar(mArchiveProgressBar, caption);
    }
    
    /** Wyświetla lub chowa pasek postępu pobierania notowań ciągłych.
     * @param caption   tekst wyświetlany na pasku postępu
     * @see StatusBar#triggerProgressBar(JProgressBar, String)
     */
    public void triggerCurrentProgressBar(String caption) {
        this.triggerProgressBar(mCurrentProgressBar, caption);
    }
    
}
