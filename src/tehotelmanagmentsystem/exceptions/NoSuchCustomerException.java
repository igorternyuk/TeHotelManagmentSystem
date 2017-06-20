package tehotelmanagmentsystem.exceptions;

/**
 *
 * @author igor
 */
public class NoSuchCustomerException extends Exception {

    public NoSuchCustomerException() {
    }

    /**
     * Constructs an instance of <code>NoSuchCustomerException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchCustomerException(String msg) {
        super(msg);
    }
}
