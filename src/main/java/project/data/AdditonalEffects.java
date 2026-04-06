package project.data;

public class AdditonalEffects {
    
    private MoveEffect statChange;
    private MoveEffect conditionChange;
    private MoveEffect weatherChange;
    private MoveEffect terrainChange;
    private MoveEffect switchOut;
    private MoveEffect recoil;
    private MoveEffect heal;

    public void setStatChange(MoveEffect statChange) {
        this.statChange = statChange;
    }
    public void setConditionChange(MoveEffect conditionChange) {
        this.conditionChange = conditionChange;
    }
    public void setWeatherChange(MoveEffect weatherChange) {
        this.weatherChange = weatherChange;
    }
    public void setTerrainChange(MoveEffect terrainChange) {
        this.terrainChange = terrainChange;
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
    


    public MoveEffect getStatChange() {
        return this.statChange;
    }
    public MoveEffect getConditionChange() {
        return this.conditionChange;
    }
    public MoveEffect getWeatherChange() {
        return this.weatherChange;
    }
    public MoveEffect getTerrainChange() {
        return this.terrainChange;
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
    
    

    
   

    
    
    
    
}
