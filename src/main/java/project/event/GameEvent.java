package project.event;

public class GameEvent {

    public static final String MOVE_HITS = "Move-Hits";
    public static final String STAT_CHANGE = "Stat-Change";
    public static final String STATUS_CONDITION_CHANGE = "Status-Condition-Change";
    public static final String SWITCH_IN = "Switch-In";
    public static final String MOVE_MAKES_CONTACT = "Move-Makes-Contact";
    public static final String END_OF_TURN = "End-Of-Turn";
    public static final String END_OF_ROUND = "End-Of-Round";
    public static final String DAMAGE_MULTIPLIER = "Damage-Multiplier";
    public static final String MOVE_EFFECTIVENESS = "Move-Effectiveness";
    public static final String FIND_MOVE_ORDER = "Find-Move-Order";
    public static final String WEATHER_EFFECT = "Weather-Effect";
    public static final String MOVE_ACCURACY = "Move-Accuracy";
    public static final String PRIMARY_STATUS_BEFORE = "Primary-Status-Effect-Before";
    public static final String STATUS_BEFORE = "Status-Effect-Before";
    public static final String BEFORE_MOVE = "Before-Move";
    public static final String SWITCH_OUT = "Switch-Out";
    public static final String MOVE_SELECTION = "Move-Selection";
    public static final String USE_MOVE = "Use-Move";
    public static final String MOVE_INTERRUPTED = "Move-Interrupted";

    public static final String[] ALL_EVENTS = {
        GameEvent.MOVE_HITS,
        GameEvent.STAT_CHANGE,
        GameEvent.STATUS_CONDITION_CHANGE,
        GameEvent.SWITCH_IN,
        GameEvent.MOVE_MAKES_CONTACT,
        GameEvent.END_OF_TURN,
        GameEvent.END_OF_ROUND,
        GameEvent.DAMAGE_MULTIPLIER,
        GameEvent.MOVE_EFFECTIVENESS,
        GameEvent.FIND_MOVE_ORDER,
        GameEvent.WEATHER_EFFECT,
        GameEvent.MOVE_ACCURACY,
        GameEvent.PRIMARY_STATUS_BEFORE,
        GameEvent.STATUS_BEFORE,
        GameEvent.BEFORE_MOVE,
        GameEvent.SWITCH_OUT,
        GameEvent.MOVE_SELECTION,
        GameEvent.USE_MOVE,
        GameEvent.MOVE_INTERRUPTED
    };
    
    private final Event onMoveHit;// Move hits
    private final Event onStatChange; // Pokemon stats are changed
    private final Event onStatusChange;// Pokemon receives a status condition
    private final Event onSwitchIn;// Pokemon switches in
    private final Event onMoveContact;// Hit with a contact move
    private final Event onEndTurn;// End of Turn
    private final Event onEndRound;// End of Round
    private final Event onDamageMultiplier; // Additional move power booster
    private final Event onMoveEffectiveness; 
    private final Event onMoveOrder;
    private final Event onWeatherEffect;
    private final Event onMoveAccuracy;
    private final Event onPrimaryStatusBefore;
    private final Event onStatusBefore; // Status Effects triggered before moving
    private final Event onBeforeMove; // Before move (ex. Pokemon might flinch)
    private final Event onSwitchOut; // Attempt to switch Pokemon out
    private final Event onMoveSelection; // When Pokemon is choosing a move
    private final Event onUseMove; 
    private final Event onMoveInterrupted;

    public GameEvent() {
        this.onMoveHit = new Event(GameEvent.MOVE_HITS);
        this.onStatChange = new Event(GameEvent.STAT_CHANGE);
        this.onStatusChange = new Event(GameEvent.STATUS_CONDITION_CHANGE);
        this.onSwitchIn = new Event(GameEvent.SWITCH_IN);
        this.onMoveContact = new Event(GameEvent.MOVE_MAKES_CONTACT);
        this.onEndTurn = new Event(GameEvent.END_OF_TURN);
        this.onEndRound = new Event(GameEvent.END_OF_ROUND);
        this.onDamageMultiplier = new Event(GameEvent.DAMAGE_MULTIPLIER);
        this.onMoveEffectiveness = new Event(GameEvent.MOVE_EFFECTIVENESS);
        this.onMoveOrder = new Event(GameEvent.FIND_MOVE_ORDER);
        this.onWeatherEffect = new Event(GameEvent.WEATHER_EFFECT);
        this.onMoveAccuracy = new Event(GameEvent.MOVE_ACCURACY);
        this.onPrimaryStatusBefore = new Event(GameEvent.PRIMARY_STATUS_BEFORE);
        this.onStatusBefore = new Event(GameEvent.STATUS_BEFORE);
        this.onBeforeMove = new Event(GameEvent.BEFORE_MOVE);
        this.onSwitchOut = new Event(GameEvent.SWITCH_OUT);
        this.onMoveSelection = new Event(GameEvent.MOVE_SELECTION);
        this.onUseMove = new Event(GameEvent.USE_MOVE);
        this.onMoveInterrupted = new Event(GameEvent.MOVE_INTERRUPTED);
    }


    // Methods
    public Event getEvent(String eventName) {
        return switch (eventName) {
            case GameEvent.MOVE_HITS -> this.onMoveHit;
            case GameEvent.STAT_CHANGE -> this.onStatChange;
            case GameEvent.STATUS_CONDITION_CHANGE -> this.onStatusChange;
            case GameEvent.SWITCH_IN -> this.onSwitchIn;
            case GameEvent.MOVE_MAKES_CONTACT -> this.onMoveContact;
            case GameEvent.END_OF_TURN -> this.onEndTurn;
            case GameEvent.END_OF_ROUND -> this.onEndRound;
            case GameEvent.DAMAGE_MULTIPLIER -> this.onDamageMultiplier;
            case GameEvent.MOVE_EFFECTIVENESS -> this.onMoveEffectiveness;
            case GameEvent.FIND_MOVE_ORDER -> this.onMoveOrder;
            case GameEvent.WEATHER_EFFECT -> this.onWeatherEffect;
            case GameEvent.MOVE_ACCURACY -> this.onMoveAccuracy;
            case GameEvent.PRIMARY_STATUS_BEFORE -> this.onPrimaryStatusBefore;
            case GameEvent.STATUS_BEFORE -> this.onStatusBefore;
            case GameEvent.BEFORE_MOVE -> this.onBeforeMove;
            case GameEvent.SWITCH_OUT -> this.onSwitchOut;
            case GameEvent.MOVE_SELECTION -> this.onMoveSelection;
            case GameEvent.USE_MOVE -> this.onUseMove;
            case GameEvent.MOVE_INTERRUPTED -> this.onMoveInterrupted;
            default -> throw new IllegalArgumentException("Invalid event id");
        };
    }

    public void updateOnEvent(String eventName, EventData data) {
        getEvent(eventName).update(data);
    }

    public void addEventListener(String eventName, String id, Observer e) {
        getEvent(eventName).addListener(id, e);
    }

    public void removeEventListener(String eventName, String id) {
        getEvent(eventName).removeListener(id);
    }

    public void removeEventListener(String[] eventNames, String id) {
        for (String name : eventNames) GameEvent.this.removeEventListener(name, id);
    }

    public void updateEventMaps() {
        for (String name : GameEvent.ALL_EVENTS) getEvent(name).updateMap();
    }
}
