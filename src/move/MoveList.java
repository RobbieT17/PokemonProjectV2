package move;
import battle.BattleField;
import battle.Weather;
import stats.Type;

public class MoveList {
    
    public static Move ember() {
        MoveAction action = (p1, p2, move) -> {
            MoveAction.dealDamage(p1, p2, move);
            // TODO: Add burn effect
        };

        return new MoveBuilder()
        .setId(52)
        .setName("Ember")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPow(40)
        .setPP(25)
        .setAction(action)
        .buildMove();
    }

    public static Move growth() {
        MoveAction action = (p1, p2, move) -> {   
            if (BattleField.currentWeather == Weather.SUNNY) {
                MoveAction.attackStat(p1, 2);
                MoveAction.spAttackStat(p1, 2);
            }
            else{
                MoveAction.attackStat(p1, 1);
                MoveAction.spAttackStat(p1, 1);
            }    
        };

        return new MoveBuilder()
        .setId(74)
        .setName("Growth")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .buildMove();
    }

    public static Move rainDance() {
        MoveAction action = (p1, p2, move) -> {
            Weather.changeWeather(Weather.RAIN);
        };

        return new MoveBuilder()
        .setId(240)
        .setName("Rain Dance")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(action)
        .buildMove();
    }

    public static Move smokescreen() {
        MoveAction action = (p1, p2, move) -> {
            if (!MoveAction.moveHits(p1, p2, move)) return;
            MoveAction.accuracyStat(p2, -1);
        };

        return new MoveBuilder()
        .setId(108)
        .setName("Smokescreen")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .buildMove();
    }

    public static Move solarBeam() {
        MoveAction action = (p1, p2, move) -> {
            if (BattleField.currentWeather == Weather.SUNNY) MoveAction.dealDamage(p1, p2, move);
            else MoveAction.changeMove(p1, p2, move);
        };

        return new MoveBuilder()
        .setId(76)
        .setName("Solar Beam")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPow(120)
        .setPP(10)
        .setAction(action)
        .buildMove();
    }

    public static Move sunnyDay() {
        MoveAction action = (p1, p2, move) -> {
            Weather.changeWeather(Weather.SUNNY);
        };

        return new MoveBuilder()
        .setId(241)
        .setName("Sunny Day")
        .setType(Type.FIRE)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(action)
        .buildMove();
    }

    public static Move tackle() {
        MoveAction action = (p1, p2, move) -> {
            MoveAction.dealDamage(p1, p2, move);
        };

        return new MoveBuilder()
        .setId(33)
        .setName("Tackle")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPow(40)
        .setPP(35)
        .setContact(true)
        .setAction(action)
        .buildMove();
    }

    public static Move tailWhip() {
        MoveAction action = (p1, p2, move) -> {
            if (!MoveAction.moveHits(p1, p2, move)) return;
            MoveAction.defenseStat(p2, -1);
        };

        return new MoveBuilder()
        .setId(39)
        .setName("Tail Whip")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAction(action)
        .buildMove();
    }

    public static Move vineWhip() {
        MoveAction action = (p1, p2, move) -> {
            MoveAction.dealDamage(p1, p2, move);
        };

        return new MoveBuilder()
        .setId(22)
        .setName("Vine Whip")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPow(40)
        .setPP(25)
        .setContact(true)
        .setAction(action)
        .buildMove();
    }

    public static Move waterGun() {
        MoveAction action = (p1, p2, move) -> {
            MoveAction.dealDamage(p1, p2, move);
        };

        return new MoveBuilder()
        .setId(55)
        .setName("Water Gun")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPow(40)
        .setPP(25)
        .setAction(action)
        .buildMove();
    }



}
