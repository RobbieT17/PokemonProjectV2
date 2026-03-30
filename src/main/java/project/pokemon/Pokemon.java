package project.pokemon;

import project.battle.BattleA;
import project.battle.BattleLog;
import project.battle.Weather;
import project.event.EventData;
import project.event.GameEvent;
import project.exceptions.*;
import project.move.Move;
import project.network.ClientHandler;
import project.player.PokemonTrainer;
import project.stats.*;


public class Pokemon {

// Error Messages
    public static final String INVALID_DAMAGE_ERR = "Damage value must be positive";

// Class Variables
    // Default Level
    public static final int DEFAULT_LEVEL = 50;

// Object Variables
    private final int level; // Pokemon Level, higher level means a stronger pokemon 
    private final String pokemonName; // Pokemon's Name (Given by the player)
    private final PokemonType pokemonType; // Pokemon have one or two types
    private final int pokedexID; // National Pokedex Number

    // Pokemon Stats
    private final HealthPoints hp; // Amount of HP the Pokemon has
    private final PokemonStat[] stats; // [atk, def, spAtk, spDef, spd, acc, eva]
    private final double weight; // Weight of the Pokemon

    // Moves
    private final Move[] moves; // Available moves, can have up to four

    //Conditions
    private final PokemonConditions conditions; 

    // Ability
    private Ability ability;

    // Held Item
    private HeldItem item;

    // Owner of the Pokemon
    private PokemonTrainer owner;

    // Event Listeners
    private final GameEvent events;

    // Other Stats
    private Move moveSelected; // Move selected for the round
    private Move firstMove; // First move used since switched in
    private Move lastMove; // Move used the last turn
    private int damageDealt; // Amount of damage dealt during the round
    private int damageReceived; // Amount of damage received from opposing Pokemon
    private int roundCount; // Rounds since last switch-in

// Constructor
    // Creates a new Pokemon for trainers to use in battle
    public Pokemon(
        int level,
        String name,
        PokemonType types,
        int id,
        double weight,
        HealthPoints hp,
        PokemonStat[] stats, 
        Move[] moves,
        PokemonConditions conditions
    ) {
        this.level = level;
        this.pokemonName = name;
        this.pokemonType = types;

        this.pokedexID = id;
        this.weight = weight;
        this.hp = hp;
        this.stats = stats;

        this.moves = moves;
        this.conditions = conditions;

        this.events = new GameEvent();
    }

// Methods

    /**
     * Uses a move on a target
     * Decrements that moves PP
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    public void useMove(EventData data) {
        BattleLog.add("%s used %s!", this, this.moveSelected);
        try {
            this.events.updateOnEvent(GameEvent.USE_MOVE, data);
            this.moveSelected.getPp().decrement(this.getConditions().hasKey(StatusCondition.FORCED_MOVE_ID));
            this.moveSelected.getAction().act(data);
        } catch (MoveEndedEarlyException e) {
            BattleLog.add(e.getMessage());
        }
    }

    /**
     * Checks any condition, then
     * Pokemon uses their move if actionable
     * Pokemon is considered to have moved (even if they cannot act)
     * 
     * @param move the Move chosen
     * @param defender the target Pokemon
     */
    public void useTurn(EventData data){
        try {
            this.getEvents().updateOnEvent(GameEvent.BEFORE_MOVE, data);
            this.getEvents().updateOnEvent(GameEvent.PRIMARY_STATUS_BEFORE, data);
            this.getEvents().updateOnEvent(GameEvent.STATUS_BEFORE, data);

            this.useMove(data);
            this.conditions.setInterrupted(false); // Successful Move
        } catch (MoveInterruptedException | PokemonCannotActException e) {
            BattleLog.add(e.getMessage());
            this.events.updateOnEvent(GameEvent.MOVE_INTERRUPTED, data);

            // Stops any ongoing moves
            this.conditions.removeCondition(StatusCondition.FOCUSED_ID);
            this.conditions.removeCondition(StatusCondition.FORCED_MOVE_ID);
            this.conditions.removeCondition(StatusCondition.RAMPAGE_ID);
            this.conditions.setInterrupted(true);
        } 
        this.conditions.setHasMoved(true); 
        this.events.updateOnEvent(GameEvent.END_OF_TURN, data);
    }

    /**
     * Takes damage which lowers HP
     * If HP drops to 0, the pokemon faints
     * 
     * @throws IllegalArgumentException if value isn't positive
     * @param value damage received
     */ 
    public void takeDamage(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);

        this.conditions.setTookDamage(true);
        this.damageReceived += value;

