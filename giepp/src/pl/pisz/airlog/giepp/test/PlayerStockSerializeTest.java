/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.test;

import java.util.List;
import java.util.LinkedList;

import java.io.FileOutputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
 
import pl.pisz.airlog.giepp.game.PlayerStock;

import pl.pisz.airlog.giepp.xml.PlayerStockTransformer;
import pl.pisz.airlog.giepp.xml.OwnedStocksBuilder;

public class PlayerStockSerializeTest {

    private static String parseString(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter stringWriter = new StringWriter();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // by default 0 but sets new lines
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        
        return stringWriter.getBuffer().toString();
    }

    private static boolean testRead(Element node, PlayerStockTransformer transformer,
            String expectedCompany, Integer expectedAmount, Integer expectedStartPrice) {
        PlayerStock stock = null;
        try {
            stock = transformer.transform(node);
        } catch (IllegalArgumentException e) {
            System.out.println(e);
        }
        
        return stock.getCompanyName().equals(expectedCompany) && stock.getAmount().equals(expectedAmount)
                && stock.getStartPrice().equals(expectedStartPrice);
    }

    private static void testWrite(Document document, List<PlayerStock> stocks,
            PlayerStockTransformer transformer) {
        Element root = document.getDocumentElement();
        for (PlayerStock stock : stocks) root.appendChild(transformer.transform(stock));  // serializacja
        
        /* sprawdzanie poprawności poprzez deserializację */
        NodeList nodes = root.getChildNodes();
        for (int i = 0; i < stocks.size(); i++) {
            PlayerStock stock = stocks.get(i);
            boolean success = testRead((Element) nodes.item(i), transformer, stock.getCompanyName(),
                    stock.getAmount(), stock.getStartPrice());
            
            System.out.println(String.format("Parsing %d... %s", i, success));
        }
    }

    public static void main(String[] args)
            throws Exception {
        OwnedStocksBuilder osb = new OwnedStocksBuilder(
                DocumentBuilderFactory.newInstance().newDocumentBuilder());
        Document xmlDoc = osb.newDocument();
        PlayerStockTransformer pst = new PlayerStockTransformer(xmlDoc);                
        List<PlayerStock> stocks = new LinkedList<PlayerStock>();
        
        stocks.add(new PlayerStock("SampleCompany", 666, 12300));
        stocks.add(new PlayerStock("SamplierCompany", 12, 2141));
                    
        testWrite(xmlDoc, stocks, pst);
    
        String doc = parseString(xmlDoc);
        System.err.println("Serialized:\n" + doc);
    }

}

