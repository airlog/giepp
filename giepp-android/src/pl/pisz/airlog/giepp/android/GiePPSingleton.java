package pl.pisz.airlog.giepp.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;
import pl.pisz.airlog.giepp.game.ActionError;
import pl.pisz.airlog.giepp.game.ActionException;
import pl.pisz.airlog.giepp.game.Game;
import android.app.AlertDialog;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/** Obiekt tej klasy przetrzymuje obiekt klasy {@link Game} oraz przydatne do aktualizowania
 * widoków w aplikacji obiekty takie jak {@link AccountAdapter}, {@link AllRecordsAdapter}, 
 * {@link ObservedAdapter}, {@link MainActivity}, {@link MyAccountFragment}, {@link StatsFragment}
 * oraz {@link CompanyDetailsActivity}. Obiekt tej klasy może być tylko jeden.*/
public class GiePPSingleton {
	
	private static GiePPSingleton instance = null;
	
	/** Tworzy obiekt tylko wtedy jeśli obiekt tej klasy nie został stworzony już wcześniej.*/
	public static GiePPSingleton getInstance() {
		if (GiePPSingleton.instance == null) GiePPSingleton.instance = new GiePPSingleton();
		return GiePPSingleton.instance;
	}
	
	private String name;
	private Game game;
	private AllRecordsAdapter adapter1;
	private ObservedAdapter adapter2;
	private AccountAdapter adapter3;
	private MainActivity act;
	private boolean refreshing;
	private boolean refreshingArchival;
	private MyAccountFragment fragment1;
	private StatsFragment fragment4;
	private CompanyDetailsActivity details;
	
	private GiePPSingleton() {
		try{
			File dir = new File(Environment.getExternalStorageDirectory() + "/giepp");
			if (!dir.exists()) dir.mkdir();
			
			File file1 = new File(Environment.getExternalStorageDirectory() + "/giepp/owned.xml");
			File file2 = new File(Environment.getExternalStorageDirectory() + "/giepp/archived.xml");
			File file3 = new File(Environment.getExternalStorageDirectory() + "/giepp/observed.xml");
			File file4 = new File(Environment.getExternalStorageDirectory() + "/giepp/stats.xml");
			
			if (! file1.exists()) file1.createNewFile();
			if (! file2.exists()) file2.createNewFile();
			if (! file3.exists()) file3.createNewFile();
			if (! file4.exists()) file4.createNewFile();
			
			game = new Game(new GPWDataSource(),new GPWDataParser(),LocalStorage.newInstance(file1, file2, file3, file4));
			this.refreshing = false;
		}catch(IOException e){
			Log.e("giepp","Nie mozna stworzyc plikow");
			System.exit(1);
		}
	}
	
	/** Za pomocą metody {@link Game#refreshCurrent()} pobierane są aktualne dane. Przed
	 * pobraniem wywoływana jest metoda {@link MainActivity#updateProgressBar()}. Po pobraniu wywoływane
	 * są metody: {@link MainActivity#updateProgressBar()}. Wywoływane są też metody 
	 * {@link MyAccountFragment#updateView()}, {@link StatsFragment#updateView()}, {@link AccountAdapter#update(ArrayList)},
	 *{@link AllRecordsAdapter#update(ArrayList)}, {@link ObservedAdapter#update(ArrayList,ArrayList)} oraz 
	 *{@link CompanyDetailsActivity#updateMaxToBuySell()} w celu odświeżenia danych w każdym miejscu 
	 *aplikacji.
	 *   */
	public void refreshCurrent(){
		if (refreshing) {
				return;
		}
		refreshing = true;
		if (act!= null) {
			act.updateProgressBar();
		}

		(new Thread() {
			
			@Override
			public void run(){
				try{
					Log.i("System.out","Zaczynam sciagac");
					game.refreshCurrent();
					Log.i("giepp","Dane aktualne sciagniete");
					act.runOnUiThread(new Runnable(){
						public void run(){
							if (fragment1 != null) {
								fragment1.updateView();
							}
							if (details != null) {
								details.updateMaxToBuySell();
							}
							if (fragment4 != null) {
								fragment4.updateView();
							}
							if(adapter1 != null){		
								adapter1.update(game.getCurrent());
							}
							if(adapter2 != null){
								adapter2.update(game.getCurrent(),game.getObserved());
							}
							if(adapter3 != null){		
								adapter3.update(game.getOwned());
							}
							refreshing = false;
							if (act!= null) {
								act.updateProgressBar();
							}
						}
					});
					Log.i("System.out","aktualne sciagniete");
				}catch(Exception e){
					Log.i("giepp","Blad"+e);
				}
			}
		}).start();
	}
	
