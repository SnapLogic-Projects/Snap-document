import java.io.Externalizable;

/**
 * This class holds the metadata associated with a {@link Document}. This class allows the snap
 * authors to lookup any key under any namespace section of the metadata.
 *
 * @author ksubramanian
 * @since 2013
 */
public interface Metadata extends Externalizable {

    /**
     * Returns the document id of this document.
     * <p> The document id is unique across all the documents handled by the jcc during a given
     * session.
     *
     * @return id
     */
    String getId();

    /**
     * Returns the value for the given key defined under the specified namespace.
     *
     * @param namespaceRegex Namespace regex pattern
     * @param key            Metadata key
     * @return value
     */
    String lookup(String namespaceRegex, String key);

    /**
     * Returns a copy of this metadata object.
     *
     * @return copy of metadata
     * @throws DocumentException when there is a problem creating copy of this metadata object.
     */
    Metadata copy() throws DocumentException;
}