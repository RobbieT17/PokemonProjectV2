package project.data;

public class MoveEffect {
    
    private boolean self;
    private int chance = 100;
    private int duration;
    private double percent;
    private String effect;
    private StatData stats;

    public void setSelf(boolean self) {
        this.self = self;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setPercent(double percent) {
        this.percent = percent;
    }
    

    public void setEffect(String effect) {
        // For enum identifies (those use underscores) 
        this.effect = effect.replaceAll("[ -]", "_");
    }

    public void setStats(StatData stats) {
        this.stats = stats;
    }

    public boolean isSelf() {
        return this.self;
    }

    public int getChance() {
        return this.chance;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getEffect() {
        return this.effect;
    }

    public StatData getStats() {
        return this.stats;
    }

    public double getPercent() {
        return this.percent;
    }

    // Wrapper Functions
    public int[] statsToIntArray() {
        return new int[] {
            this.stats.getAtk(),
            this.stats.getDef(),
            this.stats.getSpAtk(),
            this.stats.getSpDef(),
            this.stats.getSpd(),
            this.stats.getAcc(),
            this.stats.getEva()
        };
    }
}
