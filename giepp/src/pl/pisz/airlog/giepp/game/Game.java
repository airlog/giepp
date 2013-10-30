package pl.pisz.airlog.giepp.game;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.util.HashMap;

import pl.pisz.airlog.giepp.data.ArchivedStock;
import pl.pisz.airlog.giepp.data.CurrentStock;
import pl.pisz.airlog.giepp.data.DataManager;

public class Game {	

	private final long MONEY_ON_START = 1000000;		//10 000 zl
	private final int MAX_TIME = 900000;				//15 minut

	private DataManager dataManager;
	private Long money;
	private Integer restarts;
	private ArrayList<String> observed;
	private ArrayList<PlayerStock> owned;
	private HashMap<String, ArrayList<ArchivedStock> > archived;
	private ArrayList<CurrentStock> current;
	private Date lastRefresh;	
	private Calendar calendar;
	
	public Game() {
		dataManager = new DataManager();
		calendar = Calendar.getInstance();
	}
	
	public void buy(String company, int amount) throws ActionException {
		Date now = calendar.getTime();
		if (now.getTime() - lastRefresh.getTime() < MAX_TIME ){
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

	}

	public void sell(String company, int amount) throws ActionException {
		Date now = calendar.getTime();
		if (now.getTime() - lastRefresh.getTime() < MAX_TIME){
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
	}

	public void refreshData() {
		//TODO sciagniecie wszystkich potrzebnych archiwalnych danych
		try{
			current = dataManager.getCurrent();
			lastRefresh = calendar.getTime();
		}catch (IOException e) {
			//TODO ładne poradzenie sobie z wyjątkiem 
			//brak połączenia? strona gpw nie działa?
		}
	}
	
	/** Ustawia pola zgodnie z danymi z plikow XML  **/
	public void loadData() {
	}
	
	/** Zapisuje wszystkie dane do plików XML **/
	public void saveAllData() {
	}
	
	public void restartGame(){
		this.observed = new ArrayList<String>();
		this.owned = new ArrayList<PlayerStock>();
		this.restarts += 1;
		this.money = MONEY_ON_START;
		saveAllData();
	}
}
