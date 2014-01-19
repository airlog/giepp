package pl.pisz.airlog.giepp.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;
import pl.pisz.airlog.giepp.game.Game;
import android.os.Environment;
import android.util.Log;

public class GiePPSingleton{
	
	private static GiePPSingleton instance = null;
	
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
								adapter1.zmiana(game.getCurrent());
							}
							if(adapter2 != null){
								adapter2.zmiana(game.getCurrent(),game.getObserved());
							}
							if(adapter3 != null){		
								adapter3.zmiana(game.getOwned());
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
	
	public int getMaximumToBuy(String companyName) {
		return game.maximumToBuy(companyName);
	}
	public int getMinimumToSell(String companyName) {
		return game.minimumToSell(companyName);
	}
	
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
			adapter2.zmiana(game.getCurrent(),game.getObserved());
		}
		if(adapter3 != null){		
			adapter3.zmiana(game.getOwned());
		}
	}
	
	public void setCompanyDetailsActivity(CompanyDetailsActivity details){
		this.details = details;
	}
	
	public void buy(String companyName, int amount) {
		try {
			game.buy(companyName,amount);
		}catch( Exception e){
			Log.i("giepp","Blad1: " + e);
		}
		try {
			act.runOnUiThread(new Runnable(){
				public void run(){
					if (fragment1 != null) {
						fragment1.updateView();
						Log.i("giepp","moje konto updatowane");
					}
					if (adapter3 != null) {		
						adapter3.zmiana(game.getOwned());
						Log.i("giepp","lista3 updatowana");
					}
				}
			});
		}catch(Exception e){
			Log.i("giepp","Blad2: " + e);
		}
	}
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
						adapter3.zmiana(game.getOwned());
						Log.i("giepp","lista3 updatowana");
					}
				}
			});
		}
		catch(Exception e){
			Log.i("giepp","nie udalo sie: " + e);
		}
	}
	
	public ArrayList<CurrentStock> getCurrent() {
		return game.getCurrent();
	}
	
	public CurrentStock getCurrent(String name) {
		for(CurrentStock cs : game.getCurrent()) {
			if (cs.getName().equals(name)) {
				return cs;
			}
		}
		return null;
	}
	public void refreshArchival(int d1, int m1, int y1, int d2, int m2, int y2) {
		refreshingArchival = true;
		System.out.println("start: " + d1 + "-" + m1 + "-" + y1);
		System.out.println("end: " + d2 + "-" + m2 + "-" + y2);
		game.refreshArchival(d1,m1+1,y1,d2,m2+1,y2);
		refreshingArchival = false;
	}
	
	public boolean isRefreshingArchival() {
		return refreshingArchival;
	}

	public boolean isRefreshingCurrent() {
		return refreshing;
	}

	public int getAmount(String companyName) {
		ArrayList<PlayerStock> owned = game.getOwned();
		for(PlayerStock p : owned ) {
			if (p.getCompanyName().equals(companyName)) {
				return p.getAmount();
			}
		}
		return 0;
	}
	
	public long getMoney() {
		return game.getMoney();
	}
	public long getMoneyInStock() {
		return game.getMoneyInStock();
	}
	
	public Stats getStats() {
		return game.getStats();
	}
	
	public ArrayList<PlayerStock> getOwned(){
		return game.getOwned();
	}
	public void addToObserved(String name){
		game.addToObserved(name);
		act.runOnUiThread(new Runnable(){
			public void run(){
				if(adapter2 != null){		
					adapter2.zmiana(game.getCurrent(),game.getObserved());
					Log.i("giepp","lista2 updatowana");
				}
			}
		});
	}
	public void removeFromObserved(String name) {
		game.removeFromObserved(name);
		act.runOnUiThread(new Runnable(){
			public void run(){
				if(adapter2 != null){		
					adapter2.zmiana(game.getCurrent(),game.getObserved());
					Log.i("giepp","lista2 updatowana");
				}
			}
		});
	}
	public ArrayList<ArchivedStock> getArchival(String name){
		return game.getArchived().get(name);
	}
	public ArrayList<String> getObserved(){
		return game.getObserved();
	}
	public void setFragment1(MyAccountFragment fragment1){
		this.fragment1 = fragment1;
	}

	public void setFragment4(StatsFragment fragment4){
		this.fragment4 = fragment4;
	}

	public void setAdapter1(AllRecordsAdapter adapter1){
		this.adapter1 = adapter1;
	}
	public void setAdapter2(ObservedAdapter adapter2){
		this.adapter2 = adapter2;
	}
	public void setAdapter3(AccountAdapter adapter3){
		this.adapter3 = adapter3;
	}
	public void setActivity(MainActivity act){
		this.act = act;
	}
	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
}