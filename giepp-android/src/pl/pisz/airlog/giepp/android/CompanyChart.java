package pl.pisz.airlog.giepp.android;


import java.text.DecimalFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.ArchivedStock;
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
		for (int i = 0 ; i<width; i++) {
			canvas.drawRect(i, 0, i+1, 1, paint);
			canvas.drawRect(i, height-1, i+1, height, paint);
		}
		for (int j = 0 ; j<height; j++) {
			canvas.drawRect(0, j, 1, j+1, paint);
			canvas.drawRect(width-1, j,width, j+1, paint);
		}
		
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
		}
	}

}