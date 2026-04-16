package project.game.move;

import java.util.function.Function;

import project.game.event.EventManager;
import project.game.move.movelist.DarkMoveList;
import project.game.move.movelist.FireMoveList;
import project.game.move.movelist.FlyingMoveList;
import project.game.move.movelist.GrassMoveList;
import project.game.move.movelist.GroundMoveList;
import project.game.move.movelist.IceMoveList;
import project.game.move.movelist.NormalMoveList;
import project.game.move.movelist.PoisonMoveList;
import project.game.move.movelist.RockMoveList;
import project.game.move.movelist.SpaceMoveList;
import project.game.move.movelist.SteelMoveList;
import project.game.move.movelist.WaterMoveList;

public enum Movedex {
    Avalanche(IceMoveList::avalanche),
    Earthquake(GroundMoveList::earthquake),
    Endure(NormalMoveList::endure),
    Facade(NormalMoveList::facade),
    False_Swipe(NormalMoveList::falseSwipe),
    Fake_Out(DarkMoveList::fakeOut),
    Grass_Knot(GrassMoveList::grassKnot),
    Gravity_Well(SpaceMoveList::gravitySwitch),
    Growth(NormalMoveList::growth),
    Gyro_Ball(SteelMoveList::gyroBall),
    Haze(IceMoveList::haze),
    Heat_Crash(FireMoveList::heatCrash),
    Hurricane(FlyingMoveList::hurricane),
    Protect(NormalMoveList::protect),
    Scorching_Sands(GroundMoveList::scorchingSands),
    Sleep_Talk(NormalMoveList::sleepTalk),
    Smack_Down(RockMoveList::smackDown),
    Solar_Beam(GrassMoveList::solarBeam),
    Stomping_Tantrum(GroundMoveList::stompingTantrum),
    Synthesis(GrassMoveList::synthesis),
    Temper_Flare(FireMoveList::temperFlare),
    Venoshock(PoisonMoveList::venoshock),
    Whirlpool(WaterMoveList::whirlpool);

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
    public static void processMove(EventManager e) {
        String moveName = e.data.moveUsed.getMoveName();
        Movedex.valueOf(moveName).act(e);
    }
}
