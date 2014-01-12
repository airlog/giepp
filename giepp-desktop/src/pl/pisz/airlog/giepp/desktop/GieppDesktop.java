package pl.pisz.airlog.giepp.desktop;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
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
import pl.pisz.airlog.giepp.desktop.util.Pair;
import pl.pisz.airlog.giepp.desktop.util.PropertyLoader;
import pl.pisz.airlog.giepp.desktop.util.SimplePropertyLoader;
import pl.pisz.airlog.giepp.desktop.widgets.CurrentStockTable;
import pl.pisz.airlog.giepp.desktop.widgets.MyStockTable;

public class GieppDesktop {

    private static String VERSION_MAJOR        = null;
    private static String VERSION_MINOR        = null;
    private static String VERSION_PATCH        = null;
    private static String VERSION_DECORATOR    = null;
    
    private static GieppProperties newProperties(String path) {
        PropertyLoader loader = new SimplePropertyLoader();
        BufferedReader reader = null;
        
        InputStream stream = null;
        for (int i = 0; i < 2; i++) {
            try {
                if (i == 0) stream = new FileInputStream(new File(path));
                else stream = ClassLoader.getSystemClassLoader().getResourceAsStream(path);
                
                reader = new BufferedReader(new InputStreamReader(stream));
                loader.parse(reader);
                break;
            } catch (IOException e) {
                if (i == 0) System.err.println("Warning: couldn't load config from system, will try JAR");
                else System.err.println("Warning: couldn't load config from JAR");
            } catch (NullPointerException e) {
                System.err.println("Warning: null when loading properties! (i = " + i + ")");
            } finally {
                try {
                    if (reader != null) reader.close();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
        
        List<Pair<String, String>> pairs = loader.getProperties();
        return new GieppProperties(pairs);
    }
        
    public static String getVersion() {
        return String.format("%s.%s.%s-%s", VERSION_MAJOR, VERSION_MINOR, VERSION_PATCH, VERSION_DECORATOR);
    }
    
    public static String getCompilationDate() {
        return "2014-01-12";
    }
    
    public static ImageIcon getIcon(String path) {
        ImageIcon icon = new ImageIcon(path);
        if (icon.getImage().getWidth(null) < 0) {
            icon = new ImageIcon(ClassLoader.getSystemClassLoader().getResource(path));
        }
        
        return icon;
    }
    
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
    
    public static ImageIcon resizeIcon(ImageIcon src, int nw, int nh) {
        Image img = src.getImage();
        
        BufferedImage bi = new BufferedImage(nw, nh, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        g.drawImage(img, 0, 0, nw, nh, null, null);
        
        return new ImageIcon(bi);
    }
    
    public static void main(String[] args) {        
        GieppProperties properties = GieppDesktop.newProperties("res/config.cfg");
        GieppDesktop.VERSION_MAJOR = properties.getProperty(GieppProperties.KEY_VERSION_MAJOR);
        GieppDesktop.VERSION_MINOR = properties.getProperty(GieppProperties.KEY_VERSION_MINOR);
        GieppDesktop.VERSION_PATCH = properties.getProperty(GieppProperties.KEY_VERSION_PATCH);
        GieppDesktop.VERSION_DECORATOR = properties.getProperty(GieppProperties.KEY_VERSION_DECORATOR);
        
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
               
               MainMenuBar mmb = new MainMenuBar();
               mmb.setMenuListener(new MainMenuBar.MainMenuListener() {                   
                   @Override
                   public void onFileQuit(java.awt.event.ActionEvent ae) {
                       buyDialog.setVisible(false);
                       sellDialog.setVisible(false);
                       daysDialog.setVisible(false);
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
                   
                   @Override
                   public void onHelpAbout(java.awt.event.ActionEvent ae) {
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
