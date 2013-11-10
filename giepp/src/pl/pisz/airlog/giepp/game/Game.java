package pl.pisz.airlog.giepp.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.BadDate;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.data.DataManager;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;

public class Game {	

	private final long MONEY_ON_START = 1000000;		//10 000 zl
	private final int MAX_TIME = 900000;				//15 minut
	private final int DAYS = 5;

	private DataManager dataManager;
	private Long money;
	private Integer restarts;
	private ArrayList<String> observed;
	private ArrayList<PlayerStock> owned;
	private TreeMap<String, ArrayList<ArchivedStock> > archived;
	private ArrayList<CurrentStock> current;
	private Date lastRefresh;	
	private Calendar calendar;
	
	public Game() {
		dataManager = new DataManager(new GPWDataSource(), new GPWDataParser());
		loadDataFromXML();
	}
	
	public void buy(String company, int amount) throws ActionException {
		calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		if (now.getTime() - lastRefresh.getTime() > MAX_TIME ){
			throw new ActionException(ActionError.TOO_OLD_DATA);
		}
		int price = -1;
		for (int i = 0; i<current.size(); i++) {
			if (current.get(i).getName().equals(company)) {
				price = current.get(i).getEndPrice();
				break;
			}
		}
		if (price == -1) {
			throw new ActionException(ActionError.COMPANY_NOT_FOUND);
		}
		if (price * amount > money) {
			throw new ActionException(ActionError.LACK_OF_MONEY);
		}

		money -= price*amount;
		PlayerStock stock = null;
		for (int i = 0; i<owned.size(); i++) {
			if (owned.get(i).getCompanyName().equals(company)) {
				stock = owned.get(i);
				break;
			}
		}
		if (stock == null) {
			owned.add(new PlayerStock(company,amount,price*amount));
		}
		else {
			stock.setAmount(stock.getAmount()+amount);
			stock.setStartPrice(stock.getStartPrice()+price*amount);
		}
		
		dataManager.saveStats(new Stats(money,restarts));
		dataManager.saveOwned(owned);
	}

	public void sell(String company, int amount) throws ActionException {
		calendar = Calendar.getInstance();
		Date now = calendar.getTime();
		if (now.getTime() - lastRefresh.getTime() > MAX_TIME){
			throw new ActionException(ActionError.TOO_OLD_DATA);
		}
		PlayerStock stock = null;
		for (int i = 0; i<owned.size(); i++) {
			if (owned.get(i).getCompanyName().equals(company)) {
				stock = owned.get(i);
				break;
			}
		}
		if (stock == null || stock.getAmount() < amount) {
			throw new ActionException(ActionError.LACK_OF_STOCK);
		}
		int price = -1;
		for (int i = 0; i<current.size(); i++) {
			if (current.get(i).getName().equals(company)) {
				price = current.get(i).getEndPrice();
				break;
			}
		}
		if (price == -1) {
			throw new ActionException(ActionError.COMPANY_NOT_FOUND);
		}
		money += amount*price;
		if (stock.getAmount() == amount) {
			owned.remove(stock);
		}
		else {
			stock.setAmount(stock.getAmount()-amount);
			stock.setStartPrice(stock.getStartPrice()-price*amount);
		}
		
		dataManager.saveStats(new Stats(money,restarts));
		dataManager.saveOwned(owned);
	}

	private void toMap(ArrayList<ArchivedStock> stock, int index) {
		for (int i = 0 ; i < stock.size(); i++) {
			ArrayList<ArchivedStock> found = archived.get(stock.get(i).getName());
			if (found != null) {
				found.add(index,stock.get(i));
			}
			else {
				ArrayList<ArchivedStock> newStock = new ArrayList<ArchivedStock>();
				archived.put(stock.get(i).getName(), newStock);
				newStock.add(stock.get(i));
			}
		}
	}

	private void downloadArchived(int days){
		int downloaded = 0;
		
		calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int month = calendar.get(Calendar.MONTH)+1;
		int year = calendar.get(Calendar.YEAR);
		int lastDay = 0;
		int lastMonth = 0;
		int lastYear = 0;

		if ( !(archived == null || archived.size()==0) ) { //nie wiem czy drugie potrzebne?
			//z formatu yyyy-mm-dd
			String date = archived.get(archived.firstKey()).get(0).getDate();
			lastDay = Integer.parseInt(date.substring(8,10));
			lastMonth = Integer.parseInt(date.substring(5,7));
			lastYear = Integer.parseInt(date.substring(0,4));
		}
		else {
			archived = new TreeMap<String,ArrayList<ArchivedStock>>();
		}
		
		while (downloaded < days && !(lastYear==year && lastMonth==month && lastDay==day)) {
			try {
				ArrayList<ArchivedStock> stock = dataManager.getArchival(day,month,year);
				if (stock!=null) {
					toMap(stock,downloaded);
					downloaded++;
				}
			} catch (IOException e) {
				//TODO ładne poradzenie sobie z wyjątkiem 
			} catch (BadDate e){
				//TODO ładne poradzenie sobie z wyjątkiem 
			}
			day--;
			if (day == 0) {
				day=31;
				month--;
			}
			if (month == 0) {
				month = 12;
				year--;
			}
		}
		
		saveFirst(3);
		dataManager.saveArchival(archived);		
	}
	
	public void saveFirst(int days){
		Set keys = archived.keySet();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			ArrayList<ArchivedStock> value = (ArrayList<ArchivedStock>) archived.get(key);
			while(value.size() > days){
				value.remove(days);
			}
//			value.removeRange(days,value.size());
		}		
	}
	
	public void refreshData() {

		downloadArchived(DAYS);
		calendar = Calendar.getInstance();

		try{
			current = dataManager.getCurrent();
		}catch (IOException e) {
			//TODO ładne poradzenie sobie z wyjątkiem 
		}
		lastRefresh = calendar.getTime();
	}
	
	/** Ustawia pola zgodnie z danymi z plikow XML  **/
	public void loadDataFromXML() {
		Stats stats = dataManager.getStats();
		money = stats.getMoney();
		restarts = stats.getRestarts();
		
		observed = dataManager.getObserved();
		owned = dataManager.getOwned();
		
		archived = dataManager.getArchivalFromXML();
	}
		
	public void restartGame(){
		this.observed = new ArrayList<String>();
		this.owned = new ArrayList<PlayerStock>();
		this.restarts += 1;
		this.money = MONEY_ON_START;
	}
	public ArrayList<ArchivedStock> getArchived(String name){
		return archived.get(name);
	}
	public ArrayList<PlayerStock> getOwned(){
		return owned;
	}
	public long getMoney(){
		return money;
	}
	public TreeMap<String,ArrayList<ArchivedStock>> getArchived(){
		return archived;
	}
	public ArrayList<CurrentStock> getCurrent(){
		return current;
	}
	public void addToObserved(String name){
		if (observed.indexOf(name) == -1)
			observed.add(name);
	}
	public void removeFromObserved(String name){
		observed.remove(name);
	}
}
