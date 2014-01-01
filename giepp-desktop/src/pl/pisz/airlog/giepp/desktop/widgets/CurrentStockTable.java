package pl.pisz.airlog.giepp.desktop.widgets;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import pl.pisz.airlog.giepp.data.CurrentStock;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.menus.CurrentStockPopupMenu;

import pl.pisz.airlog.giepp.desktop.util.CompanySelectedListener;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;

/**
 * @author Rafal
 *
 */
public class CurrentStockTable
        extends JTable
        implements ActionListener {

    public static class TableModel
            extends AbstractTableModel {

        public static final int COLUMN_COUNT   = 7;    // tyle informacji przenosi CurrentStock
        public static final String COLUMN_NAMES[] = {
                    "Nazwa", 
                    "Ostatnia aktualizacja", "Cena rozpoczęcia",
                    "Cena minimalna", "Cena maksymalna",
                    "Cena końcowa", "Zmiana (w %)"
                };
        
        private ArrayList<CurrentStock> mStocks = new ArrayList<CurrentStock>();
        
        public TableModel() {
            super();
            
            mStocks.add(new CurrentStock("TestingStock", "13:03", 100, 80, 160, 140, new Float(2.0)));
        }
        
        public TableModel add(CurrentStock stock) {
            mStocks.add(stock);
            this.fireTableDataChanged();
            
            return this;
        }
        
        public TableModel addAll(Collection<? extends CurrentStock> c) {
            mStocks.addAll(c);
            this.fireTableDataChanged();
            
            return this;
        }

        public TableModel clear() {
            int size = mStocks.size();
            
            mStocks.clear();
            this.fireTableRowsDeleted(0, size - 1);
            
            return this;
        }
        
        @Override
        public int getRowCount() {
            return mStocks.size();
        }

        @Override
        public int getColumnCount() {
            return COLUMN_COUNT;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Object value = null;
            
            CurrentStock stock = mStocks.get(rowIndex);
            switch (columnIndex) {
            case 0:
                value = stock.getName();
                break;
            
            case 1:
                value = stock.getTime();
                break;
            
            case 2:
                value = stock.getStartPrice();
                break;
            
            case 3:
                value = stock.getMinPrice();
                break;
            
            case 4:
                value = stock.getMaxPrice();
                break;
            
            case 5:
                value = stock.getEndPrice();
                break;
            
            case 6:
                value = stock.getChange();
                break;
                
            default: break;
            }
            
            return value;
        }

        @Override
        public Class getColumnClass(int columnIndex) {            
            return this.getValueAt(0, columnIndex).getClass();
        }
        
        @Override
        public String getColumnName(int columnIndex) {
            return COLUMN_NAMES[columnIndex];
        }

        public CurrentStock getStock(int row) {
            return mStocks.get(row);
        }
        
        public void sort(Comparator<CurrentStock> comparator) {
            Collections.sort(mStocks, comparator);
            
            final AbstractTableModel model = this;
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    model.fireTableDataChanged();
                }
            });
        }
        
    }
    
    public static class TableMouseAdapter
            extends MouseAdapter {
        
        public CurrentStockTable mTable;
        
        public TableMouseAdapter(CurrentStockTable table) {
            super();
            
            mTable = table;
            
        }
        
        @Override
        public void mousePressed(MouseEvent me) {
            if (me.getButton() != MouseEvent.BUTTON3) return;
                
            int row = mTable.rowAtPoint(me.getPoint());
            if (row >= 0 && row < mTable.getRowCount()) mTable.setRowSelectionInterval(row, row);
            
            mTable.getPopup()
                    .setStockName((String) mTable.getModel().getValueAt(row, 0))
                    .show(me.getComponent(), me.getX(), me.getY());
        }
        
    }
    
    public static class HeaderMouseAdapter
            extends MouseAdapter {
        
        private CurrentStockTable mTable;
        private TableModel mTableModel;  
        
        private boolean[] mColumnSorted = new boolean[TableModel.COLUMN_COUNT];
        
        public HeaderMouseAdapter(CurrentStockTable table, TableModel model) {
            super();
            
            mTable = table;
            mTableModel = model;
        }
        
        protected void changeState(int pos) {
            for (int i = 0; i < mColumnSorted.length; i++) {
                if (i == pos) mColumnSorted[i] = !mColumnSorted[i];
                else mColumnSorted[i] = false;
            }
        }
        
        protected void triggerSortByName(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByNameComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        protected void triggerSortByTime(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByTimeComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        protected void triggerSortByStartPrice(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByStartPriceComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        protected void triggerSortByMinPrice(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByMinPriceComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        protected void triggerSortByMaxPrice(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByMaxPriceComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        protected void triggerSortByEndPrice(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByEndPriceComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        protected void triggerSortByChange(boolean sorted) {
            Comparator<CurrentStock> comparator = CurrentStock.getByChangeComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        
        @Override
        public void mouseClicked(MouseEvent me) {
            if (me.getButton() != MouseEvent.BUTTON1) return;
                
            int column = mTable.columnAtPoint(me.getPoint());
            if (column < 0 || column >= mTable.getColumnCount()) return;
                
            boolean sorted = mColumnSorted[column];
            this.changeState(column);
            switch (column) {
            case 0:
                this.triggerSortByName(sorted);
                break;
            case 1:
                this.triggerSortByTime(sorted);
                break;
            case 2:
                this.triggerSortByStartPrice(sorted);
                break;
            case 3:
                this.triggerSortByMinPrice(sorted);
                break;
            case 4:
                this.triggerSortByMaxPrice(sorted);
                break;
            case 5:
                this.triggerSortByEndPrice(sorted);
                break;
            case 6:
                this.triggerSortByChange(sorted);
                break;
            }
        }
        
    }
    
    public static class ChangeRenderer
            extends DefaultTableCellRenderer {
    
        public static Color COLOR_INCREASED = Color.GREEN;
        public static Color COLOR_DECREASED = Color.RED;
        
        private Color mDefaultColor;
        
        public ChangeRenderer() {
            super();
            
            mDefaultColor = this.getForeground();
        }
        
        @Override
        protected void setValue(Object value) {
            this.setHorizontalAlignment(SwingConstants.RIGHT);
            super.setValue(value);
            
            if (!(value instanceof Float)) return;
            
            float f = (Float) value;
            if (f > 0.0f) this.setForeground(COLOR_INCREASED);
            else if (f < 0.0f) this.setForeground(COLOR_DECREASED);
            else this.setForeground(mDefaultColor);
        }
        
    }

    public static class PriceRenderer
            extends DefaultTableCellRenderer {
                
        @Override
        protected void setValue(Object value) {            
            super.setValue(value);
            this.setHorizontalAlignment(SwingConstants.RIGHT);
            if (!(value instanceof Integer)) return;
            
            int i = (Integer) value;
            double price = ((double) i) * 0.01;
            
            this.setText(HelperTools.getPriceFormat().format(price));
        }
        
    }
    
    private CurrentStockPopupMenu mPopupMenu;
    private BuyStockDialog mBuyDialog;
    
    private CompanySelectedListener mCompanySelectedListener = null;
    
    public CurrentStockTable(TableModel model, BuyStockDialog buyDialog) {
        super();
        
        mPopupMenu = new CurrentStockPopupMenu(this);
        mBuyDialog = buyDialog;
        
        this.setModel(model);
        this.addMouseListener(new TableMouseAdapter(this));
        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().addMouseListener(new HeaderMouseAdapter(this, model));
    }
    
    protected void showBuyDialog() {
        if (mBuyDialog.isVisible()) mBuyDialog.setVisible(false);
        
        mBuyDialog.setCompany(((TableModel) this.getModel()).getStock(this.getSelectedRow()));
        mBuyDialog.setVisible(true);
    }
    
    @Override
    public void valueChanged(ListSelectionEvent lse) {
        super.valueChanged(lse);
        
        if (lse.getValueIsAdjusting()) return;
        
        ListSelectionModel lsm = (ListSelectionModel) lse.getSource();
        if (lsm.isSelectionEmpty() || mCompanySelectedListener == null) return;
        
        int row = lsm.getMinSelectionIndex();
        mCompanySelectedListener.onCompanySelected((String) this.getModel().getValueAt(row, 0));
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        System.err.println(ae);
        if (ae.getActionCommand().equals("Kup")) this.showBuyDialog();
    }
        
    public void setCompanySelectedListener(CompanySelectedListener l) {
        mCompanySelectedListener = l;
    }
    
    public CurrentStockPopupMenu getPopup() {
        return mPopupMenu;
    }
    
}
