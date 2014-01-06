package pl.pisz.airlog.giepp.android;


import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.plot.Plotter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompanyChart extends View {
	
	private Paint paint;
	private ArrayList<ArchivedStock> history;
	private int days;
	private int min;
	private int max;
	
	public CompanyChart(Context ctx) {
		super(ctx);
		
		this.init();
	}
	
	public CompanyChart(Context ctx,AttributeSet attr) {
		super(ctx,attr);
		this.init();
	}
	
	public CompanyChart(Context ctx,AttributeSet attr,int styleDef) {
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
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		int width = getWidth();
		int height = getHeight();
		paint.setColor(Color.BLACK);
		
		canvas.drawLine(0, 0, 0, height, paint);
		canvas.drawLine(width-1, 0, width-1, height, paint);
		canvas.drawLine(0, 0, width, 0, paint);
		canvas.drawLine(0, height-2, width, height-2, paint);
		
		Plotter plotter = new Plotter(history,getWidth(),getHeight(),4);

		float[] legendXY = plotter.getVerticalLegendPositions();
		String[] legendS = plotter.getVerticalLegendValues();
		
		for (int i = 0; i<legendS.length ; i++) {
			canvas.drawText(legendS[i],0,legendS[i].length(),legendXY[i*2],legendXY[i*2+1]+5, paint);
			canvas.drawLine(0,legendXY[i*2+1],legendXY[i*2]/2,legendXY[i*2+1], paint);
		}

		paint.setColor(Color.BLUE);
		
		float[] points = plotter.getPoints();
		if (points == null) {
			return;
		}
		for (int i = 0 ; i<points.length/4; i++) {
			canvas.drawLine(points[4*i],points[4*i+1],points[4*i+2],points[4*i+3], paint);
			if(4*i+5 < points.length) {
				canvas.drawLine(points[4*i+2],points[4*i+3],points[4*i+4],points[4*i+5], paint);
			}
		}
		/*
		
		if (days == 0) return;
		
		int deltaX = width/10;
		int deltaY = 2;
		height -= 2*deltaY;
		width -= deltaX;
		
		int daysX = width/days;
		float unitY = height/((max-min)*1.0f);
		
		int maxI = 5;
		DecimalFormat df = new DecimalFormat("#0.000");
		for (int i = 1; i<maxI; i++) {
			canvas.drawRect(0, deltaY+i*height/maxI-1, deltaX/5,deltaY+i*height/maxI+1,paint);
			float number = ((min + i*1.0f*(max-min)/maxI)/100.0f); 
			String text = df.format(number);
			if(text.charAt(text.length()-1) == '0') text = text.substring(0,text.length()-1);
			canvas.drawText(text, 0, text.length(), deltaX/4, deltaY+(maxI-i)*height/maxI+5, paint);			
		}
		
		paint.setColor(Color.BLUE);
		int lastY = history.get(0).getMaxPrice()+history.get(0).getMaxPrice();
		for (int i = 0; i<history.size(); i++) {
			int startX = deltaX + width - (i+1)*daysX;
			int endX = deltaX + width - (i)*daysX;
			int sum = history.get(i).getMaxPrice() + history.get(i).getMinPrice();
			int startY = (int) ((max-sum*0.5f)*unitY);
			canvas.drawRect(startX, deltaY+startY-1, endX, deltaY+startY+1, paint);
			if (i>0) canvas.drawRect(endX-1, deltaY+((max-lastY*0.5f)*unitY), endX+1, deltaY+startY, paint);
			lastY = sum;
		}*/
	}

}