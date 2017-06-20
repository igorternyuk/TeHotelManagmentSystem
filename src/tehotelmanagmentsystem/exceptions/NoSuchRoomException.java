package tehotelmanagmentsystem.exceptions;

/**
 *
 * @author igor
 */
public class NoSuchRoomException extends Exception {

    public NoSuchRoomException() {
    }

    /**
     * Constructs an instance of <code>NoSuchRoomException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public NoSuchRoomException(String msg) {
        super(msg);
    }
}
