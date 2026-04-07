package project.game.pokemon;

import project.game.pokemon.stats.Type;
import project.game.pokemon.stats.TypeName;

public class PokemonType {

// Object Variables
	private final Type primary; // Main Type
	private final Type secondary; // Secondary Type (not all Pokemon have one)

	private final TypeName[] typeResistances; // Types the Pokemon resists (receive half damage)
	private final TypeName[] typeWeaknesses;  // Types the Pokemon is weak to (receive double damage)
	private final TypeName[] typeImmunities; // Types the Pokemon is immune to (receives no damage)
	
	
// Constructor	
	// Creates a new PokemonType object, every Pokemon has one
	public PokemonType(
        Type type1, 
        Type type2,
        TypeName[]  resistances,
        TypeName[]  weaknesses,
        TypeName[] immunities
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
	public boolean typeEquals(Type type) {
		return (this.hasSecondaryType()) 
		? this.primary == type || this.secondary == type
		: this.primary == type;
	}

	@Override
	public String toString() {
		return new StringBuilder()
		.append(this.primary.name())
		.append(this.hasSecondaryType() ? String.format("-%s", this.secondary.name()) : "")
		.toString();
	}
	
// Getters
	public Type getPrimaryType() {return this.primary;}
	public Type getSecondaryType() {return this.secondary;}
	public TypeName[] getTypeResistances() {return this.typeResistances;}
	public TypeName[] getTypeWeaknesses() {return this.typeWeaknesses;}
	public TypeName[] getTypeImmunities() {return this.typeImmunities;}
}

