package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;
import javax.swing.JTextField;

import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.game.Game;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

/** Panel prezentujący aktualny stan konta.
 * @author Rafal
 */
public class MyStockDetailsPanel extends JPanel {
    
    private JTextField mMoneyField = new JTextField();
    private JTextField mStockField = new JTextField();
    private JTextField mTotalField = new JTextField();
    
    /** Tworzy nowy obiekt.
     * 
     */
    public MyStockDetailsPanel() {
        super(false);
        
        Color defColor = mMoneyField.getBackground();
        
        mMoneyField.setEditable(false);
        mMoneyField.setBackground(defColor);
               
        mStockField.setEditable(false);
        mStockField.setBackground(defColor);
        
        mTotalField.setEditable(false);
        mTotalField.setBackground(defColor);
        
        this.initComponent();
    }
    
    private void initComponent() {
        FormLayout layout = new FormLayout(
                "3dlu, right:pref, 3dlu, pref:grow, 3dlu",
                "3dlu, p, p, p, 3dlu"
                );
        PanelBuilder builder = new PanelBuilder(layout, this);
        CellConstraints cc = new CellConstraints();
        
        builder.addLabel("na koncie", cc.xy(2, 2));
        builder.add(mMoneyField, cc.xy(4, 2));
        builder.addLabel("w akcjach", cc.xy(2, 3));
        builder.add(mStockField, cc.xy(4, 3));
        builder.addLabel("w sumie", cc.xy(2, 4));
        builder.add(mTotalField, cc.xy(4, 4));
    }
    
    /** Ustawia ładnie sformatowaną cenę w określonym polu tekstowym.
     * @param textField pole tekstowe
     * @param price     cena do sformatowania
     */
    protected void setPriceText(JTextField textField, double price) {
        textField.setText(HelperTools.getPriceFormat().format(price));
    }
    
    /** Uaktualnia pola, pobierając aktualny stan gry.
     * @see GameUtilities
     */
    protected void updateFields() {
        Game game = GameUtilities.getInstance();
        
        double money = 0.01 * (double) game.getMoney();
        double value = 0.01 * (double) game.getMoneyInStock();
        
        this.setPriceText(mMoneyField, money);
        this.setPriceText(mStockField, value);
        this.setPriceText(mTotalField, money + value);
    }
    
    /**
     * Każde odmalowanie widoku powoduje aktualizację wyświetlanych wartości.
     */
    @Override
    protected void paintComponent(Graphics g) {
        this.updateFields();        
        super.paintComponent(g);
    }
        
}
