package move;

import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import stats.GameType;
import stats.StatusCondition;
import utility.RandomValues;

// Interface storing all available Pokemon moves (Moves listed alphabetically)
public interface MoveList {

// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    
    public static Move acidSpray() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.spDefenseStat(d, -2);
        };

        return new MoveBuilder()
        .setId(491)
        .setName("Acid Spray")
        .setType(GameType.POISON)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(40)
        .setAction(action)
        .buildMove();
    }

    public static Move acrobatics() {
        return new MoveBuilder()
        .setId(512)
        .setName("Acrobatics")
        .setType(GameType.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(55)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move aerialAce() {
        return new MoveBuilder()
        .setId(332)
        .setName("Aerial Ace")
        .setType(GameType.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move airCutter() {
        return new MoveBuilder()
        .setId(314)
        .setName("Air Cutter")
        .setType(GameType.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(25)
        .setPower(60)
        .setAccuracy(95)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move airSlash() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.applyFlinch(d, 30);
        };

        return new MoveBuilder()
        .setId(403)
        .setName("Air Slash")
        .setType(GameType.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(75)
        .setAccuracy(95)
        .setAction(action)
        .buildMove();
    }

    public static Move amnesia() {
        return new MoveBuilder()
        .setId(133)
        .setName("Amnesia")
        .setType(GameType.PSYCHIC)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction((a, d, m) -> MoveAction.spDefenseStat(a, 1))
        .buildMove();
    }

    public static Move aquaTail() {
        return new MoveBuilder()
        .setId(401)
        .setName("Aqua Tail")
        .setType(GameType.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(90)
        .setAccuracy(90)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

// BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB

    public static Move bite() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.applyFlinch(d, 30);
        };

        return new MoveBuilder()
        .setId(44)
        .setName("Bite")
        .setType(GameType.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(25)
        .setPower(60)
        .setAction(action)
        .buildMove();
    }

    public static Move bodySlam() {
        MoveAction action = (a, d, m) -> {
            // TODO: Double Damage when Pokemon has minimized condition
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.PARALYSIS, 30);
        };

        return new MoveBuilder()
        .setId(34)
        .setName("Body Slam")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(85)
        .setAction(action)
        .buildMove();
    }

    public static Move brickBreak() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            // TODO: Disables Light Screen / Reflect
        };

        return new MoveBuilder()
        .setId(280)
        .setName("Brick Break")
        .setType(GameType.FIGHTING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .buildMove();
    }

    public static Move bulldoze() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.speedStat(d, -1);
        };

        return new MoveBuilder()
        .setId(523)
        .setName("Bulldoze")
        .setType(GameType.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(60)
        .setContact(false)
        .setAction(action)
        .buildMove();
    }

    public static Move bulletSeed() {
        return new MoveBuilder()
        .setId(331)
        .setName("Bullet Seed")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(30)
        .setPower(25)
        .setContact(false)
        .setAction((a, d, m) -> MoveAction.multiHit(a, d, m))
        .buildMove();
    }

// CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
    
    public static Move charm() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.attackStat(d, -2);
        };

        return new MoveBuilder()
        .setId(204)
        .setName("Charm")
        .setType(GameType.FAIRY)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .buildMove();
    }

    public static Move curse() {
        // TODO: Two Verison of move, Ghost-Type, and Non-Ghost-Type
        MoveAction action = (a, d, m) -> {
            MoveAction.speedStat(a, -1);
            MoveAction.attackStat(a, 1);
            MoveAction.defenseStat(a, 1);
        };

        return new MoveBuilder()
        .setId(174)
        .setName("Curse")
        .setType(GameType.GHOST)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(action)
        .buildMove();
    }

// DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
    
    public static Move doubleEdge() {
        return new MoveBuilder()
        .setId(38)
        .setName("Double-Edge")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(120)
        .setAction((a, d, m) -> MoveAction.dealDamageRecoil(a, d, m, 33))
        .buildMove();
    }

    public static Move dragonBreath() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.PARALYSIS, 30);
        };

        return new MoveBuilder()
        .setId(225)
        .setName("Dragon Breath")
        .setType(GameType.DRAGON)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .buildMove();
    }

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

    public static Move dragonTail() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            // TODO: Force Switch
        };

        return new MoveBuilder()
        .setId(525)
        .setName("Dragon Tail")
        .setType(GameType.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(60)
        .setAccuracy(90)
        .setPriority(-6)
        .setAction(action)
        .buildMove();
    }

// EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

    public static Move earthPower() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.spDefenseStat(d, -1, 10);
        };

        return new MoveBuilder()
        .setId(414)
        .setName("Earth Power")
        .setType(GameType.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
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

    public static Move endure() {
        return new MoveBuilder()
        .setId(203)
        .setName("Endure")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setPriority(4)
        .setAction((a, d, m) -> MoveAction.pokemonBraces(a, a.conditions().endured(), a + " braced itself!"))
        .buildMove();
    }

    public static Move energyBall() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.spDefenseStat(d, -1, 10);
        };

        return new MoveBuilder()
        .setId(412)
        .setName("Energy Ball")
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
        .buildMove();
    }

// FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF

    public static Move facade() {
        MoveAction action = (a, d, m) -> {
            // Double power (140) if user is burned, paralyzed, or poisoned
            if (a.hasPrimaryCondition(StatusCondition.BURN) |
            a.hasPrimaryCondition(StatusCondition.PARALYSIS) |
            a.hasPrimaryCondition(StatusCondition.POISON))
                m.doublePower();

            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(263)
        .setName("Facade")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(70)
        .setAction(action)
        .buildMove();
    }

    public static Move falseSwipe() {
        MoveAction action = (a, d, m) -> {
            // Leaves opponent with at least 1 HP
            d.conditions().endured().setActive(true);
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(206)
        .setName("False Swipe")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(40)
        .setPower(40)
        .setAction(action)
        .buildMove();
    }

    public static Move fireFang() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.BURN, 10);
            MoveAction.applyFlinch(d, 30);
        };

        return new MoveBuilder()
        .setId(424)
        .setName("Fire Fang")
        .setType(GameType.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(65)
        .setAccuracy(95)
        .setAction(action)
        .buildMove();
    }

    public static Move firePunch() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.BURN, 10);
        };

        return new MoveBuilder()
        .setId(7)
        .setName("Fire Punch")
        .setType(GameType.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .buildMove();
    }

    public static Move flameCharge() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.speedStat(a, 1);
        };

        return new MoveBuilder()
        .setId(488)
        .setName("Flame Charge")
        .setType(GameType.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(50)
        .setAction(action)
        .buildMove();
    }

    public static Move flamethrower() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.BURN, 10);
        };

        return new MoveBuilder()
        .setId(53)
        .setName("Flamethrower")
        .setType(GameType.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(90)
        .setAction(action)
        .buildMove();
    }

    public static Move flareBlitz() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamageRecoil(a, d, m, 33);
            MoveAction.statusEffect(d, StatusCondition.BURN, 10);
        };

        return new MoveBuilder()
        .setId(394)
        .setName("Flare Blitz")
        .setType(GameType.FIRE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(120)
        .setAction(action)
        .buildMove();
    }

    public static Move flashCannon() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.spDefenseStat(d, -1, 10);
        };

        return new MoveBuilder()
        .setId(430)
        .setName("Flash Cannon")
        .setType(GameType.STEEL)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(80)
        .setAction(action)
        .buildMove();
    }

    public static Move furyAttack() {
        return new MoveBuilder()
        .setId(31)
        .setName("Fury Attack")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(15)
        .setAccuracy(85)
        .setAction((a, d, m) -> MoveAction.multiHit(a, d, m))
        .buildMove();
    }

    public static Move frenzyPlant() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.rechargeMove(a);
        };

        return new MoveBuilder()
        .setId(338)
        .setName("Frenzy Plant")
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

// GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG

    public static Move gigaDrain() {
        return new MoveBuilder()
        .setId(202)
        .setName("Giga Drain")
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(75)
        .setAction((a, d, m) -> MoveAction.dealDamageDrain(a, d, m, 50))
        .buildMove();
    }

    public static Move gigaImpact() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.rechargeMove(a);
        };

        return new MoveBuilder()
        .setId(416)
        .setName("Giga Impact")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

    public static Move grassKnot() {
        MoveAction action = (a, d, m) -> {
            // Move power varies based on weight
            int power =
            d.weight() <= 21.8
            ? 20
            : d.weight() <= 54.9
                ? 40
                : d.weight() <= 110
                    ? 60
                    : d.weight() < 220.2
                        ? 80
                        : d.weight() < 440.7
                            ? 100
                            : m.power();
            
            m.setPower(power);
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(447)
        .setName("Grass Knot")
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(120) // Varies
        .setContact(true)
        .setAction(action)
        .buildMove();
    }

    public static Move growl() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
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

// HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

    public static Move heatWave() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.BURN, 10);
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

    public static Move hydroPump() {
        return new MoveBuilder()
        .setId(56)
        .setName("Hydro Pump")
        .setType(GameType.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(110)
        .setAccuracy(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move hyperBeam() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.rechargeMove(a);
        };

        return new MoveBuilder()
        .setId(63)
        .setName("Hyper Beam")
        .setType(GameType.NORMAL)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

// IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

    public static Move inferno() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.BURN, 100);
        };

        return new MoveBuilder()
        .setId(517)
        .setName("Inferno")
        .setType(GameType.FIRE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(100)
        .setAccuracy(50)
        .setAction(action)
        .buildMove();
    }

    public static Move ironDefense() {
        return new MoveBuilder()
        .setId(334)
        .setName("Iron Defense")
        .setType(GameType.STEEL)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAction((a, d, m) -> MoveAction.defenseStat(a, 2))
        .buildMove();
    }

// JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK

// LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL

    public static Move leafStorm() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.spAttackStat(a, -2);
        };

        return new MoveBuilder()
        .setId(437)
        .setName("Leaf Storm")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(5)
        .setPower(130)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

    public static Move leechSeed() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.applySeeded(a, d);
        };

        return new MoveBuilder()
        .setId(73)
        .setName("Leech Seed")
        .setType(GameType.GRASS)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

// MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM

    public static Move magicalLeaf() {
        MoveAction action = (a, d, m) -> {

        };

        return new MoveBuilder()
        .setId(345)
        .setName("Magical Leaf")
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(action)
        .buildMove();
    }
// NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
// OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

// PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP

    public static Move petalBlizzard() {
        return new MoveBuilder()
        .setId(572)
        .setName("Petal Blizzard")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(90)
        .setContact(false)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move petalDance() {
        return new MoveBuilder()
        .setId(80)
        .setName("Petal Dance")
        .setType(GameType.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(120)
        .setContact(true)
        .setAction((a, d, m) -> MoveAction.rampageMove(a, d, m))
        .buildMove();
    }

    public static Move poisonJab() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.statusEffect(d, StatusCondition.POISON, 30);
        };

        return new MoveBuilder()
        .setId(398)
        .setName("Poison Jab")
        .setType(GameType.POISON)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(80)
        .setAction(action)
        .buildMove();
    }

    public static Move poisonPowder() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.canApplyEffect(d, StatusCondition.POISON);
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

    public static Move powerWhip() {
        return new MoveBuilder()
        .setId(438)
        .setName("Power Whip")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAccuracy(85)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move protect() {
        return new MoveBuilder()
        .setId(182)
        .setName("Protect")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setPriority(4)
        .setAction((a, d, m) -> MoveAction.pokemonBraces(a, a.conditions().protect(), a + " protected itself!"))
        .buildMove();
    }

// QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ

// RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR

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

    public static Move rapidSpin() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.speedStat(a, 1);
            // TODO: Removes Trap Effects
        };

        return new MoveBuilder()
        .setId(229)
        .setName("Rapid Spin")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(40)
        .setPower(50)
        .setAction(action)
        .buildMove();
    }
    
    public static Move razorLeaf() {
        return new MoveBuilder()
        .setId(75)
        .setName("Razor Leaf")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(25)
        .setPower(55)
        .setAccuracy(95)
        .setCritRatio(Move.HIGH_CRIT)
        .setContact(false)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move rest() {
        MoveAction action = (a, d, m) -> {
            MoveAction.restoreHp(a, 100);
            MoveAction.statusEffect(a, StatusCondition.SLEEP, 100);
        };

        return new MoveBuilder()
        .setId(156)
        .setName("Rest")
        .setType(GameType.PSYCHIC)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(action)
        .buildMove();
    }

    public static Move roar() {
        MoveAction action = (a, d, m) -> {
            // TODO: Implement force switch (need to track Pokemon's owner in Pokemon class)
        };

        return new MoveBuilder()
        .setId(46)
        .setName("Roar")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setPriority(-6)
        .setAction(action)
        .buildMove();
    }

    public static Move rockTomb() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.speedStat(d, -1);
        };

        return new MoveBuilder()
        .setId(317)
        .setName("Rock Tomb")
        .setType(GameType.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(60)
        .setAccuracy(95)
        .setContact(false)
        .setAction(action)
        .buildMove();
    }

// SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    public static Move scaryFace() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.speedStat(d, -2);
        };

        return new MoveBuilder()
        .setId(184)
        .setName("Scary Face")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(action)
        .buildMove();
    }

    public static Move scratch() {
        return new MoveBuilder()
        .setId(10)
        .setName("Scratch")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(35)
        .setPower(40)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move seedBomb() {
        return new MoveBuilder()
        .setId(402)
        .setName("Seed Bomb")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setContact(false)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move shadowClaw() {
        return new MoveBuilder()
        .setId(421)
        .setName("Shadow Claw")
        .setType(GameType.GHOST)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(70)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move shellSmash() {
        MoveAction action = (a, d, m) -> {
            MoveAction.defenseStat(a, -1);
            MoveAction.spDefenseStat(a, -1);
            MoveAction.attackStat(a, 2);
            MoveAction.spAttackStat(a, 2);
            MoveAction.speedStat(a, 2);
        };

        return new MoveBuilder()
        .setId(504)
        .setName("Shell Smash")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAction(action)
        .buildMove();
    }

    public static Move slash() {
        return new MoveBuilder()
        .setId(163)
        .setName("Slash")
        .setType(GameType.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(70)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move sleepPowder() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.canApplyEffect(d, StatusCondition.SLEEP);
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

    public static Move sleepTalk() {
        MoveAction action = (a, d, m) -> {
            // Only works when Pokemon is asleep
            if (!a.hasPrimaryCondition(StatusCondition.SLEEP)){
                BattleLog.add(Move.FAILED);
                return;
            }

            // Uses random move in moveset, doesn't choose Sleep Talk
            Move randomMove = m;
            while (randomMove.equals(m)) {
                int i = RandomValues.generateInt(0, a.moves().length - 1);
                randomMove = a.moves()[i];
            }

            BattleLog.add("%s used %s!", a, randomMove);
            randomMove.action().act(a, d, randomMove);
        };

        return new MoveBuilder()
        .setId(214)
        .setName("Sleep Talk")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
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

    public static Move stompingTantrum() {
        MoveAction action = (a, d, m) -> {
            if (a.conditions().moveInterrupted()) m.doublePower();
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(707)
        .setName("Stomping Tantrum")
        .setType(GameType.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(75)
        .setAction(action)
        .buildMove();
    }

    public static Move stunSpore() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.canApplyEffect(d, StatusCondition.PARALYSIS);
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

    public static Move sweetScent() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.evasionStat(d, -1);
        };

        return new MoveBuilder()
        .setId(230)
        .setName("Sweet Scent")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .buildMove();
    }

    public static Move swift() {
        return new MoveBuilder()
        .setId(129)
        .setName("Swift")
        .setType(GameType.NORMAL)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .buildMove();
    }

    public static Move swordsDance() {
        return new MoveBuilder()
        .setId(14)
        .setName("Swords Dance")
        .setType(GameType.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction((a, d, m) -> MoveAction.attackStat(a, 2))
        .buildMove();
    }

    public static Move synthesis() {
        MoveAction action = (a, d, m) -> {
            double percent = (BattleField.currentWeather == Weather.SUNNY) 
                ? 67
                : (BattleField.currentWeather == Weather.CLEAR) ? 50 : 25;

            MoveAction.restoreHp(a, percent);            
        };

        return new MoveBuilder()
        .setId(235)
        .setName("Synthesis")
        .setType(GameType.GRASS)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(action)
        .buildMove();
    }

// TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
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

    public static Move toxic() {
        MoveAction action = (a, d, m) -> {
            MoveAction.moveHits(a, d, m);
            MoveAction.canApplyEffect(d, StatusCondition.POISON);
        };

        return new MoveBuilder()
        .setId(92)
        .setName("Toxic")
        .setType(GameType.POISON)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAccuracy(90)
        .setAction(action)
        .buildMove();
    }

    public static Move trailblaze() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.speedStat(a, 1);
        };

        return new MoveBuilder()
        .setId(885)
        .setName("Trailblaze")
        .setType(GameType.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(50)
        .setAction(action)
        .buildMove();
    }

// UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU

// VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV

    public static Move venoshock() {
        MoveAction action = (a, d, m) -> {
            if (d.hasPrimaryCondition(StatusCondition.POISON)) m.doublePower();
            MoveAction.dealDamage(a, d, m);
        };

        return new MoveBuilder()
        .setId(474)
        .setName("Venoshock")
        .setType(GameType.POISON)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(65)
        .setAction(action)
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

// WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW

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

    public static Move waterPulse() {
        MoveAction action = (a, d, m) -> {
            MoveAction.dealDamage(a, d, m);
            MoveAction.volatileStatusEffect(d, StatusCondition.CONFUSION, 20);
        };

        return new MoveBuilder()
        .setId(352)
        .setName("Water Pulse")
        .setType(GameType.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .buildMove();
    }

    public static Move waveCrash() {
        return new MoveBuilder()
        .setId(834)
        .setName("Wave Crash")
        .setType(GameType.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAction((a, d, m) -> MoveAction.dealDamageRecoil(a, d, m, 33))
        .buildMove();
    }

    public static Move withdraw() {
        return new MoveBuilder()
        .setId(110)
        .setName("Withdraw")
        .setType(GameType.WATER)
        .setCategory(Move.STATUS)
        .setPP(40)
        .setAction((a, d, m) -> MoveAction.defenseStat(a, 1))
        .buildMove();
    }


// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
// ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ

// STRUGGLE: Used when pokemon is out of moves
    public static Move struggle() {
        return new MoveBuilder()
        .setId(165)
        .setName("Struggle")
        .setType(GameType.TYPELESS)
        .setCategory(Move.PHYSICAL)
        .setPP(-1)
        .setPower(40)
        .setAction((a, d, m) -> MoveAction.dealDamageRecoil(a, d, m, 25))
        .buildMove();
    }

}
