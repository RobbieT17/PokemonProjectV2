package project.data;

public class MoveEffect {
    
    private boolean self;
    private int chance = 100;
    private String effect;
    private int atk, def, spAtk, spDef, spd, acc, eva;

    public void setSelf(boolean self) {
        this.self = self;
    }

    public void setChance(int chance) {
        this.chance = chance;
    }

    public void setAtk(int atk) {
        this.atk = atk;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public void setSpAtk(int spAtk) {
        this.spAtk = spAtk;
    }

    public void setSpDef(int spDef) {
        this.spDef = spDef;
    }

    public void setSpd(int spd) {
        this.spd = spd;
    }

    public void setAcc(int acc) {
        this.acc = acc;
    }
    
    public void setEva(int eva) {
        this.eva = eva;
    }

    public boolean isSelf() {
        return self;
    }

    public int getChance() {
        return chance;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getSpAtk() {
        return spAtk;
    }

    public int getSpDef() {
        return spDef;
    }

    public int getSpd() {
        return spd;
    }

    public int getAcc() {
        return acc;
    }
    
    public int getEva() {
        return eva;
    }

    
}
