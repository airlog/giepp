package pl.pisz.airlog.giepp.desktop.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import javax.swing.event.ListSelectionEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import pl.pisz.airlog.giepp.data.PlayerStock;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.menus.CurrentStockPopupMenu;

import pl.pisz.airlog.giepp.desktop.util.CompanySelectedListener;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;

/** Tabela prezentująca posiadane pakiety akcji.
 *
 * Klasa zapewnia obiekty bezpośrednio wykorzystywane przez tabelę, mające mniejsze lub większe
 * z nią połączenie. Klasa implementuje także interfejs {@link ActionListener} co umożliwa jej
 * reagowanie na kliknięcia myszą na pola w tabeli.
 *
 * @author Rafal
 */
public class MyStockTable extends JTable
        implements ActionListener {

    /** Jedyny akceptowalny model tabeli.
     * @author Rafal
     */
    public static final class TableModel extends AbstractTableModel {

        public static final int COLUMN_COUNT   = 4;
        public static final String COLUMN_NAMES[] = {
                    "Nazwa", 
                    "Ilość", "Kwota zakupu",
                    "Wartość",
                };
        
        private ArrayList<PlayerStock> mStocks = new ArrayList<PlayerStock>();
        
        /** Tworzy nowy obiekt.
         *
         */
        public TableModel() {
            super();
        }
        
        /** Dodaje obiekt akcji do tabeli. 
         * @param stock obiekt akcji
         * @return  ten obiekt
         */
        public TableModel add(PlayerStock stock) {
            mStocks.add(stock);
            this.fireTableDataChanged();
            
            return this;
        }
        
        /** Dodaje wszystkie obiekty akcji z danej kolekcji do tabeli.
         * @param c kolekcja obiektów akcji
         * @return  ten obiekt
         */
        public TableModel addAll(Collection<? extends PlayerStock> c) {
            mStocks.addAll(c);
            this.fireTableDataChanged();
            
            return this;
        }

        /** Usuwa wszystkie wiersze z tabeli.
         * @return  ten obiekt
         */
        public TableModel clear() {
            int size = mStocks.size();
            
            if (size > 0) {
                mStocks.clear();
                this.fireTableRowsDeleted(0, size - 1);
            }
            
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
            
            PlayerStock stock = mStocks.get(rowIndex);
            switch (columnIndex) {
            case 0:
                value = stock.getCompanyName();
                break;
            
            case 1:
                value = stock.getAmount();
                break;
            
            case 2:
                value = stock.getStartPrice();
                break;
            
            case 3:
                value = stock.getCurrentValue();
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

        /** Zwraca obiek akcji.
         * @param row   numer wiersza
         * @return  obiekt akcji z żądanego wiersza
         */
        public PlayerStock getStock(int row) {
            return mStocks.get(row);
        }
        
        /** Sortuje wiersze.
         * @param comparator    komparator określający porządek
         */
        public void sort(Comparator<PlayerStock> comparator) {
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

    /** Implementacja adaptera myszy dla wierszy tabeli.
     * @author Rafal
     */
    public static class TableMouseAdapter extends MouseAdapter {
        
        public MyStockTable mTable;
        
        /** Tworzy nowy obiekt.
         * @param table tabela, która nasłuchuje.
         */
        public TableMouseAdapter(MyStockTable table) {
            super();
            
            mTable = table;
            
        }
        
        @Override
        public void mousePressed(MouseEvent me) {
            if (me.getButton() != MouseEvent.BUTTON3) return;
                
            int row = mTable.rowAtPoint(me.getPoint());
            if (row >= 0 && row < mTable.getRowCount()) mTable.setRowSelectionInterval(row, row);
            
            String company = (String) mTable.getModel().getValueAt(row, 0);
            mTable.getPopup()
                    .setStockName(company)
                    .setObserveCommandFor(company)
                    .show(me.getComponent(), me.getX(), me.getY());
        }
        
    }
    
    /** Implementacja adaptera myszy dla nagłówka tabeli.
     * 
     * Kliknięcie w nagłówek tabeli powoduje posortowanie jej zgodnie z określonym porządkiem.
     * 
     * @author Rafal
     */
    public static class HeaderMouseAdapter extends MouseAdapter {
        
        private MyStockTable mTable;
        private TableModel mTableModel;  
        
        private boolean[] mColumnSorted = new boolean[TableModel.COLUMN_COUNT];
        
        /** Tworzy nowy obiekt.
         * @param table tabela
         * @param model model tabeli
         */
        public HeaderMouseAdapter(MyStockTable table, TableModel model) {
            super();
            
            mTable = table;
            mTableModel = model;
        }
        
        /** Zmienia stan danej kolumny.
         * 
         * Przez stan kolumny rozumiane jest czy tabela jest względem niej posortowana. Umożliwa
         * to dynamiczny wybór porządku wierszy.
         * 
         * @param pos   numer kolumny
         */
        protected void changeState(int pos) {
            for (int i = 0; i < mColumnSorted.length; i++) {
                if (i == pos) mColumnSorted[i] = !mColumnSorted[i];
                else mColumnSorted[i] = false;
            }
        }
        
        /** Sortuje wiersze po nazwie.
         * @param sorted    jeśli <i>true</i> wiersze zostaną posortowane odwrotnie
         */
        protected void triggerSortByName(boolean sorted) {
            Comparator<PlayerStock> comparator = PlayerStock.getByNameComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        /** Sortuje wiersze po ilości akcji.
         * @param sorted    jeśli <i>true</i> wiersze zostaną posortowane odwrotnie
         */
        protected void triggerSortByAmount(boolean sorted) {
            Comparator<PlayerStock> comparator = PlayerStock.getByAmountComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        /** Sortuje wiersze po cenie.
         * @param sorted    jeśli <i>true</i> wiersze zostaną posortowane odwrotnie
         */
        protected void triggerSortByPrice(boolean sorted) {
            Comparator<PlayerStock> comparator = PlayerStock.getByPriceComparator();
            if (sorted) comparator = HelperTools.getReverseComparator(comparator);
            
            mTableModel.sort(comparator);
        }
        
        /** Sortuje wiersze po wartości pakietu.
         * @param sorted    jeśli <i>true</i> wiersze zostaną posortowane odwrotnie
         */
        protected void triggerSortByValue(boolean sorted) {
            Comparator<PlayerStock> comparator = PlayerStock.getByValueComparator();
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
                this.triggerSortByAmount(sorted);
                break;
            case 2:
                this.triggerSortByPrice(sorted);
                break;
            case 3:
                this.triggerSortByValue(sorted);
                break;
            }
        }
        
    }
    
    /** Definiuje sposób wyświetlania cen w tabeli.
     * @author Rafal
     */
    public static class PriceRenderer extends DefaultTableCellRenderer {
                
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
    private SellStockDialog mSellDialog;
    
    private CompanySelectedListener mCompanySelectedListener = null;
    
    /** Tworzy nowy obiekt.
     * @param model         model tabeli
     * @param buyDialog     okno dialogowe zakupu
     * @param sellDialog    okno dialogowe sprzedaży
     */
    public MyStockTable(TableModel model,
             BuyStockDialog buyDialog, SellStockDialog sellDialog) {
        super();
        
        mPopupMenu = new CurrentStockPopupMenu(this);
        mBuyDialog = buyDialog;
        mSellDialog = sellDialog;
        
        this.setModel(model);
        this.addMouseListener(new TableMouseAdapter(this));
        this.getTableHeader().setReorderingAllowed(false);
        this.getTableHeader().addMouseListener(new HeaderMouseAdapter(this, model));
    }
    
    /** Wyświetla okno zakupu. */
    protected void showBuyDialog() {
        if (mBuyDialog.isVisible()) mBuyDialog.setVisible(false);
        
        String company = ((TableModel) this.getModel()).getStock(this.getSelectedRow()).getCompanyName();
        mBuyDialog.setCompany(GameUtilities.getCurrentStockByName(company));
        mBuyDialog.setVisible(true);
    }
    
    /** Wyświetla okno sprzedaży. */
    protected void showSellDialog() {
        if (mSellDialog.isVisible()) mSellDialog.setVisible(false);
        
        String company = ((TableModel) this.getModel()).getStock(this.getSelectedRow()).getCompanyName();
        mSellDialog.setCompany(GameUtilities.getCurrentStockByName(company));
        mSellDialog.setVisible(true);    
    }
    
    /** Dodaje zaznaczony wiersz do obserwowanych. */
    protected void observeStock() {
        String company = ((TableModel) this.getModel()).getStock(this.getSelectedRow()).getCompanyName();
        GameUtilities.getInstance().addToObserved(company);
        GameUtilities.refreshObservedTable();
    }
    
    /** Usuwa zaznaczony wiersz z obserwowanych. */
    protected void unobserveStock() {
        String company = ((TableModel) this.getModel()).getStock(this.getSelectedRow()).getCompanyName();
        GameUtilities.getInstance().removeFromObserved(company);
        GameUtilities.refreshObservedTable();
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
    
    /** Obsługa kliknięć myszy na menu kontekstowe. */
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getActionCommand().equals("Kup")) this.showBuyDialog();
        else if (ae.getActionCommand().equals("Sprzedaj")) this.showSellDialog();
        else if (ae.getActionCommand().equals(CurrentStockPopupMenu.ITEM_OBSERVE)) this.observeStock();
        else if (ae.getActionCommand().equals(CurrentStockPopupMenu.ITEM_UNOBSERVE)) this.unobserveStock();
    }
    
    /** Ustawia obiekt nasłuchujący na zmiany wybranego wiersza.
     * @param l
     */
    public void setCompanySelectedListener(CompanySelectedListener l) {
        mCompanySelectedListener = l;
    }
    
    /**
     * @return  menu kontekstowe tabeli
     */
    public CurrentStockPopupMenu getPopup() {
        return mPopupMenu;
    }
    
}
