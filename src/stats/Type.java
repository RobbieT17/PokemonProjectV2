package stats;

public abstract class Type {

// Pokemon GameType Names
	public final static String TYPELESS = "No Type";

	public final static String BUG = "Bug";
	public final static String DARK = "Dark";
	public final static String DRAGON = "Dragon";
	public final static String ELECTRIC = "Electric";
	public final static String FAIRY = "Fairy";
	public final static String FIGHTING = "Fighting";
	public final static String FIRE = "Fire";
	public final static String FLYING = "Flying";
	public final static String GHOST = "Ghost";
	public final static String GRASS = "Grass";
	public final static String GROUND = "Ground";
	public final static String ICE = "Ice";
	public final static String NORMAL = "Normal";
	public final static String POISON = "Poison";
	public final static String PSYCHIC = "Psychic";
	public final static String ROCK = "Rock";
	public final static String STEEL = "Steel";
	public final static String WATER = "Water";

// Abstract Methods
	public abstract String typeName();

	public abstract String[] resistances();

	public abstract String[] weaknesses();

	public abstract String[] immunities();


// Selector Methods
	public static Type getType(String type) {
		return switch (type) {
			case Type.BUG -> bugType();
			case Type.DARK -> darkType();
            case Type.DRAGON -> dragonType();
			case Type.ELECTRIC -> electricType();
			case Type.FAIRY -> fairyType();
			case Type.FIGHTING -> fightingType();
			case Type.FIRE -> fireType();
			case Type.FLYING -> flyingType();
			case Type.GHOST -> ghostType();
			case Type.GRASS -> grassType();
			case Type.GROUND -> groundType();
			case Type.ICE -> iceType();
			case Type.NORMAL -> normalType();
			case Type.POISON -> poisonType();
			case Type.PSYCHIC -> psychicType();
			case Type.ROCK -> rockType();
			case Type.STEEL -> steelType();
			case Type.WATER ->  waterType();
			default -> throw new IllegalArgumentException("Type inputted does not exist");
		};
	}

	// Class Getters
	public static Type bugType() {
		return new BugType();
	}

	public static Type darkType() {
		return new DarkType();
	}

	public static Type dragonType() {
		return new DragonType();
	}

	public static Type electricType() {
		return new ElectricType();
	}

	public static Type fairyType() {
		return new FairyType();
	}

	public static Type fightingType() {
		return new FightingType();
	}

	public static Type fireType() {
		return new FireType();
	}

	public static Type flyingType() {
		return new FlyingType();
	}

	public static Type ghostType() {
		return new GhostType();
	}

	public static Type grassType() {
		return new GrassType();
	}

	public static Type groundType() {
		return new GroundType();
	}

	public static Type iceType() {
		return new IceType();
	}

	public static Type normalType() {
		return new NormalType();
	}

	public static Type poisonType() {
		return new PoisonType();
	}

	public static Type psychicType() {
		return new PsychicType();
	}

	public static Type rockType() {
		return new RockType();
	}

	public static Type steelType() {
		return new SteelType();
	}

	public static Type waterType() {
		return new WaterType();
	}

	// Child Classes
	private static class BugType extends Type {

		@Override
		public String typeName() {
			return Type.BUG;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIGHTING, Type.GRASS, Type.GROUND };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIRE, Type.FLYING, Type.ROCK };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class DarkType extends Type {

		@Override
		public String typeName() {
			return Type.DARK;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.DARK, Type.GHOST };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.BUG, Type.FAIRY, Type.FIGHTING };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.PSYCHIC };
		}

	}

	private static class DragonType extends Type {

		@Override
		public String typeName() {
			return Type.DRAGON;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.ELECTRIC, Type.FIRE, Type.GRASS, Type.WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.DRAGON, Type.FAIRY, Type.ICE };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class ElectricType extends Type {

		@Override
		public String typeName() {
			return Type.ELECTRIC;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.ELECTRIC, Type.FLYING, Type.STEEL };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.GROUND };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FairyType extends Type {

		@Override
		public String typeName() {
			return Type.FAIRY;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.DARK, Type.FIGHTING };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.POISON, Type.STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.DRAGON };
		}

	}

	private static class FightingType extends Type {

		@Override
		public String typeName() {
			return Type.FIGHTING;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.DARK, Type.ROCK };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FAIRY, Type.FLYING, Type.PSYCHIC };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FireType extends Type {

		@Override
		public String typeName() {
			return Type.FIRE;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.FIRE, Type.FAIRY, Type.GRASS };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.GROUND, Type.ROCK, Type.WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FlyingType extends Type {

		@Override
		public String typeName() {
			return Type.FLYING;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.FIGHTING, Type.GRASS };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.ELECTRIC, Type.ICE, Type.ROCK };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.GROUND };
		}

	}

	private static class GhostType extends Type {

		@Override
		public String typeName() {
			return GHOST;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.POISON };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.DARK, Type.GHOST };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.FIGHTING, Type.NORMAL };
		}

	}

	private static class GrassType extends Type {

		@Override
		public String typeName() {
			return Type.GRASS;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.ELECTRIC, Type.GRASS, Type.GROUND, Type.WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.BUG, Type.FIRE, Type.FLYING, Type.ICE, Type.POISON };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class GroundType extends Type {

		@Override
		public String typeName() {
			return Type.GROUND;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.POISON, Type.ROCK };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.GRASS, Type.ICE, Type.WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.ELECTRIC };
		}

	}

	private static class IceType extends Type {

		@Override
		public String typeName() {
			return Type.ICE;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.ICE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIGHTING, Type.FIRE, Type.ROCK, Type.STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class NormalType extends Type {

		@Override
		public String typeName() {
			return Type.NORMAL;
		}

		@Override
		public String[] resistances() {
			return new String[] {};
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIGHTING };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.GHOST };
		}

	}

	private static class PoisonType extends Type {

		@Override
		public String typeName() {
			return Type.POISON;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.FAIRY, Type.FIGHTING, Type.GRASS, Type.POISON };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.GROUND, Type.PSYCHIC };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class PsychicType extends Type {

		@Override
		public String typeName() {
			return Type.PSYCHIC;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIGHTING, Type.PSYCHIC };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.BUG, Type.DARK, Type.GHOST };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class RockType extends Type {

		@Override
		public String typeName() {
			return Type.ROCK;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIRE, Type.FLYING, Type.NORMAL, Type.POISON };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIGHTING, Type.GRASS, Type.GROUND, Type.WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class SteelType extends Type {

		@Override
		public String typeName() {
			return Type.STEEL;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.DRAGON, Type.FAIRY, Type.FLYING, Type.GRASS, Type.ICE, Type.NORMAL, Type.PSYCHIC, Type.ROCK, Type.STEEL };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIRE, Type.FIGHTING, Type.GROUND };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.POISON };
		}

	}

	private static class WaterType extends Type {

		@Override
		public String typeName() {
			return WATER;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIRE, Type.ICE, Type.STEEL, Type.WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.ELECTRIC, Type.GRASS };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}
}
