package project.game.move.movelist;

import project.game.battle.BattleField;
import project.game.battle.Weather;
import project.game.builders.MoveBuilder;
import project.game.move.Move;
import project.game.move.MoveListHelperFunctions;
import project.game.move.moveactions.MoveAction;
import project.game.move.moveactions.MoveActionAccuracy;
import project.game.move.moveactions.MoveActionAttackDamage;
import project.game.move.moveactions.MoveActionChangeCondition;
import project.game.move.moveactions.MoveActionChangeStat;
import project.game.move.moveactions.MoveActionCharge;
import project.game.move.moveactions.MoveActionHealthRestore;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusConditionManager.StatusConditionID;
import project.game.pokemon.stats.Type;

public class GrassMoveList {

    public static Move bulletSeed() {
        return new MoveBuilder()
        .setId(331)
        .setName("Bullet Seed")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(30)
        .setPower(25)
        .setContact(false)
        .setAction(e -> MoveActionAttackDamage.multiHit(e))
        .build();
    }

    public static Move energyBall() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(412)
        .setName("Energy Ball")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
        .build();
    }

    public static Move frenzyPlant() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionCharge.rechargeMove(e);
        };

        return new MoveBuilder()
        .setId(338)
        .setName("Frenzy Plant")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move gigaDrain() {
        return new MoveBuilder()
        .setId(202)
        .setName("Giga Drain")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(75)
        .setAction(e -> MoveActionAttackDamage.dealDamageDrain(e, 50))
        .build();
    }

    public static Move grassKnot() {
        MoveAction action = e -> {
            Pokemon d = e.eventData.attackTarget;
            // Move power varies based on weight 
            e.eventData.moveUsed.setPower(
            d.getWeight() <= 21.8
                ? 20
                : d.getWeight() <= 54.9
                    ? 40
                    : d.getWeight() <= 110
                        ? 60
                        : d.getWeight() < 220.2
                            ? 80
                            : d.getWeight() < 440.7
                                ? 100
                                : 120);
            MoveActionAttackDamage.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(447)
        .setName("Grass Knot")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(120) // Varies
        .setContact(true)
        .setAction(action)
        .build();
    }

    public static Move leafStorm() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, -2, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(437)
        .setName("Leaf Storm")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(5)
        .setPower(130)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move leechSeed() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.SEEDED_ID);
        };

        return new MoveBuilder()
        .setId(73)
        .setName("Leech Seed")
        .setType(Type.GRASS)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move magicalLeaf() {
        return new MoveBuilder()
        .setId(345)
        .setName("Magical Leaf")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move petalBlizzard() {
        return new MoveBuilder()
        .setId(572)
        .setName("Petal Blizzard")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(90)
        .setContact(false)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move petalDance() {
        return new MoveBuilder()
        .setId(80)
        .setName("Petal Dance")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(120)
        .setContact(true)
        .setAction(e -> MoveActionCharge.rampageMove(e))
        .build();
    }

    public static Move powerWhip() {
        return new MoveBuilder()
        .setId(438)
        .setName("Power Whip")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAccuracy(85)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move razorLeaf() {
        return new MoveBuilder()
        .setId(75)
        .setName("Razor Leaf")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(25)
        .setPower(55)
        .setAccuracy(95)
        .setCritRatio(Move.HIGH_CRIT)
        .setContact(false)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move seedBomb() {
        return new MoveBuilder()
        .setId(402)
        .setName("Seed Bomb")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setContact(false)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move sleepPowder() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.SLEEP_ID);
            MoveAction.displayFailMessage(e);
        };

        return new MoveBuilder()
        .setId(79)
        .setName("Sleep Powder")
        .setType(Type.GRASS)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAccuracy(75)
        .setAction(action)
        .build();
    }

    public static Move solarBeam() {
        MoveAction action = e -> {
            if (BattleField.currentWeather == Weather.SUNNY) {
                MoveActionAttackDamage.dealDamage(e);
            }
            else {
                MoveActionCharge.chargeMove(e);
            }
        };

        return new MoveBuilder()
        .setId(76)
        .setName("Solar Beam")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPower(120)
        .setPP(10)
        .setAction(action)
        .build();
    }

    public static Move stunSpore() {
        MoveAction action = e -> {
            MoveActionAccuracy.moveHits(e);
            MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS_ID);
        };

        return new MoveBuilder()
        .setId(78)
        .setName("Stun Spore")
        .setType(Type.GRASS)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAccuracy(75)
        .setAction(action)
        .build();
    }

    public static Move synthesis() {
        MoveAction action = e -> {
            MoveListHelperFunctions.targetsUser(e.eventData);
            double percent = (BattleField.currentWeather == Weather.SUNNY) 
                ? 67
                : (BattleField.currentWeather == Weather.CLEAR) ? 50 : 25;

            MoveActionHealthRestore.restoreHp(e, percent);            
        };

        return new MoveBuilder()
        .setId(235)
        .setName("Synthesis")
        .setType(Type.GRASS)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(action)
        .build();
    }   
    
    public static Move trailblaze() {
        MoveAction action = e -> {
            MoveActionAttackDamage.dealDamage(e);
            MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 1, 0, 0));
        };

        return new MoveBuilder()
        .setId(885)
        .setName("Trailblaze")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(50)
        .setAction(action)
        .build();
    }

    public static Move vineWhip() {
        return new MoveBuilder()
        .setId(22)
        .setName("Vine Whip")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPower(40)
        .setPP(25)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }
}
