package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.plot.Plotter;

public class PlotPanel
        extends JPanel {

    private static final int PLOT_LEGEND_COUNT      = 4;
    private static final int PLOT_MIN_HEIGHT        = 150;
    private static final Color PLOT_LINE_COLOR      = Color.BLUE;
    private static final Color PLOT_LEGEND_COLOR    = Color.BLACK;
    private static final Color PLOT_GRID_COLOR      = Color.GRAY;
    
    private String mCompany;
        
    @Override
    protected void paintComponent(Graphics g) {
        if (this.getHeight() < PLOT_MIN_HEIGHT) return; // za mały wykres jest brzydki
        
        super.paintComponent(g);
        if (mCompany == null) return; 
        
        System.err.println(String.format("plotting '%s'", mCompany));
        
        Plotter plotter = new Plotter(this.getArchiveForCompany(mCompany),
                this.getWidth(), this.getHeight(), PLOT_LEGEND_COUNT);
        
        int[] points = this.floatArrayToIntArray(plotter.getPoints());  // prawa -> lewa
        int[] legend = this.floatArrayToIntArray(plotter.getVerticalLegendPositions());  // góra -> dół
        String[] values = plotter.getVerticalLegendValues();  // góra -> dół
        
        System.err.println(String.format("received %d coordinates from the archive", points.length));
        
        this.drawLines(g, points);
        this.drawLegend(g, legend, values);
    }
    
    protected int[] floatArrayToIntArray(float[] array) {
        int[] res = new int[array.length];
        
        int k = 0;
        for (float f : array) res[k++] = (int) f;
        
        return res;
    }
    
    protected ArrayList<ArchivedStock> getArchiveForCompany(String company) {
        return GameUtilities.getInstance().getArchived(company);
    }
    
    protected void drawLines(Graphics g, int[] points) {
        int linesCount = points.length>>2;  // podziel przez 4 (4 wartości na narysowanie linii)
        int[] xPoints = new int[linesCount<<1];
        int[] yPoints = new int[linesCount<<1];
        
        int xi = 0, yi = 0;
        for (int i = 0; i < points.length; i += 4) {
            xPoints[xi++] = points[i + 0];
            yPoints[yi++] = points[i + 1];
            xPoints[xi++] = points[i + 2];
            yPoints[yi++] = points[i + 3];
        }
             
        g.setColor(PLOT_LINE_COLOR);
        g.drawPolyline(xPoints, yPoints, xPoints.length);
    }
    
    protected void drawLegend(Graphics g, int[] legend, String... values) {
        g.setColor(PLOT_LEGEND_COLOR);
        int vi = 0;
        for (int i = 0; i < legend.length; i += 2) {
            g.drawString(values[vi++], legend[i + 0], legend[i + 1]);
        }
    }
    
    public PlotPanel setCompany(String company) {
        mCompany = company;
        return this;
    }
        
}
