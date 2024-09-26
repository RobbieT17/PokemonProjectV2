package event;

public class GameEvent {

    public static final String MOVE_HITS = "Move-Hits";
    public static final String STAT_CHANGE = "Stat-Change";
    public static final String STATUS_CONDITION_CHANGE = "Status-Condition-Change";
    public static final String SWITCH_IN = "Switch-In";
    public static final String MOVE_MAKES_CONTACT = "Move-Makes-Contact";
    public static final String END_OF_ROUND = "End-Of-Round";
    public static final String DAMAGE_MULTIPLIER = "Damage-Multiplier";
    public static final String MOVE_EFFECTIVENESS = "Move-Effectiveness";
    public static final String FIND_MOVE_ORDER = "Find-Move-Order";
    public static final String WEATHER_EFFECT = "Weather-Effect";
    
    private final Event onMoveHit;// Move hits
    private final Event onStatChange; // Pokemon stats are changed
    private final Event onStatusChange;// Pokemon receives a status condition
    private final Event onSwitchIn;// Pokemon switches in
    private final Event onMoveContact;// Hit with a contact move
    private final Event onEndRound;// End of Round
    private final Event onDamageMultiplier;
    private final Event onMoveEffectiveness;
    private final Event onMoveOrder;
    private final Event onWeatherEffect;

    public GameEvent() {
        this.onMoveHit = new Event(GameEvent.MOVE_HITS);
        this.onStatChange = new Event(GameEvent.STAT_CHANGE);
        this.onStatusChange = new Event(GameEvent.STATUS_CONDITION_CHANGE);
        this.onSwitchIn = new Event(GameEvent.SWITCH_IN);
        this.onMoveContact = new Event(GameEvent.MOVE_MAKES_CONTACT);
        this.onEndRound = new Event(GameEvent.END_OF_ROUND);
        this.onDamageMultiplier = new Event(GameEvent.DAMAGE_MULTIPLIER);
        this.onMoveEffectiveness = new Event(GameEvent.MOVE_EFFECTIVENESS);
        this.onMoveOrder = new Event(GameEvent.FIND_MOVE_ORDER);
        this.onWeatherEffect = new Event(GameEvent.WEATHER_EFFECT);
    }


    // Methods
    public Event getEvent(String eventName) {
        return switch (eventName) {
            case GameEvent.MOVE_HITS -> this.onMoveHit;
            case GameEvent.STAT_CHANGE -> this.onStatChange;
            case GameEvent.STATUS_CONDITION_CHANGE -> this.onStatusChange;
            case GameEvent.SWITCH_IN -> this.onSwitchIn;
            case GameEvent.MOVE_MAKES_CONTACT -> this.onMoveContact;
            case GameEvent.END_OF_ROUND -> this.onEndRound;
            case GameEvent.DAMAGE_MULTIPLIER -> this.onDamageMultiplier;
            case GameEvent.MOVE_EFFECTIVENESS -> this.onMoveEffectiveness;
            case GameEvent.FIND_MOVE_ORDER -> this.onMoveOrder;
            case GameEvent.WEATHER_EFFECT -> this.onWeatherEffect;
            default -> throw new IllegalArgumentException("Invalid event id");
        };
    }

    public void onEvent(String eventName, EventData data) {
        getEvent(eventName).update(data);
    }

    public void addEventSubscriber(String eventName, Observer o) {
        getEvent(eventName).addListener(o);
    }

    public void removeEventSubscriber(String eventName, Observer o) {
        getEvent(eventName).removeListener(o);
    }
}
