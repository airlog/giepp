/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.test;

import java.util.List;
import java.util.LinkedList;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
 
import pl.pisz.airlog.giepp.data.ArchivedStock;

import pl.pisz.airlog.giepp.xml.ArchivedStockTransformer;
import pl.pisz.airlog.giepp.xml.StocksArchiveBuilder;

public class ArchivedStockSerializeTest {

    private static String parseString(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter stringWriter = new StringWriter();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // by default 0 but sets new lines
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        
        return stringWriter.getBuffer().toString();
    }

    private static boolean testRead(Element node, ArchivedStockTransformer transformer,
            String expectedName, Integer expectedMinPrice, Integer expectedMaxPrice) {                    
        ArchivedStock stock = null;
        try {
            stock = transformer.transform(node);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        
        return stock.getName().equals(expectedName) && stock.getMinPrice().equals(expectedMinPrice)
                && stock.getMaxPrice().equals(expectedMaxPrice);
    }

    private static void testWrite(Document document, List<ArchivedStock> stocks,
            ArchivedStockTransformer transformer) {
        Element root = document.getDocumentElement();
        for (ArchivedStock stock : stocks) root.appendChild(transformer.transform(stock));  // serializacja
        
        /* sprawdzanie poprawności poprzez deserializację */
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < stocks.size(); i++) {
            ArchivedStock stock = stocks.get(i);
            boolean success = testRead((Element) nodes.item(i), transformer, stock.getName(),
                    stock.getMinPrice(), stock.getMaxPrice());
            
            System.out.println(String.format("Parsing %d... %s", i, success));
        }
    }

    public static void main(String[] args)
            throws Exception {
        StocksArchiveBuilder sab = new StocksArchiveBuilder(
                DocumentBuilderFactory.newInstance().newDocumentBuilder());
        Document xmlDoc = sab.newDocument();
        ArchivedStockTransformer ast = new ArchivedStockTransformer(xmlDoc);                
        List<ArchivedStock> stocks = new LinkedList<ArchivedStock>();
        
        stocks.add(new ArchivedStock("SampleCompany", 666, 12300));
        stocks.add(new ArchivedStock("SamplierCompany", 12, 2141));
                    
        testWrite(xmlDoc, stocks, ast);
    
        String doc = parseString(xmlDoc);
        System.err.println("Serialized:\n" + doc);
    }

}

