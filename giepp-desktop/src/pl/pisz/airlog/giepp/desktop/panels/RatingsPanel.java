package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.table.TableCellRenderer;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;

/**
 * @author Rafal
 *
 */
public class RatingsPanel
        extends JPanel {

    private JSplitPane mSplitPane;    
    private CompanyDetailsPanel mDetailsPanel;
    private CurrentStockTable mStockTable;
    
    public RatingsPanel(CurrentStockTable.TableModel tableModel,
            BuyStockDialog buyDialog, SellStockDialog sellDialog) {
        super(new BorderLayout(), false);        
        
        mDetailsPanel = new CompanyDetailsPanel();
        
        mStockTable = new CurrentStockTable(tableModel, buyDialog, sellDialog);
        
        this.initWidgets();
        this.initComponents();
    }
    
    private void initWidgets() {
        TableCellRenderer priceRenderer = new CurrentStockTable.PriceRenderer();
        
        mStockTable.getColumnModel().getColumn(2).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(3).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(4).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(5).setCellRenderer(priceRenderer);
        mStockTable.getColumnModel().getColumn(6).setCellRenderer(
                new CurrentStockTable.ChangeRenderer());
        mStockTable.setCompanySelectedListener(mDetailsPanel);
    }
    
    private void initComponents() {
        JScrollPane sp = new JScrollPane(mStockTable);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        mSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sp, mDetailsPanel);
        mSplitPane.setOneTouchExpandable(true);
                        
        this.add(mSplitPane);
    }
        
}
