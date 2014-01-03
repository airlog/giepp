package pl.pisz.airlog.giepp.test;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import pl.pisz.airlog.giepp.data.Stats;
import pl.pisz.airlog.giepp.xml.StatsFileBuilder;
import pl.pisz.airlog.giepp.xml.StatsTransformer;

public class StatsSerializeTest {

    private Long mMoney;
    private Integer mRestarts;
    private Long mMaxMoney;
	private Long mMinMoney;
    
    public StatsSerializeTest(long money, int restarts, long maxMoney, long minMoney) {
        mMoney = money;
        mRestarts = restarts;
        mMaxMoney = maxMoney;
        mMinMoney = minMoney;
    }
    
    private static String parseString(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter stringWriter = new StringWriter();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // by default 0 but sets new lines
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        
        return stringWriter.getBuffer().toString();
    }

    private boolean testRead(Element node, StatsTransformer transformer) {                    
        Stats stat = transformer.transform(node);
        
        return stat.getMoney().equals(mMoney) && stat.getRestarts().equals(mRestarts)
        		&& stat.getMaxMoney().equals(mMaxMoney) && stat.getMinMoney().equals(mMinMoney);
    }

    private void testWrite(Document document, Stats stat, StatsTransformer transformer) {
        Element root = document.getDocumentElement();
        root.appendChild(transformer.transform(stat));  // serializacja
        
        /* sprawdzanie poprawności poprzez deserializację */
        boolean success = testRead(root, transformer);
        System.out.println(String.format("Parsing... %s", success));
    }

    public void test()
            throws Exception {
        StatsFileBuilder sfb = new StatsFileBuilder(
                DocumentBuilderFactory.newInstance().newDocumentBuilder());
        Document xmlDoc = sfb.newDocument();
        StatsTransformer st = new StatsTransformer(xmlDoc);                
        
        Stats stat = new Stats();
        
        stat
        .setMoney(mMoney)
        .setRestarts(mRestarts)
        .setMaxMoney(mMaxMoney)
        .setMinMoney(mMinMoney);
        
        this.testWrite(xmlDoc, stat, st);
    }
    
    public static void main(String[] args)
            throws Exception {        
        (new StatsSerializeTest(123456, 12, 5030455, 999)).test();
    }

}
