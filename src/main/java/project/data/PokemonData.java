package project.data;

import java.util.List;

import project.game.builders.PokemonTypeBuilder;
import project.game.pokemon.PokemonType;
import project.game.pokemon.stats.HealthPoints;
import project.game.pokemon.stats.StatPoint;
import project.game.pokemon.stats.StatPoint.StatID;
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
            new StatPoint(StatID.Attack, StatPoint.calculate(atk, level)),
            new StatPoint(StatID.Defense, StatPoint.calculate(def, level)),
            new StatPoint(StatID.Special_Attack, StatPoint.calculate(spAtk, level)),
            new StatPoint(StatID.Special_Defense, StatPoint.calculate(spDef, level)),
            new StatPoint(StatID.Speed, StatPoint.calculate(spd, level)),
            new StatPoint(StatID.Accuracy, 100),
            new StatPoint(StatID.Evasion, 100)
        };
    }
}
