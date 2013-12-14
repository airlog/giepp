package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Rafal
 *
 */
public class CompanyDetailsPanel
        extends JPanel {

    private JTextField mCompanyField = new JTextField();
    private JTextField mPriceField = new JTextField();
    
    public CompanyDetailsPanel() {
        super(false);
        
        Color defColor = mCompanyField.getBackground();
        
        mCompanyField.setEditable(false);
        mCompanyField.setBackground(defColor);
        
        mPriceField.setEditable(false);
        mPriceField.setBackground(defColor);
        
        this.initComponent();
    }
    
    private void initComponent() {
        FormLayout layout = new FormLayout(
                "3dlu, right:pref, 3dlu, pref:grow, 3dlu",
                "3dlu, p, p"
                );
        PanelBuilder builder = new PanelBuilder(layout, this);
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel("Firma", cc.xy(2, 2));
        builder.add(mCompanyField, cc.xy(4, 2));
        builder.addLabel("Aktualna cena", cc.xy(2, 3));
        builder.add(mPriceField, cc.xy(4, 3));
    }
    
}
