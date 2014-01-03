/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.data.Stats;

/** Serializes {@link String} objects, associated with observed stocks, to a designed XML file.
 * @see Stats
 */
public class ObservedTransformer
        implements Transformer<String> {

    private static final String INVALID_NODE    = "Not a valid PlayerStock's XML form"; 

    /** XML document to which transformed elements will be appended. */
    private Document xmlDocument;

    /** Creates new instance.
     * A valid XML document instance must be provided to be able to append created elements to
     * that particular document.
     *
     * @param   xmlDocument document to which created DOM elements will be appended
     */
    public ObservedTransformer(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    
    /** Wrapper method for creating new DOM node. */
    private Element newElement(String tag) {
        return this.xmlDocument.createElement(tag);
    }
    
    /** Converts {@link String} to DOM element allowing XML serialization.
     * @see Transformer#transform(T)
     */
    @Override
    public Element transform(String object) {        
        Element node = this.newElement("company");
        
        node.setTextContent(object);
                
        return node;
    }
    
    @Override
    public String transform(Node node)
            throws IllegalArgumentException {
        if (!node.getNodeName().equals("company")) throw new IllegalArgumentException("invalid XML node");
        return node.getTextContent();
    }

}

