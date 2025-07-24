package project.stats;

import project.pokemon.Pokemon;

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
        for (int i = 0; i < p.getMoves().length; i++) 
            sb.append(String.format("[%d] %s", i, p.getMoves()[i].moveStats())); 
            
        return sb.toString();
    }

    public static String showCondition(Pokemon p) {
        if (p.getConditions().isFainted()) return "FAINTED";
        if (!p.getConditions().hasPrimary()) return "";
        return p.getConditions().getPrimaryCondition().toString();
    }

    public static String showVolatileConditions(Pokemon p) {
        StringBuilder sb = new StringBuilder();


        for (StatusCondition c : p.getConditions().getVolatileConditions().values()) 
            sb.append(c.getEffectName())
            .append(", ");
        
        String s = sb.toString();
        return s.length() > 2 ? s.substring(0, s.length() - 2) : s;    
      
    }

    public static String showPartyStats(Pokemon p) {
        return String.format("- %s <%s> (HP: %s) %s%n", p, p.getPokemonType(), p.getHp(), showCondition(p));
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
        .append(String.format("Name: %s  |  ", p))
        .append(String.format("Type: %s%n", p.getPokemonType().toString()))
        .append(String.format("%nHP: %s%n", p.getHp().toString()))
        .append(String.format("%nItem: %s%n", showItem(p)))
        .append(String.format("%nStatus Effect: %s%n", showCondition(p)))
        .append(String.format("Other Effects: %s%n", showVolatileConditions(p)))
        .append(String.format("%nMOVES: %n%s", listMoves(p)))
        .toString();
    }
}
