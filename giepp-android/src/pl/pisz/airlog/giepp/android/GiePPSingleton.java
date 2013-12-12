package pl.pisz.airlog.giepp.android;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;
import pl.pisz.airlog.giepp.game.ActionException;
import pl.pisz.airlog.giepp.game.Game;
import android.app.Activity;
import android.util.Log;

public class GiePPSingleton{
	
	private static GiePPSingleton instance = null;
	
	public static GiePPSingleton getInstance() {
		if (GiePPSingleton.instance == null) GiePPSingleton.instance = new GiePPSingleton();
		return GiePPSingleton.instance;
	}
	
	private int nr;
	private String name;
	private Game game;
	private AllRecordsAdapter adapter1;
	private ObservedAdapter adapter2;
	private AccountAdapter adapter3;
	private Activity act;
	private boolean refreshing;
	
	private GiePPSingleton() {
		try{
			File file1 = File.createTempFile("file1", "xml");
			File file2 = File.createTempFile("file2", "xml");
			File file3 = File.createTempFile("file3", "xml");
			File file4 = File.createTempFile("stats", "xml");
			game = new Game(new GPWDataSource(),new GPWDataParser(),LocalStorage.newInstance(file1, file2, file3, file4));
			this.refreshing = false;
		}catch(IOException e){
			Log.e("giepp","Nie mozna stworzyc plikow");
			System.exit(1);
		}
	}
	public void refresh(){
		if (refreshing) {
				return;
		}
		refreshing = true;
		(new Thread() {
			
			@Override
			public void run(){
				try{
					Log.i("giepp","Zaczynam sciagac");
					game.refreshData();
					Set<String> keys = game.getArchived().keySet();
					for(String k: keys){
						ArrayList<ArchivedStock> l = game.getArchived().get(k);
						for(int i = 0; i<l.size(); i++)
							Log.i("giepp",l.get(i).getName()+": "+l.get(i).getMaxPrice());
					}
					Log.i("giepp","Dane sciagniete");
					
					try{	
						game.buy("ALIOR",10);
					}catch(ActionException e2){
						Log.i("giepp",e2.getReason()+"");
					}
					catch(Exception e3){
						Log.i("giepp","Blad3:" + e3);
					}
					
					act.runOnUiThread(new Runnable(){
						public void run(){
							if(adapter1 != null){		
								adapter1.zmiana(game.getCurrent());
								Log.i("giepp","lista1 updatowana");
							}
							else{
								Log.i("giepp","adapter1 null");
							}
							if(adapter2 != null){
								adapter2.zmiana(game.getCurrent(),game.getObserved());
								Log.i("giepp","lista2 updatowana");
							}
							else{
								Log.i("giepp","adapter2 null");
							}
							if(adapter3 != null){		
								adapter3.zmiana(game.getOwned());
								Log.i("giepp","lista3 updatowana");
							}
							else{
								Log.i("giepp","adapter3 null");
							}
						}
					});
				}catch(Exception e){
					Log.i("giepp","Blad"+e);
				}
				refreshing = false;
			}
		}).start();
	}
	
	public ArrayList<CurrentStock> getCurrent(){
		return game.getCurrent();
	}

	public long getMoney() {
		return game.getMoney();
	}
	public long getMoneyInStock() {
		return game.getMoneyInStock();
	}
	public ArrayList<PlayerStock> getOwned(){
		return game.getOwned();
	}
	public void addToObserved(String name){
		game.addToObserved(name);
	}
	public ArrayList<String> getObserved(){
		return game.getObserved();
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
	public void setActivity(Activity act){
		this.act = act;
	}
	
	public void setNr(int nr){
		this.nr = nr;
	}
	public int getNr(){
		return nr;
	}

	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return name;
	}
}