	/**Wywołuje metodę {@link Game#maximumToBuy(String)}.
	 * @param companyName nazwa firmy
	 * @return wynik metody {@link Game#maximumToBuy(String)}
	 *   */
	public int getMaximumToBuy(String companyName) {
		return game.maximumToBuy(companyName);
	}
	
	/**Wywołuje metodę {@link Game#minimumToSell(String)}.
	 * @param companyName nazwa firmy
	 * @return wynik metody {@link Game#minimumToSell(String)}
	 *   */
	public int getMinimumToSell(String companyName) {
		return game.minimumToSell(companyName);
	}
	
	/**Wywołuje metodę {@link Game#restartGame()}. Po wykonaniu tej metody wywoływane są
	 * {@link MyAccountFragment#updateView()}, 
	 * {@link MyAccountFragment#updateView()}, {@link StatsFragment#updateView()}, {@link AccountAdapter#update(ArrayList)}
	 * oraz {@link ObservedAdapter#update(ArrayList,ArrayList)} aby odświeżone zostały wszystkie wyświetlane w 
	 * aplikacji informacje dotyczące gry.
	 *   */	
	public void restartGame() {
		game.restartGame();
		if (details != null) {
			details.updateMaxToBuySell();
		}
		if (fragment1 != null) {
			fragment1.updateView();
		}
		if (fragment4 != null) {
			fragment4.updateView();
		}
		if(adapter2 != null){
			adapter2.update(game.getCurrent(),game.getObserved());
		}
		if(adapter3 != null){		
			adapter3.update(game.getOwned());
		}
	}
	
