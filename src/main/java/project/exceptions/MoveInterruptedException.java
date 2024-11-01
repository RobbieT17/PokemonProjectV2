package project.exceptions;

/*
 * Exception is thrown when a move cannot be completed
 * Usually happens when the move misses the target Pokemon
 * or if the Pokemon faints (additional effects aren't added)
 */
public class MoveInterruptedException extends RuntimeException {
    // Throws new exception without a message
    public MoveInterruptedException() {
        super("");
    }

    /**
     * Throws new exception with a message
     * @param message explains why the exception was thrown
     */
    public MoveInterruptedException(String message) {
        super(message);
    }

    public MoveInterruptedException(String message, Object... args) {
        super(String.format(message, args));
    }

}
