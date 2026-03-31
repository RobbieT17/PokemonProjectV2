package project.event;

public class GameEvents {

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
        GameEvents.MOVE_HITS,
        GameEvents.STAT_CHANGE,
        GameEvents.STATUS_CONDITION_CHANGE,
        GameEvents.SWITCH_IN,
        GameEvents.MOVE_MAKES_CONTACT,
        GameEvents.END_OF_TURN,
        GameEvents.END_OF_ROUND,
        GameEvents.DAMAGE_MULTIPLIER,
        GameEvents.MOVE_EFFECTIVENESS,
        GameEvents.FIND_MOVE_ORDER,
        GameEvents.WEATHER_EFFECT,
        GameEvents.MOVE_ACCURACY,
        GameEvents.PRIMARY_STATUS_BEFORE,
        GameEvents.STATUS_BEFORE,
        GameEvents.BEFORE_MOVE,
        GameEvents.SWITCH_OUT,
        GameEvents.MOVE_SELECTION,
        GameEvents.USE_MOVE,
        GameEvents.MOVE_INTERRUPTED
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

    public GameEvents() {
        this.onMoveHit = new Event(GameEvents.MOVE_HITS);
        this.onStatChange = new Event(GameEvents.STAT_CHANGE);
        this.onStatusChange = new Event(GameEvents.STATUS_CONDITION_CHANGE);
        this.onSwitchIn = new Event(GameEvents.SWITCH_IN);
        this.onMoveContact = new Event(GameEvents.MOVE_MAKES_CONTACT);
        this.onEndTurn = new Event(GameEvents.END_OF_TURN);
        this.onEndRound = new Event(GameEvents.END_OF_ROUND);
        this.onDamageMultiplier = new Event(GameEvents.DAMAGE_MULTIPLIER);
        this.onMoveEffectiveness = new Event(GameEvents.MOVE_EFFECTIVENESS);
        this.onMoveOrder = new Event(GameEvents.FIND_MOVE_ORDER);
        this.onWeatherEffect = new Event(GameEvents.WEATHER_EFFECT);
        this.onMoveAccuracy = new Event(GameEvents.MOVE_ACCURACY);
        this.onPrimaryStatusBefore = new Event(GameEvents.PRIMARY_STATUS_BEFORE);
        this.onStatusBefore = new Event(GameEvents.STATUS_BEFORE);
        this.onBeforeMove = new Event(GameEvents.BEFORE_MOVE);
        this.onSwitchOut = new Event(GameEvents.SWITCH_OUT);
        this.onMoveSelection = new Event(GameEvents.MOVE_SELECTION);
        this.onUseMove = new Event(GameEvents.USE_MOVE);
        this.onMoveInterrupted = new Event(GameEvents.MOVE_INTERRUPTED);
    }


    // Methods
    public Event getEvent(String eventName) {
        return switch (eventName) {
            case GameEvents.MOVE_HITS -> this.onMoveHit;
            case GameEvents.STAT_CHANGE -> this.onStatChange;
            case GameEvents.STATUS_CONDITION_CHANGE -> this.onStatusChange;
            case GameEvents.SWITCH_IN -> this.onSwitchIn;
            case GameEvents.MOVE_MAKES_CONTACT -> this.onMoveContact;
            case GameEvents.END_OF_TURN -> this.onEndTurn;
            case GameEvents.END_OF_ROUND -> this.onEndRound;
            case GameEvents.DAMAGE_MULTIPLIER -> this.onDamageMultiplier;
            case GameEvents.MOVE_EFFECTIVENESS -> this.onMoveEffectiveness;
            case GameEvents.FIND_MOVE_ORDER -> this.onMoveOrder;
            case GameEvents.WEATHER_EFFECT -> this.onWeatherEffect;
            case GameEvents.MOVE_ACCURACY -> this.onMoveAccuracy;
            case GameEvents.PRIMARY_STATUS_BEFORE -> this.onPrimaryStatusBefore;
            case GameEvents.STATUS_BEFORE -> this.onStatusBefore;
            case GameEvents.BEFORE_MOVE -> this.onBeforeMove;
            case GameEvents.SWITCH_OUT -> this.onSwitchOut;
            case GameEvents.MOVE_SELECTION -> this.onMoveSelection;
            case GameEvents.USE_MOVE -> this.onUseMove;
            case GameEvents.MOVE_INTERRUPTED -> this.onMoveInterrupted;
            default -> throw new IllegalArgumentException("Invalid event id");
        };
    }

    public void updateEvent(String eventName, EventData data) {
        getEvent(eventName).update(data);
    }

    public void addEventListener(String eventName, String id, Observer e) {
        getEvent(eventName).addListener(id, e);
    }

    public void removeEventListener(String eventName, String id) {
        getEvent(eventName).removeListener(id);
    }

    public void removeEventListener(String[] eventNames, String id) {
        for (String name : eventNames) GameEvents.this.removeEventListener(name, id);
    }

    public void updateEventMaps() {
        for (String name : GameEvents.ALL_EVENTS) getEvent(name).updateMap();
    }
}
