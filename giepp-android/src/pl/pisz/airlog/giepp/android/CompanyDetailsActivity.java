package pl.pisz.airlog.giepp.android;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;

public class CompanyDetailsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

	private int maxToBuy = 0;
	private int maxToSell = 0;
	private String companyName;
	private CheckBox checkBox;
	private TextView owned;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);                
		this.companyName = GiePPSingleton.getInstance().getName();
		this.getActionBar().setTitle(companyName);

		setContentView(R.layout.company_details);
		Button buy = (Button) findViewById(R.id.buy);
		Button sell = (Button) findViewById(R.id.sell);
		
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		if(GiePPSingleton.getInstance().getObserved().contains(companyName)) {
			checkBox.setChecked(true);
		}
		checkBox.setOnCheckedChangeListener(this);
		
		owned = (TextView) findViewById(R.id.company_details_owned);
		updateMaxToBuySell();

		buy.setOnClickListener(this);
		sell.setOnClickListener(this);
		
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
		if (isChecked) {
			GiePPSingleton.getInstance().addToObserved(companyName);
    		Log.i("giepp","Dodaje do obserwowanych: " + companyName);
    	}
    	else {
    		GiePPSingleton.getInstance().removeFromObserved(companyName);
    		Log.i("giepp","Usuwam z obserwowanych: " + companyName);    		
    	}
    }

    public void onClick(View v){
    	
    	if(v.getId() == R.id.buy) {
    		final Dialog dialog = new BuySellDialog(this,companyName,maxToBuy,1);
    		Log.i("giepp","Tworze dialog");
			dialog.setTitle("Kupno");	 		
			dialog.show();
		  }
    	else if (v.getId() == R.id.sell){
    		final Dialog dialog = new BuySellDialog(this,companyName,maxToSell,2);
    		Log.i("tabsfragments","Tworze dialog");
			dialog.setTitle("Sprzedaż");	 		
			dialog.show();
		  }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void updateMaxToBuySell(){
		for(PlayerStock ps : GiePPSingleton.getInstance().getOwned()) {
			if (ps.getCompanyName().equals(companyName)) {
				this.maxToSell = ps.getAmount();
				break;				
			}
		}
		for(CurrentStock cs : GiePPSingleton.getInstance().getCurrent()) {
			if (cs.getName().equals(companyName)) {
				if(cs.getEndPrice() > 0 ) {
					this.maxToBuy = (int) (GiePPSingleton.getInstance().getMoney() / cs.getEndPrice());
				}
				break;		
			}
		}
		owned.setText(GiePPSingleton.getInstance().getAmount(companyName)+"");
    }
}


class BuySellDialog extends Dialog implements View.OnClickListener, OnValueChangeListener {
	
	private NumberPicker np;
	private Button buttonOK;
	private Button buttonNO;
	private int amount;
	private int max;
	private String companyName;
	private int type;
	private CompanyDetailsActivity act;
	
	public BuySellDialog(CompanyDetailsActivity act, String companyName, int max, int type){
		super(act);
		this.act = act;
		this.companyName = companyName;
		this.max = max;
		this.type = type;
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
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
		amount = newVal;
	    Log.i("value is",""+newVal);

	}

    public void onClick(View v){
    	if(v.getId() == R.id.buttonOK) {
    		switch (type) {
    			default:
    			case 1:
    				Log.i("giepp","Kupuje " + amount + " akcji " + companyName);
    				GiePPSingleton.getInstance().buy(companyName,amount);
    				act.updateMaxToBuySell();
    				break;
    			case 2:
    				GiePPSingleton.getInstance().sell(companyName,amount);
    				Log.i("giepp","Sprzedaje " + amount + " akcji " + companyName);
    				act.updateMaxToBuySell();
    				break;
    		}
    	}
    	dismiss();
    }
}