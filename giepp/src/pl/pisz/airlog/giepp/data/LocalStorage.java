package pl.pisz.airlog.giepp.data;

import java.io.*;

import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.xml.sax.SAXException;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.xml.OwnedStocksBuilder;
import pl.pisz.airlog.giepp.xml.PlayerStockTransformer;
import pl.pisz.airlog.giepp.xml.StocksArchiveBuilder;
import pl.pisz.airlog.giepp.xml.ArchivedStockTransformer;

public class LocalStorage {

    private static class FileHandle {
    
        protected Document            doc = null;
        protected DataInputStream     dis = null;
        protected DataOutputStream    dos = null;
            
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
            System.err.println("Nie istnieje!");
            file.createNewFile();
            created = true;
        }
        
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file, true);
        Document doc = null;
        try {
            if (!created) doc = LocalStorage.DOCBUILDER.parse(fis);  // FIXME: see issue 'Ugly XML' on GitHub
            doc.normalize();
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
        FileHandle oh = LocalStorage.getFileHandle(ownedStocks);
        FileHandle ah = LocalStorage.getFileHandle(archiveStocks);
        
        return new LocalStorage(oh, ah, null, null);  // TODO: change this nulls
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
        if (this.stocksFile.doc == null) {  // document not yet created
	        OwnedStocksBuilder osb = new OwnedStocksBuilder(LocalStorage.DOCBUILDER);
	        this.stocksFile.doc = osb.newDocument();
	        this.stocksFile.doc.normalize();
	    }
	    
	    ArrayList<PlayerStock> stocks = new ArrayList<PlayerStock>();
	    NodeList children = this.stocksFile.doc.getDocumentElement().getElementsByTagName("playerStock");  // TODO: remove magic string
	    if (children.getLength() > 0) {  // if any nodes
	        PlayerStockTransformer pst = new PlayerStockTransformer(this.stocksFile.doc);
	        for (int i = 0; i < children.getLength(); i++) {
	            try {
	                PlayerStock stock = pst.transform((Node) children.item(i));
	                stocks.add(stock);
	            } catch (IllegalArgumentException e) {  // if sth went wrong
	                System.err.println(e);
	            }
	        }
	    }
	    
	    return stocks;
	}

    public TreeMap<String, ArrayList<ArchivedStock>> getArchivalFromXML() {
        if (this.archiveFile.doc == null) {  // document not yet created
	        StocksArchiveBuilder asb = new StocksArchiveBuilder(LocalStorage.DOCBUILDER);
	        this.archiveFile.doc = asb.newDocument();
	        this.archiveFile.doc.normalize();
	    }
	    
	    TreeMap<String, ArrayList<ArchivedStock>> map = new TreeMap<String, ArrayList<ArchivedStock>>();
	    NodeList children = this.archiveFile.doc.getDocumentElement().getElementsByTagName("archivedStock");  // TODO: remove magic string
	    if (children.getLength() > 0) {  // if any nodes
	        ArchivedStockTransformer ast = new ArchivedStockTransformer(this.archiveFile.doc);
	        for (int i = 0; i < children.getLength(); i++) {
	            try {
	                ArchivedStock stock = ast.transform(children.item(i));
	                ArrayList<ArchivedStock> list = map.get(stock.getName());
	                if (list == null) {  // no such key in the map
	                    list = new ArrayList<ArchivedStock>();
	                    map.put(stock.getName(), list);
	                }
	                list.add(stock);
	            } catch (IllegalArgumentException e) {  // if sth went wrong
	                System.err.println(e);
	            }
	        }
	    }
	    
	    return map;
    }

}

