package pl.pisz.airlog.giepp.desktop.dialogs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import pl.pisz.airlog.giepp.desktop.GieppDesktop;
import pl.pisz.airlog.giepp.desktop.panels.ImagePanel;

public class AboutDialog
        extends JDialog {

    public static final String CAPTION  = "GiePP - O aplikacji";
            
    private JPanel mImagePanel;
    
    public AboutDialog(JFrame parent, Image appLogo) {
        super(parent, CAPTION);
        
        mImagePanel = new ImagePanel(appLogo);
        
        try {
            this.initComponent();
        } catch (IOException e) {
            System.err.println("Can't create AboutFrame: " + e);
            
            JPanel container = new JPanel(new BorderLayout());
            container.add(mImagePanel, BorderLayout.CENTER);
            
            this.setContentPane(container);
        }
    }
        
    private void initComponent() throws IOException {
        this.setLayout(new BorderLayout());
        
        JEditorPane area = new JEditorPane(GieppDesktop.getUrlForResource("res/about.html"));
        area.setEditable(false);
        area.setBackground(this.getBackground());
        
        JLabel title = new JLabel("GiePP for desktops", JLabel.CENTER);
        title.setFont(title.getFont().deriveFont(18.0f));
                
        JPanel versionPanel = this.newPropertyPanel("wersja:", GieppDesktop.getVersion());
        JPanel datePanel = this.newPropertyPanel("wydano:", GieppDesktop.getCompilationDate());
        JPanel propertyPanel = new JPanel(new GridLayout(2, 1));
        propertyPanel.add(versionPanel);
        propertyPanel.add(datePanel);
        
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.add(propertyPanel, BorderLayout.NORTH);
        textPanel.add(area, BorderLayout.CENTER);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));
        panel.add(title, BorderLayout.NORTH);
        panel.add(textPanel, BorderLayout.CENTER);
        
        this.add(mImagePanel, BorderLayout.CENTER);
        this.add(panel, BorderLayout.SOUTH);
    }
    
    protected JPanel newPropertyPanel(String tag, String value) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel tagLabel = new JLabel(tag, JLabel.LEFT);
        JLabel valueLabel = new JLabel(value, JLabel.LEFT);
        valueLabel.setFont(valueLabel.getFont().deriveFont(valueLabel.getFont().getStyle() & ~Font.BOLD));
        panel.add(tagLabel);
        panel.add(valueLabel);
        
        return panel;
    }
            
}
