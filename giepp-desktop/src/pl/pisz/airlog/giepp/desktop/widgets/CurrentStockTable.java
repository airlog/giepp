package pl.pisz.airlog.giepp.desktop.widgets;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import java.text.DecimalFormat;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import pl.pisz.airlog.giepp.data.CurrentStock;

import pl.pisz.airlog.giepp.desktop.menus.CurrentStockPopupMenu;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;

/**
 * @author Rafal
 *
 */
public class CurrentStockTable
        extends JTable
        implements ActionListener, MouseListener {

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
    
    // TODO: można spróbować przenieść do CurrentStockTable
    public static class HeaderMouseAdapter
            extends MouseAdapter {
        
        private JTable      mTable;
        private TableModel  mTableModel;
        
        private boolean[] mColumnSorted = new boolean[TableModel.COLUMN_COUNT];
        
        public HeaderMouseAdapter(JTable table, TableModel model) {
            super();
            
            mTable = table;
            mTableModel = model;
            mColumnSorted[0] = true;
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
        
        DecimalFormat mFormat = new DecimalFormat("#0.00");
        
        @Override
        protected void setValue(Object value) {            
            super.setValue(value);
            this.setHorizontalAlignment(SwingConstants.RIGHT);
            if (!(value instanceof Integer)) return;
            
            int i = (Integer) value;
            double price = ((double) i) * 0.01;
            
            this.setText(mFormat.format(price));
        }
        
    }
    
    private CurrentStockPopupMenu mPopupMenu;
    
    public CurrentStockTable() {
        super();
        
        mPopupMenu = new CurrentStockPopupMenu(this);
        
        this.addMouseListener(this);
        this.getTableHeader().setReorderingAllowed(false);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        System.out.println(ae);
    }
    
    @Override
    public void mouseClicked(MouseEvent me) {}

    @Override
    public void mousePressed(MouseEvent me) {
        if (me.getButton() != MouseEvent.BUTTON3) return;
            
        int row = this.rowAtPoint(me.getPoint());
        if (row >= 0 && row < this.getRowCount()) this.setRowSelectionInterval(row, row);
        
        mPopupMenu
                .setStockName((String) this.getModel().getValueAt(row, 0))
                .show(me.getComponent(), me.getX(), me.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {}
    
    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
    
}
