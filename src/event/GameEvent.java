package event;

public class GameEvent {
    public static final Event onMoveHit = new Event(0);// Move hits
    public static final Event onStatChange = new Event(1); // Pokemon stats are changed
    public static final Event onStatusChange = new Event(2);// Pokemon receives a status condition
    public static final Event onSwitchIn = new Event(3);// Pokemon switches in
    public static final Event onMoveContact = new Event(4);// Hit with a contact move
    public static final Event onEndRound = new Event(5);// End of Round
    public static final Event onDamageMultiplier = new Event(6);
    public static final Event onMoveEffectiveness = new Event(7);
    public static final Event onMoveOrder = new Event(8);
    public static final Event onWeatherEffect = new Event(9);
}
