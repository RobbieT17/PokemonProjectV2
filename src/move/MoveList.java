package move;

import battle.BattleField;
import battle.Input;
import battle.Weather;
import stats.GameType;
import stats.StatusCondition;

public interface MoveList {

    public static Move dragonClaw() {
        return new MoveBuilder()
        .setId(337)
        .setName("Dragon Claw")
        .setType(GameType.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move ember() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.BURN, 10); 
        };

        return new MoveBuilder()
        .setId(52)
        .setName("Ember")
        .setType(GameType.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(25)
        .setPower(40)
        .setAction(action)
        .buildMove();
    }

    public static Move growl() {
        MoveAction action = (a, d, m) -> {
            MoveAction.attackStat(d, -1);
        };

        return new MoveBuilder()
        .setId(45)
        .setName("Growl")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(40)
        .setAction(action)
        .buildMove();
    }

    public static Move growth() {
        MoveAction action = (a, d, m) -> {   
            if (BattleField.currentWeather == Weather.SUNNY) {
                MoveAction.attackStat(a, 2);
                MoveAction.spAttackStat(a, 2);
                return;
            }
          
            MoveAction.attackStat(a, 1);
            MoveAction.spAttackStat(a, 1);
        };

        return new MoveBuilder()
        .setId(74)
        .setName("Growth")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .buildMove();
    }

    public static Move heatWave() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);

        };

        return new MoveBuilder()
        .setId(257)
        .setName("Heat Wave")
        .setType(GameType.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(95)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

    public static Move poisonPowder() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.guaranteedEffect(d, StatusCondition.POISON);
        };

        return new MoveBuilder()
        .setId(77)
        .setName("Poison Powder")
        .setType(GameType.POISON)
        .setCategory(Move.STATUS)
        .setPP(35)
        .setAccuracy(100)
        .setAction(action)
        .buildMove();
    }

    public static Move rainDance() {
        return new MoveBuilder()
        .setId(240)
        .setName("Rain Dance")
        .setType(GameType.WATER)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction((a, d, m) -> MoveAction.changeWeather(Weather.RAIN))
        .buildMove();
    }

    public static Move sleepPowder() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.guaranteedEffect(d, StatusCondition.SLEEP, Input.randomInt(1, 3));
        };

        return new MoveBuilder()
        .setId(79)
        .setName("Sleep Powder")
        .setType(GameType.GRASS)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAccuracy(75)
        .setAction(action)
        .buildMove();
    }

    public static Move smokescreen() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.accuracyStat(d, -1);
        };

        return new MoveBuilder()
        .setId(108)
        .setName("Smokescreen")
        .setType(GameType.NORMAL)
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
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPower(120)
        .setPP(10)
        .setAction(action)
        .buildMove();
    }

    public static Move stunSpore() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.guaranteedEffect(d, StatusCondition.PARALYSIS);
        };

        return new MoveBuilder()
        .setId(78)
        .setName("Stun Spore")
        .setType(GameType.GRASS)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAccuracy(75)
        .setAction(action)
        .buildMove();
    }

    public static Move sunnyDay() {  
        return new MoveBuilder()
        .setId(241)
        .setName("Sunny Day")
        .setType(GameType.FIRE)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction((a, d, m) -> MoveAction.changeWeather(Weather.SUNNY))
        .buildMove();
    }

    public static Move tackle() {
        return new MoveBuilder()
        .setId(33)
        .setName("Tackle")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPower(40)
        .setPP(35)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move tailWhip() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.defenseStat(d, -1);
        };

        return new MoveBuilder()
        .setId(39)
        .setName("Tail Whip")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAction(action)
        .buildMove();
    }

    public static Move takeDown() {    
        return new MoveBuilder()
        .setId(36)
        .setName("Take Down")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(90)
        .setAccuracy(85)
        .setAction((a, d, m) -> MoveAction.dealDamageRecoil(a, d, m, 25))
        .buildMove();
    }

    public static Move vineWhip() {
        return new MoveBuilder()
        .setId(22)
        .setName("Vine Whip")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPower(40)
        .setPP(25)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move waterGun() {
        return new MoveBuilder()
        .setId(55)
        .setName("Water Gun")
        .setType(GameType.WATER)
        .setCategory(Move.SPECIAL)
        .setPower(40)
        .setPP(25)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    // Struggle: Used when pokemon is out of moves
    public static Move struggle() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamageRecoil(a, d, m, 25);
        };

        return new MoveBuilder()
        .setId(165)
        .setName("Struggle")
        .setType(GameType.NO_TYPE)
        .setCategory(Move.PHYSICAL)
        .setPP(-1)
        .setPower(40)
        .setAction(action)
        .buildMove();
    }



}
