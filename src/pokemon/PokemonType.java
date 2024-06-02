package pokemon;

import stats.GameType;

public class PokemonType {

	// Object Variables
	private final GameType primary;
	private final GameType secondary;

	private final String[] typeResistances;
	private final String[] typeWeaknesses;
	private final String[] typeImmunities;
	
	
	// Constructor	
	public PokemonType(
        GameType type1, 
        GameType type2,
        String[]  resistances,
        String[]  weaknesses,
        String[] immunities
        ) {
		this.primary = type1;
		this.secondary = type2;
		
		this.typeResistances = resistances;
		this.typeWeaknesses = weaknesses;
		this.typeImmunities = immunities;
	}
	
	// Methods
	public boolean hasSecondaryType() {
		return this.secondary != null;
	}

	// To String Method
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append(this.primary.typeName());

		if (this.secondary != null)
		    sb.append("-").append(this.secondary.typeName());

		sb.append("\n");

		return sb.toString();
	}
	
	// Getters
	public GameType primaryType() {
		return this.primary;
	}
	
	public GameType secondaryType() {
		return this.secondary;
	}
	
	public String[]  typeResistances() {
		return this.typeResistances;
	}
	
	public String[]  typeWeaknesses() {
		return this.typeWeaknesses;
	}
	
	public String[]  typeImmunities() {
		return this.typeImmunities;
	}
}

