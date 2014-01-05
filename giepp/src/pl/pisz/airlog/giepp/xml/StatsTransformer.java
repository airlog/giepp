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

    protected void parseChild(Node child, Stats stats)
            throws IllegalArgumentException {
        if (child.getNodeName().equals("money")) {
            try {
                long money = Long.parseLong(child.getTextContent());
                stats.setMoney(money);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(StatsTransformer.INVALID_NODE + " (bad money format)");
            }
        }
        else if (child.getNodeName().equals("restarts")) {
            try {
                int num = Integer.parseInt(child.getTextContent());
                stats.setRestarts(num);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(StatsTransformer.INVALID_NODE + " (bad restarts format)");
            } 
        }
        else if (child.getNodeName().equals("max_money")) {
            try {
                long money = Long.parseLong(child.getTextContent());
                stats.setMaxMoney(money);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(StatsTransformer.INVALID_NODE + " (bad max_money format)");
            }
        }
        else if (child.getNodeName().equals("min_money")) {
            try {
                long money = Long.parseLong(child.getTextContent());
                stats.setMinMoney(money);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(StatsTransformer.INVALID_NODE + " (bad min_money format)");
            }
        }
    }
    
    /** Converts {@link Stats} to DOM element allowing XML serialization.
     * @see Transformer#transform(T)
     */
    @Override
    public Element transform(Stats object) {        
        Element moneyNode = this.newElement("money"),
                restartsNode = this.newElement("restarts"),
                maxMoneyNode = this.newElement("max_money"),
                minMoneyNode = this.newElement("min_money");
        
        moneyNode.setTextContent(object.getMoney().toString());
        restartsNode.setTextContent(object.getRestarts().toString());
        maxMoneyNode.setTextContent(object.getMaxMoney().toString());
        minMoneyNode.setTextContent(object.getMinMoney().toString());
        
        Element group = this.newElement("group");
        group.appendChild(moneyNode);
        group.appendChild(restartsNode);
        group.appendChild(maxMoneyNode);
        group.appendChild(minMoneyNode);
        
        return group;
    }
    
    @Override
    public Stats transform(Node node)
            throws IllegalArgumentException {        
        NodeList children = node.getChildNodes();
        if (children.getLength() != 1) {
            throw new IllegalArgumentException(StatsTransformer.INVALID_NODE
                    + " (either too many or not enough group tags)");
        }
        
        Node group = children.item(0);
        children = group.getChildNodes();
        Stats stats = new Stats();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            this.parseChild(child, stats);
        }
        
        return stats;
    }

}

