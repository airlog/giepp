/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import org.w3c.dom.Element;

/** High-level interface for transforming classes to XML. 
 * This interface provides methods allowing to convert an object of class T to desired XML structure.
 */
public interface Transformer<T> {

    /** Converts object to XML DOM element.
     * @return  object serialized to XML
     */
    public abstract Element transform(T object);
    
    public abstract T transform(Element node)
            throws IllegalArgumentException;

}

