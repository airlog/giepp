package pl.pisz.airlog.giepp.data;

import java.io.*;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.SAXException;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.xml.OwnedStocksBuilder;
import pl.pisz.airlog.giepp.xml.PlayerStockTransformer;
import pl.pisz.airlog.giepp.xml.StocksArchiveBuilder;

public class LocalStorage {

    private static class FileHandle {
    
        private Document            doc = null;
        private DataInputStream     dis = null;
        private DataOutputStream    dos = null;
            
        public FileHandle(Document doc, FileInputStream fis, FileOutputStream fos) {
            this.doc = doc;
            this.dis = new DataInputStream(new BufferedInputStream(fis));
            this.dos = new DataOutputStream(new BufferedOutputStream(fos));
        }
        
        @Override
        protected final void finalize() throws Throwable {
            this.dis.close();
            this.dos.close();
        }
                
    }
    
    private static DocumentBuilder DOCBUILDER = null;
    
    // FIXME: this method is just ugly
    private static FileHandle getFileHandle(File file)
            throws IOException {        
        boolean created = false;
        if (!file.exists()) {
            file.createNewFile();
            created = true;
        }
        
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file);
        Document doc = null;
        try {
            if (!created) doc = LocalStorage.DOCBUILDER.parse(fis);
        } catch (SAXException e) {
            System.out.println(e);
        }
        
        return new FileHandle(doc, fis, fos);
    }
    
    private static void initBuilder(DocumentBuilderFactory dbf) throws IOException {
        try { 
            LocalStorage.DOCBUILDER = dbf.newDocumentBuilder();
        } catch (Exception e) { throw new IOException(); }
    }
    
    private static boolean isInited() {
        return LocalStorage.DOCBUILDER != null;
    }
    
    public static final LocalStorage newInstance(File ownedStocks, File archiveStocks,
            File observedStocks, File stats)
            throws IOException {        
        if (!LocalStorage.isInited()) LocalStorage.initBuilder(DocumentBuilderFactory.newInstance());        
        FileHandle handle = LocalStorage.getFileHandle(ownedStocks);
        
        return new LocalStorage(null, null, null, null);
    }

    private FileHandle  stocksFile    = null;
    private FileHandle  archiveFile   = null;
    private FileHandle  observedFile  = null;
    private FileHandle  statsFile     = null;
    
    private LocalStorage(FileHandle owned, FileHandle archive, FileHandle observed, FileHandle stats) {
        this.stocksFile = owned;
        this.archiveFile = archive;
        this.observedFile = observed;
        this.statsFile = stats;
    }

    public ArrayList<PlayerStock> getOwned(){		
	    if (this.stocksFile.doc == null) {
	        OwnedStocksBuilder osb = new OwnedStocksBuilder(LocalStorage.DOCBUILDER);
	        this.stocksFile.doc = osb.newDocument();
	    }
	    
	    ArrayList<PlayerStock> stocks = new ArrayList<PlayerStock>();
	    NodeList children = this.stocksFile.doc.getDocumentElement().getChildNodes();
	    if (children.getLength() > 0) {
	        PlayerStockTransformer pst = new PlayerStockTransformer(this.stocksFile.doc);
	        for (int i = 0; i < children.getLength(); i++) {
	            try {
	                PlayerStock stock = pst.transform((Element) children.item(i));
	                stocks.add(stock);
	            } catch (IllegalArgumentException e) {
	                System.err.println(e);
	            }
	        }
	    }
	    
	    return stocks;
	}

}

