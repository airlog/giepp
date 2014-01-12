package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel
        extends JPanel {

    private Image mImage;
    
    public ImagePanel(Image image) {
        super();
    
        mImage = image;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int width = mImage.getWidth(null);
        int height = mImage.getHeight(null);
        g.drawImage(mImage, this.getWidth()/2 - width/2, this.getHeight()/2 - height/2, null);
    }

}
