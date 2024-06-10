package pokemon;

public interface PokemonStats {
    
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
        if (!p.hasPrimaryCondition()) return "";
        return p.conditions().primaryCondition().toString();
    }

    public static String showPartyStats(Pokemon p) {
        return String.format("%s (HP: %s) %s%n", p, p.hp(), showCondition(p));
    }
}
