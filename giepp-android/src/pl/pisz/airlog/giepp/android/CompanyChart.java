package pl.pisz.airlog.giepp.android;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class CompanyChart extends View{
	int height = 150;
	int width = 200;
	
	Paint paint;
	
	public CompanyChart(Context ctx){
		super(ctx);
		
		this.init();
	}
	
	public CompanyChart(Context ctx,AttributeSet attr){
		super(ctx,attr);
		this.init();
	}
	
	public CompanyChart(Context ctx,AttributeSet attr,int styleDef){
		super(ctx,attr,styleDef);
		this.init();
	}
	
	
	private void init(){
		this.paint = new Paint();
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		paint.setColor(Color.BLUE);
		canvas.drawCircle(50,50,30, paint);					

	}

}