package project.pokemon;

import project.stats.Type;

public class PokemonType {

// Object Variables
	private final Type primary; // Main Type
	private final Type secondary; // Secondary Type (not all Pokemon have one)

	private final String[] typeResistances; // Types the Pokemon resists (receive half damage)
	private final String[] typeWeaknesses;  // Types the Pokemon is weak to (receive double damage)
	private final String[] typeImmunities; // Types the Pokemon is immune to (receives no damage)
	
	
// Constructor	
	// Creates a new PokemonType object, every Pokemon has one
	public PokemonType(
        Type type1, 
        Type type2,
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
	// True if Pokemon has a second type
	public boolean hasSecondaryType() {
		return this.secondary != null;
	}

	// Returns true if the types matches
	public boolean typeEquals(String type) {
		return (this.hasSecondaryType()) 
		? this.primary.isType(type) || this.secondary.isType(type)
		: this.primary.isType(type);
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append(this.primary.typeName())
		.append(this.hasSecondaryType() ? String.format("-%s", this.secondary.typeName()) : "")
		.toString();
	}
	
// Getters
	public Type primaryType() {
		return this.primary;
	}
	
	public Type secondaryType() {
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

