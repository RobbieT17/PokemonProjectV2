package move;

import battle.BattleField;
import battle.Weather;
import stats.GameType;
import stats.StatusCondition;

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

// DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
    
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

// EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

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
        .setAction((a, d, m) -> MoveAction.pokemonEndures(a))
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

// GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG

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
        .setAction((a, d, m) -> MoveAction.pokemonProtects(a))
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
