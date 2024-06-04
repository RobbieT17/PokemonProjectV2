package stats;

public abstract class GameType {

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
	public static GameType getType(String type) {
		return switch (type) {
			case BUG -> bugType();
			case DARK -> darkType();
            case DRAGON -> dragonType();
			case ELECTRIC -> electricType();
			case FAIRY -> fairyType();
			case FIGHTING -> fightingType();
			case FIRE -> fireType();
			case FLYING -> flyingType();
			case GHOST -> ghostType();
			case GRASS -> grassType();
			case GROUND -> groundType();
			case ICE -> iceType();
			case NORMAL -> normalType();
			case POISON -> poisonType();
			case PSYCHIC -> psychicType();
			case ROCK -> rockType();
			case STEEL -> steelType();
			case WATER ->  waterType();
			default -> throw new IllegalArgumentException("Type inputted does not exist");
		};
	}

	// Class Getters
	public static GameType bugType() {
		return new BugType();
	}

	public static GameType darkType() {
		return new DarkType();
	}

	public static GameType dragonType() {
		return new DragonType();
	}

	public static GameType electricType() {
		return new ElectricType();
	}

	public static GameType fairyType() {
		return new FairyType();
	}

	public static GameType fightingType() {
		return new FightingType();
	}

	public static GameType fireType() {
		return new FireType();
	}

	public static GameType flyingType() {
		return new FlyingType();
	}

	public static GameType ghostType() {
		return new GhostType();
	}

	public static GameType grassType() {
		return new GrassType();
	}

	public static GameType groundType() {
		return new GroundType();
	}

	public static GameType iceType() {
		return new IceType();
	}

	public static GameType normalType() {
		return new NormalType();
	}

	public static GameType poisonType() {
		return new PoisonType();
	}

	public static GameType psychicType() {
		return new PsychicType();
	}

	public static GameType rockType() {
		return new RockType();
	}

	public static GameType steelType() {
		return new SteelType();
	}

	public static GameType waterType() {
		return new WaterType();
	}

	// Child Classes
	private static class BugType extends GameType {

		@Override
		public String typeName() {
			return BUG;
		}

		@Override
		public String[] resistances() {
			return new String[] { FIGHTING, GRASS, GROUND };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { FIRE, FLYING, ROCK };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class DarkType extends GameType {

		@Override
		public String typeName() {
			return DARK;
		}

		@Override
		public String[] resistances() {
			return new String[] { DARK, GHOST };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { BUG, FAIRY, FIGHTING };
		}

		@Override
		public String[] immunities() {
			return new String[] { PSYCHIC };
		}

	}

	private static class DragonType extends GameType {

		@Override
		public String typeName() {
			return DRAGON;
		}

		@Override
		public String[] resistances() {
			return new String[] { ELECTRIC, FIRE, GRASS, WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { DRAGON, FAIRY, ICE };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class ElectricType extends GameType {

		@Override
		public String typeName() {
			return ELECTRIC;
		}

		@Override
		public String[] resistances() {
			return new String[] { ELECTRIC, FLYING, STEEL };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { GROUND };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FairyType extends GameType {

		@Override
		public String typeName() {
			return FAIRY;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, DARK, FIGHTING };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { POISON, STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] { DRAGON };
		}

	}

	private static class FightingType extends GameType {

		@Override
		public String typeName() {
			return FIGHTING;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, DARK, ROCK };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { FAIRY, FLYING, PSYCHIC };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FireType extends GameType {

		@Override
		public String typeName() {
			return FIRE;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, FIRE, FAIRY, GRASS };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { GROUND, ROCK, WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FlyingType extends GameType {

		@Override
		public String typeName() {
			return FLYING;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, FIGHTING, GRASS };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { ELECTRIC, ICE, ROCK };
		}

		@Override
		public String[] immunities() {
			return new String[] { GROUND };
		}

	}

	private static class GhostType extends GameType {

		@Override
		public String typeName() {
			return GHOST;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, POISON };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { DARK, GHOST };
		}

		@Override
		public String[] immunities() {
			return new String[] { FIGHTING, NORMAL };
		}

	}

	private static class GrassType extends GameType {

		@Override
		public String typeName() {
			return GRASS;
		}

		@Override
		public String[] resistances() {
			return new String[] { ELECTRIC, GRASS, GROUND, WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { BUG, FIRE, FLYING, ICE, POISON };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class GroundType extends GameType {

		@Override
		public String typeName() {
			return GROUND;
		}

		@Override
		public String[] resistances() {
			return new String[] { POISON, ROCK };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { GRASS, ICE, WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] { ELECTRIC };
		}

	}

	private static class IceType extends GameType {

		@Override
		public String typeName() {
			return ICE;
		}

		@Override
		public String[] resistances() {
			return new String[] { ICE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { FIGHTING, FIRE, ROCK, STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class NormalType extends GameType {

		@Override
		public String typeName() {
			return NORMAL;
		}

		@Override
		public String[] resistances() {
			return new String[] {};
		}

		@Override
		public String[] weaknesses() {
			return new String[] { FIGHTING };
		}

		@Override
		public String[] immunities() {
			return new String[] { GHOST };
		}

	}

	private static class PoisonType extends GameType {

		@Override
		public String typeName() {
			return POISON;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, FAIRY, FIGHTING, GRASS, POISON };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { GROUND, PSYCHIC };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class PsychicType extends GameType {

		@Override
		public String typeName() {
			return PSYCHIC;
		}

		@Override
		public String[] resistances() {
			return new String[] { FIGHTING, PSYCHIC };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { BUG, DARK, GHOST };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class RockType extends GameType {

		@Override
		public String typeName() {
			return ROCK;
		}

		@Override
		public String[] resistances() {
			return new String[] { FIRE, FLYING, NORMAL, POISON };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { FIGHTING, GRASS, GROUND, WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class SteelType extends GameType {

		@Override
		public String typeName() {
			return STEEL;
		}

		@Override
		public String[] resistances() {
			return new String[] { BUG, DRAGON, FAIRY, FLYING, GRASS, ICE, NORMAL, PSYCHIC, ROCK, STEEL };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { FIRE, FIGHTING, GROUND };
		}

		@Override
		public String[] immunities() {
			return new String[] { POISON };
		}

	}

	private static class WaterType extends GameType {

		@Override
		public String typeName() {
			return WATER;
		}

		@Override
		public String[] resistances() {
			return new String[] { FIRE, ICE, STEEL, WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { ELECTRIC, GRASS };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}
}
