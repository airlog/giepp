package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.PlayerStock;
import android.content.Intent;
import android.graphics.Color;
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

/** Fragment zawierający informacje o posiadanych przez
 * gracza pieniądzach oraz akcjach.*/
public class MyAccountFragment extends Fragment implements OnItemClickListener {
		
		private AccountAdapter adapter3;
		private TextView textMoney;
		private TextView textStock;
		private TextView textMoneyAll;
		private TextView suma_ilosc;
		private TextView suma_zakup;
		private TextView suma_teraz;
		
		/** Po kliknięciu wyświetlane jest {@link CompanyDetailsActivity} dotyczące wybranej firmy
		 * (firma ta jest zapisywana w {@link GiePPSingleton}).*/
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent(getActivity(), CompanyDetailsActivity.class);
			String name = adapter3.getName(position);
			GiePPSingleton.getInstance().setName(name);
			getActivity().startActivity(intent);      
			
		}
		
		/** Na podstawie layoutu my_account.xml tworzony jest widok. Tworzony jest 
		 * adapter {@link AccountAdapter}.*/
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.my_account,container, false);
			textMoney = (TextView) rootView.findViewById(R.id.money);
			textMoneyAll = (TextView)  rootView.findViewById(R.id.money_all);
			textStock = (TextView) rootView.findViewById(R.id.stock);

			suma_ilosc = (TextView) rootView.findViewById(R.id.suma_ilosc);
			suma_zakup = (TextView)  rootView.findViewById(R.id.suma_zakup);
			suma_teraz = (TextView) rootView.findViewById(R.id.suma_teraz);
			
			updateView();

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
			GiePPSingleton.getInstance().setFragment1(this);
			return rootView;
		}
		
		/** W widoku aktualizowane są informacje o posiadanych przez gracza pieniądzach.*/
		public void updateView() {
			NumberFormat formatter = new DecimalFormat("#0.00");

			double money = GiePPSingleton.getInstance().getMoney()/100.0;
			String moneyS = formatter.format(money);
			
			double stock = GiePPSingleton.getInstance().getMoneyInStock()/100.0;
			String stockS = formatter.format(stock);

			double all = (stock + money);
			String allS = formatter.format(all);
			
			textMoney.setText(moneyS+" PLN");
			textStock.setText(stockS+" PLN");
			textMoneyAll.setText(allS+" PLN");
			
			ArrayList<PlayerStock> owned = GiePPSingleton.getInstance().getOwned();
			
			int ilosc = 0;
			int zakup = 0;
			long teraz = 0;
			
			ArrayList<CurrentStock> c = GiePPSingleton.getInstance().getCurrent();
			for (PlayerStock o : owned) {
				ilosc += o.getAmount();
				zakup += o.getStartPrice();
				long terazL = 0;
				for(int i = 0; i<c.size();i++)
					if(o.getCompanyName().equals(c.get(i).getName())){
						terazL = c.get(i).getEndPrice();
						break;
					}
				teraz += terazL*o.getAmount();
			}
			suma_ilosc.setText(ilosc+"");
			double zakupD = zakup/100.0;
			String zakupS = formatter.format(zakupD);
			double terazD = teraz/100.0;
			String terazS = formatter.format(terazD);
			
			suma_zakup.setText(zakupS);
			suma_teraz.setText(terazS);
			if (zakup <= teraz) {
				suma_teraz.setTextColor(Color.GREEN);			
			}
			else {
				suma_teraz.setTextColor(Color.RED);			
			}
		}
}
