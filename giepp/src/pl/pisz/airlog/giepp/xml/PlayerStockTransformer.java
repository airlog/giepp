/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import org.w3c.dom.Element;
import org.w3c.dom.Document;

import pl.pisz.airlog.giepp.game.PlayerStock;

/** Serializes {@link PlayerStock} objects to a designed XML file.
 * @see PlayerStock
 */
public class PlayerStockTransformer
        implements Transformer<PlayerStock> {

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
        amount.setTextContent( Integer.toString(object.getAmount()) );
        startPrice.setTextContent( Integer.toString(object.getStartPrice()) );
        
        xmlObject.appendChild(company);
        xmlObject.appendChild(amount);
        xmlObject.appendChild(startPrice);
        
        return xmlObject;
    }

}

