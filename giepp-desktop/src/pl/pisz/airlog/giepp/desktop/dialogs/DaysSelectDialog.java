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
public class DaysSelectDialog
        extends JDialog
        implements ActionListener, ChangeListener {    
    
    private JSpinner mDaysSpinner;
    
    private JButton mRefreshButton = new JButton("Odśwież");
    private JButton mCancelButton = new JButton("Anuluj");
    
    public DaysSelectDialog(JFrame owner) {
        super(owner, "Pobierz dane archiwalne");
        
        SpinnerNumberModel model = new SpinnerNumberModel();
        model.setMinimum(0);
        model.setMaximum(365);
        
        mDaysSpinner = new JSpinner(model);
        mDaysSpinner.addChangeListener(this);
        
        mRefreshButton.addActionListener(this);
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
        insidePanel.add(mRefreshButton, cc.xy(1, 1));
        insidePanel.add(mCancelButton, cc.xy(2, 1));
        
        builder.addLabel("Ilość dni", cc.xy(2, 3));
        builder.add(mDaysSpinner, cc.xyw(4, 3, 2));
        builder.add(insidePanel, cc.xyw(2, 8, 4));
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == mRefreshButton) this.onBuyClicked();
        else if (ae.getSource() == mCancelButton) this.onCancelClicked();
    }
    
    @Override
    public void stateChanged(ChangeEvent ce) {
        // TODO
    }
        
    public void onBuyClicked() {
        GameUtilities.refreshArchival((Integer) mDaysSpinner.getValue());
        this.setVisible(false);
    }
    
    public void onCancelClicked() {
        this.setVisible(false);
    }
  
}
