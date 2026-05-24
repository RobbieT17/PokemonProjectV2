package project.game.move;

import java.util.function.Function;

import project.game.event.EventManager;


public enum Movedex {
    Avalanche(MoveList::avalanche),
    Earthquake(MoveList::earthquake),
    Endure(MoveList::endure),
    Facade(MoveList::facade),
    False_Swipe(MoveList::falseSwipe),
    Fake_Out(MoveList::fakeOut),
    Grass_Knot(MoveList::grassKnot),
    Growth(MoveList::growth),
    Gyro_Ball(MoveList::gyroBall),
    Haze(MoveList::haze),
    Heat_Crash(MoveList::heatCrash),
    Hurricane(MoveList::hurricane),
    Protect(MoveList::protect),
    Scorching_Sands(MoveList::scorchingSands),
    Sleep_Talk(MoveList::sleepTalk),
    Smack_Down(MoveList::smackDown),
    Solar_Beam(MoveList::solarBeam),
    Stomping_Tantrum(MoveList::stompingTantrum),
    Synthesis(MoveList::synthesis),
    Temper_Flare(MoveList::temperFlare),
    Venoshock(MoveList::venoshock),
    Whirlpool(MoveList::whirlpool);

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