        if (this.getConditions().getEndure().isActive()) {
            this.conditions.getEndure().setActive(false);
            takeDamageEndure(value);
        }
        else this.hp.change(-value); 
        if (this.hp.depleted()) this.faints();   
    }

    
    // Takes damage equal to a percent of max HP
    public void takeDamagePercentMaxHP(double percent, String message) {
        int damage = (int) (this.getHp().getCurrentHealthPoints() * percent);
        BattleLog.add("%s took %d damage%s", this, damage, message + "!");
        this.takeDamage(damage);
    }

    /**
     * Takes damage, but prevents HP from dropping to zero
     * @throws IllegalArgumentException if value isn't positive
     * @param value damage received
     */
    private void takeDamageEndure(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);

        this.hp.change(-value);
        if (this.hp.depleted()) {
            this.hp.change(1);
            BattleLog.add("%s endured the hit!", this);
        }
    }

    /**
     * Restores HP
     * @throws IllegalArgumentException if value isn't positive
     * @param value health restored
     */
    public void restoreHP(int value) {
        if (value <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.hp.change(value);
    }

     // Takes damage equal to a percent of max HP
     public void restoreHpPercentMaxHP(double percent, String message) {
        int heal = (int) (this.hp.getCurrentHealthPoints() * percent);
        BattleLog.add("%s restored %d HP%s", this, heal, message + "!");
        this.restoreHP(heal);
    }

    public String listMoveSelect() {
        StringBuilder sb = new StringBuilder();

        sb.append(StatDisplay.showSomeStats(this));
        sb.append("\nPlease select a move >>");

        return sb.toString();
    }

    public boolean hpLessThanPercent(double percent) {
        return this.hp.getCurrentHealthPoints() / (double) this.hp.getMaxHealthPoints() < 0.01 * percent; 
    }

    public boolean firstRound() {
        return this.roundCount == 0;
    }


    // User selects a move through input
    public Move chooseMove(ClientHandler c) {
        Move m = null;

        while (true) {
            try {
                c.writeToBuffer(this.listMoveSelect());
                String input = c.readFromBuffer();
                int i = Integer.parseInt(input);

                m = this.moves[i];
                if (!(m.getPp().depleted() || m.getDisabled())) {
                    break;
                }

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                c.writeToBuffer("Invalid input, try again.");
            }

        }
        return m;
    }


    @Override
    public String toString() {
        return this.pokemonName;
    }


// Boolean Methods
    public boolean hasNoMoves() {
        for (Move m : this.moves) 
            if (!(m.getPp().depleted() || m.getDisabled())) return false;
        return true;
    }

    public boolean isType(String type) {
        return this.pokemonType.typeEquals(type);
    }
 
// Setters
    public void faints() {
        this.conditions.setFainted(true);
        BattleLog.add("%s fainted!", this);
    } 
    
    public void addDealtDamage(int d) {
        if (d <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.damageDealt += d;
    }

    public void addDamageReceived(int d) {
        if (d <= 0) throw new IllegalArgumentException(Pokemon.INVALID_DAMAGE_ERR);
        this.damageReceived += d;
    }

    public void resetDamageDealt() {
        this.damageDealt = 0;
    }

    public void setMove(Move m) {
        this.moveSelected = m;
    }

    public void resetMove() {
        if (this.moveSelected == null) return;
        if (this.firstRound()) this.firstMove = this.moveSelected;

        this.moveSelected.resetStats();
        this.lastMove = this.moveSelected;
        this.moveSelected = null;
    }

    public void clearStatMods() {
        for (PokemonStat s : this.stats) s.resetMod();
    }

    public void afterEffects() {
        if (BattleA.skipRound) {
            this.conditions.setSwitchedIn(false);
            return;
        }
        if (this.conditions.isFainted()) {
            this.conditions.clearPrimary();
            this.conditions.clearVolatileConditions();
            return;
        }
            
        this.clearStatMods();
        this.conditions.setTookDamage(false);
        this.conditions.setHasMoved(false);
     
    
        if (this.conditions.isSwitchedIn()) {
            this.conditions.setSwitchedIn(false);
            return;
        }

        this.resetMove();
 
        try {
            this.getEvents().updateOnEvent(GameEvent.END_OF_ROUND, null);
            this.getEvents().updateOnEvent(GameEvent.WEATHER_EFFECT, null);
            Weather.weatherEffect(this);
        } catch (PokemonFaintedException e) {
        }  
        
        this.damageDealt = 0;
        this.damageReceived = 0;
        this.roundCount++;
    }

    // Clears any temporary effects and volatile conditions
    public void backToTrainer() {
        this.resetMove();
        this.conditions.clearAtReturn();
        this.firstMove = null;
        this.lastMove = null;   
        this.damageDealt = 0;
        this.damageReceived = 0;  
        this.roundCount = 0;

        this.events.updateOnEvent(GameEvent.SWITCH_OUT, null);
    }

    public void removeItem() {
        if (this.item == null) return;
        this.item.removeEffect();
    }

// Setters
    public void setAbility(Ability a) {this.ability = a;}
    public void setOwner(PokemonTrainer pt) {this.owner = pt;}
    public void setItem(HeldItem i) {this.item = i;}

// Getters
    public int getLevel() {return this.level;}
    public String getPokemonName() {return this.pokemonName;}
    public PokemonType getPokemonType() {return this.pokemonType;}
    public int getPokedexID() {return this.pokedexID;}
    public HealthPoints getHp() {return this.hp;}
    public PokemonStat[] getStats() {return this.stats;}
    public PokemonStat getAttack() {return this.stats[PokemonStat.ATTACK];}
	public PokemonStat getDefense() {return this.stats[PokemonStat.DEFENSE];}
	public PokemonStat getSpecialAttack() {return this.stats[PokemonStat.SPECIAL_ATTACK];}
	public PokemonStat getSpecialDefense() {return this.stats[PokemonStat.SPECIAL_DEFENSE];}
	public PokemonStat getSpeed() {return this.stats[PokemonStat.SPEED];}
	public PokemonStat getAccuracy() {return this.stats[PokemonStat.ACCURACY];}
	public PokemonStat getEvasion() {return this.stats[PokemonStat.EVASION];}
	public double getWeight() {return this.weight;}
    public Move[] getMoves() {return this.moves;}
    public PokemonConditions getConditions() {return this.conditions;}
    public Move getMoveSelected() {return this.moveSelected;}
    public Move getFirstMove() {return this.firstMove;}
    public Move getLastMove() {return this.lastMove;}
    public int getDamageDealt() {return this.damageDealt;}
    public int getDamageReceived() {return this.damageReceived;}
    public int getRoundCount() {return this.roundCount;}
    public Ability getAbility() {return this.ability;}
    public HeldItem getItem() {return this.item;}
    public PokemonTrainer getOwner() {return this.owner;}
    public GameEvent getEvents() {return this.events;}

}
