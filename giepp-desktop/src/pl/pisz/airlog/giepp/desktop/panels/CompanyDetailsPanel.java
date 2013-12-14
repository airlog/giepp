package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Color;

import java.util.List;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.pisz.airlog.giepp.game.Game;
import pl.pisz.airlog.giepp.data.CurrentStock;

import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.CompanySelectedListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Rafal
 *
 */
public class CompanyDetailsPanel
        extends JPanel
        implements CompanySelectedListener {
    
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

    @Override
    public void onCompanySelected(String company) {
        if (company == null) return;
        
        mCompanyField.setText(company);
        long endPrice = GameUtilities.getInstance().getEndPrice(company);
        if (endPrice == 0) {
            mPriceField.setText("ERROR");
            return;
        }
        mPriceField.setText(HelperTools.getPriceFormat().format((double) endPrice * 0.01));
    }
        
}
