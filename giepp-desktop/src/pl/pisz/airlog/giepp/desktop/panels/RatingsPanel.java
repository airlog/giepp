package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

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
    public RatingsPanel(TableModel tableModel) {
        super(new BorderLayout(), false);
        
        mStockTable = new CurrentStockTable();
        mStockTable.setModel(tableModel);
        mStockTable.getColumnModel().getColumn(6).setCellRenderer(
                new CurrentStockTable.ChangeRenderer());
        
        this.initComponent();
    }
    
    private void initComponent() {
        JScrollPane sp = new JScrollPane(mStockTable);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        this.add(sp);
    }

}
