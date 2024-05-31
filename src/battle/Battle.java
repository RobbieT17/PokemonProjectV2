package battle;

import java.util.Scanner;
import move.Move;
import move.MoveList;
import pokemon.Pokemon;
import pokemon.PokemonList;

public class Battle {
    public static void chooseMove(Pokemon p) {
        if (p.charged()) return;

        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Move move = p.moveSelected();

        if (p.hasNoMoves()) {
            BattleLog.add(String.format("%s has no moves!", p));
            p.setMove(MoveList.struggle());
        }

        System.out.println("\n" + p.showStats());

        while (!done) {
            try {
                System.out.printf("What move should %s use? ", p);
                int i = scanner.nextInt();

                move = p.moves()[i];
                done = !move.pp().depleted();
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Invalid input try again");
            }

        }
        p.setMove(move);
    }

    public static void main(String[] args) {
        Pokemon p1 = PokemonList.bulbasaur("");
        Pokemon p2 = PokemonList.bulbasaur("Bob");

        while (!p1.fainted() && !p2.fainted()) {
            chooseMove(p1);
            chooseMove(p2);

            try {
                p1.useTurn(p1.moveSelected(), p2);
                p2.useTurn(p2.moveSelected(), p1);
            } catch (PokemonFaintedException e) {
                BattleLog.add(e.getMessage());
            }

            BattleField.endOfRound(p1, p2); 
            BattleLog.out();
        }
    }

}
