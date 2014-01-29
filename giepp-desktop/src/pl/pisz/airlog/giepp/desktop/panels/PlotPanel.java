package pl.pisz.airlog.giepp.desktop.panels;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.desktop.util.GameUtilities;
import pl.pisz.airlog.giepp.plot.Plotter;

/** Panel wyświetlający wykres notowań archiwalnych określonej firmy.
 * 
 * Panel wyświetla wykres rysowany przy pomocy klasy {@link Plotter}.
 * 
 * @author Rafal
 * @see Plotter
 */
public class PlotPanel extends JPanel {

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
                
        Plotter plotter = new Plotter(this.getArchiveForCompany(mCompany),
                this.getWidth(), this.getHeight(), PLOT_LEGEND_COUNT, PLOT_LEGEND_COUNT);
        
        int[] points = this.floatArrayToIntArray(plotter.getPoints());  // prawa -> lewa
        
        int[] legendv = this.floatArrayToIntArray(plotter.getVerticalLegendPositions());  // góra -> dół
        String[] valuesv = plotter.getVerticalLegendValues();  // góra -> dół
        
        int[] legendh = this.floatArrayToIntArray(plotter.getHorizontalLegendPosition());
        String[] valuesh = plotter.getHorizontalLegendValues();
        
        this.drawLines(g, points);
        this.drawLegend(g, legendv, valuesv);
        this.drawLegend(g, legendh, valuesh);
    }
    
    /** Konwertuje tablicę liczb zmiennoprzecinkowych w tablicę liczb całkowitych.
     * @param array tablica liczb zmiennoprzecinkowych
     * @return  tablica liczb całkowitych powstaych w wyniku zwykłego rzutowania
     */
    protected int[] floatArrayToIntArray(float[] array) {
        if (array == null) return new int[] {};
        int[] res = new int[array.length];
        
        int k = 0;
        for (float f : array) res[k++] = (int) f;
        
        return res;
    }
    
    /** Zwraca kolekcję danych archiwalnych dla żądanej firmy.
     * @param company   nazwa firmy
     * @return  lista danych archiwalnych
     */
    protected ArrayList<ArchivedStock> getArchiveForCompany(String company) {
        return GameUtilities.getInstance().getArchived(company);
    }
    
    /** Umożliwa szybkie narysowanie krzywej korzystając z danych zwracanych przez {@link Plotter}.
     * @param g         kontroler rysowania
     * @param points    tablica punktów w postaci <i>[x0 y0 x1 y1 ...]</i>
     */
    protected void drawLines(Graphics g, int[] points) {
        int linesCount = (points.length>>1) - 1;    // ilość linii dla N punktów
        int[] xPoints = new int[linesCount + 1];    // jeśli mamy M linii to buduje ją (M + 1) punktów
        int[] yPoints = new int[linesCount + 1];
        
        int xi = 0, yi = 0;
        for (int i = 0; i < points.length; i += 2) {
            xPoints[xi++] = points[i + 0];
            yPoints[yi++] = points[i + 1];
        }
             
        g.setColor(PLOT_LINE_COLOR);
        g.drawPolyline(xPoints, yPoints, xPoints.length);
    }

    /** Umożliwia szybkie narysowanie legendy korzystając z danych zwracanych przez {link Plotter}.
     * @param g         kontroler rysowania
     * @param legend    lista punktów, w których rysować legendę (postać: [x0 y0 x1 y1 ...])
     * @param values
     */
    protected void drawLegend(Graphics g, int[] legend, String... values) {
        g.setColor(PLOT_LEGEND_COLOR);
        int vi = 0;
        for (int i = 0; i < legend.length; i += 2) {
            g.drawString(values[vi++], legend[i + 0], legend[i + 1]);
        }
    }
    
    /** Ustawia nazwę firmy, dla której wykres ma zostać utworzony.
     * @param company   nazwa firmy
     * @return  ten obiekt
     */
    public PlotPanel setCompany(String company) {
        mCompany = company;
        return this;
    }
        
}
