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

public class StatisticBuilder {

    public static class StatisticItem {
        
        private StatisticBuilder mParent;
        
        private String mTitle;
        private String mDesc;
        private String mValue;
        
        protected StatisticItem(StatisticBuilder parent) {
            mParent = parent;
        }
        
        public StatisticBuilder done() {
            return mParent;
        }
        
        public StatisticItem setTitle(String title) {
            mTitle = title;
            return this;
        }
        
        public StatisticItem setDescription(String desc) {
            mDesc = desc;
            return this;
        }
        
        public StatisticItem setValue(String value) {
            mValue = value;
            return this;
        }
        
    }
    
    private LinkedList<StatisticItem> mItems = new LinkedList<StatisticItem>();
    
    public StatisticBuilder() {}
    
    protected String newColumnLayout() {
        return "3dlu, right:pref, 3dlu, pref:grow, 3dlu";
    }
    
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

    protected FormLayout newLayout() {
        return new FormLayout(this.newColumnLayout(), this.newRowLayout());
    }
    
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
        
    public StatisticItem newItem() {
        StatisticItem item = new StatisticItem(this);
        mItems.add(item);
        
        return item;
    }
        
    public JPanel createPanel() {
        return this.newBuilder(this.newLayout()).getPanel();
    }
    
}

class Label
        extends JLabel {
    
    public Label(String label) {
        super(label);
        
        Font f = this.getFont();
        this.setFont(f.deriveFont(f.getStyle() & ~Font.BOLD));
    }
    
}

class ReadOnlyTextField
        extends JTextField {

    public ReadOnlyTextField(String value, String desc) {
        super(value);
        
        Color color = this.getBackground();
        this.setEditable(false);
        this.setBackground(color);
        
        this.setToolTipText(desc);
    }
    
}
