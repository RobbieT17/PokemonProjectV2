package project.game.move.moveactions;

public interface MoveAction {

    public static int[] stats(int atk, int def, int spAtk, int spDef, int spd, int acc, int eva) {
        return new int[] {atk, def, spAtk, spDef, spd, acc, eva};
    }

}
