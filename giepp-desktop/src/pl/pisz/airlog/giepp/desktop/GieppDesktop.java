package pl.pisz.airlog.giepp.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import pl.pisz.airlog.giepp.desktop.dialogs.AboutDialog;
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

/** Klasa nadzorująca działanie programu.
 * @author Rafal
 */
public class GieppDesktop {

    private static int VERSION_MAJOR           = 1;
    private static int VERSION_MINOR           = 0;
    private static int VERSION_PATCH           = 2;
    private static String VERSION_DECORATOR    = "beta";
    
    /** Aktualna wersja programu.
     * @return  aktualna wersja programu jako string
     */
    public static String getVersion() {
        return String.format("%d.%d-%s-%d", VERSION_MAJOR, VERSION_MINOR, VERSION_DECORATOR, VERSION_PATCH);
    }
    
    /** Data wydania aktualnej wersji progamu.
     * @return  data wydania
     */
    public static String getReleasedDate() {
        return "2014-01-12";
    }
    
    /** Ładuje grafikę z podanej ścieżki, obsługuje również archiwa JAR.
     * @param path  ścieżka do grafiki
     * @return  wczytana grafika
     */
    public static ImageIcon getIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImage().getWidth(null) < 0) {
            icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(path));
        }
        
        return icon;
    }
    
    /** Tworzy adres URL danego pliku lub jeśli nie istnieje jego odpowiednika w archiwum JAR.
     * @param path  ścieżka do pliku
     * @return  odpowiadający ścieżke adres URL
     */
    public static URL getUrlForResource(String path) {
        URL url = null;
        try {
            File file = new File(path);
            if (file.exists()) url = file.toURI().toURL();
            else url = ClassLoader.getSystemClassLoader().getResource(path);    
        }
        catch (MalformedURLException e) {
            url = ClassLoader.getSystemClassLoader().getResource(path);
        }
        
        return url;
    }
    
    /** Zmienia rozmiar grafiki.
     * @param src   grafika
     * @param nw    nowa szerokość
     * @param nh    nowa wysokość
     * @return  grafika po transformacjach
     */
    public static ImageIcon resizeIcon(ImageIcon src, int nw, int nh) {
        Image img = src.getImage();
        
        BufferedImage bi = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, nw, nh, null, null);
        
        return new ImageIcon(bi);
    }
    
    /** Uruchamia aplikację.
     * @param args  niewykorzystywane
     */
    public static void main(String[] args) {
        final MyStockTable.TableModel myStockModel = new MyStockTable.TableModel();
        final CurrentStockTable.TableModel currentStockModel = new CurrentStockTable.TableModel();
        final CurrentStockTable.TableModel observedModel = new CurrentStockTable.TableModel();
                        
        final StatusBar statusBar = new StatusBar();
        GameUtilities.newInstance(myStockModel, currentStockModel, observedModel, statusBar);        

        // run GUI
        SwingUtilities.invokeLater(new Runnable() {
           @Override
           public void run() {               
               final BuyStockDialog buyDialog = new BuyStockDialog(null);
               buyDialog.setMinimumSize(new Dimension(320, 0));
               buyDialog.pack();
               
               final SellStockDialog sellDialog = new SellStockDialog(null);
               sellDialog.setMinimumSize(new Dimension(320, 0));
               sellDialog.pack();
               
               final DaysSelectDialog daysDialog = new DaysSelectDialog(null);
               daysDialog.setMinimumSize(new Dimension(320, 0));
               daysDialog.pack();
               
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
                                             
               Image icon = GieppDesktop.getIcon("res/icon.png").getImage();
               
               final MainFrame frame = new MainFrame(panels, titles, icons);
               frame.setStatusBar(statusBar);
               
               final JDialog aboutDialog = new AboutDialog(frame, icon);
               aboutDialog.setMinimumSize(new Dimension(480, 320));
               aboutDialog.setResizable(false);
               
               final MainMenuBar mmb = new MainMenuBar();
               mmb.setMenuListener(new MainMenuBar.MainMenuListener() {                   
                   @Override
                   public void onFileQuit(ActionEvent ae) {
                       buyDialog.setVisible(false);
                       sellDialog.setVisible(false);
                       daysDialog.setVisible(false);
                       System.exit(0);
                   }
                   
                   @Override
                   public void onFileNew(ActionEvent ae) {
                       if (JOptionPane.showConfirmDialog(mmb,"Jesteś pewny, że chcesz rozpocząć grę od nowa?") == JOptionPane.OK_OPTION) {
                           GameUtilities.getInstance().restartGame();
                           GameUtilities.refreshMyStockTable();
                           GameUtilities.refreshObservedTable();
                       }
                   }
                   
                   @Override
                   public void onSaveGame(ActionEvent ae) {
                       if (!GameUtilities.getInstance().saveGame()) {
                           JOptionPane.showMessageDialog(
                                   mmb.getParent(),
                                   "Nie udało się zapisać stanu gry.",
                                   "Błąd zapisu",
                                   JOptionPane.ERROR_MESSAGE
                               );
                       }
                   }
                   
                   @Override
                   public void onRefresh(ActionEvent ae) {
                       GameUtilities.refreshData();
                   }
                   
                   @Override
                   public void onArchiveDownload(ActionEvent ae) {
                       daysDialog.setVisible(true);
                   }
                   
                   @Override
                   public void onHelpAbout(ActionEvent ae) {
                       aboutDialog.setVisible(true);
                   }
               });
               
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
