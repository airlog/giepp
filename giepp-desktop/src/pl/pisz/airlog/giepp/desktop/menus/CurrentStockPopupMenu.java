package pl.pisz.airlog.giepp.desktop.menus;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * @author Rafal
 *
 */
public class CurrentStockPopupMenu
        extends JPopupMenu {

    private static final String ITEM_DEFAULT_STOCK  = "<firma>";
    private static final String ITEM_BUY            = "Kup";
    private static final String ITEM_SELL           = "Sprzedaj";
    private static final String ITEM_OBSERVE        = "Obserwuj";
    
    private JMenuItem mStockItem;
    private JMenuItem mBuyItem;
    private JMenuItem mSellItem;
    private JMenuItem mObserveItem;
    
    public CurrentStockPopupMenu() {
        super();
        
        mStockItem = new JMenuItem(ITEM_DEFAULT_STOCK);
        mStockItem.setEnabled(false);
        
        mBuyItem = new JMenuItem(ITEM_BUY);
        
        mSellItem = new JMenuItem(ITEM_SELL);
        
        mObserveItem = new JMenuItem(ITEM_OBSERVE);
        
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
    
    public CurrentStockPopupMenu setStockName(String name) {
        mStockItem.setText(name);
        
        return this;
    }
       
}
