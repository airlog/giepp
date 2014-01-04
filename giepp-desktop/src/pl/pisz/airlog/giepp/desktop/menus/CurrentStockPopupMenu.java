package pl.pisz.airlog.giepp.desktop.menus;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;

/**
 * @author Rafal
 *
 */
public class CurrentStockPopupMenu
        extends JPopupMenu {

    public static final String ITEM_DEFAULT_STOCK  = "<firma>";
    public static final String ITEM_BUY            = "Kup";
    public static final String ITEM_SELL           = "Sprzedaj";
    public static final String ITEM_OBSERVE        = "Obserwuj";
    public static final String ITEM_UNOBSERVE      = "Nie obserwuj";
    
    private JMenuItem mStockItem;
    private JMenuItem mBuyItem;
    private JMenuItem mSellItem;
    private JMenuItem mObserveItem;
    
    private boolean mLastChange = false;
    
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
    
    public CurrentStockPopupMenu setStockName(String name) {
        mStockItem.setText(name);
                
        return this;
    }
    
    public CurrentStockPopupMenu setObserveCommandFor(String company) {
        if (GameUtilities.isObserved(company)) mObserveItem.setText(ITEM_UNOBSERVE);
        else mObserveItem.setText(ITEM_OBSERVE);
        
        return this;
    }
           
}
