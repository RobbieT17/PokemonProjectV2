package project.game.event;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.event.GameEvents.EventID;
import project.game.move.Move;
import project.game.move.Move.MoveCategory;
import project.game.move.MoveActions;
import project.game.pokemon.Pokemon;
import project.game.processors.Processor;

public class EventManager {
    
    public final EventData data;
    public final MoveActions moveActions;

    private Processor processor;

    public EventManager(BattleData data, Pokemon user) {
        this.data = new EventData(data, user, user, user.getMoveSelected());
        this.moveActions = new MoveActions(this);

    }
  
    public EventManager(BattleData data, Pokemon user, Pokemon target) {
        this.data = new EventData(data, user, target, user.getMoveSelected());
        this.moveActions = new MoveActions(this);
    }

    public EventManager(BattleData data, Pokemon user, Pokemon target, Move move, MoveActions actions) {
        this.data = new EventData(data, user, target, move);
        this.moveActions = actions;
    }
    
    /**
     * Logs failed Status moves
     */
    public void logFailMessage() {
        if (this.data.moveUsed.isCategory(MoveCategory.Status)) {
            BattleLog.add(this.data.message);
        }
    }

    /**
     * Notifies the user Pokemon (the move user) of the specified event 
     * @param id Event ID
     */
    public void notifyUserPokemon(EventID id) {
        this.data.attackUser.getEvents().updateEvent(id, this.data);
    }

    /**
     * Notifies the attack target Pokemon of the specified event
     * @param id Event ID
     */
    public void notifyAttackTargetPokemon(EventID id) {
        this.data.attackTarget.getEvents().updateEvent(id, this.data);
    }

    /**
     * Notifes the effect target Pokemon of the specified event.
     * @param id Event ID
     */
    public void notifyEffectTargetPokemon(EventID id) {
        this.data.effectTarget.getEvents().updateEvent(id, this.data);   
    }

    public void updateEventMaps() {
        this.data.attackUser.getEvents().updateEventMaps();
    }

    public void startNewProcess(Processor processor) {
        this.processor = processor;
        this.processor.process();
    }

    public Processor getProcessor() {
        return this.processor;
    }

}
