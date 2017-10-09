/**
 * This exception should be used when there is a problem during document creation or manipulation.
 *
 * @author ksubramanian
 */
public class DocumentException extends SnapException {
    private static final long serialVersionUID = 1L;

    public DocumentException(String message) {
        super(message);
    }

    public DocumentException(String message, String errorMessageForUser) {
        super(message, errorMessageForUser);
    }

    public DocumentException(Throwable cause, String message) {
        super(cause, message);
    }

    public DocumentException(Throwable cause, String message, String... params) {
        super(cause, message, params);
    }
}