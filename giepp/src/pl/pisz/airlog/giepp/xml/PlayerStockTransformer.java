/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pl.pisz.airlog.giepp.data.PlayerStock;

/** Serializes {@link PlayerStock} objects to a designed XML file.
 * @see PlayerStock
 */
public class PlayerStockTransformer
        implements Transformer<PlayerStock> {

    private static final Integer    NODE_CHILDREN   = 3;
    private static final String     INVALID_NODE    = "Not a valid PlayerStock's XML form"; 

    /** XML document to which transformed elements will be appended. */
    private Document xmlDocument;

    /** Creates new instance.
     * A valid XML document instance must be provided to be able to append created elements to
     * that particular document.
     *
     * @param   xmlDocument document to which created DOM elements will be appended
     */
    public PlayerStockTransformer(Document xmlDocument) {
        this.xmlDocument = xmlDocument;
    }
    
    /** Wrapper method for creating new DOM node. */
    private Element newElement(String tag) {
        return this.xmlDocument.createElement(tag);
    }

    /** Converts {@link PlayerStock} to DOM element allowing XML serialization.
     * @see Transformer#transform(T)
     */
    @Override
    public Element transform(PlayerStock object) {
        Element xmlObject = this.newElement("playerStock"),
                company = this.newElement("company"),
                amount = this.newElement("amount"),
                startPrice = this.newElement("startPrice");
        
        company.setTextContent( object.getCompanyName() );
        amount.setTextContent( object.getAmount().toString() );
        startPrice.setTextContent( object.getStartPrice().toString() );
        
        xmlObject.appendChild(company);
        xmlObject.appendChild(amount);
        xmlObject.appendChild(startPrice);
        
        return xmlObject;
    }
    
    @Override
    public PlayerStock transform(Element node)
            throws IllegalArgumentException {
        NodeList children = node.getChildNodes();
        if (children.getLength() != PlayerStockTransformer.NODE_CHILDREN) {
            throw new IllegalArgumentException(PlayerStockTransformer.INVALID_NODE
                    + " (invalid children quantity)");
        }
        
        Node companyNd = children.item(0), amountNd = children.item(1),
                startPriceNd = children.item(2);
        String  company = companyNd.getTextContent();
        Integer amount, startPrice;
        
        try {
            amount = Integer.parseInt(amountNd.getTextContent());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(PlayerStockTransformer.INVALID_NODE
                    + " (bad amount value)");
        }
        
        try {
            startPrice = Integer.parseInt(startPriceNd.getTextContent());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(PlayerStockTransformer.INVALID_NODE
                    + " (bad startPrice value)");
        }
        
        return new PlayerStock(company, amount, startPrice);
    }

}

