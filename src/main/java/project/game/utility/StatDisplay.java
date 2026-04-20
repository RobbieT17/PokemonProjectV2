package project.game.utility;

import project.game.battle.BattlePosition;
import project.game.pokemon.Pokemon;
import project.game.pokemon.effects.StatusCondition;

public interface StatDisplay {
    
    public static String listStats(Pokemon p) {
        return new StringBuilder()
        .append(p.getAttack().showStat())
        .append(p.getDefense().showStat())
        .append(p.getSpecialAttack().showStat())
        .append(p.getSpecialDefense().showStat())
        .append(p.getSpeed().showStat())
        .append(p.getAccuracy().showStat())
        .append(p.getEvasion().showStat())
        .toString();
    }

    public static String listMoves(Pokemon p) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.getMoves().size(); i++) 
            sb.append(String.format("[%d] %s", i, p.getMoves().get(i).moveStats())); 
            
        return sb.toString();
    }


    public static String showVolatileConditions(Pokemon p) {
        StringBuilder sb = new StringBuilder();

        for (StatusCondition c : p.getConditions().getVolatileConditions().values()) 
            sb.append(c.getEffectName())
            .append(", ");
        
        String s = sb.toString();
        return s.length() > 2 ? s.substring(0, s.length() - 2) : s;    
      
    }

    private static String getStats(Pokemon p) {
        return String.format("%s <%s> (HP: %s) %s%n", 
        p, p.getPokemonType(), p.getHp(), p.getConditions());
    }

    private static String toListItem(Pokemon p, String prefix) {
        return String.format("- %s %s", prefix, StatDisplay.getStats(p));
    }

    private static String toNumItem(Pokemon p, int n, String prefix) {
        return String.format("[%d] %s %s", n, prefix, StatDisplay.getStats(p));
    } 


    public static String showPartyStats(Pokemon p) {
        return String.format(StatDisplay.toListItem(p, ""));
    }

    public static String showPartyStats(Pokemon p, int n) {
        return String.format(StatDisplay.toNumItem(p, n, ""));
    }

    public static String showPartyStats(Pokemon p, String prefix) {
        return String.format(StatDisplay.toListItem(p, prefix));
    }

    public static String showPartyStats(Pokemon p, int n, String prefix) {
        return String.format(StatDisplay.toNumItem(p, n, prefix));
    }

    public static String showAbility(Pokemon p) {
        return p.getAbility() != null ? p.getAbility().toString() : "";
    }

    public static String showItem(Pokemon p) {
        return p.getItem() != null ? p.getItem().toString() : "";
    }


    // List Pokemon's stats
    public static String showAllStats(Pokemon p) {
		return new StringBuilder()
		.append(String.format("Name: %s  |  ", p))
		.append(String.format("Type: %s  |  ", p.getPokemonType().toString()))
        .append(String.format("LEVEL %d  |  ", p.getLevel()))
		.append(String.format("Pokedex #: %d%n", p.getPokedexID()))
		.append(String.format("%nHP: %s%n", p.getHp().toString()))
		.append(String.format("%n%s", listStats(p)))
        .append(String.format("%nMOVES: %n%s", listMoves(p)))
        .toString();
	}

    public static String showSomeStats(Pokemon p) {
        return new StringBuilder()
        .append(String.format("============================================================%n"))
        .append(String.format("Name: %s  |  ", p))
        .append(String.format("Type: %s  |  ", p.getPokemonType().toString()))
        .append(String.format("HP: %s%n", p.getHp().toString()))
        .append(String.format("%nAbility: %s%n", showAbility(p)))
        .append(String.format("Item: %s%n", showItem(p)))
        .append(String.format("%nStatus Effect: %s%n", p.getConditions()))
        .append(String.format("Other Effects: %s%n", showVolatileConditions(p)))
        .append(String.format("============================================================%n"))
        .append(String.format("MOVES: %n%s", listMoves(p)))
        .append(String.format("============================================================%n"))
        .toString();
    }


    public static String displayTargetsSelected(BattlePosition[] targets, Pokemon p) {
        StringBuilder sb = new StringBuilder("Target(s): ");

        for (BattlePosition target : targets) {
            Pokemon targetPokemon = target.getCurrentPokemon();
            sb.append(targetPokemon == p ? "Self" : targetPokemon)
            .append(", ");
        }

        // Removes trailing comma
        String s = sb.toString();
        return s.length() > 2 ? s.substring(0, s.length() - 2) : s;  
    }
}
