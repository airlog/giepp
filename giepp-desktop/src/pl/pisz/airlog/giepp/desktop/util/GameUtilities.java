package pl.pisz.airlog.giepp.desktop.util;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.game.Game;

import pl.pisz.airlog.giepp.desktop.panels.StatusBar;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;

/** Klasa jest wrapperem na {@link Game} i ułatwia wykonywanie wielu operacji.
 * Pierwszą metodą wywoływaną przed wywołaniem GUI powinno być
 * {@link GameUtilities#newInstance(pl.pisz.airlog.giepp.desktop.widgets.MyStockTable.TableModel, pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable.TableModel, pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable.TableModel, StatusBar)}.
 *
 * @author Rafal
 * @see Game
 */
public class GameUtilities {
    
    private static Game instance = null;
    
    private static MyStockTable.TableModel      mMyStockTableModel          = null;
    private static CurrentStockTable.TableModel mCurrentStockTableModel     = null;
    private static CurrentStockTable.TableModel mObservedStockTableModel    = null;
    
    private static StatusBar   mStatusBar  = null;
    
    private static Thread mRefreshRatingsThread = null;
    private static Thread mRefreshArchiveThread = null;
    
    /** Usuwa firmy nie zawarte w drugim argumencie.
     * Wykorzystywane w wyświetlaniu danych w {@link GameUtilities#mObservedStockTableModel}.
     * 
     * @param stocks    filtrowana lista
     * @param observed  firmy o tych nazwach pozostaną w liście
     */
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
    
    /** Sprawdza czy obiekt {@link Game} został już utworzony.
     * @throws IllegalStateException    obiekt {@link Game} nie został jeszcze utworzony
     */
    protected static void checkGame() {
        if (instance == null) throw new IllegalStateException("Game not inited!");
    }
    
    /** Tworzy nowy obiekt klasy {@link Game} i 'wrappuje go'.
     * @param myStockTableModel model tabeli z widoku <i>Moje konto</i>
     * @param currentStockTableModel    model tabeli z widoku <i>Notowania</i>
     * @param observedStockTableModel   model tabeli z widoku <i>Obserwowane</i>
     * @param statusBar
     * @return  utworzony obiekt {@link Game}
     */
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
    
    /** Zwraca wrappowaną obiekt klasy {@link Game}.
     * @return wrappowany obiekt
     */
    public static Game getInstance() {
        GameUtilities.checkGame();
        return instance;
    }
    
    /** Odświeża aktualne notowania.
     * Gwaratuje, że tylko jeden wątek będzie odświeżał dane. Metodę należy uruchamiać jedynie
     * z wątku GUI.
     *
     * @see GameUtilities#refreshArchival(int)
     * @see RefreshRatingsTask
     * 
     * @see javax.swing.SwingUtilities#invokeAndWait(Runnable)
     * @see javax.swing.SwingUtilities#invokeLater(Runnable)
     */
    public static void refreshData() {
        GameUtilities.checkGame();
        
        if (mRefreshRatingsThread != null && mRefreshRatingsThread.isAlive()) {
            System.err.println("Tylko jeden wątek odświeżania notowań może być aktywny!");
            return;
        }
        
        mStatusBar.triggerCurrentProgressBar("   Pobieranie notowań   ");
        mRefreshRatingsThread = new Thread(new RefreshRatingsTask(mStatusBar, mCurrentStockTableModel));
        mRefreshRatingsThread.start();
    }
    
    /** Odświeża dane archiwalne.
     * Gwaratuje, że tylko jeden wątek będzie odświeżał dane. Metodę należy uruchamiać jedynie
     * z wątku GUI.
     *
     * @see GameUtilities#refreshArchival(int)
     * @see RefreshArchiveTask
     * 
     * @see javax.swing.SwingUtilities#invokeAndWait(Runnable)
     * @see javax.swing.SwingUtilities#invokeLater(Runnable)
     */
    public static void refreshArchival(final int days) {
        
        if (mRefreshArchiveThread != null && mRefreshArchiveThread.isAlive()) {
            System.err.println("Tylko jeden wątek odświeżania archiwum może być aktywny!");
            return;
        }
        
        mStatusBar.triggerArchiveProgressBar("   Pobieranie archiw...   ");        
        mRefreshArchiveThread = new Thread(new RefreshArchiveTask(mStatusBar, days));
        mRefreshArchiveThread.start();
    }
    
    /** Odświeża dane zawarte w {@link GameUtilities#mMyStockTableModel}.
     * 
     */
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
    
    /** Odświeża dane zawarte w {@link GameUtilities#mObservedStockTableModel}.
     * 
     */
    public static void refreshObservedTable() {
        String[] observed = GameUtilities.getInstance().getObserved().toArray(new String[] {});
        List<CurrentStock> stocks = new ArrayList<CurrentStock>(); 
        
        stocks.addAll(GameUtilities.getInstance().getCurrent());
        
        GameUtilities.filterStocks(stocks, observed);
                
        mObservedStockTableModel.clear();
        mObservedStockTableModel.addAll(stocks);
    }
    
    /** Sprawdza czy firma o zadanej nazwie znajduje się w obserwoanych.
     * @param company   nazwa sprawdzanej firmy
     * @return  <i>true</i> jeśli jest obserwowana, <i>false</i> w przeciwnym wypadku
     */
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
    
    /** Zwraca obiekt klasy {@link CurrentStock} odpowiadający zadanej nazwie firmy.
     * @param company   nazwa szukanej firmy
     * @return  obiekt {@link CurrentStock} lub <i>null</i> jeśli nie znaleziono
     */
    public static CurrentStock getCurrentStockByName(String company) {
        GameUtilities.checkGame();
        
        for (CurrentStock stock : GameUtilities.getInstance().getCurrent()) {
            if (stock.getName().equals(company)) return stock;
        }
        
        return null;
    }
    
    private GameUtilities() {}
        
}
