package project.game.pokemon.stats;

public class StatPoint {

	public enum StatID {Attack, Defense, Special_Attack, Special_Defense, Speed, Accuracy, Evasion};

// Object Variables
	private final StatID statID;
	private final int base; // Base power
	private final StatStage stage; // Stage of stat
	
	private double mod; // Mod multiplier

	public StatPoint(StatID id, int base) {
		this.statID = id;
		this.base = base;
		this.stage = new StatStage(!isAccuracyOrEvasion());
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
		return this.stage.getValue() != 0 
		? String.format(" (%s) [%s Stage]", sign(this.calcPower() - this.base), sign(this.stage.getValue())) 
		: "";
	}

	private boolean isAccuracyOrEvasion() {
		return this.statID == StatID.Accuracy || this.statID == StatID.Evasion;
	}

	/**
	 * Changes the stat stage. Stage range is between -6 and 6 inclusive. 
	 * 
	 * @param change
	 * @return true if the change was successful, returns false if the
	 * stage falls outside of the range.
	 */
	public boolean changeStage(int change) {
		// Validation
		if (change == 0) {
			throw new IllegalArgumentException("Stage change cannot be zero");
		}

		if (this.stage.isAtHighestOrLowestStage(change)) {
			return false;
		}

		// Changes stage
		this.stage.changeStage(change);

		return true;
	}

	public void setStage(int stage) {
		if (stage > StatStage.HIGHEST_STAT_STAGE || stage < StatStage.LOWEST_STAT_STAGE) {
			throw new IllegalArgumentException(String.format("Cannot set stage. Value (%d) is outside of the range [-6, 6]", stage));
		}

		this.stage.setStage(stage);

	}	

	// Displays stat's current power and stage
	public String showStat() {
		return isAccuracyOrEvasion()
		? String.format("%s: %d%%%s%n", this.statID.name(), this.calcPower(), this.showStage())
		: String.format("%s: %d%s%n", this.statID.name(), this.calcPower(), this.showStage());
	}

	// Attack: 52 (-32) [-4 Stage]
	// Evasion: 80% (-20%) [-2 Stage]

	@Override
	public String toString() {
		return this.statID.name().replaceAll("_", "-");
	}

	public int calcPower() {return (int) (this.base * this.stage.getMultiplier() * this.mod);}

// Setters
	
	public void setMod(double percent) {this.mod = percent * 0.01;}
	public void resetMod() {this.mod = 1.0;}

// Getters
	public StatID getStatID() {return this.statID;} 
	public int getBase() {return this.base;}
	public int getStage() {return this.stage.getValue();}
	public double getMod() {return this.mod;}
}