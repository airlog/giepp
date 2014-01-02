package pl.pisz.airlog.giepp.game;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.BadDate;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.DataManager;
import pl.pisz.airlog.giepp.data.DataParser;
import pl.pisz.airlog.giepp.data.DataSource;
import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.data.gpw.GPWDataParser;
import pl.pisz.airlog.giepp.data.gpw.GPWDataSource;

public class Game {	

	private static final int MAX_TIME = 900000;				//15 minut
	private static final int DAYS = 5;
	private static final int MAX_DAYS_IN_MEMORY = 30;
	
	public static final long MONEY_ON_START = 1000000;		//10 000 zl
	
	private Long money;
	private Integer restarts;
	
	private DataManager dataManager;
	private ArrayList<String> observed;
	private ArrayList<PlayerStock> owned;
	private TreeMap<String, ArrayList<ArchivedStock> > archived;
	private ArrayList<CurrentStock> current;
	
	private Date lastRefresh;	
	private Calendar calendar;
		
	public Game(DataSource dataSource, DataParser dataParser, LocalStorage localStorage) {
		this.dataManager = new DataManager(dataSource, dataParser, localStorage);
		this.current = new ArrayList<CurrentStock>();
		this.lastRefresh = new Date(0);
		
		this.loadDataFromXML();
	}
	
	/** Sprawdzenie czy czas od ostaniej aktualizacji danych jest dozwolony.
	 * @return <i>true</i> jeśli dane są poprawne (ważne)
	 */
	protected boolean isDataValid() {
	    calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        
        return !(now.getTime() - lastRefresh.getTime() > MAX_TIME);
	}

	/** Wyszukuje podanej firmy w posiadanych akcjach.
	 * 
	 * @param name nazwa akcji szukanej firmy
	 * @return obiekt {@link PlayerStock} lub <i>null</i> jeśli nie znaleziono
	 */
	public PlayerStock getOwnedStockByName(String name) {
	    for (PlayerStock stock : this.owned) {
            if (stock.getCompanyName().equals(name)) return stock;
        }
	    
	    return null;
	}
	
	public void buy(String company, int amount) throws ActionException {
		if (amount < 0) throw new ActionException(ActionError.NEGATIVE_AMOUNT);
	    if (!this.isDataValid()) throw new ActionException(ActionError.TOO_OLD_DATA);
		
		/* wyszukanie aktualnej ceny za jeden pakiet akcji */
		int price = -1;
		for (int i = 0; i < current.size(); i++) {
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

		/* aktualizacja danych gracza */
		money -= price*amount;    // nowy stan gotówki
		
		// uaktualnienie lub dodanie kupionych pakietów
		PlayerStock stock = this.getOwnedStockByName(company);
		if (stock == null) {
			owned.add(new PlayerStock(company, amount, price*amount));
		}
		else {
			stock.setAmount(stock.getAmount()+amount);
			stock.setStartPrice(stock.getStartPrice()+price*amount);
		}
		
		/* zapisanie danych na dysku */
		try {
		    dataManager.saveStats(new Stats(money,restarts));
		    dataManager.saveOwned(owned);
	    } catch (IOException e) {
	        // TODO: uwaga na błąd
	    	System.out.println(e);
	    }
	}

	public void sell(String company, int amount) throws ActionException {
	    if (amount < 0) throw new ActionException(ActionError.NEGATIVE_AMOUNT);
	    if (!this.isDataValid()) throw new ActionException(ActionError.TOO_OLD_DATA);
		
	    /* sprawdzenie ilości posiadanych akcji */
		PlayerStock stock = this.getOwnedStockByName(company);
		if (stock == null || stock.getAmount() < amount) {
			throw new ActionException(ActionError.LACK_OF_STOCK);
		}
		
		/* wyszukanie aktualnej ceny za jeden pakiet akcji */
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
		
		/* aktualizacja danych */
		money += amount*price;
		
		if (stock.getAmount() == amount) {
			owned.remove(stock);
		}
		else {
			stock.setAmount(stock.getAmount()-amount);
			stock.setStartPrice(stock.getStartPrice()-price*amount);
		}
		
		/* zapisanie danych na dysku */
		try {
		    dataManager.saveStats(new Stats(money,restarts));
		    dataManager.saveOwned(owned);
	    } catch (IOException e) {
	        // TODO: uwaga na błąd
	    }
	}

	private void toMap(ArrayList<ArchivedStock> stock, TreeMap<String,Integer> downloaded_days) {
		for (int i = 0 ; i < stock.size(); i++) {
			ArrayList<ArchivedStock> found = archived.get(stock.get(i).getName());
			if (found != null) {
				int days = downloaded_days.get(stock.get(i).getName());
				found.add(days,stock.get(i));
				downloaded_days.put(stock.get(i).getName(), days+1);
			}
			else {
				downloaded_days.put(stock.get(i).getName(), 1);
				ArrayList<ArchivedStock> newStock = new ArrayList<ArchivedStock>();
				archived.put(stock.get(i).getName(), newStock);
				newStock.add(stock.get(i));
			}
		}
	}

	private void downloadArchived(int days){
		int downloaded = 0;
		int max = days*3;
		int attempt = 0;
		TreeMap<String,Integer> downloaded_days = new TreeMap<String,Integer>();
		
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
					toMap(stock,downloaded_days);
					downloaded++;
				}
			} catch (IOException e) {
				return;
				//TODO ładne poradzenie sobie z wyjątkiem 
			} catch (BadDate e){
				//TODO ładne poradzenie sobie z wyjątkiem 
			}
			attempt++;
			if (attempt == max) break; //aby funkcja sie zawsze skonczyla
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
		
