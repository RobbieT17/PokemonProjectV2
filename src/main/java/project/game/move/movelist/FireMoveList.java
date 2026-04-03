package project.game.move.movelist;

import project.game.battle.BattleLog;
import project.game.battle.Weather;
import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionChangeWeather;
import project.game.move.moveactions.MoveActionCharge;
import project.game.pokemon.effects.StatusCondition;
import project.game.pokemon.stats.Type;

public class FireMoveList {

    public static Move blastBurn() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionCharge.rechargeMove(e);
        };

        return new MoveBuilder()
        .setId(307)
        .setName("Blast Burn")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move ember() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 10); 
        };

        return new MoveBuilder()
        .setId(52)
        .setName("Ember")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(25)
        .setPower(40)
        .setAction(action)
        .build();
    }

    public static Move fireBlast() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 30);
        };

        return new MoveBuilder()
        .setId(126)
        .setName("Fire Blast")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(110)
        .setAccuracy(85)
        .setAction(action)
        .build();
    }

    public static Move fireFang() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 10);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.FLINCH_ID, 30);
        };

        return new MoveBuilder()
        .setId(424)
        .setName("Fire Fang")
        .setType(Type.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(65)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }

    public static Move firePledge() {
        return new MoveBuilder()
        .setId(519)
        .setName("Fire Pledge")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move firePunch() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 10);
        };

        return new MoveBuilder()
        .setId(7)
        .setName("Fire Punch")
        .setType(Type.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move flameCharge() {
        MoveAction action = e -> {
            MoveListHelperFunctions.affectsUser(e.eventData);
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 1, 0, 0));
        };

        return new MoveBuilder()
        .setId(488)
        .setName("Flame Charge")
        .setType(Type.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(50)
        .setAction(action)
        .build();
    }

    public static Move flamethrower() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 10);
        };

        return new MoveBuilder()
        .setId(53)
        .setName("Flamethrower")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(90)
        .setAction(action)
        .build();
    }

    public static Move flareBlitz() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamageRecoil(e, 33);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 10);
        };

        return new MoveBuilder()
        .setId(394)
        .setName("Flare Blitz")
        .setType(Type.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(120)
        .setAction(action)
        .build();
    }

    public static Move heatCrash() {
        MoveAction action = e -> {
            /*
             * Power varies based on the weight of both user and the target
             * The greater the difference, the greater the power
             */
            double ratio = e.eventData.user.getWeight() / e.eventData.attackTarget.getWeight();

            e.eventData.moveUsed.setPower(
            ratio < 2
                ? 40
                : ratio < 3
                    ? 60
                    : ratio < 4
                        ? 80
                        : ratio < 5 
                            ? 100
                            : 120);
            // TODO: Deals double damage to minimized opponents
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(535)
        .setName("Heat Crash")
        .setType(Type.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120) // Varies
        .setAction(action)
        .build();
    }

    public static Move heatWave() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID, 10);
        };

        return new MoveBuilder()
        .setId(257)
        .setName("Heat Wave")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(95)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move inferno() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID);
        };

        return new MoveBuilder()
        .setId(517)
        .setName("Inferno")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(100)
        .setAccuracy(50)
        .setAction(action)
        .build();
    }

    public static Move overheat() {
        MoveAction action = e -> {
            MoveListHelperFunctions.affectsUser(e.eventData);
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(-2, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(315)
        .setName("Overheat")
        .setType(Type.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(130)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move sunnyDay() {  
        return new MoveBuilder()
        .setId(241)
        .setName("Sunny Day")
        .setType(Type.FIRE)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(e -> MoveActionChangeWeather.changeWeather(e, Weather.SUNNY))
        .build();
    }

    public static Move temperFlare() {
        MoveAction action = e -> {
            if (e.eventData.user.getConditions().isInterrupted()) {
                e.eventData.moveUsed.doublePower();
            }
            
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(915)
        .setName("Temper Flare")
        .setType(Type.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move willOWisp() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.BURN_ID);
            BattleLog.add(e.eventData.message);
        };

        return new MoveBuilder()
        .setId(261)
        .setName("Will-O-Wisp")
        .setType(Type.FIRE)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAccuracy(85) 
        .setAction(action)
        .build();
    }

}
