package pl.pisz.airlog.giepp.desktop.util;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;

import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;
import pl.pisz.airlog.giepp.game.Game;

public class GameUtilities {
    
    private static Game instance = null;
    
    private static MyStockTable.TableModel mMyStockTableModel               = null;
    private static CurrentStockTable.TableModel mCurrentStockTableModel     = null;
    private static CurrentStockTable.TableModel mObservedStockTableModel    = null;
    
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
            CurrentStockTable.TableModel observedStockTableModel) {
        if (instance == null) instance = HelperTools.newGame();
        
        mMyStockTableModel = myStockTableModel;
        mCurrentStockTableModel = currentStockTableModel;
        mObservedStockTableModel = observedStockTableModel;
        
        return instance;
    }
    
    public static Game getInstance() {
        GameUtilities.checkGame();
        return instance;
    }
        
    public static void refreshData() {
        GameUtilities.checkGame();
        
        (new Thread() {
            @Override
            public void run() {
                GameUtilities.instance.refreshCurrent();
                final List<CurrentStock> stocks = GameUtilities.instance.getCurrent();
                Collections.sort(stocks, CurrentStock.getByNameComparator());
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mCurrentStockTableModel
                        .clear()
                        .addAll(stocks);
                    }
                });
            }
        }).start();
    }
    
    public static void refreshArchival() {
        (new Thread() {
            @Override
            public void run() {
                GameUtilities.instance.refreshArchival();
            }
        }).start();
    }
    
    public static void refreshMyStockTable() {
        GameUtilities.checkGame();
        
        List<PlayerStock> stocks = instance.getOwned();
        List<CurrentStock> currents = instance.getCurrent();
//        Collections.sort(stocks, PlayerStock.getByNameComparator());
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
        
        System.err.println(String.format("stocks before filter: %d", stocks.size()));
        GameUtilities.filterStocks(stocks, observed);
        System.err.println(String.format("stocks after filter: %d", stocks.size()));
                
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
        
    private GameUtilities() {}
        
}
