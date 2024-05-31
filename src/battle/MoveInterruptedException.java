package battle;

public class MoveInterruptedException extends RuntimeException {
    public MoveInterruptedException() {
        super("");
    }

    public MoveInterruptedException(String message) {
        super(message);
    }

}
