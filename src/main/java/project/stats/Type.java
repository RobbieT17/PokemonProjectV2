package project.stats;

public abstract class Type {

	// Pokemon GameType Names
	public final static String TYPELESS = "No Type";

	public final static String BUG = "Bug";
	public final static String DARK = "Dark";
	public final static String DIGITAL = "Digital";
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
	public final static String SOUND = "Sound";
	public final static String SPACE = "Space";
	public final static String STEEL = "Steel";
	public final static String WATER = "Water";
	public final static String ZOMBIE = "Zombie";

	public final static String[] ALL_TYPES = {
			Type.BUG, Type.DARK, Type.DIGITAL, Type.DRAGON, Type.ELECTRIC, Type.FAIRY, Type.FIGHTING, Type.FIRE,
			Type.FLYING, Type.GHOST, Type.GRASS, Type.GROUND, Type.ICE, Type.NORMAL, Type.POISON, Type.PSYCHIC,
			Type.ROCK, Type.SOUND, Type.SPACE, Type.STEEL, Type.WATER, Type.ZOMBIE
	};

	// Abstract Methods
	public abstract int typeId();

	public abstract String typeName();

	public abstract String[] resistances();

	public abstract String[] weaknesses();

	public abstract String[] immunities();

	// Selector Methods
	public static Type getType(String type) {
		return switch (type) {
			case Type.BUG -> bugType();
			case Type.DARK -> darkType();
			case Type.DIGITAL -> digitalType();
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
			case Type.SOUND -> soundType();
			case Type.SPACE -> spaceType();
			case Type.STEEL -> steelType();
			case Type.WATER -> waterType();
			case Type.ZOMBIE -> zombieType();
			default -> throw new IllegalArgumentException("Type inputted does not exist");
		};
	}

	// Checks if type matches specified type
	public boolean isType(String type) {
		return this.typeName().equals(type);
	}

	// Class Getters
	public static Type bugType() {
		return new BugType();
	}

	public static Type darkType() {
		return new DarkType();
	}

