package pl.pisz.airlog.giepp.plot;

import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.game.Game;

/** Klasa wykorzystuje dane archiwalne zawarte w {@link Game} i zapewnia punkty do narysowania wykresu.
* @author Joanna
*/
public class Plotter {
	
	private ArrayList<ArchivedStock> mArchival;
	
	private int mWidth;
	private int mHeight;
	
	private int mMax;
	private int mMin;
	private int mMarginX;   // ucięcie z lewego boku (legenda)
	private int mMarginY;   // ucięcie z góry i dołus
	
	private int mLegendCount;
	
	public Plotter(ArrayList<ArchivedStock> archival, int width, int height, int legendCount) {
		mArchival = archival;
		mWidth = width;
		mHeight = height;
		mLegendCount = legendCount;
		
		mMarginX = mWidth/10;
		mMarginY = 2;
		
		if (mArchival == null || mArchival.size() == 0) {
			return;
		}
		this.findMin();
		this.findMax();
	}
	 
	private void findMin() {
		int min = mArchival.get(0).getMinPrice(); 
		
		for (ArchivedStock a : mArchival) {
			if (a.getMinPrice() < min) {
				min = a.getMinPrice();
			}
		}
		mMin = min;
	}
	
	private void findMax() {
		int max = mArchival.get(0).getMaxPrice(); 
		
		for (ArchivedStock a : mArchival) {
			if (a.getMaxPrice() > max) {
				max = a.getMaxPrice();
			}
		}
		mMax = max;
	}
	
	/** Zwraca tablicę punktów umożliwiających narysowanie łamanej.
	 * @return lista punktów [x0 y0 x1 y1 ...]
	 */	
	public float[] getPoints() {
		if (mArchival == null || mArchival.size() == 0) {
			return null;
		}
		
		float[] result = new float[4 * mArchival.size()];
		
		int days = mArchival.size();

		int height = mHeight - 2 * mMarginY;
		int width = mWidth - mMarginX;
		
		int deltaX = width/days;
		
		// unikanie dzielenia przez 0
		// autor: Joanna
		if (mMax == mMin) {
		    mMax += 1;
		    mMin -= 1;
		}
		
		float unitY = (float) height/(float) (mMax - mMin);
		
		for (int i = 0; i<mArchival.size(); i++) {
			int startX = mMarginX + width - (i+1)*deltaX;
			int endX = mMarginX + width - (i)*deltaX;
			
			int sum = mArchival.get(i).getMaxPrice() + mArchival.get(i).getMinPrice();
			int y = (int) ((mMax-sum*0.5f)*unitY);

			result[4*i] = endX;
			result[4*i + 1] = y;
			result[4*i + 2] = startX;
			result[4*i + 3] = y;
		}
		
		return result;
	}
	
	/** Zwraca tablicę wartości legendy.
	 * @return lista stringów [s0, s1 ...]
	 */
	public String[] getVerticalLegendValues() {
		String[] result = new String[mLegendCount];
		DecimalFormat df = new DecimalFormat("#0.000");

		for (int i = 0; i < mLegendCount; i++) {
			float number = ((mMin + (i + 1) * (float) (mMax - mMin)/(mLegendCount + 1))/100.0f); 
			String text = df.format(number);
			if (text.endsWith("0")) {
				text = text.substring(0,text.length()-1);
			}
			result[i] = text; 
		}
		return result;
	}
	 
	/** Zwraca tablicę współrzędnych X, Y dla legendy.
	 * @return lista punktów [x0 y0 x1 y1 ...]
	 */
	public float[] getVerticalLegendPositions() {
		float[] result = new float[mLegendCount*2];

		for (int i = 0; i < mLegendCount * 2; i += 2) {
			result[i] = mMarginX/4;     // margines dla legendy
			result[i + 1] = mMarginY + (mLegendCount - i/2) * mHeight/(mLegendCount + 1);
		}
		
		return result;
	}
	 
}

