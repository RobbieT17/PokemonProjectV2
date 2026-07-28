package project.game.pokemon.stats;

public class StatStage {

// Class Variables
	protected final static int LOWEST_STAT_STAGE = -6;
	protected final static int HIGHEST_STAT_STAGE = 6;

// Object Variables
    private final boolean isNorm;
    
    private int value;
    private double multiplier;
    
    public StatStage(boolean cond) {
        this.isNorm = cond;
        this.value = 0;
        this.multiplier = 1.0;
    }

	// Changes a stat based on the stage and base stat
	private void calcValue() {
		this.multiplier = switch (this.value){
			case -6 -> this.isNorm ? 0.25 : 0.33;
			case -5 -> this.isNorm ? 0.28 : 0.375;
			case -4 -> this.isNorm ? 0.33 : 0.429;
			case -3 -> this.isNorm ? 0.4 : 0.5;
			case -2 -> this.isNorm ? 0.5 : 0.6;
			case -1 -> this.isNorm ? 0.66 : 0.75;
			case 1 -> this.isNorm ? 1.5 : 1.33;
			case 2 -> this.isNorm ? 2 : 1.66;
			case 3 -> this.isNorm ? 2.5 : 2;
			case 4 -> this.isNorm ? 3 : 2.33;
			case 5 -> this.isNorm ? 3.5 : 2.66;
			case 6 -> this.isNorm ? 4 : 3;
			default -> 1.0;
		};

	}

    /**
	 * Checks if the stat's stage is out side the range of the max and min.
	 * @param change number of stage increases/decreases
	 * @return true if at highest stage (positive chance) or if at lowest stage (negative change)
	 */
	public boolean isAtHighestOrLowestStage(int change) {
		return change > 0 ? this.value == StatStage.HIGHEST_STAT_STAGE : this.value == StatStage.LOWEST_STAT_STAGE;
	}

    public void changeStage(int s) {
        // Changes stage
		this.value += s;

		if (this.value < StatStage.LOWEST_STAT_STAGE) {
			this.value = StatStage.LOWEST_STAT_STAGE;
		}
		else if (this.value > StatStage.HIGHEST_STAT_STAGE) {
			this.value = StatStage.HIGHEST_STAT_STAGE;
		}

		this.calcValue();
    }

    public void setStage(int s) {
        this.value = s;
        this.calcValue();
    }

    public int getValue() {return this.value;}
    public double getMultiplier() {return this.multiplier;}
	public boolean isNorm() {return  this.isNorm;}
}
