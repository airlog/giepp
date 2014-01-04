package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StatsFragment extends Fragment {
		
		private TextView textMaxMoney;
		private TextView textMinMoney;
		private TextView textRestarts;
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.stats,container, false);
			
			textMaxMoney = (TextView) rootView.findViewById(R.id.stats_max_money);
			textMinMoney = (TextView) rootView.findViewById(R.id.stats_min_money);
			textRestarts = (TextView) rootView.findViewById(R.id.stats_restarts);
			
			updateView();
			
			GiePPSingleton.getInstance().setFragment4(this);
			
			return rootView;
		}
		
		public void updateView() {
			NumberFormat formatter = new DecimalFormat("#0.00");

			double maxMoneyD = GiePPSingleton.getInstance().getStats().getMaxMoney()/100.0;
			String maxMoneyS = formatter.format(maxMoneyD);
			
			double minMoneyD = GiePPSingleton.getInstance().getStats().getMinMoney()/100.0;
			String minMoneyS = formatter.format(minMoneyD);

			int restarts = GiePPSingleton.getInstance().getStats().getRestarts();
			
			textMaxMoney.setText(maxMoneyS+" PLN");
			textMinMoney.setText(minMoneyS+" PLN");
			textRestarts.setText(restarts+"");
		}
}
