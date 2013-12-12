package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.PlayerStock;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class MyAccountFragment extends Fragment implements OnItemClickListener{
		
		private AccountAdapter adapter3;
	
		public MyAccountFragment() {
		
		}	

		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Log.i("tabsfragments","kliknieto");
			Intent intent = new Intent(getActivity(), CompanyDetailsActivity.class);
			String name = adapter3.getName(position);
			GiePPSingleton.getInstance().setName(name);
			getActivity().startActivity(intent);      
			
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.my_account,container, false);
			TextView textMoney = (TextView) rootView.findViewById(R.id.money);
			TextView textStock = (TextView) rootView.findViewById(R.id.stock);
			
			double money = GiePPSingleton.getInstance().getMoney()/100.0;
			NumberFormat formatter = new DecimalFormat("#0.00");
			String moneyS = formatter.format(money);
			
			double stock = GiePPSingleton.getInstance().getMoneyInStock()/100.0;
			String stockS = formatter.format(stock);

			textMoney.setText(moneyS+" PLN");
			textStock.setText(stockS+" PLN");
			ListView list =  (ListView) rootView.findViewById(R.id.my_account_list);
			ArrayList<PlayerStock> o1 = GiePPSingleton.getInstance().getOwned();
			ArrayList<PlayerStock> o2 = new ArrayList<PlayerStock>();
			for(int i =0; i<o1.size(); i++) {
				o2.add(o1.get(i));
			}
			adapter3 = new AccountAdapter(getActivity(), R.layout.my_account_list_layout,o2);
			GiePPSingleton.getInstance().setAdapter3(adapter3);
			list.setAdapter(adapter3);
			list.setOnItemClickListener(this);

			return rootView;
		}
}
