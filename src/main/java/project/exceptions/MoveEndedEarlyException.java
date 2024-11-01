package project.exceptions;

/*
 * Throws an exception when the Pokemon has to
 * end it's move early, this is usually an intended
 * and applied for multi-step moves, etc. Solar Beam
 */
public class MoveEndedEarlyException extends RuntimeException {
    // Throws new exception without a message
    public MoveEndedEarlyException() {
        super("");
    }

    /**
     * Throws new exception with a message
     * @param message explains why the exception was thrown
     */
    public MoveEndedEarlyException(String message) {
        super(message);
    }

    public MoveEndedEarlyException(String message, Object... args) {
        super(String.format(message, args));
    }

}
