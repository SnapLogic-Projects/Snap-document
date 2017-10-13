package com.snaplogic.api;

import com.snaplogic.Document;
import com.snaplogic.api.SnapException;

/**
 * This exception should be used by the snap author to notify the platform about data errors.
 * <p>
 * The following code shows how to use {@link com.snaplogic.snap.api.SnapDataException} inside snap
 * process method.
 * {@code
 *      public void process(Document document, InputView input) {
 *          try {
 *              parseData(document);
 *          } catch(Exception ex) {
 *              throw new SnapDataException(ex, "Failed to process document");
 *          }
 *      }
 * }
 * </p>
 * NOTE: The document passed to this exception is the error document to write to the error view,
 * it should contain any messages and information that the user will need to correct the error.
 * If you would like the error document to be generated from the messages in this exception, use
 * the constructors that do not accept a Document object.
 *
 * @author ksubramanian
 */
public class SnapDataException extends com.snaplogic.SnapException {
    private static final long serialVersionUID = 1L;
    protected Document document;
    private BinaryOutput binaryData;

    public SnapDataException(String message) {
        super(message);
    }

    public SnapDataException(Throwable cause, String message) {
        super(cause, message);
    }

    public SnapDataException(Document document, String message) {
        super(message);
        this.document = document;
    }

    public SnapDataException(Document document, Throwable cause, String message) {
        super(cause, message);
        this.document = document;
    }

    public SnapDataException(BinaryOutput binaryData, String message) {
        super(message);
        this.binaryData = binaryData;
    }

    public SnapDataException(BinaryOutput binaryData, Throwable cause, String message) {
        super(cause, message);
        this.binaryData = binaryData;
    }

    /**
     * Returns the document that caused this data error.
     *
     * @return errorDocument
     */
    public final Document getErrorDocument() {
        return document;
    }

    /**
     * Returns the binary data that caused this data error.
     *
     * @return errorData
     */
    public final BinaryOutput getErrorData() {
        return binaryData;
    }
}
