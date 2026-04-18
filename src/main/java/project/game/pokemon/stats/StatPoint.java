package project.game.pokemon.stats;

public class StatPoint {

	public enum StatID {Attack, Defense, Special_Attack, Special_Defense, Speed, Accuracy, Evasion};

// Class Variables
	private final static int LOWEST_STAT_STAGE = -6;
	private final static int HIGHEST_STAT_STAGE = 6;

// Object Variables
	private final StatID statID;
	private final int base; // Base power

	private int power; // Power depending on the current stage
	private int stage; // Stage of the stat, max: 6 min: -6
	private double mod; // Modified Stat Multiplier

	public StatPoint(StatID id, int base) {
		this.statID = id;
		this.base = base;

		this.power = base;
		this.stage = 0;
		this.mod = 1.0;
	}
	
// Class Function
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

	public static int calculateHp(int baseHp, int level) {
		return (((2 * baseHp) * level) / 100) + level + 10;
	}

	public static int calculate(int baseStat, int level) {
		return ((baseStat * 2 * level) / 100) + 5;
	}

// Methods
	// If not a stage 0, shows the difference in power and current stage
	private String showStage() {
		return this.stage != 0 
		? String.format(" (%s) [%s Stage]", sign(this.power - this.base), sign(this.stage)) 
		: "";
	}

	private boolean isAccuracyOrEvasion() {
		return this.statID == StatID.Accuracy || this.statID == StatID.Evasion;
	}


	// Changes a stat based on the stage and base stat
	public void changeStat() {
		double mod = switch (this.stage){
			case -6 -> 0.25;
			case -5 -> 0.28;
			case -4 -> 0.33;
			case -3 -> 0.4;
			case -2 -> 0.5;
			case -1 -> 0.66;
			case 1 -> 1.5;
			case 2 -> 2;
			case 3 -> 2.5;
			case 4 -> 3;
			case 5 -> 3.5;
			case 6 -> 4;
			default -> 1.0;
		};

		this.power = (int) (this.base * mod);
	}

	// Changes a stat based on an accuracy/evasion of 100%
	public void changeStatForAccuracyOrEvasion() {
		double mod = switch (this.stage){
			case -6 -> 0.33;
			case -5 -> 0.375;
			case -4 -> 0.429;
			case -3 -> 0.5;
			case -2 -> 0.6;
			case -1 -> 0.75;
			case 1 -> 1.33;
			case 2 -> 1.66;
			case 3 -> 2;
			case 4 -> 2.33;
			case 5 -> 2.66;
			case 6 -> 3;
			default -> 1.0;
		};

		this.power = (int) (this.base * mod);
	}

	// Changes stage of a stat. The stage should never be outside the range of -6 and 6
	public void changeStage(int change) {
		this.stage += change;

		if (this.stage < StatPoint.LOWEST_STAT_STAGE) {
			this.stage = StatPoint.LOWEST_STAT_STAGE;
		}
		else if (this.stage > StatPoint.HIGHEST_STAT_STAGE) {
			this.stage = StatPoint.HIGHEST_STAT_STAGE;
		}

		if (this.isAccuracyOrEvasion()) {
			this.changeStatForAccuracyOrEvasion();
		}
		else {
			this.changeStat(); 
		}
	}

	/**
	 * Checks if the stat's stage is out side the range of the max and min.
	 * @param change number of stage increases/decreases
	 * @return true if at highest stage (positive chance) or if at lowest stage (negative change)
	 */
	public boolean isAtHighestOrLowestStage(int change) {
		if (change == 0) throw new IllegalArgumentException("Stage change cannot be zero");
		return change > 0 ? this.stage == StatPoint.HIGHEST_STAT_STAGE : this.stage == StatPoint.LOWEST_STAT_STAGE;
	}

	// Displays stat's current power and stage
	public String showStat() {
		return isAccuracyOrEvasion()
		? String.format("%s: %d%%%s%n", this.statID.name(), this.power, this.showStage())
		: String.format("%s: %d%s%n", this.statID.name(), this.power, this.showStage());
	}

	// Attack: 52 (-32) [-4 Stage]
	// Evasion: 80% (-20%) [-2 Stage]

	@Override
	public String toString() {
		return this.statID.name().replaceAll("_", "-");
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
	public StatID getStatID() {return this.statID;}
	public int getBase() {return this.base;}
	public int getPower() {return (int) (this.power * this.mod);}
	public int getStage() {return this.stage;}
}