		saveFirst(MAX_DAYS_IN_MEMORY);
		try {
		    dataManager.saveArchival(archived);		
	    } catch (IOException e) {
	        // FIXME: uwaga na błąd
	    	System.out.println(e);
	    }
	}
	
	public void saveFirst(int days){
		Set keys = archived.keySet();
		for (Iterator i = keys.iterator(); i.hasNext();) {
			String key = (String) i.next();
			ArrayList<ArchivedStock> value = (ArrayList<ArchivedStock>) archived.get(key);
			while(value.size() > days){
				value.remove(days);
			}
		}		
	}
	
	public void refreshArchival() {
		this.downloadArchived(DAYS);
	}
	
	public void refreshArchival(int days) {
		this.downloadArchived(days);
	}
	
	public void refreshCurrent(){
		this.calendar = Calendar.getInstance();
		try{
			this.current = dataManager.getCurrent();
		}catch (IOException e) {
			//TODO ładne poradzenie sobie z wyjątkiem 
		}
		this.lastRefresh = calendar.getTime();		
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
		if (observed.indexOf(name) == -1) observed.add(name);
	}
	
	public void removeFromObserved(String name){
		observed.remove(name);
	}
	
	public ArrayList<String> getObserved(){
		return observed;
	}
	
	public long getMoneyInStock() {
		long sum = 0;
		for (int i = 0; i < owned.size(); i++) {
			for (int j = 0; j < current.size(); j++) {
				if (owned.get(i).getCompanyName().equals(current.get(j).getName())) {
					sum += owned.get(i).getAmount() * current.get(j).getEndPrice();
				}
			}
		}
		return sum;
	}
	
	public long getEndPrice(String companyName) {
		int price = 0;

		for (int i = 0; i < current.size(); i++) {
			if ( companyName.equals(current.get(i).getName()) ) {
				price = current.get(i).getEndPrice();
				break;
			}
		}
		return price;
	}

	/** do testow **/
	public static void main(String args[]) throws Exception{
		System.out.println(System.getProperty("http.agent"));
		File ownedStocks = File.createTempFile("owned", ".xml");
		File archiveStocks = File.createTempFile("archived", ".xml");
		File observedStocks = File.createTempFile("observed", ".xml");
		File stats = File.createTempFile("stats", ".xml");
		Game g = new Game(new GPWDataSource(),
				new GPWDataParser(),LocalStorage.newInstance(ownedStocks, archiveStocks, observedStocks, stats));
		g.refreshArchival(4);
		ArrayList<ArchivedStock> hist = g.getArchived().get("ZYWIEC");
		for (ArchivedStock h : hist) {
			System.out.println(h.getDate() + ": " + h.getMaxPrice());
		}
/*		Set<String> keys = g.getArchived().keySet();
		
		for(String k: keys){
			ArrayList<ArchivedStock> l = g.getArchived().get(k);
			
			for(int i = 0; i<l.size(); i++)
				System.out.println(k+": "+l.get(i).getDate()+": "+l.get(i).getMaxPrice());
		}
		g.refreshCurrent();
		ArrayList<CurrentStock> cs= g.getCurrent();
		for (CurrentStock c : cs)
			System.out.println(c.getName()+": "+c.getEndPrice());
	*/		
	}

}
