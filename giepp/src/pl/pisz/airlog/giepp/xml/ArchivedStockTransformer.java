/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.data.ArchivedStock;

/** Serializes {@link ArchivedStock} objects to a designed XML file.
 * @see ArchivedStock
 */
public class ArchivedStockTransformer
        implements Transformer<ArchivedStock> {

    private static final Integer    NODE_CHILDREN   = 4;
    private static final String     INVALID_NODE    = "Not a valid ArchivedStock's XML form"; 

    /** XML document to which transformed elements will be appended. */
    private Document xmlDocument;

    /** Creates new instance.
     * A valid XML document instance must be provided to be able to append created elements to
     * that particular document.
     *
     * @param   xmlDocument document to which created DOM elements will be appended
     */
    public ArchivedStockTransformer(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    
    /** Wrapper method for creating new DOM node. */
    private Element newElement(String tag) {
        return this.xmlDocument.createElement(tag);
    }

    /** Converts {@link ArchivedStock} to DOM element allowing XML serialization.
     * @see Transformer#transform(T)
     */
    @Override
    public Element transform(ArchivedStock object) {
        Element xmlObject = this.newElement("archivedStock"),
                name = this.newElement("name"),
                date = this.newElement("date"),
                minPrice = this.newElement("minPrice"),
                maxPrice = this.newElement("maxPrice");
        
        name.setTextContent( object.getName() );
        date.setTextContent( object.getDate() );
        minPrice.setTextContent( object.getMinPrice().toString() );
        maxPrice.setTextContent( object.getMaxPrice().toString() );
        
        xmlObject.appendChild(name);
        xmlObject.appendChild(date);
        xmlObject.appendChild(minPrice);
        xmlObject.appendChild(maxPrice);
        
        return xmlObject;
    }
    
    /** Converts {@link ArchivedStock} DOM Element to a class.
     * @see Transformer#transform(Element)
     */
    @Override
    public ArchivedStock transform(Element node)
            throws IllegalArgumentException {
        NodeList children = node.getChildNodes();
        if (children.getLength() != ArchivedStockTransformer.NODE_CHILDREN) {
            throw new IllegalArgumentException(ArchivedStockTransformer.INVALID_NODE
                    + " (invalid children quantity)");
        }
        
        Node nameNd = children.item(0), dateNd = children.item(1),
                minPriceNd = children.item(2), maxPriceNd = children.item(3);
        String  name = nameNd.getTextContent(), date = dateNd.getTextContent();
        Integer minPrice, maxPrice;
        
        try {
            minPrice = Integer.parseInt(minPriceNd.getTextContent());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ArchivedStockTransformer.INVALID_NODE
                    + " (bad minPrice value)");
        }
        
        try {
            maxPrice = Integer.parseInt(maxPriceNd.getTextContent());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ArchivedStockTransformer.INVALID_NODE
                    + " (bad maxPrice value)");
        }
        
        ArchivedStock as = new ArchivedStock(name, minPrice, maxPrice);
        as.setDate(date);
        return as;
    }

}

