/**
 * Messages is the container for all the externalizable messages in this package.
 *
 * @author ksubramanian
 * @since 2013
 */
class Messages {
    static final String PLEASE_FILE_A_DEFECT_AGAINST_SNAP = "Please file a defect against the " +
            "snap";
    static final String NOTHING_TO_MERGE = "Nothing to merge";
    static final String ERR_NULL_OBJECT = "Deep copy should never be called for a null object";
    static final String ERR_COPY = "Failed to make copy, detail: %s, %s";
    static final String DATA_IS_NOT_SERIALIZABLE = "Document data is " +
            "not serializable or externalizable";
    static final String RESOLUTION_SERIALIZABLE = "Please check if all objects in the document" +
            " hierarchy are serializable. <a href=\"http://en.wikipedia.org/wiki/" +
            "Serialization\">Click here</a> for more information on serialization.";
    static final String CANNOT_FIND_MD5_MESSAGE_DIGEST_ALGORITHM = "Cannot find MD5 message " +
            "digest algorithm";
    static final String ERROR_SERIALIZING_DOCUMENT_DURING_DIGEST = "Error serializing document " +
            "during digest";
}