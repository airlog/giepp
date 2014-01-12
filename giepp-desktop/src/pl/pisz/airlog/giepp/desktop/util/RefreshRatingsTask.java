package pl.pisz.airlog.giepp.desktop.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.desktop.panels.StatusBar;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;

/** Umożliwa odświeżenie aktualnych notowań.
 * @author Rafal
 * @see pl.pisz.airlog.giepp.game.Game
 * @see pl.pisz.airlog.giepp.game.Game#refreshCurrent()
 */
public class RefreshRatingsTask
        implements Runnable {
    
    private CurrentStockTable.TableModel mCurrentStockTableModel;
    private StatusBar mStatusBar;

    /** Tworzy nowy obiekt.
     * @param statusBar pasek stanu programu
     * @param tableModel    model tabeli z widku <i>Notowania</i>
     */
    public RefreshRatingsTask(StatusBar statusBar, CurrentStockTable.TableModel tableModel) {        
        mStatusBar = statusBar;
        mCurrentStockTableModel = tableModel;
    }
    
    @Override
    public void run() {
        GameUtilities.getInstance().refreshCurrent();
        
        final List<CurrentStock> stocks = GameUtilities.getInstance().getCurrent();
        Collections.sort(stocks, CurrentStock.getByNameComparator());
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    GameUtilities.refreshObservedTable();   // może być pierwsze sprawdzenie
                    GameUtilities.refreshMyStockTable();
                    
                    mCurrentStockTableModel
                    .clear()
                    .addAll(stocks);
                                                
                    mStatusBar.triggerCurrentProgressBar(null);
                }
            });
        }
        catch (InvocationTargetException e) {
            System.err.println(e);
        }
        catch (InterruptedException e) {
            System.err.println(e);
        }
    }
    
}
