package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.util.StatisticBuilder;

/** Panel wyświetlający statystyki gracza.
 * @author Rafal
 * @see StatisticBuilder
 */
public class StatisticPanel extends JPanel {

    /** Tworzy nowy obiekt.
     * 
     */
    public StatisticPanel() {
        super(new BorderLayout());
        
        this.init();
    }
        
    private void init() {                
        JScrollPane sp = new JScrollPane(this.createContentPanel());
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
                
        this.add(sp, BorderLayout.CENTER);
    }
    
    private void reinit() {
        this.removeAll();
        this.init();
    }

    /** Metoda tworzy wygląd ekranu statystyk korzystając z określonych statystyk.
     * @return  panel z ładnie sformatowanymi statystykami
     */
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
    
    /**
     * Dane odświeżane są przy każdym wyświetleniu panelu.
     */
    @Override
    public void setVisible(boolean b) {
        if (b) {  // wymuś odświeżanie statystyk
            this.reinit();
        }
        
        super.setVisible(b);
    }
        
}
