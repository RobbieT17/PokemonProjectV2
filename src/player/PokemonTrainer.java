package player;

import battle.BattleLog;
import java.util.InputMismatchException;
import java.util.Scanner;
import pokemon.Pokemon;

public class PokemonTrainer {

    // Class Variables
    public static final int MAX_PARTY_CAPACITY = 6;
    
    // Variables
    private final String playerName;
    private final Pokemon[] team;

    private Pokemon pokemonInBattle;

    // Constructor
    public PokemonTrainer(String name, Pokemon[] team) {
        this.playerName = name;
        this.team = team;
    }

    // Methods
    private String listPokemon() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < this.team.length; i++)
            sb.append(String.format("[%d] %s", i, this.team[i].showPartyStats()));

        return sb.toString();
    }

    public boolean outOfPokemon() {
        for (Pokemon p : this.team)
            if (!p.fainted()) return false;
        return true;
    }

    public String showPokemon() {
        return new StringBuilder()
        .append(String.format("%s's Pokemon:%n", this))
        .append(this.listPokemon())
        .toString();
    }

    public void choosePokemon() {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Pokemon pokemon = this.pokemonInBattle;

        BattleLog.addPrintln(this.showPokemon());

        while (!done) {
            try {
                BattleLog.addPrint(String.format("%s, choose a Pokemon: ", this));
                int i = scanner.nextInt();

                pokemon = this.team()[i];
                done = !pokemon.fainted();
            } catch (IndexOutOfBoundsException e) {
                BattleLog.addPrintln("Invalid input try again");
            } catch (InputMismatchException e) {
                BattleLog.addPrintln("Invalid input try again");
                scanner.next();
            }

        }

        this.returns();
        this.sendOut(pokemon);
    }

    @Override
    public String toString() {
        return this.playerName;
    }

    // Setters
    public void sendOut(Pokemon p) {
        this.pokemonInBattle = p;
        BattleLog.add(String.format("%s sends out %s!", this, p)); 
    }

    public void returns() {
        if (this.pokemonInBattle == null) return;
        
        BattleLog.add(String.format("%s returns %s!", this, this.pokemonInBattle));
        this.pokemonInBattle.backToTrainer();
        this.pokemonInBattle = null;
    }

    // Getters
    public String playerName() {
        return this.playerName;
    }

    public Pokemon[] team() {
        return this.team;
    }

    public Pokemon pokemonInBattle() {
        return this.pokemonInBattle;
    }
}
