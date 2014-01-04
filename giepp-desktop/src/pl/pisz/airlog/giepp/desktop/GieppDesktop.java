package pl.pisz.airlog.giepp.desktop;

import java.awt.Dimension;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.data.CurrentStock;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.frames.MainFrame;
import pl.pisz.airlog.giepp.desktop.menus.MainMenuBar;
import pl.pisz.airlog.giepp.desktop.panels.MyStocksPanel;
import pl.pisz.airlog.giepp.desktop.panels.RatingsPanel;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.util.HelperTools;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;

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
        final MyStockTable.TableModel myStockModel = new MyStockTable.TableModel();
        
        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {               
               String[] titles = new String[] {"Moje konto", "Notowania", "Obserwowane", "Statystyki"};
               JPanel[] panels = new JPanel[titles.length];
               for (int i = 0; i < titles.length; i++) panels[i] = HelperTools.newTextPanel(titles[i]);
               
               final BuyStockDialog buyDialog = new BuyStockDialog(null);
               buyDialog.setMinimumSize(new Dimension(320, 0));
               buyDialog.pack();
               
               final SellStockDialog sellDialog = new SellStockDialog(null);
               sellDialog.setMinimumSize(new Dimension(320, 0));
               sellDialog.pack();
               
               panels[0] = new MyStocksPanel(myStockModel, buyDialog, sellDialog);
               panels[1] = new RatingsPanel(currentStockModel, buyDialog, sellDialog);
               
               final JFrame frame = new MainFrame(panels, titles);                   
               
               MainMenuBar mmb = new MainMenuBar();
               mmb.setMenuListener(new MainMenuBar.MainMenuListener() {
                   @Override
                   public void onFileQuit(java.awt.event.ActionEvent ae) {
                       System.exit(0);
                   }
               });
                                             
               frame.setJMenuBar(mmb);
               frame.setMinimumSize(new Dimension(800, 600));
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
               frame.setVisible(true);
           }
        });
        
        // update data
        Game game = GameUtilities.getInstance();
        game.refreshCurrent();
        final List<CurrentStock> list = game.getCurrent();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                currentStockModel.clear();
                currentStockModel.addAll(list);
            }
        });
        game.refreshArchival();
    }

}
