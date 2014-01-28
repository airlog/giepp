package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.TextView;

/**Activity zawierające wykres zawierający dane archiwalne dla danej firmy oraz informacje aktualne.
 * W tym activity znajduje się też informacja o ilości podiadanych akcji tej firmy przez gracza.
 * Za pomocą CheckBoxa znajdującego się w tym activity można dodać firmę do obserowanych
 * albo ją z tamtąd usunąć. Za pomocą przycisków Kup i Sprzedaj można kupić lub sprzedać
 * akcje danej firmy.*/
public class CompanyDetailsActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener{

	private int maxToBuy = 0;
	private int maxToSell = 0;
	private int minToSell = 0;
	private String companyName;
	private CheckBox checkBox;
	private TextView owned;
	
	private TextView nameTV;
	private TextView timeTV;
	private TextView minTV;
	private TextView maxTV;
	private TextView priceTV;
	private TextView changeTV;
	
	private DecimalFormat df;
	
	/** Na podstawie layoutu company_details.xml tworzony jest widok.*/
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);                
		this.companyName = GiePPSingleton.getInstance().getName();
		this.getActionBar().setTitle(companyName);
		GiePPSingleton.getInstance().setCompanyDetailsActivity(this);
		
		setContentView(R.layout.company_details);
		Button buy = (Button) findViewById(R.id.buy);
		Button sell = (Button) findViewById(R.id.sell);
		
		df = new DecimalFormat("#0.00");
		
		checkBox = (CheckBox) findViewById(R.id.checkBox);
		if(GiePPSingleton.getInstance().getObserved().contains(companyName)) {
			checkBox.setChecked(true);
		}
		checkBox.setOnCheckedChangeListener(this);
		
		nameTV = (TextView) findViewById(R.id.company_details_firma);
		timeTV = (TextView) findViewById(R.id.company_details_czas);
		maxTV = (TextView) findViewById(R.id.company_details_max);
		minTV = (TextView) findViewById(R.id.company_details_min);
		priceTV = (TextView) findViewById(R.id.company_details_cena);
		changeTV = (TextView) findViewById(R.id.company_details_zmiana);
		
		owned = (TextView) findViewById(R.id.company_details_owned);
		
		updateMaxToBuySell();

		buy.setOnClickListener(this);
		sell.setOnClickListener(this);
		
	}

	/**W przypadku zaznaczenia CheckBoxa firma dodawana jest do obesrwowanych
	 * ({@link GiePPSingleton#addToObserved(String)}). W przypadku 
	 * odznaczenia firma usuwana jest z obserwowanych ({@link GiePPSingleton#removeFromObserved(String)})
	 * @param isChecked informacja czy CheckBox jest zaznaczony czy nie*/
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			GiePPSingleton.getInstance().addToObserved(companyName);
			Log.i("giepp","Dodaje do obserwowanych: " + companyName);
		}
		else {
			GiePPSingleton.getInstance().removeFromObserved(companyName);
			Log.i("giepp","Usuwam z obserwowanych: " + companyName);    		
		}
	}

	/**W przypadku kliknięcia w przycisk Kup wyświetlany jest dialog służący do kupowania
	 * akcji z możliwością wyboru ilości kupowanych akcji ({@link GiePPSingleton#buy(String, int)}).
	 * W przypadku kliknięcia w przycisk Sprzedaj wyświetlany jest dialog służący do spzredawania
	 * akcji z możliwością wyboru ilości sprzedawanych akcji ({@link GiePPSingleton#sell(String, int)}). 
	 * @param v element, który został kliknięty*/
	public void onClick(View v){
		
		if(v.getId() == R.id.buy) {
			final Dialog dialog = new BuySellDialog(this,companyName,0,maxToBuy,1);
			Log.i("giepp","Tworze dialog");
			dialog.setTitle("Kupno");	 		
			dialog.show();
		}
		else if (v.getId() == R.id.sell){
			final Dialog dialog = new BuySellDialog(this,companyName,minToSell,maxToSell,2);
			Log.i("tabsfragments","Tworze dialog");
			dialog.setTitle("Sprzedaż");	 		
			dialog.show();
		}
	}

	/** Uaktualniane są maksymalne i minimalne ilości akcji danej firmy jakie można kupić i sprzedać
	 * ({@link GiePPSingleton#getMaximumToBuy(String)}, {@link GiePPSingleton#getMinimumToSell(String)}).
	 * Dodatkowo uaktualniane są wyświetlane we fragmencie informacje o ilości posiadanych akcji
	 * oraz o aktualnych danych dotyczących firmy.
	 * */
	public void updateMaxToBuySell() {
		maxToBuy = GiePPSingleton.getInstance().getMaximumToBuy(companyName);
		minToSell = GiePPSingleton.getInstance().getMinimumToSell(companyName);
		for(PlayerStock ps : GiePPSingleton.getInstance().getOwned()) {
			if (ps.getCompanyName().equals(companyName)) {
				this.maxToSell = ps.getAmount();
				break;				
			}
		}
		owned.setText(GiePPSingleton.getInstance().getAmount(companyName)+"");
		CurrentStock cs = GiePPSingleton.getInstance().getCurrent(companyName);
		if (cs == null) {
			nameTV.setText(companyName);
			timeTV.setText("");
			minTV.setText("");
			maxTV.setText("");
			priceTV.setText("");
			changeTV.setText("");
		} else {
			nameTV.setText(cs.getName());
			timeTV.setText(cs.getTime());
			minTV.setText(df.format(cs.getMinPrice()/100.0));
			maxTV.setText(df.format(cs.getMaxPrice()/100.0));
			priceTV.setText(df.format(cs.getEndPrice()/100.0));
			if (cs.getChange() >= 0) {
				changeTV.setText("+"+df.format(cs.getChange())+"%");
				changeTV.setTextColor(Color.GREEN);
			}
			else {
				changeTV.setText(df.format(cs.getChange())+"%");
				changeTV.setTextColor(Color.RED);
			}
		}
	}
}


class BuySellDialog extends Dialog implements View.OnClickListener {
	
	private NumberPicker np;
	private Button buttonOK;
	private Button buttonNO;
//	private int amount;
	private int max;
	private int min;
	private String companyName;
	private int type;
	private CompanyDetailsActivity act;
	
	public BuySellDialog(CompanyDetailsActivity act, String companyName, int min, int max, int type){
		super(act);
		this.act = act;
		this.companyName = companyName;
		this.min = min;
		this.max = max;
		this.type = type;
		if (this.min > this.max) {
			this.min = 0;
			this.max = 0;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.dialog);
		np = (NumberPicker) findViewById(R.id.numberPicker1);
		np.setMaxValue(max);
		np.setMinValue(min);
		np.setWrapSelectorWheel(true);
    //  np.setOnValueChangedListener(this);
		buttonOK = (Button) findViewById(R.id.buttonOK);	
		buttonNO = (Button) findViewById(R.id.buttonNO);
		buttonOK.setOnClickListener(this);
		buttonNO.setOnClickListener(this);
	}
	
/*	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		amount = newVal;
	    Log.i("value is",""+newVal);

	}
*/
	public void onClick(View v){
		if(v.getId() == R.id.buttonOK) {
			switch (type) {
				default:
				case 1:
					GiePPSingleton.getInstance().buy(companyName,np.getValue());
					act.updateMaxToBuySell();
					break;
				case 2:
					GiePPSingleton.getInstance().sell(companyName,np.getValue());
					act.updateMaxToBuySell();
					break;
			}
		}
		dismiss();
	}
}