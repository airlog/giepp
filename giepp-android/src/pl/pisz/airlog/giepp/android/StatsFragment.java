package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

public class StatsFragment extends Fragment implements View.OnClickListener {
		
		private TextView textMaxMoney;
		private TextView textMinMoney;
		private TextView textRestarts;
		private Button buttonRestart;
		private Button buttonRefreshArchival;
		
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.stats_restart_button) {
				final Dialog dialog = new ConfirmDialog(getActivity());
				dialog.setTitle("Potwierdzenie");	 		
				dialog.show();
			}
			else if (v.getId() == R.id.stats_refresh_archival) {
				final Dialog dialog = new RefreshArchivalDialog(getActivity());
				dialog.setTitle("Dane archiwalne");	 		
				dialog.show();
			}
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.stats,container, false);
			
			textMaxMoney = (TextView) rootView.findViewById(R.id.stats_max_money);
			textMinMoney = (TextView) rootView.findViewById(R.id.stats_min_money);
			textRestarts = (TextView) rootView.findViewById(R.id.stats_restarts);
			
			buttonRestart = (Button) rootView.findViewById(R.id.stats_restart_button);
			buttonRefreshArchival = (Button) rootView.findViewById(R.id.stats_refresh_archival);
			
			updateView();

			buttonRestart.setOnClickListener(this);
			buttonRefreshArchival.setOnClickListener(this);

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

class ConfirmDialog extends Dialog implements View.OnClickListener {
	
	private Button buttonOK;
	private Button buttonNO;
	
	public ConfirmDialog(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.confirm_dialog);
		buttonOK = (Button) findViewById(R.id.confirm_buttonOK);	
		buttonNO = (Button) findViewById(R.id.confirm_buttonNO);
		buttonOK.setOnClickListener(this);
		buttonNO.setOnClickListener(this);
	}

	public void onClick(View v) {
		if(v.getId() == R.id.confirm_buttonOK) {
			GiePPSingleton.getInstance().restartGame();
		}
		dismiss();
	}
}

class RefreshArchivalDialog extends Dialog implements View.OnClickListener {
	
	private DatePicker start;
	private DatePicker end;
	private Button buttonOK;
	private Button buttonNO;
	
	public RefreshArchivalDialog(Context ctx) {
		super(ctx);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.refresh_archival_dialog);
		buttonOK = (Button) findViewById(R.id.refresh_archival_buttonOK);	
		buttonNO = (Button) findViewById(R.id.refresh_archival_buttonNO);
		start = (DatePicker) findViewById(R.id.date_start);
		end = (DatePicker) findViewById(R.id.date_end);
		
		start.setCalendarViewShown(false);
		end.setCalendarViewShown(false);
				
		buttonOK.setOnClickListener(this);
		buttonNO.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.refresh_archival_buttonOK) {
			Log.i("giepp","start: " + start.getDayOfMonth() + "-" + start.getMonth() + "-" + start.getYear());
			Log.i("giepp","end: " + end.getDayOfMonth() + "-" + end.getMonth() + "-" + end.getYear());
		}
		dismiss();
	}
}