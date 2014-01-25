package pl.pisz.airlog.giepp.desktop.util;

import java.awt.Color;
import java.awt.Font;
import java.util.LinkedList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Umożliwa szybkie stworzenie layouta dla wielu statystyk.
 * 
 * Każda statystyka jest wyświetlana w postaci <i>etykieta - opis - wartość</i>. Ta klasa ma na celu
 * umożliwie jak najszybsze stworzenia panelu wyświetlającego takie dane. W tym cely wykorzystane
 * został wzorzec <i>chaining</i>, bardzo popularny np. w jQuery.
 * 
 * @author Rafal
 * @see FormLayout
 */
public class StatisticBuilder {

    /** Reprezentuje pojedyńczą statystykę.
     * 
     * Poprzez przechowywanie referencji na {@link StatisticBuilder} możliwe jest szybkie powracanie
     * do tworzenia nowego elementu. Obiekty tej klasy można utworzyć tylko poprzez metodę {@link StatisticBuilder#newItem()}.
     * 
     * @author Rafal
     */
    public static class StatisticItem {
        
        private StatisticBuilder mParent;
        
        private String mTitle;
        private String mDesc;
        private String mValue;
        
        /** Tworzy nowy obiekt.
         * @param parent    obiekt tworzący element
         */
        protected StatisticItem(StatisticBuilder parent) {
            mParent = parent;
        }
        
        /** Kończy modyfikację obiektu.
         * @return  obiekt, który utworzył element
         */
        public StatisticBuilder done() {
            return mParent;
        }
        
        /** Ustawia etykietę.
         * @param title etykieta
         * @return  ten obiekt
         */
        public StatisticItem setTitle(String title) {
            mTitle = title;
            return this;
        }
        
        /** Ustawia opis.
         * @param desc  opis
         * @return  ten obiekt
         */
        public StatisticItem setDescription(String desc) {
            mDesc = desc;
            return this;
        }
        
        /** Ustawia wartość statystyki.
         * @param value wartość statystyki
         * @return  ten obiekt
         */
        public StatisticItem setValue(String value) {
            mValue = value;
            return this;
        }
        
    }
    
    private LinkedList<StatisticItem> mItems = new LinkedList<StatisticItem>();
    
    /** Tworzy nowy obiekt.
     * 
     */
    public StatisticBuilder() {}
    
    /**
     * @return  zwraca preferencje układu kolumn
     */
    protected String newColumnLayout() {
        return "3dlu, right:pref, 3dlu, pref:grow, 3dlu";
    }
    
    /**
     * @return  zwraca preferenceje układu wierszy
     */
    protected String newRowLayout() {
        StringBuffer sb = new StringBuffer();
        
        sb.append("5dlu,");
        for (StatisticItem item : mItems) {
            sb
            .append("2dlu,")
            .append("p,")
            .append("2dlu,");
        }
        sb.append("5dlu");
        
        return sb.toString();
    }

    /** Tworzy nowy {@link FormLayout}.
     * @return  układ
     * @see StatisticBuilder#newColumnLayout()
     * @see StatisticBuilder#newRowLayout()
     */
    protected FormLayout newLayout() {
        return new FormLayout(this.newColumnLayout(), this.newRowLayout());
    }
    
    /** Tworzy nowy {@link PanelBuilder} wypełeniony zadeklarowanymi statystykami.
     * @param layout    układ opisujący wygląd panelu
     * @return  częściowo wypełniony {@link PanelBuilder}
     */
    protected PanelBuilder newBuilder(FormLayout layout) {
        PanelBuilder builder = new PanelBuilder(layout);
        CellConstraints cc = new CellConstraints();
        
        int y = 2;
        for (StatisticItem item : mItems) {
            builder.add(new Label(item.mTitle), cc.xy(2, y + 1));
            builder.add(new ReadOnlyTextField(item.mValue, item.mDesc), cc.xy(4, y + 1));
            y += 3; // see newRowLayout
        }
        
        return builder;
    }    
    
    /** Tworzy i dodaje nową statystykę.
     * @return  modyfikowalny obiekt statystyk
     */
    public StatisticItem newItem() {
        StatisticItem item = new StatisticItem(this);
        mItems.add(item);
        
        return item;
    }
        
    /** Tworzy panel na podstawie zadeklarowanych statystyk i layoutów.
     * @return  nowy panel
     */
    public JPanel createPanel() {
        return this.newBuilder(this.newLayout()).getPanel();
    }
    
}

class Label extends JLabel {
    
    public Label(String label) {
        super(label);
        
        Font f = this.getFont();
        this.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
    }
    
}

class ReadOnlyTextField extends JTextField {

    public ReadOnlyTextField(String value, String desc) {
        super(value);
        
        Color color = this.getBackground();
        this.setEditable(false);
        this.setBackground(color);
        
        this.setToolTipText(desc);
    }
    
}
