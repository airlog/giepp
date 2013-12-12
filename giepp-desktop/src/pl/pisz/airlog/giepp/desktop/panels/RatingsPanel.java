package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableCellRenderer;

import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;

/**
 * @author Rafal
 *
 */
public class RatingsPanel
        extends JPanel {

    private CurrentStockTable mStockTable;
    
    /**
     * 
     */
    public RatingsPanel(CurrentStockTable.TableModel tableModel) {
        super(new BorderLayout(), false);
        
        TableCellRenderer priceRenderer = new CurrentStockTable.PriceRenderer();
        
        mStockTable = new CurrentStockTable();
        mStockTable.setModel(tableModel);
        mStockTable.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(4).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(5).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(6).setCellRenderer(
                new CurrentStockTable.ChangeRenderer());
        mStockTable.getTableHeader().addMouseListener(
                new CurrentStockTable.HeaderMouseAdapter(mStockTable, tableModel));
        
        this.initComponent();
    }
    
    private void initComponent() {
        JScrollPane sp = new JScrollPane(mStockTable);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.add(sp);
    }

}
