package pl.pisz.airlog.giepp.desktop;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.CurrentStock;

import pl.pisz.airlog.giepp.desktop.frames.MainFrame;
import pl.pisz.airlog.giepp.desktop.menus.MainMenuBar;
import pl.pisz.airlog.giepp.desktop.panels.RatingsPanel;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;

import pl.pisz.airlog.giepp.game.Game;

/**
 * @author Rafal
 */
public class GieppDesktop {

    /**
     * @param args
     */
    public static void main(String[] args) {
        final CurrentStockTable.TableModel currentStockModel = new CurrentStockTable.TableModel();
        
        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {               
               String[] titles = new String[] {"Moje konto", "Notowania", "Obserwowane", "Statystyki"};
               JPanel[] panels = new JPanel[titles.length];
               for (int i = 0; i < titles.length; i++) panels[i] = HelperTools.newTextPanel(titles[i]);
                              
               panels[1] = new RatingsPanel(currentStockModel);
               
               MainMenuBar mmb = new MainMenuBar();
               mmb.setMenuListener(new MainMenuBar.MainMenuListener() {
                  @Override
                  public void onFileQuit(java.awt.event.ActionEvent ae) {
                      System.exit(0);
                  }
               });
               
               JFrame frame = new MainFrame(panels, titles);
                              
               frame.setJMenuBar(mmb);
               frame.setMinimumSize(new Dimension(800, 600));
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
               frame.setVisible(true);
           }
        });
        
        // update data
        Game game = HelperTools.newGame();
        game.refreshData();
        final List<CurrentStock> list = game.getCurrent();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentStockModel.clear();
                currentStockModel.addAll(list);
            }
        });
        currentStockModel.sort(HelperTools.getReverseComparator(CurrentStock.getByChangeComparator()));
    }

}
