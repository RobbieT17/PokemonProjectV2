package project.move;

import project.event.EventData;

// Interface storing all available Pokemon moves (Moves listed alphabetically)
public interface MoveListHelperFunctions {

    public static int[] stats(int atk, int def, int spAtk, int spDef, int spd, int acc, int eva) {
        return new int[] {atk, def, spAtk, spDef, spd, acc, eva};
    }

    public static void targetsUser(EventData data) {
        data.attackTarget = data.user;
        data.effectTarget = data.user;
    }

    public static void affectsUser(EventData data) {
        data.effectTarget = data.user;
    }

}
