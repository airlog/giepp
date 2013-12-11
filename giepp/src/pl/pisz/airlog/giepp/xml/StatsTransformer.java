/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.data.Stats;

/** Serializes {@link Stats} objects to a designed XML file.
 * @see Stats
 */
public class StatsTransformer
        implements Transformer<Stats> {

    private static final String     INVALID_NODE    = "Not a valid PlayerStock's XML form"; 

    /** XML document to which transformed elements will be appended. */
    private Document xmlDocument;

    /** Creates new instance.
     * A valid XML document instance must be provided to be able to append created elements to
     * that particular document.
     *
     * @param   xmlDocument document to which created DOM elements will be appended
     */
    public StatsTransformer(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    
    /** Wrapper method for creating new DOM node. */
    private Element newElement(String tag) {
        return this.xmlDocument.createElement(tag);
    }

    /** Converts {@link Stats} to DOM element allowing XML serialization.
     * @see Transformer#transform(T)
     */
    @Override
    public Element transform(Stats object) {        
        return null;
    }
    
    @Override
    public Stats transform(Node node)
            throws IllegalArgumentException {        
        return null;
    }

}

