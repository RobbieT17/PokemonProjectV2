package project.game.move;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.movelist.DarkMoveList;
import project.game.move.movelist.DragonMoveList;
import project.game.move.movelist.ElectricMoveList;
import project.game.move.movelist.FairyMoveList;
import project.game.move.movelist.FightingMoveList;
import project.game.move.movelist.FireMoveList;
import project.game.move.movelist.FlyingMoveList;
import project.game.move.movelist.GhostMoveList;
import project.game.move.movelist.GrassMoveList;
import project.game.move.movelist.GroundMoveList;
import project.game.move.movelist.IceMoveList;
import project.game.move.movelist.NormalMoveList;
import project.game.move.movelist.PoisonMoveList;
import project.game.move.movelist.PsychicMoveList;
import project.game.move.movelist.RockMoveList;
import project.game.move.movelist.SoundMoveList;
import project.game.move.movelist.SpaceMoveList;
import project.game.move.movelist.SteelMoveList;
import project.game.move.movelist.WaterMoveList;
import project.game.move.movelist.ZombieMoveList;

public enum Movedex {
    Acid_Spray(PoisonMoveList::acidSpray),
    Acrobatics(FlyingMoveList::acrobatics),
    Aerial_Ace(FlyingMoveList::aerialAce),
    Air_Cutter(FlyingMoveList::airCutter),
    Air_Slash(FlyingMoveList::airSlash),
    Amnesia(PsychicMoveList::amnesia),
    Aqua_Jet(WaterMoveList::aquaJet),
    Aqua_Tail(WaterMoveList::aquaTail),
    Aura_Sphere(FightingMoveList::auraSphere),
    Avalanche(IceMoveList::avalanche),
    Bite(DarkMoveList::bite),
    Blast_Burn(FireMoveList::blastBurn),
    Blizzard(IceMoveList::blizzard),
    Body_Slam(NormalMoveList::bodySlam),
    Breaking_Swipe(DragonMoveList::breakingSwipe),
    Brick_Break(FightingMoveList::brickBreak),
    Bullet_Seed(GrassMoveList::bulletSeed),
    Bulldoze(GroundMoveList::bulldoze),
    Charm(FairyMoveList::charm),
    Chilling_Water(WaterMoveList::chillingWater),
    Confuse_Ray(GhostMoveList::confuseRay),
    Cosmic_Punch(SpaceMoveList::cosmicPunch),
    Crunch(DarkMoveList::crunch),
    Curse(GhostMoveList::curse),
    Dark_Pulse(DarkMoveList::darkPulse),
    Dig(GroundMoveList::dig),
    Dive(WaterMoveList::dive),
    Double_Edge(NormalMoveList::doubleEdge),
    Dragon_Breath(DragonMoveList::dragonBreath),
    Dragon_Claw(DragonMoveList::dragonClaw),
    Dragon_Dance(DragonMoveList::dragonDance),
    Dragon_Pulse(DragonMoveList::dragonPulse),
    Dragon_Tail(DragonMoveList::dragonTail),
    Earth_Power(GroundMoveList::earthPower),
    Earthquake(GroundMoveList::earthquake),
    Ember(FireMoveList::ember),
    Endure(NormalMoveList::endure),
    Energy_Ball(GrassMoveList::energyBall),
    Facade(NormalMoveList::facade),
    False_Swipe(NormalMoveList::falseSwipe),
    Fake_Out(DarkMoveList::fakeOut),
    Fire_Blast(FireMoveList::fireBlast),
    Fire_Fang(FireMoveList::fireFang),
    Fire_Pledge(FireMoveList::firePledge),
    Fire_Punch(FireMoveList::firePunch),
    Flame_Charge(FireMoveList::flameCharge),
    Flamethrower(FireMoveList::flamethrower),
    Flare_Blitz(FireMoveList::flareBlitz),
    Flash_Cannon(SteelMoveList::flashCannon),
    Flip_Turn(WaterMoveList::flipTurn),
    Fly(FlyingMoveList::fly),
    Focus_Blast(FightingMoveList::focusBlast),
    Focus_Punch(FightingMoveList::focusPunch),
    Frenzy_Plant(GrassMoveList::frenzyPlant),
    Fury_Attack(NormalMoveList::furyAttack),
    Giga_Drain(GrassMoveList::gigaDrain),
    Giga_Impact(NormalMoveList::gigaImpact),
    Grave_Shock(ZombieMoveList::graveShock),
    Grass_Knot(GrassMoveList::grassKnot),
    Gravity_Well(SpaceMoveList::gravityWell),
    Growl(NormalMoveList::growl),
    Growth(NormalMoveList::growth),
    Gyro_Ball(SteelMoveList::gyroBall),
    Hail(IceMoveList::hail),
    Haze(IceMoveList::haze),
    Heat_Crash(FireMoveList::heatCrash),
    Heat_Wave(FireMoveList::heatWave),
    Hydro_Cannon(WaterMoveList::hydroCannon),
    Hydro_Pump(WaterMoveList::hydroPump),
    Hyper_Beam(NormalMoveList::hyperBeam),
    Hurricane(FlyingMoveList::hurricane),
    Ice_Beam(IceMoveList::iceBeam),
    Ice_Punch(IceMoveList::icePunch),
    Ice_Spinner(IceMoveList::iceSpinner),
    Infect(ZombieMoveList::infect),
    Inferno(FireMoveList::inferno),
    Iron_Defense(SteelMoveList::ironDefense),
    Icy_Wind(IceMoveList::icyWind),
    Leaf_Storm(GrassMoveList::leafStorm),
    Leech_Seed(GrassMoveList::leechSeed),
    Liquidation(WaterMoveList::liquidation),
    Magical_Leaf(GrassMoveList::magicalLeaf),
    Mud_Shot(GroundMoveList::mudShot),
    Muddy_Water(WaterMoveList::muddyWater),
    Necrotic_Claw(ZombieMoveList::necroticClaw),
    Orbit_Strike(SpaceMoveList::orbitStrike),
    Outrage(DragonMoveList::outrage),
    Overheat(FireMoveList::overheat),
    Petal_Blizzard(GrassMoveList::petalBlizzard),
    Petal_Dance(GrassMoveList::petalDance),
    Poison_Jab(PoisonMoveList::poisonJab),
    Poison_Powder(PoisonMoveList::poisonPowder),
    Power_Whip(GrassMoveList::powerWhip),
    Protect(NormalMoveList::protect),
    Rapid_Spin(NormalMoveList::rapidSpin),
    Rain_Dance(WaterMoveList::rainDance),
    Razor_Leaf(GrassMoveList::razorLeaf),
    Rest(PsychicMoveList::rest),
    Roar(SoundMoveList::roar),
    Rock_Slide(RockMoveList::rockSlide),
    Rock_Tomb(RockMoveList::rockTomb),
    Rotten_Bite(ZombieMoveList::rottenBite),
    Sandstorm(RockMoveList::sandstorm),
    Scary_Face(NormalMoveList::scaryFace),
    Scratch(NormalMoveList::scratch),
    Scorching_Sands(GroundMoveList::scorchingSands),
    Seed_Bomb(GrassMoveList::seedBomb),
    Shadow_Claw(GhostMoveList::shadowClaw),
    Shell_Smash(NormalMoveList::shellSmash),
    Slash(NormalMoveList::slash),
    Sleep_Powder(GrassMoveList::sleepPowder),
    Sleep_Talk(NormalMoveList::sleepTalk),
    Smack_Down(RockMoveList::smackDown),
    Smokescreen(NormalMoveList::smokescreen),
    Solar_Beam(GrassMoveList::solarBeam),
    Stellar_Beam(SpaceMoveList::stellarBeam),
    Stomping_Tantrum(GroundMoveList::stompingTantrum),
    Struggle(NormalMoveList::struggle),
    Stun_Spore(GrassMoveList::stunSpore),
    Sunny_Day(FireMoveList::sunnyDay),
    Surf(WaterMoveList::surf),
    Swift(SpaceMoveList::swift),
    Sweet_Scent(NormalMoveList::sweetScent),
    Swords_Dance(NormalMoveList::swordsDance),
    Synthesis(GrassMoveList::synthesis),
    Tackle(NormalMoveList::tackle),
    Tail_Whip(NormalMoveList::tailWhip),
    Take_Down(NormalMoveList::takeDown),
    Temper_Flare(FireMoveList::temperFlare),
    Thunder_Punch(ElectricMoveList::thunderPunch),
    Toxic(PoisonMoveList::toxic),
    Trailblaze(GrassMoveList::trailblaze),
    Venoshock(PoisonMoveList::venoshock),
    Vine_Whip(GrassMoveList::vineWhip),
    Viral_Spread(ZombieMoveList::viralSpread),
    Water_Gun(WaterMoveList::waterGun),
    Water_Pledge(WaterMoveList::waterPledge),
    Water_Pulse(WaterMoveList::waterPulse),
    Wave_Crash(WaterMoveList::waveCrash),
    Whirlpool(WaterMoveList::whirlpool),
    Will_O_Wisp(FireMoveList::willOWisp),
    Withdraw(WaterMoveList::withdraw),
    Zen_Headbutt(PsychicMoveList::zenHeadbutt);

    private final Function<EventManager, Integer> func;

    Movedex(Function<EventManager, Integer> func) {
        this.func = func;
    }

    public void act(EventManager e) {
        this.func.apply(e);
    }

    /**
     * Processes the selected move
     * @param moveName Selected Move
     * @param e EventManager
     */
    public static void processMove(String moveName, EventManager e) {
        // Transform string to match Pascal_Case of enum values
        String id = moveName.replaceAll("[ -]", "_");
        Movedex.valueOf(id).act(e);;
    }
}
