package pl.pisz.airlog.giepp.test;

import java.io.File;
import java.io.IOException;

import java.util.List;

import pl.pisz.airlog.giepp.data.LocalStorage;
import pl.pisz.airlog.giepp.data.PlayerStock;

public class LocalStorageTest {

    public static void main(String args[]) throws Exception {
        LocalStorage storage = LocalStorage.newInstance(new File(args[0]), null, null, null);
        
        List<PlayerStock> stocks = storage.getOwned();
        System.out.println(String.format("Loaded %d owned stock(s):", stocks.size()));
        if (stocks.size() == 0) System.out.println("\tNone");
        for (PlayerStock stock : stocks) System.out.println(String.format("\t%s",
                stock.getCompanyName()));
    }

}

