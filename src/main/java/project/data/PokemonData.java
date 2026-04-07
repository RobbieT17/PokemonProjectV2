package project.data;

import java.util.List;

import project.game.builders.PokemonBuilder;
import project.game.builders.PokemonTypeBuilder;
import project.game.pokemon.PokemonType;
import project.game.pokemon.stats.HealthPoints;
import project.game.pokemon.stats.StatPoint;
import project.game.pokemon.stats.Type;

public class PokemonData {
    
    private int id;
    private String name;
    private String type1;
    private String type2;
    private double weight;
    private int hp;
    private int atk;
    private int def;
    private int spAtk;
    private int spDef;
    private int spd;
    private List<String> movePool;
    
    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setType1(String type1) {this.type1 = type1;}
    public void setType2(String type2) {this.type2 = type2;}
    public void setWeight(double weight) {this.weight = weight;}
    public void setHp(int hp) {this.hp = hp;}
    public void setAtk(int atk) {this.atk = atk;}
    public void setDef(int def) {this.def = def;}
    public void setSpAtk(int spAtk) {this.spAtk = spAtk;}
    public void setSpDef(int spDef) {this.spDef = spDef;}
    public void setSpd(int spd) {this.spd = spd;}
    public void setMovePool(List<String> movePool) {this.movePool = movePool;}
    
    public int getId() {return id;}
    public String getName() {return name;}
    public String getType1() {return type1;}
    public String getType2() {return type2;}
    public double getWeight() {return weight;}
    public int getHp() {return hp;}
    public int getAtk() {return atk;}
    public int getDef() {return def;}
    public int getSpAtk() {return spAtk;}
    public int getSpDef() {return spDef;}
    public int getSpd() {return spd;}
    public List<String> getMovePool() {return movePool;}

    // Wrapper Getters
    public Type getPrimaryType() {return Type.valueOf(this.type1);}
    public Type getSecondaryType() {return Type.valueOf(this.type2);}
    public HealthPoints getHealthPoints(int level) {return new HealthPoints(StatPoint.calculateHp(this.hp, level));}

    public PokemonType getPokemonType() {
        return this.type2 != null 
        
        ? new PokemonTypeBuilder()
                .setPrimaryType(this.getPrimaryType())
                .setSecondaryType(this.getSecondaryType())
                .build()

        : new PokemonTypeBuilder()
                .setPrimaryType(this.getPrimaryType())
                .build();
    }

    public StatPoint[] getStats(int level) {
        return new StatPoint[] {
            new StatPoint(StatPoint.ATTACK_NAME, StatPoint.ATTACK, StatPoint.calculate(this.atk, level)),
            new StatPoint(StatPoint.DEFENSE_NAME, StatPoint.DEFENSE, StatPoint.calculate(this.def, level)),
            new StatPoint(StatPoint.SPECIAL_ATTACK_NAME, StatPoint.SPECIAL_ATTACK, StatPoint.calculate(this.spAtk, level)),
            new StatPoint(StatPoint.SPECIAL_DEFENSE_NAME, StatPoint.SPECIAL_DEFENSE, StatPoint.calculate(this.spDef, level)),
            new StatPoint(StatPoint.SPEED_NAME, StatPoint.SPEED, StatPoint.calculate(this.spd, level)),
            new StatPoint(StatPoint.ACCURACY_NAME, StatPoint.ACCURACY, 100),
            new StatPoint(StatPoint.EVASION_NAME, StatPoint.EVASION, 100)
        };
    }
}
