package pl.pisz.airlog.giepp.desktop.dialogs;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.game.ActionException;
import pl.pisz.airlog.giepp.game.Game;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/**
 * @author Rafal
 *
 */
public class SellStockDialog
        extends JDialog
        implements ActionListener, ChangeListener {    

    private CurrentStock mCompanyStock;
    
    private JTextField  mCompanyField = new JTextField();
    private JTextField  mPriceField = new JTextField();
    private JTextField  mAmountField = new JTextField();
    private JSpinner    mAmountSpinner = new JSpinner(new SpinnerNumberModel());
    
    private JButton mSellButton = new JButton("Sprzedaj");
    private JButton mCancelButton = new JButton("Anuluj");
    
    public SellStockDialog(JFrame owner) {
        super(owner, "Sprzedaj akcje");
        
        Color defColor = mCompanyField.getBackground();
        
        mCompanyField.setEditable(false);
        mCompanyField.setBackground(defColor);
        
        mPriceField.setEditable(false);
        mPriceField.setBackground(defColor);
        
        mAmountField.setEditable(false);
        mAmountField.setBackground(defColor);
        
        mAmountSpinner.addChangeListener(this);
        
        mSellButton.addActionListener(this);
        mCancelButton.addActionListener(this);
        
        this.initComponent();
    }
    
    private void initComponent() {
        JPanel content = new JPanel();
        this.add(content);
        
        FormLayout layout = new FormLayout(
                "3dlu, right:pref, 3dlu, pref:grow, pref:grow, 3dlu",
                "3dlu, p, p, p, p, pref:grow, 3dlu, p, 3dlu"
                );
        PanelBuilder builder = new PanelBuilder(layout, content);
        CellConstraints cc = new CellConstraints();
        
        JPanel insidePanel = new JPanel(new FormLayout("pref:grow, pref:grow", "p"));
        insidePanel.add(mSellButton, cc.xy(1, 1));
        insidePanel.add(mCancelButton, cc.xy(2, 1));
        
        builder.addLabel("Firma", cc.xy(2, 2));
        builder.add(mCompanyField, cc.xyw(4, 2, 2));
        builder.addLabel("Ilość", cc.xy(2, 3));
        builder.add(mAmountSpinner, cc.xyw(4, 3, 2));
        builder.addLabel("Cena", cc.xy(2, 4));
        builder.add(mPriceField, cc.xyw(4, 4, 2));
        builder.addLabel("Dostępne", cc.xy(2, 5));
        builder.add(mAmountField, cc.xyw(4, 5, 2));
        builder.add(insidePanel, cc.xyw(2, 8, 4));
    }

    protected void setPriceValue(Integer price) {
        mPriceField.setText(HelperTools.getPriceFormat().format(
                (double) price * 0.01 * (double) ((Integer) mAmountSpinner.getValue()).intValue()));
    }
    
    protected void setAmountValue(Integer amount) {
        mAmountField.setText(amount.toString());
    }
       
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == mSellButton) this.onSellClicked();
        else if (ae.getSource() == mCancelButton) this.onCancelClicked();
    }
    
    @Override
    public void stateChanged(ChangeEvent ce) {
        this.setPriceValue(mCompanyStock.getEndPrice());
    }
    
    @Override
    public void setVisible(boolean b) {
        if (mCompanyStock == null) throw new IllegalStateException("Must set stock before showing the dialog");
        
        PlayerStock owned = GameUtilities.getInstance().getOwnedStockByName(mCompanyStock.getName());
        if (owned != null) {
            int amount = owned.getAmount();
            int price = mCompanyStock.getEndPrice();
            
            if (b) {
                this.setPriceValue(price);
                this.setAmountValue(amount);
                mAmountSpinner.setValue(new Integer(0));
                mCompanyField.setText(mCompanyStock.getName());
            }
        }
        else if (b) throw new IllegalStateException("Such a stock is not owned!");
        
        if (!b) {
            mCompanyStock = null;
            mCompanyField.setText("");
            mPriceField.setText("");
        }
        
        super.setVisible(b);
    }
    
    public void onSellClicked() {
        this.stateChanged(null);
        
        Game game = GameUtilities.getInstance();
        try {
            game.sell(mCompanyStock.getName(), (Integer) mAmountSpinner.getValue());
            this.setVisible(false);
            
            // TODO: wyświetl dialog potwierdzający sprzedaż
        }
        catch (ActionException e) {
            System.err.println(e);
            
            // TODO: wyświetl dialog z błędem
        }
    }
    
    public void onCancelClicked() {
        this.setVisible(false);
    }

    public void setCompany(CurrentStock stock) {
        mCompanyStock = stock;
    }
        
}
