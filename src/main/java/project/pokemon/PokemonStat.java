package project.pokemon;

public class PokemonStat {

// Class Variables
	private final static int LOWEST_STAT_STAGE = -6;
	private final static int HIGHEST_STAT_STAGE = 6;
	private final static double MULTIPLIER = 0.125;

	// Stat Names
	public static final String ATTACK_NAME = "Attack";
	public static final String DEFENSE_NAME = "Defense";
	public static final String SPECIAL_ATTACK_NAME = "Special-Attack";
	public static final String SPECIAL_DEFENSE_NAME = "Special-Defense";
	public static final String SPEED_NAME = "Speed";
	public static final String ACCURACY_NAME = "Accuracy";
	public static final String EVASION_NAME = "Evasion";

    // Stat Types ID
	public static final int ATTACK = 0;
	public static final int DEFENSE = 1;
	public static final int SPECIAL_ATTACK = 2;
	public static final int SPECIAL_DEFENSE = 3;
	public static final int SPEED = 4;
	
	public static final int ACCURACY = 5;
	public static final int EVASION = 6;

// Object Variables
	private final String statName;
	private final int statID; // ID for type of stat, view above
	private final int base; // Base power

	private int power; // Power depending on the current stage
	private int stage; // Stage of the stat, max: 6 min: -6
	private double mod; // Modified Stat Multiplier

	public PokemonStat(String name, int id, int base) {
		this.statName = name;
		this.statID = id; 
		this.base = base;
		this.power = base;
		this.stage = 0;
		this.mod = 1.0;
	}
	
// Class Methods
	private static String sign(int i) {
		return String.format("%s%d", (i < 0) ? "-" : "+", Math.abs(i));
	}

	public static String sizeOfChange(int change) {
		if (change >= 6) return " to the maximum";
		if (change > 2) return " drastically";
		if (change == 2) return " sharply";
		if (change == -2) return " harshly";
		if (change <= -6) return " to the minimum";
		if (change < -2) return " brutally";
		return "";
	}

// Methods
	// If not a stage 0, shows the difference in power and current stage
	private String showStage() {
		return this.stage != 0 
		? String.format(" (%s) [%s Stage]", sign(this.power - this.base), sign(this.stage)) 
		: "";
	}

	private boolean isAccuracyOrEvasion() {
		return this.statID == PokemonStat.ACCURACY || this.statID == PokemonStat.EVASION;
	}

	// Changes a stat based on the stage and base stat
	public void changeStat() {
		this.power = (int) (this.base * (1 + PokemonStat.MULTIPLIER * this.stage));
	}

	// Changes a stat based on an accuracy/evasion of 100%
	public void changeStatForAccuracyOrEvasion() {
		this.power = (int) (100 + PokemonStat.MULTIPLIER * 100 * this.stage);
	}

	// Changes stage of a stat. The stage should never be outside the range of -6 and 6
	public void changeStage(int change) {
		this.stage += change;

		if (this.stage < PokemonStat.LOWEST_STAT_STAGE) this.stage = PokemonStat.LOWEST_STAT_STAGE;
		else if (this.stage > PokemonStat.HIGHEST_STAT_STAGE) this.stage = PokemonStat.HIGHEST_STAT_STAGE;

		if (this.isAccuracyOrEvasion()) this.changeStatForAccuracyOrEvasion();
		else this.changeStat(); 
	}

	/**
	 * Checks if the stat's stage is out side the range of the max and min.
	 * @param change number of stage increases/decreases
	 * @return true if at highest stage (positive chance) or if at lowest stage (negative change)
	 */
	public boolean isAtHighestOrLowestStage(int change) {
		if (change == 0) throw new IllegalArgumentException("Stage change cannot be zero");
		return change > 0 ? this.stage == PokemonStat.HIGHEST_STAT_STAGE : this.stage == PokemonStat.LOWEST_STAT_STAGE;
	}

	// Displays stat's current power and stage
	public String showStat() {
		return isAccuracyOrEvasion()
		? String.format("%s: %d%%%s%n", this.statName, this.power, this.showStage())
		: String.format("%s: %d%s%n", this.statName, this.power, this.showStage());
	}

	// Attack: 52 (-32) [-4 Stage]
	// Evasion: 80% (-20%) [-2 Stage]

	@Override
	public String toString() {
		return this.statName;
	}

// Setters
	public void setStage(int stage) {
		this.stage = stage;

		if (this.isAccuracyOrEvasion()) this.changeStatForAccuracyOrEvasion();
		else this.changeStat(); 
	}	

	public void setMod(double percent) {this.mod *= 0.01 * percent;}
	public void resetMod() {this.mod = 1.0;}

// Getters
	public String statName() {return this.statName;}
	public int statID() {return this.statID;}
	public int base() {return this.base;}
	public int power() {return (int) (this.power * this.mod);}
	public int stage() {return this.stage;}
}