package project.game.move.movelist;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.battle.BattleField;
import project.game.battle.Weather;
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

public class GrassMoveList {

    public enum GrassMoveName {

        Bullet_Seed(GrassMoveList::bulletSeed),
        Energy_Ball(GrassMoveList::energyBall),
        Frenzy_Plant(GrassMoveList::frenzyPlant),
        Giga_Drain(GrassMoveList::gigaDrain),
        Grass_Knot(GrassMoveList::grassKnot),
        Leaf_Storm(GrassMoveList::leafStorm),
        Leech_Seed(GrassMoveList::leechSeed),
        Magical_Leaf(GrassMoveList::magicalLeaf),
        Petal_Blizzard(GrassMoveList::petalBlizzard),
        Petal_Dance(GrassMoveList::petalDance),
        Power_Whip(GrassMoveList::powerWhip),
        Razor_Leaf(GrassMoveList::razorLeaf),
        Seed_Bomb(GrassMoveList::seedBomb),
        Sleep_Powder(GrassMoveList::sleepPowder),
        Solar_Beam(GrassMoveList::solarBeam),
        Stun_Spore(GrassMoveList::stunSpore),
        Synthesis(GrassMoveList::synthesis),
        Trailblaze(GrassMoveList::trailblaze),
        Vine_Whip(GrassMoveList::vineWhip);

        private final Function<EventManager, Integer> func;

        GrassMoveName(Function<EventManager, Integer> func) {
            this.func = func;
        }

        public void act(EventManager e) {
            this.func.apply(e);
        }
    }

    public static int bulletSeed(EventManager e) {
        MoveActionAttackDamage.multiHit(e);
        return 0;
    }

    public static int energyBall(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, -1, 0, 0, 0), 10);
        return 0;
    }

    public static int frenzyPlant(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionCharge.rechargeMove(e);
        return 0;
    }

    public static int gigaDrain(EventManager e) {
        MoveActionAttackDamage.dealDamageDrain(e, 50);
        return 0;
    }

    public static int grassKnot(EventManager e) {
        Pokemon d = e.eventData.attackTarget;
        e.eventData.moveUsed.setPower(
            d.getWeight() <= 21.8 ? 20 :
            d.getWeight() <= 54.9 ? 40 :
            d.getWeight() <= 110 ? 60 :
            d.getWeight() < 220.2 ? 80 :
            d.getWeight() < 440.7 ? 100 : 120
        );
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int leafStorm(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, -2, 0, 0, 0, 0));
        return 0;
    }

    public static int leechSeed(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.SEEDED_ID);
        return 0;
    }

    public static int magicalLeaf(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int petalBlizzard(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int petalDance(EventManager e) {
        MoveActionCharge.rampageMove(e);
        return 0;
    }

    public static int powerWhip(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int razorLeaf(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int seedBomb(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }

    public static int sleepPowder(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.SLEEP_ID);
        MoveAction.displayFailMessage(e);
        return 0;
    }

    public static int solarBeam(EventManager e) {
        if (BattleField.currentWeather == Weather.SUNNY) {
            MoveActionAttackDamage.dealDamage(e);
        } else {
            MoveActionCharge.chargeMove(e);
        }
        return 0;
    }

    public static int stunSpore(EventManager e) {
        MoveActionAccuracy.moveHits(e);
        MoveActionChangeCondition.applyCondition(e, StatusConditionID.PARALYSIS_ID);
        return 0;
    }

    public static int synthesis(EventManager e) {
        MoveListHelperFunctions.targetsUser(e.eventData);
        double percent = (BattleField.currentWeather == Weather.SUNNY) ? 67 :
                         (BattleField.currentWeather == Weather.CLEAR) ? 50 : 25;
        MoveActionHealthRestore.restoreHp(e, percent);
        return 0;
    }

    public static int trailblaze(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        MoveActionChangeStat.changeStats(e, MoveListHelperFunctions.stats(0, 0, 0, 0, 1, 0, 0));
        return 0;
    }

    public static int vineWhip(EventManager e) {
        MoveActionAttackDamage.dealDamage(e);
        return 0;
    }
}
