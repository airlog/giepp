package pl.pisz.airlog.giepp.desktop.util;

import java.util.Calendar;

import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.desktop.panels.StatusBar;

/** Umożliwa odświeżenie danych archiwalnych.
 * @author Rafal
 * @see pl.pisz.airlog.giepp.game.Game
 * @see pl.pisz.airlog.giepp.game.Game#refreshArchival(int, int, int, int, int, int)
 */
public class RefreshArchiveTask
        implements Runnable {
    
    private StatusBar mStatusBar;
    private int mDays;

    /** Tworzy nowy obiekt.
     * @param statusBar pasek statusu programu
     * @param days  ilość dni, z których pobrane zostaną dane
     */
    public RefreshArchiveTask(StatusBar statusBar, int days) {        
        mStatusBar = statusBar;
        mDays = days;
    }
        
    @Override
    public void run() {
        /* ostatnie 30 dni */
        long currentTime = System.currentTimeMillis()/1000;
        long fixedTime = 24 * 3600 * mDays;
        
        System.err.println(String.format("%d\n%d", currentTime, fixedTime));
        
        Calendar calendar = Calendar.getInstance();
        int endDay = calendar.get(Calendar.DAY_OF_MONTH),
                endMonth = calendar.get(Calendar.MONTH) + 1,
                endYear = calendar.get(Calendar.YEAR);
        
        calendar.setTimeInMillis((currentTime - fixedTime)*1000);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH),
                startMonth = calendar.get(Calendar.MONTH) + 1,  // metoda w Game liczy od 1
                startYear = calendar.get(Calendar.YEAR);
                        
        System.err.println(String.format("Gathering archive: %d-%d-%d => %d-%d-%d",
                startDay, startMonth, startYear, endDay, endMonth, endYear));
        
        /* pobieranie */
        GameUtilities.getInstance().refreshArchival(startDay, startMonth, startYear, endDay, endMonth, endYear);
    
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mStatusBar.triggerArchiveProgressBar(null);
            }
        });
    }
    
}
