package pl.pisz.airlog.giepp.desktop.widgets;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JTable;

import javax.swing.table.AbstractTableModel;

import pl.pisz.airlog.giepp.data.CurrentStock;

/**
 * @author Rafal
 *
 */
public class CurrentStockTable
        extends JTable {

    public static class CurrentStockTableModel
            extends AbstractTableModel {

        private static final int COLUMN_COUNT   = 7;    // tyle informacji przenosi CurrentStock
        private static final String COLUMN_NAMES[] = {
                    "Nazwa", "Ostatnia aktualizacja", "Cena minimalna", "Cena maksymalna",
                    "Cena rozpoczęcia", "Cena końcowa", "Zmiana (w %)"
                };
        
        private ArrayList<CurrentStock> mStocks = new ArrayList<CurrentStock>();
        
        public CurrentStockTableModel() {
            super();
            
            mStocks.add(new CurrentStock("TestingStock", "13:03", 100, 80, 160, 140, new Float(2.0)));
        }
        
        public CurrentStockTableModel add(CurrentStock stock) {
            mStocks.add(stock);
            this.fireTableDataChanged();
            
            return this;
        }
        
        public CurrentStockTableModel addAll(Collection<? extends CurrentStock> c) {
            mStocks.addAll(c);
            this.fireTableDataChanged();
            
            return this;
        }

        public CurrentStockTableModel clear() {
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
                value = stock.getMinPrice();
                break;
            
            case 3:
                value = stock.getMaxPrice();
                break;
            
            case 4:
                value = stock.getStartPrice();
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
        
    }
    
}
