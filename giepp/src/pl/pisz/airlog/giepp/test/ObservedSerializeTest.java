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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.xml.ObservedFileBuilder;
import pl.pisz.airlog.giepp.xml.ObservedTransformer;

public class ObservedSerializeTest {
    
    private String[] mObserved;
    
    public ObservedSerializeTest(String... observed) {
        mObserved = observed;
    }
    
    private static String parseString(Document document) throws Exception {
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        StringWriter stringWriter = new StringWriter();
        
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");  // by default 0 but sets new lines
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
        
        return stringWriter.getBuffer().toString();
    }

    private boolean testRead(int i, Node node, ObservedTransformer transformer) {                    
        String company = transformer.transform(node);
        
        return company.equals(mObserved[i]);
    }

    private void testWrite(Document document, ObservedTransformer transformer, String... observed) {
        Element root = document.getDocumentElement();
        for (String company : observed) root.appendChild(transformer.transform(company));  // serializacja
        
        /* sprawdzanie poprawności poprzez deserializację */
        NodeList children = root.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            boolean success = testRead(i, children.item(i), transformer);
            System.out.println(String.format("Parsing... %s", success));
        }
    }

    public void test()
            throws Exception {
        ObservedFileBuilder ofb = new ObservedFileBuilder(
                DocumentBuilderFactory.newInstance().newDocumentBuilder());
        Document xmlDoc = ofb.newDocument();
        ObservedTransformer ot = new ObservedTransformer(xmlDoc);                
                
        this.testWrite(xmlDoc, ot, mObserved);
    }
    
    public static void main(String[] args)
            throws Exception {        
        (new ObservedSerializeTest(
                "SampleCompany0",
                "SampleCompany1",
                "SampleCompany2",
                "SampleCompany3"
                )).test();
    }

}
