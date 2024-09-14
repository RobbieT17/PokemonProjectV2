package stats;

import battle.BattleField;
import battle.Weather;
import event.EventData;
import event.Observer;
import move.Move;
import pokemon.Pokemon;

public abstract class Ability implements Observer {
    // Abilities List
    public static final int BLAZE_ID = 0;
    public static final int CHLOROPHYLL_ID = 1;
    public static final int OVERGROW_ID = 2; 
    public static final int TORRENT_ID = 3;

    
    // Variables
    private final Pokemon owner;

    // Constructor
    public Ability(Pokemon p) {
        this.owner = p;
    }

    // Abstract Methods
    public abstract String abilityName();
    public abstract int abilityId();

    // Getters
    public Pokemon owner() {return this.owner;}
  
    public class BlazeAbility extends Ability {
        public BlazeAbility(Pokemon p) {
            super(p);
        }

        @Override
        public void act(EventData e) {
            Move move = e.moveUsed();

            if (move.isType(Type.FIRE) && owner.hpLessThanPercent(33)) 
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


    public class ChlorophyllAbility extends Ability {

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


    public class OvergrowAbility extends Ability {
        public OvergrowAbility(Pokemon p) {
            super(p);
        }

        @Override
        public void act(EventData e) {
            Move move = e.moveUsed();

            if (move.isType(Type.GRASS) && owner.hpLessThanPercent(33)) 
                move.changePowerByPercent(50);
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

    public class TorrentAbility extends Ability {
        public TorrentAbility(Pokemon p) {
            super(p);
        }

        @Override
        public void act(EventData e) {
            Move move = e.moveUsed();

            if (move.isType(Type.WATER) && owner.hpLessThanPercent(33)) 
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

    

    
}
