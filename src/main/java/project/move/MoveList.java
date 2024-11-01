package project.move;

import project.battle.BattleField;
import project.battle.BattleLog;
import project.battle.Weather;
import project.event.EventData;
import project.exceptions.MoveInterruptedException;
import project.pokemon.Pokemon;
import project.stats.StatusCondition;
import project.stats.Type;
import project.utility.RandomValues;

// Interface storing all available Pokemon moves (Moves listed alphabetically)
public interface MoveList {

    private static int[] stats(int atk, int def, int spAtk, int spDef, int spd, int acc, int eva) {
        return new int[] {atk, def, spAtk, spDef, spd, acc, eva};
    }

    private static void targetSelf(EventData data) {
        data.attackTarget = data.user;
        data.effectTarget = data.user;
    }

    private static void effectSelf(EventData data) {
        data.effectTarget = data.user;
    }

// AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
    
    public static Move acidSpray() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, -2, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(491)
        .setName("Acid Spray")
        .setType(Type.POISON)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(40)
        .setAction(action)
        .build();
    }

    public static Move acrobatics() {
        return new MoveBuilder()
        .setId(512)
        .setName("Acrobatics")
        .setType(Type.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(55)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move aerialAce() {
        return new MoveBuilder()
        .setId(332)
        .setName("Aerial Ace")
        .setType(Type.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move airCutter() {
        return new MoveBuilder()
        .setId(314)
        .setName("Air Cutter")
        .setType(Type.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(25)
        .setPower(60)
        .setAccuracy(95)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move airSlash() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FLINCH_ID, 30);
        };

        return new MoveBuilder()
        .setId(403)
        .setName("Air Slash")
        .setType(Type.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(75)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }

    public static Move amnesia() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 1, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(133)
        .setName("Amnesia")
        .setType(Type.PSYCHIC)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move aquaTail() {
        return new MoveBuilder()
        .setId(401)
        .setName("Aqua Tail")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(90)
        .setAccuracy(90)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move auraSphere() {
        return new MoveBuilder()
        .setId(396)
        .setName("Aura Sphere")
        .setType(Type.FIGHTING)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(80)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move avalanche() {
        MoveAction action =  e -> {
            if (e.user.conditions().tookDamage()) e.moveUsed.doublePower();
            MoveAction.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(419)
        .setName("Avalanche")
        .setType(Type.ICE)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(60)
        .setPriority(-4)
        .setAction(action)
        .build();
    }

    
// BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB

    public static Move bite() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e ,StatusCondition.FLINCH_ID,30);
        };

        return new MoveBuilder()
        .setId(44)
        .setName("Bite")
        .setType(Type.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(25)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move blastBurn() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.rechargeMove(e);
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

    public static Move blizzard() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FREEZE_ID, 10);
        };

        return new MoveBuilder()
        .setId(59)
        .setName("Blizzard")
        .setType(Type.ICE)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(110)
        .setAccuracy(70)
        .setAction(action)
        .build();
    }

    public static Move bodySlam() {
        MoveAction action = e -> {
            // TODO: Double Damage when Pokemon has minimized condition
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.PARALYSIS_ID, 30);
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

    public static Move breakingSwipe() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(-1, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(784)
        .setName("Breaking Swipe")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move brickBreak() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            // TODO: Disables Light Screen / Reflect
        };

        return new MoveBuilder()
        .setId(280)
        .setName("Brick Break")
        .setType(Type.FIGHTING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move bulldoze() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(523)
        .setName("Bulldoze")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(60)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move bulletSeed() {
        return new MoveBuilder()
        .setId(331)
        .setName("Bullet Seed")
        .setType(Type.GRASS)
        .setCategory(Move.PHYSICAL)
        .setPP(30)
        .setPower(25)
        .setContact(false)
        .setAction(e -> MoveAction.multiHit(e))
        .build();
    }

// CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
    
    public static Move charm() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.changeStats(e, stats(-2, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(204)
        .setName("Charm")
        .setType(Type.FAIRY)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move chillingWater() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(-1, 0, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(886)
        .setName("Chilling Water")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(50)
        .setAction(action)
        .build();
    }

    public static Move confuseRay() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.CONFUSION_ID);
            MoveAction.displayFailMessage(e);
        };

        return new MoveBuilder()
        .setId(109)
        .setName("Confuse Ray")
        .setType(Type.GHOST)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }


    public static Move crunch() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, -1, 0, 0, 0, 0, 0), 20);
        };

        return new MoveBuilder()
        .setId(242)
        .setName("Crunch")
        .setType(Type.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move curse() {
        // TODO: Two Verison of move, Ghost-Type, and Non-Ghost-Type
        MoveAction action = e -> {
            MoveAction.changeStats(e, stats(1, 1, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(174)
        .setName("Curse")
        .setType(Type.GHOST)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(action)
        .build();
    }

// DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
    
    public static Move darkPulse() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FLINCH_ID, 20);
        };

        return new MoveBuilder()
        .setId(399)
        .setName("Dark Pulse")
        .setType(Type.DARK)
        .setCategory(Move.SPECIAL)
        .setPP(80)
        .setAction(action)
        .build();
    }


    public static Move dig() {
        return new MoveBuilder()
        .setId(91)
        .setName("Dig")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(80)
        .setAction(e -> MoveAction.enterImmuneState(e, StatusCondition.DIG_ID))
        .build();
    }

    public static Move dive() {
        return new MoveBuilder()
        .setId(291)
        .setName("Dive")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(80)
        .setAction(e -> MoveAction.enterImmuneState(e, StatusCondition.DIVE_ID))
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
        .setAction(e -> MoveAction.dealDamageRecoil(e, 33))
        .build();
    }

    public static Move dragonBreath() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.PARALYSIS_ID, 30);
        };

        return new MoveBuilder()
        .setId(225)
        .setName("Dragon Breath")
        .setType(Type.DRAGON)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move dragonClaw() {
        return new MoveBuilder()
        .setId(337)
        .setName("Dragon Claw")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move dragonDance() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.changeStats(e, stats(1, 0, 0, 0, 1, 0, 0));
        };

        return new MoveBuilder()
        .setId(349)
        .setName("Dragon Dance")
        .setType(Type.DRAGON)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setAction(action)
        .build();
    }

    public static Move dragonPulse() {
        return new MoveBuilder()
        .setId(406)
        .setName("Dragon Pulse")
        .setType(Type.DRAGON)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(85)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move dragonTail() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            // TODO: Force Switch
        };

        return new MoveBuilder()
        .setId(525)
        .setName("Dragon Tail")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(60)
        .setAccuracy(90)
        .setPriority(-6)
        .setAction(action)
        .build();
    }

// EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE

    public static Move earthquake() {
        MoveAction action = e -> {
            // Deals double power to opponents digging
            if (e.attackTarget.conditions().hasKey(StatusCondition.DIG_ID)) e.moveUsed.doublePower(); 
            MoveAction.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(89)
        .setName("Earthquake")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(100)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move earthPower() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(414)
        .setName("Earth Power")
        .setType(Type.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
        .build();
    }

    public static Move ember() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 10); 
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

    public static Move endure() {
        return new MoveBuilder()
        .setId(203)
        .setName("Endure")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setPriority(4)
        .setAction(e -> MoveAction.pokemonProtects(e, e.user.conditions().endure(), e.user + " braced itself!"))
        .build();
    }

    public static Move energyBall() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, -1, 0, 0, 0), 10);
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

// FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF

    public static Move facade() {
        MoveAction action = e -> {
            Pokemon a = e.user;
            // Double power (140) if user is burned, paralyzed, or poisoned
            if (a.conditions().hasKey(StatusCondition.BURN_ID) |
            a.conditions().hasKey(StatusCondition.PARALYSIS_ID) |
            a.conditions().hasKey(StatusCondition.POISON_ID))
                e.moveUsed.doublePower();

            MoveAction.dealDamage(e);
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

    public static Move fakeOut() {
        MoveAction action = e -> {
            if (!e.user.firstRound()) throw new MoveInterruptedException(Move.FAILED);

            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FLINCH_ID);
        };

        return new MoveBuilder()
        .setId(252)
        .setName("Fake Out")
        .setType(Type.DARK)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(40)
        .setAction(action)
        .build();
    }

    public static Move falseSwipe() {
        MoveAction action = e -> {
            // Leaves opponent with at least 1 HP
            e.attackTarget.conditions().endure().setActive(true);
            MoveAction.dealDamage(e);
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

    public static Move fireBlast() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 30);
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
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 10);
            MoveAction.applyCondition(e, StatusCondition.FLINCH_ID, 30);
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
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 10);
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
            effectSelf(e);
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, 1, 0, 0));
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
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 10);
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
            MoveAction.dealDamageRecoil(e, 33);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 10);
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

    public static Move flashCannon() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(430)
        .setName("Flash Cannon")
        .setType(Type.STEEL)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move flipTurn() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            // TODO: Switch User out
        };

        return new MoveBuilder()
        .setId(812)
        .setName("Flip Turn")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move fly() {
        return new MoveBuilder()
        .setId(19)
        .setName("Fly")
        .setType(Type.FLYING)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(90)
        .setAccuracy(95)
        .setAction(e -> MoveAction.enterImmuneState(e, StatusCondition.FLY_ID))
        .build();
    }

    public static Move focusBlast() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, -1, 0, 0, 0), 10);
        };

        return new MoveBuilder()
        .setId(411)
        .setName("Focus Blast")
        .setType(Type.FIGHTING)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(120)
        .setAccuracy(70)
        .setAction(action)
        .build();
    }

    public static Move focusPunch() {
        return new MoveBuilder()
        .setId(264)
        .setName("Focus Punch")
        .setType(Type.FIGHTING)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(150)
        .setPriority(-3)
        .setAction(e -> MoveAction.focusMove(e))
        .build();
    }

    public static Move frenzyPlant() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.rechargeMove(e);
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

    public static Move furyAttack() {
        return new MoveBuilder()
        .setId(31)
        .setName("Fury Attack")
        .setType(Type.NORMAL)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(15)
        .setAccuracy(85)
        .setAction(e -> MoveAction.multiHit(e))
        .build();
    }

    

// GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG

    public static Move gigaDrain() {
        return new MoveBuilder()
        .setId(202)
        .setName("Giga Drain")
        .setType(Type.GRASS)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(75)
        .setAction(e -> MoveAction.dealDamageDrain(e, 50))
        .build();
    }

    public static Move gigaImpact() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.rechargeMove(e);
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

    public static Move grassKnot() {
        MoveAction action = e -> {
            Pokemon d = e.attackTarget;
            // Move power varies based on weight 
            e.moveUsed.setPower(
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
                                : 120);
            MoveAction.dealDamage(e);
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

    public static Move growl() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.changeStats(e, stats(0, -1, 0, 0, 0, 0, 0));
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
            targetSelf(e);
            if (BattleField.currentWeather == Weather.SUNNY) {
                MoveAction.changeStats(e, stats(2, 0, 2, 0, 0, 0, 0));
                return;
            }
          
            MoveAction.changeStats(e, stats(1, 0, 1, 0, 0, 0, 0));
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

    public static Move gyroBall() {
        MoveAction action = e -> {
            e.moveUsed.setPower((int) (25.0 * e.attackTarget.speed().power() / (double) e.user.speed().power() + 1));
            MoveAction.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(360)
        .setName("Gyro Ball")
        .setType(Type.STEEL)
        .setCategory(Move.PHYSICAL)
        .setPP(5)
        .setPower(-1) // Varies
        .setAction(action)
        .build();
    }

// HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH

    public static Move hail() {
        return new MoveBuilder()
        .setId(258)
        .setName("Hail")
        .setType(Type.ICE)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(e -> MoveAction.changeWeather(e, Weather.HAIL))
        .build();
    }

    public static Move haze() {
        MoveAction action = e -> {
            MoveAction.resetStats(e, e.user);
            MoveAction.resetStats(e, e.attackTarget);
        };

        return new MoveBuilder()
        .setId(114)
        .setName("Haze")
        .setType(Type.ICE)
        .setCategory(Move.STATUS)
        .setPP(30)
        .setAction(action)
        .build();
    }

    public static Move heatCrash() {
        MoveAction action = e -> {
            /*
             * Power varies based on the weight of both user and the target
             * The greater the difference, the greater the power
             */
            double ratio = e.user.weight() / e.attackTarget.weight();

            e.moveUsed.setPower(
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
            MoveAction.dealDamage(e);
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
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 10);
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

    public static Move hurricane() {
        MoveAction action = e -> {
            Move m = e.moveUsed;
            // Perfect accuracy in the rain, 50% accuracy in harsh sunlight
            if (BattleField.currentWeather == Weather.RAIN) m.perfectAccuracy();
            else if (BattleField.currentWeather == Weather.SUNNY) m.setAccuracy(50);

           MoveAction.dealDamage(e);
           MoveAction.applyCondition(e, StatusCondition.CONFUSION_ID, 10);
        };

        return new MoveBuilder()
        .setId(542)
        .setName("Hurricane")
        .setType(Type.FLYING)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(110)
        .setAccuracy(70)
        .setAction(action)
        .build();
    }

    public static Move hydroCannon() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.rechargeMove(e);
        };

        return new MoveBuilder()
        .setId(308)
        .setName("Hydro Cannon")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(150)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move hydroPump() {
        return new MoveBuilder()
        .setId(56)
        .setName("Hydro Pump")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(5)
        .setPower(110)
        .setAccuracy(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move hyperBeam() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.rechargeMove(e);
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

// IIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII

    public static Move iceBeam() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FREEZE_ID, 10);
        };

        return new MoveBuilder()
        .setId(58)
        .setName("Ice Beam")
        .setType(Type.ICE)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAction(action)
        .build();
    }

    public static Move icePunch() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FREEZE_ID, 10);
        };

        return new MoveBuilder()
        .setId(8)
        .setName("Ice Punch")
        .setType(Type.ICE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move iceSpinner() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            // TODO: Clears Terrain effect
        };

        return new MoveBuilder()
        .setId(861)
        .setName("Ice Spinner")
        .setType(Type.ICE)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setAction(action)
        .build();
    }


    public static Move icyWind() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(196)
        .setName("Icy Wind")
        .setType(Type.ICE)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(55)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }

    public static Move infect() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.INFECT_ID);
        };

        return new MoveBuilder()
        .setId(964)
        .setName("Infect")
        .setType(Type.ZOMBIE)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(30)
        .setAction(action)
        .build();
    }


    public static Move inferno() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID);
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

    public static Move ironDefense() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.changeStats(e, stats(0, 2, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(334)
        .setName("Iron Defense")
        .setType(Type.STEEL)
        .setCategory(Move.STATUS)
        .setPP(15)
        .setAction(action)
        .build();
    }

// JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJ
// KKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKKK

// LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL

    public static Move leafStorm() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, -2, 0, 0, 0, 0));
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
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.SEEDED_ID);
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

    public static Move liquidation() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, -1, 0, 0, 0, 0, 0), 20);
        };

        return new MoveBuilder()
        .setId(710)
        .setName("Liquidation")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(85)
        .setAction(action)
        .build();
    }


// MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM

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

    public static Move muddyWater() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, 0, -1, 0), 30);
        };

        return new MoveBuilder()
        .setId(330)
        .setName("Muddy Water")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(90)
        .setAccuracy(85)
        .setAction(action)
        .build();
    }

    public static Move mudShot() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, -1, 0, 0));
        };

        return new MoveBuilder()
        .setId(341)
        .setName("Mud Shot")
        .setType(Type.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(55)
        .setAccuracy(95)
        .setAction(action)
        .build();
    }



// NNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN

// OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO

    public static Move outrage() {
        return new MoveBuilder()
        .setId(200)
        .setName("Outrage")
        .setType(Type.DRAGON)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAction(e -> MoveAction.rampageMove(e))
        .build();
    }

    public static Move overheat() {
        MoveAction action = e -> {
            effectSelf(e);
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(-2, 0, 0, 0, 0, 0, 0));
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

// PPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPPP

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
        .setAction(e -> MoveAction.rampageMove(e))
        .build();
    }

    public static Move poisonJab() {
        MoveAction action = (e) -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.POISON_ID, 30);
        };

        return new MoveBuilder()
        .setId(398)
        .setName("Poison Jab")
        .setType(Type.POISON)
        .setCategory(Move.PHYSICAL)
        .setPP(20)
        .setPower(80)
        .setAction(action)
        .build();
    }

    public static Move poisonPowder() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.POISON_ID);
            MoveAction.displayFailMessage(e);
        };

        return new MoveBuilder()
        .setId(77)
        .setName("Poison Powder")
        .setType(Type.POISON)
        .setCategory(Move.STATUS)
        .setPP(35)
        .setAccuracy(100)
        .setAction(action)
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

    public static Move protect() {
        return new MoveBuilder()
        .setId(182)
        .setName("Protect")
        .setType(Type.NORMAL)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setPriority(4)
        .setAction(e -> MoveAction.pokemonProtects(e, e.user.conditions().protect(), e.user + " protected itself!"))
        .build();
    }

// QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ

// RRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRRR

    public static Move rainDance() {
        return new MoveBuilder()
        .setId(240)
        .setName("Rain Dance")
        .setType(Type.WATER)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(e -> MoveAction.changeWeather(e, Weather.RAIN))
        .build();
    }

    public static Move rapidSpin() {
        MoveAction action = e -> {
            effectSelf(e);
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, 1, 0, 0));
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

    public static Move rest() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.restoreHp(e, 100);
            MoveAction.applyCondition(e, StatusCondition.SLEEP_ID);
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

    public static Move roar() {
        MoveAction action = e -> {
            // TODO: Implement force switch (need to track Pokemon's owner in Pokemon class)
        };

        return new MoveBuilder()
        .setId(46)
        .setName("Roar")
        .setType(Type.SOUND)
        .setCategory(Move.STATUS)
        .setPP(20)
        .setPriority(-6)
        .setAction(action)
        .build();
    }

    public static Move rockSlide() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FLINCH_ID, 30);
        };

        return new MoveBuilder()
        .setId(157)
        .setName("Rock Slide")
        .setType(Type.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(75)
        .setAccuracy(90)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move rockTomb() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, -1, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(317)
        .setName("Rock Tomb")
        .setType(Type.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(60)
        .setAccuracy(95)
        .setContact(false)
        .setAction(action)
        .build();
    }

// SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS
    
    public static Move sandstorm() {
        return new MoveBuilder()
        .setId(201)
        .setName("Sandstorm")
        .setType(Type.ROCK)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAction(e -> MoveAction.changeWeather(e, Weather.SANDSTORM))
        .build();
    }

    public static Move scaryFace() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, -2, 0, 0));
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

    public static Move scorchingSands() {
        MoveAction action = e -> {
            e.user.conditions().removeCondition(StatusCondition.FREEZE_ID);
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID, 30);
        };

        return new MoveBuilder()
        .setId(815)
        .setName("Scorching Sands")
        .setType(Type.GROUND)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(70)
        .setAction(action)
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

    public static Move shadowClaw() {
        return new MoveBuilder()
        .setId(421)
        .setName("Shadow Claw")
        .setType(Type.GHOST)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(70)
        .setCritRatio(Move.HIGH_CRIT)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move shellSmash() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.changeStats(e, stats(1, -1, 1, -1, 1, 0, 0));
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

    public static Move sleepPowder() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.SLEEP_ID);
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

    public static Move sleepTalk() {
        MoveAction action = e -> {
            Pokemon a = e.user;
            Move m = e.moveUsed;
            // Only works when Pokemon is asleep
            if (!a.conditions().hasKey(StatusCondition.SLEEP_ID)){
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
            randomMove.action().act(new EventData(a, e.attackTarget, m));
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

    public static Move smackDown() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.leaveImmuneState(e, StatusCondition.FLY_ID, "Fell from the sky!");
            MoveAction.applyCondition(e, StatusCondition.GROUNDED_ID);
        };

        return new MoveBuilder()
        .setId(479)
        .setName("Smack Down")
        .setType(Type.ROCK)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(50)
        .setContact(false)
        .setAction(action)
        .build();
    }

    public static Move smokescreen() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, 0, -1, 0));
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

    public static Move solarBeam() {
        MoveAction action = e -> {
            if (BattleField.currentWeather == Weather.SUNNY) MoveAction.dealDamage(e);
            else MoveAction.chargeMove(e);
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

    public static Move stompingTantrum() {
        MoveAction action = e -> {
            if (e.user.conditions().interrupted()) e.moveUsed.doublePower();
            MoveAction.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(707)
        .setName("Stomping Tantrum")
        .setType(Type.GROUND)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move stunSpore() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.PARALYSIS_ID);
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

    public static Move sunnyDay() {  
        return new MoveBuilder()
        .setId(241)
        .setName("Sunny Day")
        .setType(Type.FIRE)
        .setCategory(Move.STATUS)
        .setPP(5)
        .setAction(e -> MoveAction.changeWeather(e, Weather.SUNNY))
        .build();
    }

    public static Move surf() {
        return new MoveBuilder()
        .setId(57)
        .setName("Surf")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(90)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move sweetScent() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, 0, 0, -1));
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

    public static Move swift() {
        return new MoveBuilder()
        .setId(129)
        .setName("Swift")
        .setType(Type.SPACE)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAccuracy(Move.ALWAYS_HITS)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move swordsDance() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.changeStats(e, stats(2, 0, 0, 0, 0, 0, 0));
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

    public static Move synthesis() {
        MoveAction action = e -> {
            targetSelf(e);
            double percent = (BattleField.currentWeather == Weather.SUNNY) 
                ? 67
                : (BattleField.currentWeather == Weather.CLEAR) ? 50 : 25;

            MoveAction.restoreHp(e, percent);            
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

// TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT
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
            MoveAction.moveHits(e);
            MoveAction.changeStats(e, stats(0, -1, 0, 0, 0, 0, 0));
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
        .setAction(e -> MoveAction.dealDamageRecoil(e, 25))
        .build();
    }

    public static Move temperFlare() {
        MoveAction action = e -> {
            if (e.user.conditions().interrupted()) e.moveUsed.doublePower();
            MoveAction.dealDamage(e);
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

    public static Move thunderPunch() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.PARALYSIS_ID, 10);
        };

        return new MoveBuilder()
        .setId(9)
        .setName("Thunder Punch")
        .setType(Type.ELECTRIC)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(75)
        .setAction(action)
        .build();
    }

    public static Move toxic() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.BAD_POISON_ID);
        };

        return new MoveBuilder()
        .setId(92)
        .setName("Toxic")
        .setType(Type.POISON)
        .setCategory(Move.STATUS)
        .setPP(10)
        .setAccuracy(90)
        .setAction(action)
        .build();
    }

    public static Move trailblaze() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.changeStats(e, stats(0, 0, 0, 0, 1, 0, 0));
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

// UUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUU

// VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV

    public static Move venoshock() {
        MoveAction action = e -> {
            if (e.attackTarget.conditions().hasKey(StatusCondition.POISON_ID)) e.moveUsed.doublePower();
            MoveAction.dealDamage(e);
        };

        return new MoveBuilder()
        .setId(474)
        .setName("Venoshock")
        .setType(Type.POISON)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(65)
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

// WWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW

    public static Move waterGun() {
        return new MoveBuilder()
        .setId(55)
        .setName("Water Gun")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPower(40)
        .setPP(25)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move waterPledge() {
        return new MoveBuilder()
        .setId(518)
        .setName("Water Pledge")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(10)
        .setPower(80)
        .setAction(MoveAction.DEFAULT_ACTION)
        .build();
    }

    public static Move waterPulse() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.CONFUSION_ID, 20);
        };

        return new MoveBuilder()
        .setId(352)
        .setName("Water Pulse")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(20)
        .setPower(60)
        .setAction(action)
        .build();
    }

    public static Move waveCrash() {
        return new MoveBuilder()
        .setId(834)
        .setName("Wave Crash")
        .setType(Type.WATER)
        .setCategory(Move.PHYSICAL)
        .setPP(10)
        .setPower(120)
        .setAction(e -> MoveAction.dealDamageRecoil(e, 33))
        .build();
    }

    public static Move whirlpool() {
        MoveAction action = e -> {
            if (e.attackTarget.conditions().hasKey(StatusCondition.DIVE_ID)) e.moveUsed.doublePower();
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.BOUND_ID);
        };

        return new MoveBuilder()
        .setId(250)
        .setName("Whirlpool")
        .setType(Type.WATER)
        .setCategory(Move.SPECIAL)
        .setPP(15)
        .setPower(35)
        .setAccuracy(85)
        .setAction(action)
        .build();
    }

    public static Move willOWisp() {
        MoveAction action = e -> {
            MoveAction.moveHits(e);
            MoveAction.applyCondition(e, StatusCondition.BURN_ID);
            BattleLog.add(e.message);
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

    public static Move withdraw() {
        MoveAction action = e -> {
            targetSelf(e);
            MoveAction.changeStats(e, stats(0, 1, 0, 0, 0, 0, 0));
        };

        return new MoveBuilder()
        .setId(110)
        .setName("Withdraw")
        .setType(Type.WATER)
        .setCategory(Move.STATUS)
        .setPP(40)
        .setAction(action)
        .build();
    }


// XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
// YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
// ZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZZ

    public static Move zenHeadbutt() {
        MoveAction action = e -> {
            MoveAction.dealDamage(e);
            MoveAction.applyCondition(e, StatusCondition.FLINCH_ID, 20);
        };

        return new MoveBuilder()
        .setId(428)
        .setName("Zen Headbutt")
        .setType(Type.PSYCHIC)
        .setCategory(Move.PHYSICAL)
        .setPP(15)
        .setPower(80)
        .setAccuracy(90)
        .setAction(action)
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
        .setAction(e -> MoveAction.dealDamageRecoil(e, 25))
        .build();
    }

}
