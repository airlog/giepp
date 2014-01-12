package pl.pisz.airlog.giepp.desktop.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Calendar;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Collections;

import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.desktop.panels.RatingsPanel;
import pl.pisz.airlog.giepp.desktop.panels.StatusBar;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;
import pl.pisz.airlog.giepp.game.Game;

public class GameUtilities {
    
    private static Game instance = null;
    
    private static MyStockTable.TableModel      mMyStockTableModel          = null;
    private static CurrentStockTable.TableModel mCurrentStockTableModel     = null;
    private static CurrentStockTable.TableModel mObservedStockTableModel    = null;
    
    private static StatusBar   mStatusBar  = null;
    
    private static void filterStocks(List<CurrentStock> stocks, String... observed) {
        LinkedList<CurrentStock> rubbish = new LinkedList<CurrentStock>();
        for (CurrentStock stock : stocks) {
            boolean found = false;
            for (String company : observed) {
                if (!company.equals(stock.getName())) continue;
                found = true;
                break;
            }
            if (!found) rubbish.add(stock);
        }
        stocks.removeAll(rubbish);
    }
    
    protected static void checkGame() {
        if (instance == null) throw new IllegalStateException("Game not inited!");
    }
        
    public static Game newInstance(
            MyStockTable.TableModel myStockTableModel,
            CurrentStockTable.TableModel currentStockTableModel,
            CurrentStockTable.TableModel observedStockTableModel,
            StatusBar statusBar) {
        if (instance == null) instance = HelperTools.newGame();
        
        mMyStockTableModel = myStockTableModel;
        mCurrentStockTableModel = currentStockTableModel;
        mObservedStockTableModel = observedStockTableModel;
                
        mStatusBar = statusBar;
        
        return instance;
    }
    
    public static Game getInstance() {
        GameUtilities.checkGame();
        return instance;
    }
    
    // uruchamiać tylko z wątku GUI
    public static void refreshData() {
        GameUtilities.checkGame();
        
        mStatusBar.triggerCurrentProgressBar("   Pobieranie notowań   ");
        
        (new Thread() {
            @Override
            public void run() {
                GameUtilities.instance.refreshCurrent();
                final List<CurrentStock> stocks = GameUtilities.instance.getCurrent();
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
        }).start();
    }
    
    // odpalane tylko z wątku GUI
    public static void refreshArchival(final int days) {
        mStatusBar.triggerArchiveProgressBar("   Pobieranie archiw...   ");
        
        (new Thread() {
            @Override
            public void run() {
                /* ostatnie 30 dni */
                long currentTime = System.currentTimeMillis()/1000;
                long fixedTime = 24 * 3600 * days;
                
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
                GameUtilities.instance.refreshArchival(startDay, startMonth, startYear, endDay, endMonth, endYear);
            
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mStatusBar.triggerArchiveProgressBar(null);
                    }
                });
            }
        }).start();
    }
    
    public static void refreshMyStockTable() {
        GameUtilities.checkGame();
        
        List<PlayerStock> stocks = instance.getOwned();
        List<CurrentStock> currents = instance.getCurrent();
        
        // FIXME: to wyszukiwanie jest dosyć wolne, można posortować i bsearch
        // Collections.sort(stocks, PlayerStock.getByNameComparator());
        for (PlayerStock stock : stocks) {
            CurrentStock current = null;
            for (CurrentStock cs : currents) {
                if (!cs.getName().equals(stock.getCompanyName())) continue;
                current = cs;
                break;
            }
            
            if (current == null) continue;
            stock.setCurrentValue((double) stock.getAmount() * (double) current.getEndPrice() * 0.01);
        }
        
        mMyStockTableModel.clear();
        mMyStockTableModel.addAll(stocks);
    }
    
    public static void refreshObservedTable() {
        String[] observed = GameUtilities.getInstance().getObserved().toArray(new String[] {});
        List<CurrentStock> stocks = new ArrayList<CurrentStock>(); 
        
        stocks.addAll(GameUtilities.getInstance().getCurrent());
        
        GameUtilities.filterStocks(stocks, observed);
                
        mObservedStockTableModel.clear();
        mObservedStockTableModel.addAll(stocks);
    }
    
    public static boolean isObserved(String company) {
        GameUtilities.checkGame();
        
        boolean state = false;
        for (String s : instance.getObserved()) {
            if (!s.equals(company)) continue;
            state = true;
            break;
        }
        
        return state;
    }
    
    public static CurrentStock getCurrentStockByName(String company) {
        GameUtilities.checkGame();
        
        for (CurrentStock stock : GameUtilities.getInstance().getCurrent()) {
            if (stock.getName().equals(company)) return stock;
        }
        
        return null;
    }
        
    private GameUtilities() {}
        
}
