package move;

import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import stats.StatusCondition;
import stats.Type;

public interface MoveList {
    
    public static Move ember() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            
            if (d.hasNonVolatileCondition()) return;

            MoveAction.applyBurn(d, 100);
            
        };

        return new MoveBuilder()
        .setId(52)
        .setName("Ember")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPower(40)
        .setPP(25)
        .setAction(action)
        .buildMove();
    }

    public static Move growth() {
        MoveAction action = (a, d, m) -> {   
            if (BattleField.currentWeather == Weather.SUNNY) {
                MoveAction.attackStat(a, 2);
                MoveAction.spAttackStat(a, 2);
            }
            else{
                MoveAction.attackStat(a, 1);
                MoveAction.spAttackStat(a, 1);
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

    public static Move poisonPowder() {
        MoveAction action = (a, d, m) -> {
            if (d.hasNonVolatileCondition()) {
                BattleLog.add((a.hasNonVolatileCondition(StatusCondition.POISON) ? String.format("%s is already poisoned!", d) : Move.FAILED));
                return;
            }

            if (!MoveAction.moveHits(a, d, m)) return;

            MoveAction.applyPoison(d, 100);
        };

        return new MoveBuilder()
        .setId(77)
        .setName("Poison Powder")
        .setType(Type.POISON)
        .setCategory(Move.STATUS)
        .setPP(35)
        .setAccuracy(100)
        .setAction(action)
        .buildMove();
    }

    public static Move rainDance() {
        MoveAction action = (a, d, m) -> {
            MoveAction.changeWeather(Weather.RAIN);
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

    public static Move sleepPowder() {
        MoveAction action = (a, d, m) -> {
            if (d.hasNonVolatileCondition()) {
                BattleLog.add((a.hasNonVolatileCondition(StatusCondition.SLEEP) ? String.format("%s is already asleep!", d) : Move.FAILED));
                return;
            }

            if (!MoveAction.moveHits(a, d, m)) return;

            MoveAction.applySleep(d, 5);
        };

        return new MoveBuilder()
        .setId(79)
        .setName("Sleep Powder")
        .setType(Type.GRASS)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAccuracy(75)
        .setAction(action)
        .buildMove();
    }

    public static Move smokescreen() {
        MoveAction action = (a, d, m) -> {
            if (!MoveAction.moveHits(a, d, m)) return;
            MoveAction.accuracyStat(d, -1);
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
        MoveAction action = (a, d, m) -> {
            if (BattleField.currentWeather == Weather.SUNNY) MoveAction.dealDamage(a, d, m);
            else MoveAction.chargeMove(a, d, m);
        };

        return new MoveBuilder()
        .setId(76)
        .setName("Solar Beam")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPower(120)
        .setPP(10)
        .setAction(action)
        .buildMove();
    }

    public static Move sunnyDay() {
        MoveAction action = (a, d, m) -> {
            MoveAction.changeWeather(Weather.SUNNY);
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
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(33)
        .setName("Tackle")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPower(40)
        .setPP(35)
        .setAction(action)
        .buildMove();
    }

    public static Move tailWhip() {
        MoveAction action = (a, d, m) -> {
            if (!MoveAction.moveHits(a, d, m)) return;
            MoveAction.defenseStat(d, -1);
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

    public static Move takeDown() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.recoilDamage(a, 0.25);
        };
        
        return new MoveBuilder()
        .setId(36)
        .setName("Take Down")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(90)
        .setAccuracy(85)
        .setAction(action)
        .buildMove();
    }

    public static Move vineWhip() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(22)
        .setName("Vine Whip")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPower(40)
        .setPP(25)
        .setAction(action)
        .buildMove();
    }

    public static Move waterGun() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(55)
        .setName("Water Gun")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPower(40)
        .setPP(25)
        .setAction(action)
        .buildMove();
    }

    // Struggle: Used when pokemon is out of moves
    public static Move struggle() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.recoilDamage(a, 0.25);
        };

        return new MoveBuilder()
        .setId(165)
        .setName("Struggle")
        .setType(Type.NO_TYPE)
        .setCategory(Move.PHYSICAL)
        .setPP(-1)
        .setAction(action)
        .buildMove();
    }



}
