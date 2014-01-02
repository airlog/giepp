package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AccountAdapter extends ArrayAdapter<PlayerStock> {

	private ArrayList<PlayerStock> items;
	private Context context;
	    
    public AccountAdapter(Context context, int textViewResourceId,ArrayList<PlayerStock> items) {
      super(context, textViewResourceId,items);
      this.items = items;
      this.context = context;
    }

	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.my_account_list_layout, parent, false);
		TextView textView1 = (TextView) rowView.findViewById(R.id.firma);
		TextView textView2 = (TextView) rowView.findViewById(R.id.ilosc);
		TextView textView3 = (TextView) rowView.findViewById(R.id.suma);
		
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

		return rowView;
	}
	public void zmiana(ArrayList<PlayerStock> it){
		items.clear();
		for(int i =0;i<it.size(); i++)
			this.items.add(it.get(i));
		this.notifyDataSetChanged();
	}
	public String getName(int index) {
		return items.get(index).getCompanyName();
	}
}