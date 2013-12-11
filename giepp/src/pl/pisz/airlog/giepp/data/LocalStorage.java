package pl.pisz.airlog.giepp.data;

import java.io.*;

import java.util.List;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.TreeMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

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
        Document doc = null;
        try {
            if (!created) doc = LocalStorage.DOCBUILDER.parse(fis);  // FIXME: see issue 'Ugly XML' on GitHub
            doc.normalize();
        } catch (SAXException e) {
            System.out.println(e);
        }
        fis.close();
        
        return new FileHandle(doc, file);
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

    private void assertStocksDocument() {
        if (this.stocksFile.doc == null) {  // document not yet created
	        OwnedStocksBuilder osb = new OwnedStocksBuilder(LocalStorage.DOCBUILDER);
	        this.stocksFile.doc = osb.newDocument();
	        this.stocksFile.doc.normalize();
	    }
    }
    
    private void assertArchiveDocument() {
        if (this.archiveFile.doc == null) {  // document not yet created
	        StocksArchiveBuilder asb = new StocksArchiveBuilder(LocalStorage.DOCBUILDER);
	        this.archiveFile.doc = asb.newDocument();
	        this.archiveFile.doc.normalize();
	    }
    }
    
    private int clearDocument(Document document) {
        Element root = document.getDocumentElement();        
        Node node = root.getFirstChild();
        int removedCounter = 0;
        
        while (node != null) {
            root.removeChild(node);
            removedCounter++;
            
            node = root.getFirstChild();
        }
        
        return removedCounter;
    }

    public ArrayList<PlayerStock> getOwned() {
        this.assertStocksDocument();
	    
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
	    this.assertArchiveDocument();
	    
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
    
    public void saveArchival(TreeMap<String,ArrayList<ArchivedStock>> archived)
            throws IOException {
        this.assertArchiveDocument();
        this.clearDocument(this.archiveFile.doc);
        
        LinkedList<ArchivedStock> stocks = new LinkedList<ArchivedStock>();
        for (ArrayList<ArchivedStock> array : archived.values()) stocks.addAll(array);
        
        Element root = this.archiveFile.doc.getDocumentElement();
        ArchivedStockTransformer ast = new ArchivedStockTransformer(this.archiveFile.doc);
        for (ArchivedStock stock : stocks) {
            Node node = ast.transform(stock);
            root.appendChild(node);
        }
        
        this.archiveFile.save();
    }
    
    public void saveOwned(List<PlayerStock> stocks)
            throws IOException {
        this.assertStocksDocument();
        this.clearDocument(this.stocksFile.doc);
        
        Element root = this.stocksFile.doc.getDocumentElement();
        PlayerStockTransformer pst = new PlayerStockTransformer(this.stocksFile.doc);
        for (PlayerStock stock : stocks) {
            Node node = pst.transform(stock);
            root.appendChild(node);
        }
        
        this.stocksFile.save();
    }

}

class FileHandle {
    
    private     File        file = null;       
    protected   Document    doc = null;
            
    public FileHandle(Document doc, File file) {
        this.file = file;
        this.doc = doc;
    }
                
    protected String convert(Document document) throws TransformerException {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter stringWriter = new StringWriter();
            
        transformer.setOutputProperty(OutputKeys.INDENT, "no");  // by default 0 but sets new lines
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            
        return stringWriter.getBuffer().toString();
    }
                        
    public final void save() throws IOException {
        String doc = null;
        try {
            doc = this.convert(this.doc);
        } catch (TransformerException e) {
            throw new IOException(e.toString());
        }
            
        BufferedWriter dos = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file)));
        dos.write(doc, 0, doc.length());
        dos.close();
    }
        
}

