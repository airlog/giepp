package pl.pisz.airlog.giepp.android;


import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.plot.Plotter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/** Obiekt tej klasy jest wykresem, na którym można zobaczyć jak zmieniała
 * się cena akcji danej firmy w czasie pewnej liczby dni.*/
public class CompanyChart extends View {
	
	private Paint paint;
	private ArrayList<ArchivedStock> history;
	private int days;
	private int min;
	private int max;
	
	/** Tworzy obiekt tej klasy. Wywołuje metodę init(), w której następuje pobranie
	 * danych dotyczących firmy (pobierane z {@link GiePPSingleton}). Ustalana jest też liczba 
	 * dni, z których ceny będą przedstawione na wykresie oraz 
	 * maksymalna i minimalna cena akcji (potrzebne do wyskalowania wykresu).
	 * */
	public CompanyChart(Context ctx) {
		super(ctx);
		
		this.init();
	}
	
	/** Tworzy obiekt tej klasy. Wywołuje metodę init(), w której następuje pobranie
	 * danych dotyczących firmy (pobierane z {@link GiePPSingleton}). Ustalana jest też liczba 
	 * dni, z których ceny będą przedstawione na wykresie oraz 
	 * maksymalna i minimalna cena akcji (potrzebne do wyskalowania wykresu).
	 * */
	public CompanyChart(Context ctx, AttributeSet attr) {
		super(ctx,attr);
		this.init();
	}
	
	/** Tworzy obiekt tej klasy. Wywołuje metodę init(), w której następuje pobranie
	 * danych dotyczących firmy (pobierane z {@link GiePPSingleton}). Ustalana jest też liczba 
	 * dni, z których ceny będą przedstawione na wykresie oraz 
	 * maksymalna i minimalna cena akcji (potrzebne do wyskalowania wykresu).
	 * */
	public CompanyChart(Context ctx, AttributeSet attr, int styleDef) {
		super(ctx,attr,styleDef);
		this.init();
	}
		
	private void init() {
		this.paint = new Paint();
		this.history = GiePPSingleton.getInstance().getArchival(GiePPSingleton.getInstance().getName());
		
		if (history != null && history.size() > 0) {
			this.days = history.size();	
			this.min = history.get(0).getMinPrice(); 
			this.max = history.get(0).getMaxPrice();
			for (ArchivedStock a : history) {
				if (a.getMinPrice() < min) {
					min = a.getMinPrice();
				if (a.getMaxPrice() > max) {
						max = a.getMaxPrice();
					}
				}
			}
		}
		
	}
	
	/** Używając obiektu klasy Plotter rysowany jest wykres dla danej firmy.*/
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = getWidth();
		int height = getHeight();
		paint.setColor(Color.BLACK);
		
		canvas.drawLine(0, 0, 0, height, paint);
		canvas.drawLine(width-1, 0, width-1, height, paint);
		canvas.drawLine(0, 0, width, 0, paint);
		canvas.drawLine(0, height-1, width, height-1, paint);
		
		Plotter plotter = new Plotter(history,getWidth(),getHeight(),4,3);

		float[] vLegendXY = plotter.getVerticalLegendPositions();
		String[] vLegendS = plotter.getVerticalLegendValues();
		
		for (int i = 0; i<vLegendS.length ; i++) {
			canvas.drawText(vLegendS[i],0,vLegendS[i].length(),vLegendXY[i*2],vLegendXY[i*2+1]+5, paint);
			canvas.drawLine(0,vLegendXY[i*2+1],vLegendXY[i*2]/2,vLegendXY[i*2+1], paint);
		}

		float[] hLegendXY = plotter.getHorizontalLegendPosition();
		String[] hLegendS = plotter.getHorizontalLegendValues();
		
		for (int i = 0; i<hLegendS.length ; i++) {
			canvas.drawText(hLegendS[i],0,hLegendS[i].length(),hLegendXY[i*2]-40,hLegendXY[i*2+1]-15, paint);
			canvas.drawLine(hLegendXY[i*2],hLegendXY[i*2+1]-10,hLegendXY[i*2],hLegendXY[i*2+1], paint);
		}

		paint.setColor(Color.BLUE);
		
		float[] points = plotter.getPoints();
		if (points == null) {
			return;
		}
		for (int i = 0; i< points.length-2; i+=2) {
			canvas.drawLine(points[i],points[i+1],points[i+2],points[i+3], paint);
		}
		
	}

}