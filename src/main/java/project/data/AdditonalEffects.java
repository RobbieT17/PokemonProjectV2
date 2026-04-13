package project.data;

public class AdditonalEffects {
    
    private MoveEffect statChange;
    private MoveEffect conditionChange;
    private MoveEffect conditionChange2;
    private MoveEffect weatherChange;
    private MoveEffect terrainChange;
    private MoveEffect barrierChange;
    private MoveEffect switchOut;
    private MoveEffect recoil;
    private MoveEffect heal;
    private MoveEffect lifeSteal;

    public void setStatChange(MoveEffect statChange) {
        this.statChange = statChange;
    }
    public void setConditionChange(MoveEffect conditionChange) {
        this.conditionChange = conditionChange;
    }
    public void setConditionChange2(MoveEffect conditionChange2) {
        this.conditionChange2 = conditionChange2;
    }
    public void setWeatherChange(MoveEffect weatherChange) {
        this.weatherChange = weatherChange;
    }
    public void setTerrainChange(MoveEffect terrainChange) {
        this.terrainChange = terrainChange;
    }
    public void setBarrierChange(MoveEffect barrierChange) {
        this.barrierChange = barrierChange;
    }
    public void setSwitchOut(MoveEffect switchOut) {
        this.switchOut = switchOut;
    }
    public void setRecoil(MoveEffect recoil) {
        this.recoil = recoil;
    }
    public void setHeal(MoveEffect heal) {
        this.heal = heal;
    }   
    public void setLifeSteal(MoveEffect lifeSteal) {
        this.lifeSteal = lifeSteal;
    }

    public MoveEffect getStatChange() {
        return this.statChange;
    }
    public MoveEffect getConditionChange() {
        return this.conditionChange;
    }
    public MoveEffect getConditionChange2() {
        return this.conditionChange2;
    }
    public MoveEffect getWeatherChange() {
        return this.weatherChange;
    }
    public MoveEffect getTerrainChange() {
        return this.terrainChange;
    }
    public MoveEffect getBarrierChange() {
        return this.barrierChange;
    }
    public MoveEffect getSwitchOut() {
        return this.switchOut;
    }
    public  MoveEffect getRecoil() {
        return this.recoil;
    }
    public MoveEffect getHeal() {
        return this.heal;
    }
    public MoveEffect getLifeSteal() {
        return this.lifeSteal;
    }
     
}