	/** Wywołuje metode {@link Game#buy(String, int)} a następnie {@link MyAccountFragment#updateView()} oraz
	 * {@link AccountAdapter#update(ArrayList)} aby odświeżyć dane wyświetlane w aplikacji.
	 * @param companyName nazwa firmy, której akcje chce się kupić
	 * @param amount liczba akcji jaką chce się kupić
	 *  */
	public void buy(String companyName, int amount) {
		try {
			game.buy(companyName,amount);
			act.runOnUiThread(new Runnable(){
				public void run(){
					if (fragment1 != null) {
						fragment1.updateView();
						Log.i("giepp","moje konto updatowane");
					}
					if (adapter3 != null) {		
						adapter3.update(game.getOwned());
						Log.i("giepp","lista3 updatowana");
					}
				}
			});
		} catch( ActionException e){
			if (e.getReason() != ActionError.TOO_OLD_DATA) {
				return;
			}
			try {
				act.runOnUiThread(new Runnable(){
					public void run(){
						AlertDialog.Builder builder = new AlertDialog.Builder(details);
						builder.setTitle("Nie udało się wykonać działania");
						builder.setMessage("Odśwież aktualne dane.");
						builder.setCancelable(true);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});
			} catch (Exception e2){}
		}
	}
	
	/** Wywołuje metode {@link Game#sell(String, int)} a następnie {@link MyAccountFragment#updateView()} oraz
	 * {@link AccountAdapter#update(ArrayList)} aby odświeżyć dane wyświetlane w aplikacji.
	 * @param companyName nazwa firmy, której akcje chce się sprzedać
	 * @param amount liczba akcji jaką chce się sprzedać
	 *  */
	public void sell(String companyName, int amount){
		try{
			game.sell(companyName,amount);
			act.runOnUiThread(new Runnable(){
				public void run(){
					if (fragment1 != null) {
						fragment1.updateView();
						Log.i("giepp","moje konto updatowane");
					}
					if(adapter3 != null){		
						adapter3.update(game.getOwned());
						Log.i("giepp","lista3 updatowana");
					}
				}
			});
		} catch( ActionException e){
			if (e.getReason() != ActionError.TOO_OLD_DATA) {
				return;
			}
			try {
				act.runOnUiThread(new Runnable(){
					public void run(){
						AlertDialog.Builder builder = new AlertDialog.Builder(details);
						builder.setTitle("Nie udało się wykonać działania");
						builder.setMessage("Odśwież aktualne dane.");
						builder.setCancelable(true);
						AlertDialog dialog = builder.create();
						dialog.show();
					}
				});
			} catch (Exception e2){}
		}
	}
	/** Wywołuje metodę {@link Game#refreshArchival(int, int, int, int, int, int)} a następnie
	 * {@link StatsFragment#updateView()} aby ProgressBar zniknął.
	 * @param bar ProgressBar informujący czy dane archiwalne są w danym momencie aktualizowane
	 * @param d1 najstarszy dzień, z którego dane chcemy otrzymać
	 * @param m1 najstarszy numer miesiąca, z którego dane chcemy otrzymać
	 * @param y1 najstarszy rok, z którego dane chcemy otrzymać
	 * @param d2 najnowszy dzień, z którego dane chcemy otrzymać
	 * @param m2 najnowszy numer miesiąca, z którego dane chcemy otrzymać
	 * @param y2 najnowszy rok, z którego dane chcemy otrzymać
	 * */
	public void refreshArchival(final ProgressBar bar, final int d1, final int m1, final int y1, 
			final int d2, final int m2, final int y2) {
		refreshingArchival = true;
		System.out.println("start: " + d1 + "-" + m1 + "-" + y1);
		System.out.println("end: " + d2 + "-" + m2 + "-" + y2);
		bar.setVisibility(View.VISIBLE);
		(new Thread() {
			public void run() {
				game.refreshArchival(d1,m1+1,y1,d2,m2+1,y2);
				if (fragment4 != null) {
					fragment4.updateView();
				}
				refreshingArchival = false;
				try {
					act.runOnUiThread(new Runnable(){
						public void run(){
							if (bar != null) {
								bar.setVisibility(View.GONE);
							}
						}
					});
				} catch (Exception e2){}
			}
		}).start();
	}
	
	/** Zwraca informację o tym, czy w danym momencie pobierane są dane archiwalne.
	 * @return true jeśli są pobierane, false w przeciwnym wypadku */
	public boolean isRefreshingArchival() {
		return refreshingArchival;
	}

	/** Zwraca informację o tym, czy w danym momencie pobierane są dane aktualne o firmach.
	 * @return true jeśli są pobierane, false w przeciwnym wypadku */
	public boolean isRefreshingCurrent() {
		return refreshing;
	}

	/** Wywołuje metodę {@link Game#getOwned()} i w uzyskanym wyniku
	 * szuka ile akcji podanej firmy posiada gracz.
	 * @param companyName firma, o której ilość posiadanych akcji się pytamy
	 * @return ilosć posiadanych przez gracza akcji podanej firmy 
	 * */
	public int getAmount(String companyName) {
		ArrayList<PlayerStock> owned = game.getOwned();
		for(PlayerStock p : owned ) {
			if (p.getCompanyName().equals(companyName)) {
				return p.getAmount();
			}
		}
		return 0;
	}
	
	/**Wywoływana jest metoda {@link Game#getCurrent()}.
	 * @return wynik metody {@link Game#getCurrent()}*/
	public ArrayList<CurrentStock> getCurrent() {
		return game.getCurrent();
	}
	
	/**Wywoływana jest metoda {@link Game#getCurrent()} a w uzyskanym wyniku 
	 * szukany jest element odpowiadający podanej firmie.
	 * @param name nazwa firmy
	 * @return element z {@link Game#getCurrent()} odpowiadający podanej firmie
	 * */
	public CurrentStock getCurrent(String name) {
		for(CurrentStock cs : game.getCurrent()) {
			if (cs.getName().equals(name)) {
				return cs;
			}
		}
		return null;
	}

	/**Wywoływana jest metoda {@link Game#getMoney()}.
	 * @return wynik metody {@link Game#getMoney()}*/
	public long getMoney() {
		return game.getMoney();
	}
	
	/**Wywoływana jest metoda {@link Game#getMoneyInStock()}.
	 * @return wynik metody {@link Game#getMoneyInStock()}*/
	public long getMoneyInStock() {
		return game.getMoneyInStock();
	}
	
	/**Wywoływana jest metoda {@link Game#getStats()}.
	 * @return wynik metody {@link Game#getStats()}*/
	public Stats getStats() {
		return game.getStats();
	}
	
	/**Wywoływana jest metoda {@link Game#getOwned()}.
	 * @return wynik metody {@link Game#getOwned()}*/
	public ArrayList<PlayerStock> getOwned(){
		return game.getOwned();
	}
	
	/**Wywołuje metodę {@link Game#addToObserved(String)} a następnie {@link Game#getObserved()}, 
	 * {@link Game#getCurrent()}
	 * oraz {@link ObservedAdapter#update(ArrayList, ArrayList)} aby odświeżyć listę obserwownych 
	 * firm w {@link ObservedFragment}.
	 * @param name nazwa firmy, która ma być dodana do obserwowanych*/
	public void addToObserved(String name){
		game.addToObserved(name);
		act.runOnUiThread(new Runnable(){
			public void run(){
				if(adapter2 != null){		
					adapter2.update(game.getCurrent(),game.getObserved());
					Log.i("giepp","lista2 updatowana");
				}
			}
		});
	}
	
	/**Wywołuje metodę {@link Game#removeFromObserved(String)} a następnie {@link Game#getObserved()}, 
	 * {@link Game#getCurrent()}
	 * oraz {@link ObservedAdapter#update(ArrayList, ArrayList)} aby odświeżyć listę obserwownych 
	 * firm w {@link ObservedFragment}.
	 * @param name nazwa firmy, która ma być usunięta z obserwowanych*/
	public void removeFromObserved(String name) {
		game.removeFromObserved(name);
		act.runOnUiThread(new Runnable(){
			public void run(){
				if(adapter2 != null){		
					adapter2.update(game.getCurrent(),game.getObserved());
					Log.i("giepp","lista2 updatowana");
				}
			}
		});
	}
	
	/**Wywołuje metodę {@link Game#getArchived()} i w uzyskanych danych szuka
	 * najnowszej data. 
	 * @return data ostatniej aktualizacji danych archiwalnych w formacie YYYY-MM-DD lub napis "brak"*/
	public String getLastArchivalDate() {
		Set<String> keys = game.getArchived().keySet();
		for(String k: keys){
			if ( game.getArchived().get(k).size() > 0) {
				return game.getArchived().get(k).get(0).getDate();
			}
		}		
		return "brak";
	}
	
	/**Wywołuje metodę {@link Game#getArchived()}.
	 * @param name nazwa firmy, której dane archiwalne chcemy uzyskać
	 * @return zwraca element odwiadający kluczowi name z listy uzyskanej za pomocą{@link Game#getArchived()}*/
	public ArrayList<ArchivedStock> getArchival(String name) {
		return game.getArchived().get(name);
	}

	/**Wywołuje metodę {@link Game#getObserved()}.
	 * @return zwraca wynik metody {@link Game#getObserved()}*/
	public ArrayList<String> getObserved(){
		return game.getObserved();
	}
	
	/** Zwraca nazwę przechowywanej aktualnie w tej klasie firmy
	 * @return nazwa firmy*/
	public String getName(){
		return name;
	}
	
	/** Ustawiana jest wartość pola trzymajaca referencję na {@link CompanyDetailsActivity}
	 * @param details nowa wartość pola*/
	public void setCompanyDetailsActivity(CompanyDetailsActivity details){
		this.details = details;
	}

	/** Ustawiana jest wartość pola trzymajaca referencję na {@link MyAccountFragment}
	 * @param fragment1 nowa wartość pola*/
	public void setFragment1(MyAccountFragment fragment1){
		this.fragment1 = fragment1;
	}

	/** Ustawiana jest wartość pola trzymajaca referencję na {@link StatsFragment}
	 * @param fragment4 nowa wartość pola*/
	public void setFragment4(StatsFragment fragment4){
		this.fragment4 = fragment4;
	}

	/** Ustawiana jest wartość pola trzymajaca referencję na {@link AllRecordsAdapter}
	 * @param adapter1 nowa wartość pola*/
	public void setAdapter1(AllRecordsAdapter adapter1){
		this.adapter1 = adapter1;
	}

	/** Ustawiana jest wartość pola trzymajaca referencję na {@link ObservedAdapter}
	 * @param adapter2 nowa wartość pola*/
	public void setAdapter2(ObservedAdapter adapter2){
		this.adapter2 = adapter2;
	}
	
	/** Ustawiana jest wartość pola trzymajaca referencję na {@link AccountAdapter}
	 * @param adapter3 nowa wartość pola*/
	public void setAdapter3(AccountAdapter adapter3){
		this.adapter3 = adapter3;
	}
	
	/** Ustawiana jest wartość pola trzymajaca referencję na {@link MainActivity}
	 * @param act nowa wartość pola*/
	public void setActivity(MainActivity act){
		this.act = act;
	}
	
	/** Ustawiana jest wartość pola trzymajaca nazwę jednej z firm.
	 * @param name nowa wartość pola*/
	public void setName(String name){
		this.name = name;
	}
}
