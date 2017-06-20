package tehotelmanagmentsystem.exceptions;

/**
 *
 * @author igor
 */
public class NoSuchIdTypeException extends Exception {

    public NoSuchIdTypeException() {
    }

    /**
     * Constructs an instance of <code>NoSuchIdTypeException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchIdTypeException(String msg) {
        super(msg);
    }
}
