package project.stats;

import project.pokemon.Pokemon;

public interface StatDisplay {
    
    public static String listStats(Pokemon p) {
        return new StringBuilder()
        .append(p.attack().showStat())
        .append(p.defense().showStat())
        .append(p.specialAttack().showStat())
        .append(p.specialDefense().showStat())
        .append(p.speed().showStat())
        .append(p.accuracy().showStat())
        .append(p.evasion().showStat())
        .toString();
    }

    public static String listMoves(Pokemon p) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < p.moves().length; i++) 
            sb.append(String.format("[%d] %s", i, p.moves()[i].moveStats())); 
            
        return sb.toString();
    }

    public static String showCondition(Pokemon p) {
        if (p.conditions().fainted()) return "FAINTED";
        if (!p.conditions().hasPrimary()) return "";
        return p.conditions().primaryCondition().toString();
    }

    public static String showVolatileConditions(Pokemon p) {
        StringBuilder sb = new StringBuilder();


        for (StatusCondition c : p.conditions().volatileConditions().values()) 
            sb.append(c.effectName())
            .append(", ");
        
        String s = sb.toString();
        return s.length() > 2 ? s.substring(0, s.length() - 2) : s;    
      
    }

    public static String showPartyStats(Pokemon p) {
        return String.format("%s (HP: %s) %s%n", p, p.hp(), showCondition(p));
    }

    public static String showItem(Pokemon p) {
        return p.item() != null ? p.item().toString() : "";
    }


    // List Pokemon's stats
    public static String showAllStats(Pokemon p) {
		return new StringBuilder()
		.append(String.format("Name: %s  |  ", p))
		.append(String.format("Type: %s  |  ", p.pokemonType().toString()))
        .append(String.format("LEVEL %d  |  ", p.level()))
		.append(String.format("Pokedex #: %d%n", p.pokedexID()))
		.append(String.format("%nHP: %s%n", p.hp().toString()))
		.append(String.format("%n%s", listStats(p)))
        .append(String.format("%nMOVES: %n%s", listMoves(p)))
        .toString();
	}

    public static String showSomeStats(Pokemon p) {
        return new StringBuilder()
        .append(String.format("Name: %s  |  ", p))
        .append(String.format("Type: %s%n", p.pokemonType().toString()))
        .append(String.format("%nHP: %s%n", p.hp().toString()))
        .append(String.format("%nItem: %s%n", showItem(p)))
        .append(String.format("%nStatus Effect: %s%n", showCondition(p)))
        .append(String.format("Other Effects: %s%n", showVolatileConditions(p)))
        .append(String.format("%nMOVES: %n%s", listMoves(p)))
        .toString();
    }
}
