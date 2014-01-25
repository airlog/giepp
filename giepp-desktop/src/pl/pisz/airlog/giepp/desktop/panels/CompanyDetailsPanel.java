package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.CompanySelectedListener;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Panel wyświetlający informacje o wybranej firmie.
 * 
 * Panel implementuje interfejs {@link CompanySelectedListener}, który umożliwia wychwycenie zdarzeń
 * wyboru firmy z tabeli. Na tej podstawie w danych gry szukane są informacje o określonej firmie,
 * które następnie są filtrowane i wyświetlane.
 * 
 * @author Rafal
 * @see PlotPanel
 */
public class CompanyDetailsPanel extends JPanel
        implements CompanySelectedListener {
    
    private JTextField mCompanyField = new JTextField();
    private JTextField mPriceField = new JTextField();
    private JTextField mStockField = new JTextField();
    
    private PlotPanel mPlotPanel = new PlotPanel();
    
    /** Tworzy nowy obiekt.
     *
     */
    public CompanyDetailsPanel() {
        super(false);
        
        Color defColor = mCompanyField.getBackground();
        
        mCompanyField.setEditable(false);
        mCompanyField.setBackground(defColor);
        
        mPriceField.setEditable(false);
        mPriceField.setBackground(defColor);
        
        mStockField.setEditable(false);
        mStockField.setBackground(defColor);
        
        mPlotPanel.setBackground(Color.WHITE);
        
        this.initComponent();
    }
    
    private void initComponent() {
        FormLayout layout = new FormLayout(
                "3dlu, right:pref, 3dlu, pref:grow, 3dlu",
                "3dlu, p, p, p, 3dlu, fill:pref:grow, 3dlu"
                );
        PanelBuilder builder = new PanelBuilder(layout, this);
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel("Firma", cc.xy(2, 2));
        builder.add(mCompanyField, cc.xy(4, 2));
        builder.addLabel("Aktualna cena", cc.xy(2, 3));
        builder.add(mPriceField, cc.xy(4, 3));
        builder.addLabel("Posiadane akcje", cc.xy(2, 4));
        builder.add(mStockField, cc.xy(4, 4));
        builder.add(mPlotPanel, cc.xyw(2, 6, 3));
    }
    
    /** Implementacja interfejsu.
     * @see CompanySelectedListener
     */
    @Override
    public void onCompanySelected(String company) {
        if (company == null) return;
                
        mCompanyField.setText(company);
        
        long endPrice = GameUtilities.getInstance().getEndPrice(company);
        if (endPrice != 0) mPriceField.setText(HelperTools.getPriceFormat().format((double) endPrice * 0.01));
        else mPriceField.setText("ERROR");        
    
        PlayerStock companyStock = GameUtilities.getInstance().getOwnedStockByName(company);
        if (companyStock != null) mStockField.setText(companyStock.getAmount().toString());
        else mStockField.setText("0");
        
        final String companyName = company;
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                mPlotPanel.setCompany(companyName);
                mPlotPanel.repaint();
            }
        });
    }
        
}
