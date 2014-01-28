package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/** Adapter, który odpowiada za listę danych o obserwowanych przez gracza firmach z notowań ciągłych.*/
public class ObservedAdapter extends ArrayAdapter<CurrentStock> {

	private ArrayList<CurrentStock> items;
	private Context context;

	/**Tworzy obiekt na podstawie parametrów wywołując konstruktor klasy ArrayAdapter oraz wypełnia 
	 * nimi odpowiednie pola tego obiektu.
	 * @param context Context, do którego należy lista
	 * @param textViewResourceId layout określający wygląd pojedynczego elementu (wierszu) listy
	 * @param items lista zawierająca aktualne dane o obserwowanych firmach z notowań ciagłych
	*/
	public ObservedAdapter(Context context, int textViewResourceId,ArrayList<CurrentStock> items) {
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
 
		View rowView = inflater.inflate(R.layout.all_records_list_layout, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.firma);
		TextView textView2 = (TextView) rowView.findViewById(R.id.czas);
		TextView textView3 = (TextView) rowView.findViewById(R.id.cena);
		TextView textView4 = (TextView) rowView.findViewById(R.id.zmiana);
		
		textView1.setText(items.get(position).getName());
		textView2.setText(items.get(position).getTime());
		
		float changeF = items.get(position).getChange();
		double priceD = items.get(position).getEndPrice()/100.0;
		NumberFormat formatter = new DecimalFormat("#0.00");
		String changeS = formatter.format(changeF);
		String priceS = formatter.format(priceD);

		textView3.setText(priceS);

		if(items.get(position).getChange()<0) {
			textView4.setTextColor(Color.RED);
			textView4.setText(changeS+"%");
		}
		else{
			textView4.setTextColor(Color.GREEN);			
			textView4.setText("+"+changeS+"%");
		}
		return rowView;
	}
	
	/**Zmienia zawartość listy danych o obserwowanych firmach z notowań ciągłych.
	 * @param it nowa lista posiadanych przez gracza akcji.
	 * **/
	public void update(ArrayList<CurrentStock> it, ArrayList<String> obs){
		items.clear();
		for(int i =0;i<it.size(); i++) {
			if(obs.contains(it.get(i).getName())) {
				this.items.add(it.get(i));
			}
		}
		this.notifyDataSetChanged();
	}

	/** Zwraca nazwę firmy, znajdującej się na podanej pozycji w liście danych o obserwowanych firmach z notowań ciągłych.
	 * @param index pozycja na liście
	 * @return nazwa firmy
	 * */	
	public String getName(int index) {
		return items.get(index).getName();
	}
}