package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.table.TableCellRenderer;

import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;

/**
 * @author Rafal
 *
 */
public class RatingsPanel
        extends JPanel {

    private JSplitPane mSplitPane;
    
    private CurrentStockTable mStockTable;
    
    /**
     * 
     */
    public RatingsPanel(CurrentStockTable.TableModel tableModel) {
        super(new BorderLayout(), false);
                
        this.initWidgets(tableModel);
        this.initComponent();
    }
    
    private void initWidgets(CurrentStockTable.TableModel tableModel) {
        TableCellRenderer priceRenderer = new CurrentStockTable.PriceRenderer();
        
        mStockTable = new CurrentStockTable(tableModel);
        mStockTable.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(4).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(5).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(6).setCellRenderer(
                new CurrentStockTable.ChangeRenderer());
    }
    
    private void initComponent() {
        JScrollPane sp = new JScrollPane(mStockTable);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        mSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, new CompanyDetailsPanel());
        mSplitPane.setOneTouchExpandable(true);
        
        this.add(mSplitPane);
    }

}
