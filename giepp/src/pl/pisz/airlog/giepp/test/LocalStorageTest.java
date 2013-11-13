package pl.pisz.airlog.giepp.test;

import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.TreeMap;

import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.PlayerStock;
import pl.pisz.airlog.giepp.data.ArchivedStock;

public class LocalStorageTest {

    private static void printOwned(LocalStorage storage) {
        List<PlayerStock> stocks = storage.getOwned();
        System.out.println(String.format("\nLoaded %d owned stock(s):", stocks.size()));
        if (stocks.size() == 0) System.out.println("\tNone");
        for (PlayerStock stock : stocks) System.out.println(String.format("\t%s", stock.getCompanyName()));
    }

    private static void printArchive(LocalStorage storage) {
        TreeMap<String, ArrayList<ArchivedStock>> map = storage.getArchivalFromXML();
        
        System.out.println(String.format("\nLoaded %d company(ies) from archive:", map.size()));
        for (String key : map.keySet()) {
            List<ArchivedStock> stocks = map.get(key);
            
            System.out.println(String.format("\t%s:", key));
            for (ArchivedStock stock : stocks) System.out.println(String.format("\t\t%s\t%s\t%d\t%d",
                    stock.getName(), stock.getDate(), stock.getMinPrice(), stock.getMaxPrice()));
        }
    }
    
    private static void changeOwned(LocalStorage storage, List<PlayerStock> stocks)
            throws IOException {
        storage.saveOwned(stocks);
    }

    public static void main(String args[]) throws Exception {
        LocalStorage storage = LocalStorage.newInstance(new File(args[0]), new File(args[1]), null, null);
        
        LocalStorageTest.printOwned(storage);
        LocalStorageTest.printArchive(storage);
        
        List<PlayerStock> owned = new LinkedList<PlayerStock>();
        owned.add(new PlayerStock("ChangedCompany", 12, 25));
        
        LocalStorageTest.changeOwned(storage, owned);
        
        LocalStorageTest.printOwned(storage);
    }

}

