package pl.pisz.airlog.giepp.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.desktop.dialogs.BuyStockDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.DaysSelectDialog;
import pl.pisz.airlog.giepp.desktop.dialogs.SellStockDialog;
import pl.pisz.airlog.giepp.desktop.frames.MainFrame;
import pl.pisz.airlog.giepp.desktop.menus.MainMenuBar;
import pl.pisz.airlog.giepp.desktop.panels.MyStocksPanel;
import pl.pisz.airlog.giepp.desktop.panels.ObservedPanel;
import pl.pisz.airlog.giepp.desktop.panels.RatingsPanel;
import pl.pisz.airlog.giepp.desktop.panels.StatisticPanel;
import pl.pisz.airlog.giepp.desktop.panels.StatusBar;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;

/**
 * @author Rafal
 */
public class GieppDesktop {

    public static ImageIcon getIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImage().getWidth(null) < 0) {
            icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(path));
        }
        
        return icon;
    }
    
    public static ImageIcon resizeIcon(ImageIcon src, int nw, int nh) {
        Image img = src.getImage();
        
        BufferedImage bi = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, nw, nh, null, null);
        
        return new ImageIcon(bi);
    }
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        final MyStockTable.TableModel myStockModel = new MyStockTable.TableModel();
        final CurrentStockTable.TableModel currentStockModel = new CurrentStockTable.TableModel();
        final CurrentStockTable.TableModel observedModel = new CurrentStockTable.TableModel();
                
        final BuyStockDialog buyDialog = new BuyStockDialog(null);
        buyDialog.setMinimumSize(new Dimension(320, 0));
        buyDialog.pack();
        
        final SellStockDialog sellDialog = new SellStockDialog(null);
        sellDialog.setMinimumSize(new Dimension(320, 0));
        sellDialog.pack();
        
        final DaysSelectDialog daysDialog = new DaysSelectDialog(null);
        daysDialog.setMinimumSize(new Dimension(320, 0));
        daysDialog.pack();
        
        final StatusBar statusBar = new StatusBar();
        GameUtilities.newInstance(myStockModel, currentStockModel, observedModel, statusBar);        

        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {               
               ImageIcon[] icons = new ImageIcon[] {
                       GieppDesktop.resizeIcon(GieppDesktop.getIcon("res/myaccount.png"), 24, 24),
                       GieppDesktop.resizeIcon(GieppDesktop.getIcon("res/ratings.png"), 24, 24),
                       GieppDesktop.resizeIcon(GieppDesktop.getIcon("res/observed.png"), 24, 24),
                       GieppDesktop.resizeIcon(GieppDesktop.getIcon("res/stats.png"), 24, 24),
                   };             
               String[] titles = new String[] {"Moje konto", "Notowania", "Obserwowane", "Statystyki"};
               JPanel[] panels = new JPanel[titles.length];
               panels[0] = new MyStocksPanel(myStockModel, buyDialog, sellDialog);
               panels[1] = new RatingsPanel(currentStockModel, buyDialog, sellDialog);
               panels[2] = new ObservedPanel(observedModel, buyDialog, sellDialog);
               panels[3] = new StatisticPanel();
                              
               MainMenuBar mmb = new MainMenuBar();
               mmb.setMenuListener(new MainMenuBar.MainMenuListener() {                   
                   @Override
                   public void onFileQuit(java.awt.event.ActionEvent ae) {
                       System.exit(0);
                   }
                   
                   @Override
                   public void onRefresh(java.awt.event.ActionEvent ae) {
                       GameUtilities.refreshData();
                   }
                   
                   @Override
                   public void onArchiveDownload(java.awt.event.ActionEvent ae) {
                       daysDialog.setVisible(true);
                   }
               });
               
               Image icon = GieppDesktop.getIcon("res/icon.png").getImage();
               
               final MainFrame frame = new MainFrame(panels, titles, icons);
               frame.setStatusBar(statusBar);
               
               if (icon != null) frame.setIconImage(icon);
               frame.setJMenuBar(mmb);
               frame.setMinimumSize(new Dimension(800, 600));
               frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
               
               frame.setVisible(true);
           }
        });
        
        // update data
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameUtilities.refreshData();
            }
        });
    }

}