	public static Type digitalType() {
		return new DigitalType();
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

	public static Type soundType() {
		return new SoundType();
	}

	public static Type spaceType() {
		return new SpaceType();
	}

	public static Type steelType() {
		return new SteelType();
	}

	public static Type waterType() {
		return new WaterType();
	}

	public static Type zombieType() {
		return new ZombieType();
	}

	// Child Classes
	private static class BugType extends Type {

		@Override
		public int typeId() {
			return 0;
		}

		@Override
		public String typeName() {
			return Type.BUG;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIGHTING, Type.GRASS, Type.GROUND, Type.SOUND };
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
		public int typeId() {
			return 1;
		}

		@Override
		public String typeName() {
			return Type.DARK;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.DARK, Type.DIGITAL, Type.GHOST };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.BUG, Type.FAIRY, Type.FIGHTING};
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.PSYCHIC };
		}

	}

	private static class DigitalType extends Type {

		@Override
		public int typeId() {
			return 2;
		}

		@Override
		public String typeName() {
			return Type.DIGITAL;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.DRAGON, Type.ELECTRIC, Type.PSYCHIC, Type.STEEL };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.BUG, Type.GROUND, Type.ICE, Type.POISON };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.FAIRY };
		}

	}

	private static class DragonType extends Type {

		@Override
		public int typeId() {
			return 3;
		}

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
		public int typeId() {
			return 4;
		}

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
			return new String[] { Type.DIGITAL, Type.GROUND };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FairyType extends Type {

		@Override
		public int typeId() {
			return 5;
		}

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
			return new String[] { Type.DIGITAL, Type.POISON, Type.SOUND, Type.STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.DRAGON };
		}

	}

	private static class FightingType extends Type {

		@Override
		public int typeId() {
			return 6;
		}

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
			return new String[] { Type.FAIRY, Type.FLYING, Type.PSYCHIC, Type.ZOMBIE };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class FireType extends Type {

		@Override
		public int typeId() {
			return 7;
		}

		@Override
		public String typeName() {
			return Type.FIRE;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.FIRE, Type.FAIRY, Type.GRASS, Type.ICE, Type.STEEL, Type.ZOMBIE };
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
		public int typeId() {
			return 8;
		}

		@Override
		public String typeName() {
			return Type.FLYING;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.DIGITAL, Type.FIGHTING, Type.GRASS, Type.ZOMBIE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.ELECTRIC, Type.ICE, Type.ROCK, Type.SPACE };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.GROUND };
		}

	}

	private static class GhostType extends Type {

		@Override
		public int typeId() {
			return 9;
		}

		@Override
		public String typeName() {
			return GHOST;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.POISON, Type.ZOMBIE };
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
		public int typeId() {
			return 10;
		}

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
		public int typeId() {
			return 11;
		}

		@Override
		public String typeName() {
			return Type.GROUND;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.DIGITAL, Type.POISON, Type.ROCK, Type.SOUND };
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
		public int typeId() {
			return 12;
		}

		@Override
		public String typeName() {
			return Type.ICE;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.ICE, Type.SPACE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIGHTING, Type.FIRE, Type.ROCK, Type.SOUND, Type.STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class NormalType extends Type {

		@Override
		public int typeId() {
			return 13;
		}

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
			return new String[] { Type.FIGHTING, Type.ZOMBIE };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.GHOST };
		}

	}

	private static class PoisonType extends Type {

		@Override
		public int typeId() {
			return 14;
		}

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
			return new String[] { Type.DIGITAL, Type.GROUND, Type.PSYCHIC };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class PsychicType extends Type {

		@Override
		public int typeId() {
			return 15;
		}

		@Override
		public String typeName() {
			return Type.PSYCHIC;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIGHTING, Type.PSYCHIC, Type.SOUND };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.BUG, Type.DARK, Type.GHOST, Type.ZOMBIE };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class RockType extends Type {

		@Override
		public int typeId() {
			return 16;
		}

		@Override
		public String typeName() {
			return Type.ROCK;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.FIRE, Type.FLYING, Type.NORMAL, Type.POISON, Type.SPACE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIGHTING, Type.GRASS, Type.GROUND, Type.STEEL, Type.WATER };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class SoundType extends Type {

		@Override
		public int typeId() {
			return 17;
		}

		@Override
		public String typeName() {
			return Type.SOUND;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.DARK, Type.FAIRY, Type.SOUND, Type.WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.GROUND, Type.SPACE };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class SpaceType extends Type {

		@Override
		public int typeId() {
			return 18;
		}

		@Override
		public String typeName() {
			return Type.SPACE;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.FIGHTING, Type.GRASS, Type.NORMAL, Type.SPACE, Type.WATER };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.ICE, Type.ROCK };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.FLYING, Type.SOUND };
		}

	}

	private static class SteelType extends Type {

		@Override
		public int typeId() {
			return 19;
		}

		@Override
		public String typeName() {
			return Type.STEEL;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.DRAGON, Type.FAIRY, Type.FLYING, Type.GRASS, Type.ICE, Type.NORMAL,
					Type.PSYCHIC, Type.ROCK, Type.SPACE, Type.STEEL, Type.ZOMBIE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIRE, Type.FIGHTING, Type.GROUND, Type.DIGITAL };
		}

		@Override
		public String[] immunities() {
			return new String[] { Type.POISON };
		}

	}

	private static class WaterType extends Type {

		@Override
		public int typeId() {
			return 20;
		}

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
			return new String[] { Type.ELECTRIC, Type.GRASS, Type.SOUND, Type.SPACE };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}

	private static class ZombieType extends Type {

		@Override
		public int typeId() {
			return 21;
		}

		@Override
		public String typeName() {
			return Type.ZOMBIE;
		}

		@Override
		public String[] resistances() {
			return new String[] { Type.BUG, Type.DARK, Type.ELECTRIC, Type.FIGHTING, Type.GHOST, Type.GROUND, Type.ICE, Type.NORMAL,
					Type.POISON, Type.PSYCHIC, Type.ZOMBIE };
		}

		@Override
		public String[] weaknesses() {
			return new String[] { Type.FIRE, Type.GRASS, Type.STEEL };
		}

		@Override
		public String[] immunities() {
			return new String[] {};
		}

	}
}
