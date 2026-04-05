package project.data;

public class MoveEffect {
    
    private boolean self;
    private int chance = 100;
    private String effect;
    private StatData stats;

    public void setSelf(boolean self) {
        this.self = self;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public void setEffect(String effect) {
        this.effect = effect;
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

    public String getEffect() {
        return this.effect;
    }

    public StatData getStats() {
        return this.stats;
    }
    
}
