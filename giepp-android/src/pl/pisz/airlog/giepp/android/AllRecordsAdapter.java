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

public class AllRecordsAdapter extends ArrayAdapter<CurrentStock> {

	private ArrayList<CurrentStock> items;
	private Context context;
	    
    public AllRecordsAdapter(Context context, int textViewResourceId,ArrayList<CurrentStock> items) {
      super(context, textViewResourceId,items);
      this.items = items;
      this.context = context;
    }

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
	public void zmiana(ArrayList<CurrentStock> it){
		items.clear();
		for(int i =0;i<it.size(); i++)
			this.items.add(it.get(i));
		this.notifyDataSetChanged();
	}
}