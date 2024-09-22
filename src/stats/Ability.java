package stats;

import battle.BattleField;
import battle.BattleLog;
import battle.Weather;
import event.EventData;
import event.GameEvent;
import event.Observer;
import exceptions.MoveInterruptedException;
import move.Move;
import pokemon.Pokemon;

public abstract class Ability implements Observer {
    // Abilities List
    public static final int BLAZE_ID = 0;
    public static final int CHLOROPHYLL_ID = 1;
    public static final int OVERGROW_ID = 2; 
    public static final int TORRENT_ID = 3;
    public static final int WATER_ABSORB_ID = 4;

    
    // Variables
    protected  final Pokemon owner;

    // Constructor
    public Ability(Pokemon p) {
        this.owner = p;
    }

    // Abstract Methods
    public abstract String abilityName();
    public abstract int abilityId();

    // Getters
    public Pokemon owner() {return this.owner;}
  
    public static class BlazeAbility extends Ability {
        public BlazeAbility(Pokemon p) {
            super(p);
            GameEvent.onDamageMultiplier.addListener(this);
        }

        @Override
        public void act(EventData e) {
            if (this.owner != e.user()) return;

            Move move = e.moveUsed();
            if (move.isType(Type.FIRE) && this.owner.hpLessThanPercent(33)) 
                move.changePowerByPercent(50);
        }

        @Override
        public String abilityName() {
            return "Blaze";
        }

        @Override
        public int abilityId() {
            return Ability.BLAZE_ID;
        }
    }


    public static class ChlorophyllAbility extends Ability {

        public ChlorophyllAbility(Pokemon p) {
            super(p);
        }

        @Override
        public void act(EventData e) {

            if (BattleField.currentWeather == Weather.SUNNY)
                owner.modifySpeedByPercent(200);
        }

        @Override
        public String abilityName() {
            return "Chlorophyll";
        }

        @Override
        public int abilityId() {
            return Ability.CHLOROPHYLL_ID;
        }
        
    }

    public static class OvergrowAbility extends Ability {
        public OvergrowAbility(Pokemon p) {
            super(p);
            GameEvent.onDamageMultiplier.addListener(this);
        }

        @Override
        public void act(EventData e) {
            if (this.owner != e.user()) return;
            Move move = e.moveUsed();

            if (move.isType(Type.GRASS) && this.owner.hpLessThanPercent(95)) {
                move.changePowerByPercent(50);
                BattleLog.add("%s's Overgrow increased their attack power!", this.owner);
            }
                
        }

        @Override
        public String abilityName() {
            return "Overgrow";
        }

        @Override
        public int abilityId() {
            return Ability.OVERGROW_ID;
        }
        
    }

    public static class TorrentAbility extends Ability {
        public TorrentAbility(Pokemon p) {
            super(p);
            GameEvent.onDamageMultiplier.addListener(this);
        }

        @Override
        public void act(EventData e) {
            if (this.owner != e.user()) return;

            Move move = e.moveUsed();

            if (move.isType(Type.WATER) && this.owner.hpLessThanPercent(33)) 
                move.changePowerByPercent(50);
        }

        @Override
        public String abilityName() {
            return "Torrent";
        }

        @Override
        public int abilityId() {
            return Ability.TORRENT_ID;
        }
        
    }

    public static class WaterAbsorb extends Ability {
        public WaterAbsorb(Pokemon p) {
            super(p);
            GameEvent.onMoveEffectiveness.addListener(this);
        }

        @Override
        public void act(EventData e) {
            if (this.owner != e.target()) return;

            Move move = e.moveUsed();
            if (move.isType(Type.WATER)) 
                throw new MoveInterruptedException("But %s's Water Absorb soaked up the water!", this.owner);
            
        }

        @Override
        public String abilityName() {
            return "Water Absorb";
        }

        @Override
        public int abilityId() {
            return Ability.WATER_ABSORB_ID;
        }
    }

    

    
}
