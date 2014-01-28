package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/** Adapter, który odpowiada za listę posiadanych przez gracza akcji.*/
public class AccountAdapter extends ArrayAdapter<PlayerStock> {

	private ArrayList<PlayerStock> items;
	private Context context;
	
	/**Tworzy obiekt na podstawie parametrów wywołując konstruktor klasy ArrayAdapter oraz wypełnia 
	 * nimi odpowiednie pola tego obiektu.
	 * @param context Context, do którego należy lista
	 * @param textViewResourceId layout określający wygląd pojedynczego elementu (wierszu) listy
	 * @param items lista zawierająca posiadane przez gracza akcje
	 * */
	public AccountAdapter(Context context, int textViewResourceId, ArrayList<PlayerStock> items) {
		super(context, textViewResourceId,items);
		this.items = items;
		this.context = context;
	}

	/**Zwraca View zawierające wypełniony na podstawie danych element (wiersz) ListView.
	 * @param position numer elementu listy
	 * @return pojedynczy wiersz w liscie
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.my_account_list_layout, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.firma);
		TextView textView2 = (TextView) rowView.findViewById(R.id.ilosc);
		TextView textView3 = (TextView) rowView.findViewById(R.id.suma);
		TextView textView4 = (TextView) rowView.findViewById(R.id.zakup);
		
		textView1.setText(items.get(position).getCompanyName());
		textView2.setText(""+items.get(position).getAmount());
		
		ArrayList<CurrentStock> c = GiePPSingleton.getInstance().getCurrent();
		long priceL = 0;
		for(int i = 0; i<c.size();i++)
			if(items.get(position).getCompanyName().equals(c.get(i).getName())){
				priceL = c.get(i).getEndPrice();
				break;
			}
		double priceD = priceL*items.get(position).getAmount()/100.0;
		NumberFormat formatter = new DecimalFormat("#0.00");
		String priceS = formatter.format(priceD);
		textView3.setText(priceS);
		
		double startD = items.get(position).getStartPrice()/100.0;
		String startS = formatter.format(startD);
		
		if (priceD >= startD) {
			textView3.setTextColor(Color.GREEN);			
		}
		else {
			textView3.setTextColor(Color.RED);			
		}
		textView4.setText(startS);

		
		return rowView;
	}
	
	/**Zmienia zawartość listy posiadanych przez gracza akcji trzymanej w adapterze.
	 * @param it nowa lista posiadanych przez gracza akcji.
	 * **/
	public void update(ArrayList<PlayerStock> it){
		items.clear();
		for(int i =0;i<it.size(); i++)
			this.items.add(it.get(i));
		this.notifyDataSetChanged();
	}
	
	/** Zwraca nazwę firmy, znajdującej się na podanej pozycji w liście posiadanych przez gracza akcji.
	 * @param index pozycja na liście
	 * @return nazwa firmy
	 * */
	public String getName(int index) {
		return items.get(index).getCompanyName();
	}
}