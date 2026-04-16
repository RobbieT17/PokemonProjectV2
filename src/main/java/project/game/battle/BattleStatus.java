package project.game.battle;

public class BattleStatus {
    
    private final int status;
    private final String message;

    public BattleStatus(int status) {
        this.status = status;
        this.message = "";
    }

    public BattleStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int getStatus() {
        return this.status;
    }

    public String getMessage() {
        return this.message;
    }

    
}
