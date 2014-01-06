package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.CC;
import com.jgoodies.forms.layout.FormLayout;

import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.util.StatisticBuilder;

public class StatisticPanel
        extends JPanel {

    public StatisticPanel() {
        super(new BorderLayout());
        
        this.init();
    }
        
    private void init() {                
        JScrollPane sp = new JScrollPane(this.createContentPanel());
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        FormLayout layout = new FormLayout("right:p", "16dlu");
        JButton refreshButton = new JButton("Odśwież");
        final StatisticPanel parent = this;
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                parent.reinit();
                parent.repaint();
            }
        });
        
        PanelBuilder builder = new PanelBuilder(layout);        
        builder.add(refreshButton, CC.xy(1, 1));
        
        this.add(sp, BorderLayout.CENTER);
        this.add(builder.getPanel(), BorderLayout.NORTH);
    }
    
    private void reinit() {
        this.removeAll();
        this.init();
    }

    protected JPanel createContentPanel() {
        Stats stat = GameUtilities.getInstance().getStats();
        
        return (new StatisticBuilder())
                .newItem()
                    .setTitle("Restarty")
                    .setDescription("tyle razy gra była rozpoczynana od nowa")
                    .setValue(stat.getRestarts().toString())
                    .done()
                .newItem()
                    .setTitle("Stan konta")
                    .setDescription("aktualnie posiadane pieniądze")
                    .setValue(HelperTools.getPriceFormat().format((double) stat.getMoney() * 0.01))
                    .done()                
                .newItem()
                    .setTitle("Minimum")
                    .setDescription("najmniejsza osiągnięta wartość sumy gotówki i wartości posiadanych akcji")
                    .setValue(HelperTools.getPriceFormat().format((double) stat.getMinMoney() * 0.01))
                    .done()
                .newItem()
                    .setTitle("Maksimum")
                    .setDescription("największa osiągnięta wartość sumy gotówki i wartości posiadanych akcji")
                    .setValue(HelperTools.getPriceFormat().format((double) stat.getMaxMoney() * 0.01))
                    .done() 
                .createPanel();
        
    }
        
}
