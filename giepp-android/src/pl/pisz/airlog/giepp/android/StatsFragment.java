package pl.pisz.airlog.giepp.android;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

/** Fragment zawierający informacje takie jak: ile razy gracz zaczynał grę od nowa, ile miał
 * maksymalnie i minimalnie pieniędzy (wliczając akcje) oraz z jakiego dnia pochodzą ostatnie
 * dane archiwalne. We fragmencie znajdują się przyciski umożliwiające zaczęcie gry od nowa
 * oraz pobranie archiwalnych danych z wybranych dni. Podczas pobierania danych archiwalnych
 * we fragmencie widać ProgressBar. 
*/
public class StatsFragment extends Fragment implements View.OnClickListener {
		
		private TextView textMaxMoney;
		private TextView textMinMoney;
		private TextView textRestarts;
		private TextView textLastDate;
		private Button buttonRestart;
		private Button buttonRefreshArchival;
		private ProgressBar bar;

		/** W zależności od klikniętego przysku może pojawić się 
		 * dialog, w którym gracz może potwierdzić chęć zaczęcia gry od nowa
		 * lub dialog służący do wyboru przedziału czasu, z którego chce się pobrać dane archiwalne.
		*/
		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.stats_restart_button) {
				final Dialog dialog = new ConfirmDialog(getActivity());
				dialog.setTitle("Potwierdzenie");	 		
				dialog.show();
			}
			else if (v.getId() == R.id.stats_refresh_archival) {
				if (GiePPSingleton.getInstance().isRefreshingArchival()) {
					return;
				}
				final Dialog dialog = new RefreshArchivalDialog(getActivity(), bar);
				dialog.setTitle("Dane archiwalne");	 		
				dialog.show();
			}
		}
		
		/** Na podstawie layoutu stats.xml tworzony jest widok.*/
		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.stats,container, false);
			
			textMaxMoney = (TextView) rootView.findViewById(R.id.stats_max_money);
			textMinMoney = (TextView) rootView.findViewById(R.id.stats_min_money);
			textRestarts = (TextView) rootView.findViewById(R.id.stats_restarts);
			textLastDate = (TextView) rootView.findViewById(R.id.stats_last_date)
					;
			buttonRestart = (Button) rootView.findViewById(R.id.stats_restart_button);
			buttonRefreshArchival = (Button) rootView.findViewById(R.id.stats_refresh_archival);
			
			bar = (ProgressBar) rootView.findViewById(R.id.stats_progressBar);
			bar.setVisibility(View.GONE);
			if (GiePPSingleton.getInstance().isRefreshingArchival()) {				
				bar.setVisibility(View.VISIBLE);
			}

			updateView();

			buttonRestart.setOnClickListener(this);
			buttonRefreshArchival.setOnClickListener(this);

			GiePPSingleton.getInstance().setFragment4(this);
			
			return rootView;
		}
		
		/** W widoku aktualizowane są wszystkie wyświetlane informacje.*/
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
			textLastDate.setText(GiePPSingleton.getInstance().getLastArchivalDate());
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
	private ProgressBar bar;
	
	public RefreshArchivalDialog(Context ctx, ProgressBar bar) {
		super(ctx);
		this.bar = bar;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.refresh_archival_dialog);
		buttonOK = (Button) findViewById(R.id.refresh_archival_buttonOK);	
		buttonNO = (Button) findViewById(R.id.refresh_archival_buttonNO);
		start = (DatePicker) findViewById(R.id.date_start);
		end = (DatePicker) findViewById(R.id.date_end);
		
	//	start.setCalendarViewShown(false);
	//	end.setCalendarViewShown(false);
				
		buttonOK.setOnClickListener(this);
		buttonNO.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.refresh_archival_buttonOK) {
			GiePPSingleton.getInstance().refreshArchival(bar, start.getDayOfMonth(), start.getMonth(), start.getYear(),
						end.getDayOfMonth(), end.getMonth(), end.getYear());
		}
		dismiss();
	}
	
	/*private class RefreshArchivalTask extends AsyncTask<Integer, Void, Integer> {

		@Override
		protected Integer doInBackground(Integer... date) {
			GiePPSingleton.getInstance().refreshArchival(date[0],date[1],date[2],date[3],date[4],date[5]);
			return 0;
		}
		
		@Override
		protected void onPostExecute(Integer i) {
			if (bar != null) {
				bar.setVisibility(View.GONE);
			}
		}
	}*/
	
}