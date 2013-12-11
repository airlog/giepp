/**
 * @author  Rafal
 */

package pl.pisz.airlog.giepp.xml;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;

/** Wrapper for {@link DocumentBuilder}.
 * This class will ensure that each XML Document created with linked {@link DocumentBuilder} will
 * have a valid root element.
 */
public class StocksArchiveBuilder {

    /** Wrapped builder. */
    private DocumentBuilder builder;

    /** Creates a new instance.
     * @param   documentBuilder builder to wrap
     */
    public StocksArchiveBuilder(DocumentBuilder documentBuilder) {
        this.builder = documentBuilder;
    }
    
    /** Appends new root node to the given document. 
     * @param   document    document to which append
     */
    private Document appendRoot(Document document) {
        document.appendChild(document.createElement("archive"));  // owned stocks XML file's root
    
        return document;
    }
    
    /** Creates a new XML document.
     * @return  XML document with StocksArchive structure
     */
    public Document newDocument() {
        return this.appendRoot(this.builder.newDocument());
    }

}

