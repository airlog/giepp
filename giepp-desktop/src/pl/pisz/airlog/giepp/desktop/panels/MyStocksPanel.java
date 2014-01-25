package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;

/** Panel widoku mojego konta.
 * Panel zawiera informacje o aktualnym stanie konta i tabelę prezentującą aktualnie posiadane
 * akcje.
 * 
 * @author Rafal
 * @see MyStockTable
 * @see MyStockDetailsPanel
 */
public class MyStocksPanel extends JPanel {
    
    private MyStockDetailsPanel mDetailsPanel;
    private MyStockTable mStockTable;
    
    /** Tworzy nowy obiekt.
     * @param tableModel    model tabeli wykorzystywanej w widoku
     * @param buyDialog     okno dialogowe zakupu
     * @param sellDialog    okno dialogowe sprzedaży
     */
    public MyStocksPanel(MyStockTable.TableModel tableModel,
            BuyStockDialog buyDialog, SellStockDialog sellDialog) {
        super(new BorderLayout(), false);
        
        mDetailsPanel = new MyStockDetailsPanel();
        mStockTable = new MyStockTable(tableModel, buyDialog, sellDialog);
        
        this.initWidgets();
        this.initComponent();
    }
    
    private void initWidgets() {
        TableCellRenderer priceRenderer = new MyStockTable.PriceRenderer();
        
        mStockTable.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);
    }
    
    private void initComponent() {        
        JScrollPane sp = new JScrollPane(mStockTable);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.add(mDetailsPanel, BorderLayout.NORTH);
        this.add(sp, BorderLayout.CENTER);
    }

}
