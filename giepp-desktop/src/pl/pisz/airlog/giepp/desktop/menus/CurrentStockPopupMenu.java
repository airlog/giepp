package pl.pisz.airlog.giepp.desktop.menus;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;

/** Menu wyświetlające się po kliknięciu prawym przyciskiem myszy na wybrany wiersz tabeli.
 *
 * Przed wyświetleniem menu konieczne jest wywołanie metod {@link CurrentStockPopupMenu#setStockName(String)},
 * ustawiającą nazwę klikniętego wiersza, oraz {@link CurrentStockPopupMenu#setObserveCommandFor(String)},
 * przeszukującą obserwowane akcje celem ustawienia odpowiedniej etykiety dla obiektu menu.
 *
 * @author Rafal
 * @see pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable
 */
public class CurrentStockPopupMenu extends JPopupMenu {

    /** Domyślna etykieta menu. */
    public static final String ITEM_DEFAULT_STOCK  = "<firma>";
    
    /** Etykieta operacji kupowania. */
    public static final String ITEM_BUY            = "Kup";
    
    /** Etykieta operacji sprzedawania. */
    public static final String ITEM_SELL           = "Sprzedaj";
    
    /** Etykieta operacji obserwowania. */
    public static final String ITEM_OBSERVE        = "Obserwuj";
    
    /** Etykieta operacji usunięcia z obserwowanych. */
    public static final String ITEM_UNOBSERVE      = "Nie obserwuj";
    
    private JMenuItem mStockItem;
    private JMenuItem mBuyItem;
    private JMenuItem mSellItem;
    private JMenuItem mObserveItem;
    
    /** Tworzy nowy obiekt.
     * @param al    obiekt przetwarzający sygnały (wybór pola z menu)
     */
    public CurrentStockPopupMenu(ActionListener al) {
        super();
        
        mStockItem = new JMenuItem(ITEM_DEFAULT_STOCK);
        mStockItem.setEnabled(false);
        
        mBuyItem = new JMenuItem(ITEM_BUY);
        mBuyItem.setMnemonic(KeyEvent.VK_K);
        mBuyItem.addActionListener(al);
        
        mSellItem = new JMenuItem(ITEM_SELL);
        mSellItem.setMnemonic(KeyEvent.VK_S);
        mSellItem.addActionListener(al);
        
        mObserveItem = new JMenuItem(ITEM_OBSERVE);
        mObserveItem.setMnemonic(KeyEvent.VK_O);
        mObserveItem.addActionListener(al);
        
        this.initComponent();
    }
    
    private void initComponent() {
        this.add(mStockItem);
        this.addSeparator();
        this.add(mBuyItem);
        this.add(mSellItem);
        this.addSeparator();
        this.add(mObserveItem);
    }
    
    /** Przeszukuje bazę posiadanych akcji.
     * @param company   poszukiwana firma
     * @return  obiekt akcji reprezentujący szukaną firmę lub <i>null</i>, jeśli nie znaleziono
     */
    protected PlayerStock findOwnedStock(String company) {
        for (PlayerStock stock : GameUtilities.getInstance().getOwned()) {
            if (stock.getCompanyName().equals(company)) return stock;
        }
        return null;
    }
    
    @Override
    public void show(Component invoker, int x, int y) {
        mSellItem.setEnabled(this.findOwnedStock(mStockItem.getText()) != null);
        
        super.show(invoker, x, y);
    }
    
    /** Ustawia etykietę tego menu.
     * Etykietą menu jest nazwa firmy, na której wiersz kliknięto.
     *
     * @param name  nazwa firmy
     * @return  ten obiekt
     */
    public CurrentStockPopupMenu setStockName(String name) {
        mStockItem.setText(name);
                
        return this;
    }
    
    /** Ustawia etykietę elementu menu.
     * Ustawia etykietę {@link CurrentStockPopupMenu#ITEM_OBSERVE} jeśli firma jest nieobserwowana
     * i {@link CurrentStockPopupMenu#ITEM_UNOBSERVE} jeśli już ją obserwujemy.
     *
     * @param company  nazwa firmy
     * @return  ten obiekt
     */
    public CurrentStockPopupMenu setObserveCommandFor(String company) {
        if (GameUtilities.isObserved(company)) mObserveItem.setText(ITEM_UNOBSERVE);
        else mObserveItem.setText(ITEM_OBSERVE);
        
        return this;
    }
           
}
