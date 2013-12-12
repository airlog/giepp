package pl.pisz.airlog.giepp.android;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;

public class CompanyDetailsActivity extends Activity implements View.OnClickListener{

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        this.getActionBar().setTitle(GiePPSingleton.getInstance().getName());
        
		setContentView(R.layout.company_details);
		TextView title = (TextView) findViewById(R.id.title);
		title.setText("Informacja o firmie: " + GiePPSingleton.getInstance().getName());
		Button buy = (Button) findViewById(R.id.buy);
		Button sell = (Button) findViewById(R.id.sell);
		buy.setOnClickListener(this);
		sell.setOnClickListener(this);
    }

    public void onClick(View v){
    	
    	if(v.getId() == R.id.buy) {
    		final Dialog dialog = new DialogBuySell(this,GiePPSingleton.getInstance().getNr(),140,1);
    		Log.i("tabsfragments","Tworze dialog");
			dialog.setTitle("Kupno");	 		
			dialog.show();
		  }
    	else{
    		final Dialog dialog = new DialogBuySell(this,GiePPSingleton.getInstance().getNr(),140,2);
    		Log.i("tabsfragments","Tworze dialog");
			dialog.setTitle("Sprzedaż");	 		
			dialog.show();
		  }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}


class DialogBuySell extends Dialog implements View.OnClickListener, OnValueChangeListener {
	
	private NumberPicker np;
	private Button buttonOK;
	private Button buttonNO;
	private int ile;
	private int max;
	private int firma;
	private int type;
	
	public DialogBuySell(Context ctx, int firma, int max, int type){
		super(ctx);
		this.firma = firma;
		this.max = max;
		this.type = type;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
     //   requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog);
		np = (NumberPicker) findViewById(R.id.numberPicker1);
		np.setMaxValue(max);
        np.setMinValue(0);
        np.setWrapSelectorWheel(true);
        np.setOnValueChangedListener(this);
        buttonOK = (Button) findViewById(R.id.buttonOK);	
        buttonNO = (Button) findViewById(R.id.buttonNO);
        buttonOK.setOnClickListener(this);
        buttonNO.setOnClickListener(this);
   }
	
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		ile = newVal;
	    Log.i("value is",""+newVal);

	}

    public void onClick(View v){
    	Log.i("tabsfragments","Dismiss");
    	dismiss();
    }
}