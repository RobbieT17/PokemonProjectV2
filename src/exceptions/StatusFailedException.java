package exceptions;

public class StatusFailedException extends RuntimeException {
    
     // Throws new exception without a message
     public StatusFailedException() {
        super("");
    }

    /**
     * Throws new exception with a message
     * @param message explains why the exception was thrown
     */
    public StatusFailedException(String message) {
        super(message);
    }

    public StatusFailedException(String message, Object... args) {
        super(String.format(message, args));
    }
}
