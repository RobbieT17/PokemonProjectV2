package project.move.movelist;

import project.battle.BattleField;
import project.battle.BattleLog;
import project.battle.Weather;
import project.event.EventData;
import project.event.EventManager;
import project.move.Move;
import project.move.MoveBuilder;
import project.move.MoveListHelperFunctions;
import project.move.moveactions.MoveAction;
import project.move.moveactions.MoveActionAccuracy;
import project.move.moveactions.MoveActionAttackDamage;
import project.move.moveactions.MoveActionBracing;
import project.move.moveactions.MoveActionChangeCondition;
import project.move.moveactions.MoveActionChangeStat;
import project.move.moveactions.MoveActionCharge;
import project.move.moveactions.MoveActionHealthRestore;
import project.pokemon.Pokemon;
import project.stats.StatusCondition;
import project.stats.Type;
import project.utility.RandomValues;

public class NormalMoveList {

    public static Move bodySlam() {
        MoveAction action = e -> {
            // TODO: Double Damage when Pokemon has minimized condition
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.PARALYSIS_ID, 30);
        };

        return new MoveBuilder()
        .setId(34)
        .setName("Body Slam")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(85)
        .setAction(action)
        .build();
    }

    public static Move doubleEdge() {
        return new MoveBuilder()
        .setId(38)
        .setName("Double-Edge")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(120)
        .setAction(e -> MoveActionAttackDamage.dealDamageRecoil(e, 33))
        .build();
    }

    public static Move endure() {
        return new MoveBuilder()
        .setId(203)
        .setName("Endure")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setPriority(4)
        .setAction(e -> MoveActionBracing.pokemonProtects(e, e.eventData.user.getConditions().getEndure(), e.eventData.user + " braced itself!"))
        .build();
    }

    public static Move facade() {
        MoveAction action = e -> {
            Pokemon a = e.eventData.user;
            // Double power (140) if user is burned, paralyzed, or poisoned
            if (a.getConditions().hasKey(StatusCondition.BURN_ID) |
            a.getConditions().hasKey(StatusCondition.PARALYSIS_ID) |
            a.getConditions().hasKey(StatusCondition.POISON_ID)) {
                e.eventData.moveUsed.doublePower();
            }

            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(263)
        .setName("Facade")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(70)
        .setAction(action)
        .build();
    }

    public static Move falseSwipe() {
        MoveAction action = e -> {
            // Leaves opponent with at least 1 HP
            e.eventData.attackTarget.getConditions().getEndure().setActive(true);
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(206)
        .setName("False Swipe")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(40)
        .setPower(40)
        .setAction(action)
        .build();
    }

    public static Move furyAttack() {
        return new MoveBuilder()
        .setId(31)
        .setName("Fury Attack")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(15)
        .setAccuracy(85)
        .setAction(e -> MoveActionAttackDamage.multiHit(e))
        .build();
    }

    public static Move gigaImpact() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionCharge.rechargeMove(e);
        };

        return new MoveBuilder()
        .setId(416)
        .setName("Giga Impact")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move growl() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(45)
        .setName("Growl")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(40)
        .setAction(action)
        .build();
    }

    public static Move growth() {
        MoveAction action = e -> {   
            MoveListHelperFunctions.targetsUser(e.eventData);
            if (BattleField.currentWeather == Weather.SUNNY) {
                MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(2, 0, 2, 0, 0, 0, 0));
                return;
            }
          
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, 0, 1, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(74)
        .setName("Growth")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move hyperBeam() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionCharge.rechargeMove(e);
        };

        return new MoveBuilder()
        .setId(63)
        .setName("Hyper Beam")
        .setType(Type.NORMAL)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move protect() {
        return new MoveBuilder()
        .setId(182)
        .setName("Protect")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setPriority(4)
        .setAction(e -> MoveActionBracing.pokemonProtects(e, e.eventData.user.getConditions().getProtect(), e.eventData.user + " protected itself!"))
        .build();
    }

    public static Move rapidSpin() {
        MoveAction action = e -> {
            MoveListHelperFunctions.affectsUser(e.eventData);
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 1, 0, 0));
            // TODO: Removes Trap Effects
        };

        return new MoveBuilder()
        .setId(229)
        .setName("Rapid Spin")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(40)
        .setPower(50)
        .setAction(action)
        .build();
    }

    public static Move rest() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
            MoveActionHealthRestore.restoreHp(e, 100);
            MoveActionChangeCondition.applyCondition(e, StatusCondition.SLEEP_ID);
        };

        return new MoveBuilder()
        .setId(156)
        .setName("Rest")
        .setType(Type.PSYCHIC)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(action)
        .build();
    }

    public static Move scaryFace() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, -2, 0, 0));
        };

        return new MoveBuilder()
        .setId(184)
        .setName("Scary Face")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(action)
        .build();
    }

    public static Move scratch() {
        return new MoveBuilder()
        .setId(10)
        .setName("Scratch")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(35)
        .setPower(40)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move shellSmash() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(1, -1, 1, -1, 1, 0, 0));
        };

        return new MoveBuilder()
        .setId(504)
        .setName("Shell Smash")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAction(action)
        .build();
    }

    public static Move slash() {
        return new MoveBuilder()
        .setId(163)
        .setName("Slash")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(70)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move sleepTalk() {
        MoveAction action = e -> {
            Pokemon a = e.eventData.user;
            Move m = e.eventData.moveUsed;
            // Only works when Pokemon is asleep
            if (!a.getConditions().hasKey(StatusCondition.SLEEP_ID)){
                BattleLog.add(Move.FAILED);
                return;
            }

            // Uses random move in moveset, doesn't choose Sleep Talk
            Move randomMove = m;
            while (randomMove.equals(m)) {
                int i = RandomValues.generateInt(0, a.getMoves().length - 1);
                randomMove = a.getMoves()[i];
            }

            BattleLog.add("%s used %s!", a, randomMove);

            randomMove.getAction().act(new EventManager(e.eventData.user, e.eventData.attackTarget));
        };

        return new MoveBuilder()
        .setId(214)
        .setName("Sleep Talk")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(action)
        .build();
    }

    public static Move smokescreen() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 0, -1, 0));
        };

        return new MoveBuilder()
        .setId(108)
        .setName("Smokescreen")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move sweetScent() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 0, 0, -1));
        };

        return new MoveBuilder()
        .setId(230)
        .setName("Sweet Scent")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move swordsDance() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(2, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(14)
        .setName("Swords Dance")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move tackle() {
        return new MoveBuilder()
        .setId(33)
        .setName("Tackle")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPower(40)
        .setPP(35)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move tailWhip() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, -1, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(39)
        .setName("Tail Whip")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAction(action)
        .build();
    }

    public static Move takeDown() {    
        return new MoveBuilder()
        .setId(36)
        .setName("Take Down")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(90)
        .setAccuracy(85)
        .setAction(e -> MoveActionAttackDamage.dealDamageRecoil(e, 25))
        .build();
    }

    // STRUGGLE: Used when pokemon is out of moves
    public static Move struggle() {
        return new MoveBuilder()
        .setId(165)
        .setName("Struggle")
        .setType(Type.TYPELESS)
        .setCategory(Move.PHYSICAL)
        .setPP(-1)
        .setPower(40)
        .setAction(e -> MoveActionAttackDamage.dealDamageRecoil(e, 25))
        .build();
    }


}
