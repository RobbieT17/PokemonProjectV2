package battle;

import java.util.Scanner;
import move.Move;
import move.MoveList;
import pokemon.Pokemon;
import pokemon.PokemonList;

public class Battle {
    public static Move chooseMove(Pokemon p) {
        Scanner scanner = new Scanner(System.in);
        boolean done = false;
        Move move = null;

        if (p.hasNoMoves()) {
            BattleLog.add(String.format("%s has no moves!", p));
            return MoveList.struggle();
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
        return move;
    }

    public static void main(String[] args) {
        Pokemon p1 = PokemonList.bulbasaur("");
        Pokemon p2 = PokemonList.squirtle("");

        Move m1 = null;
        Move m2 = null;

        while (!p1.fainted() && !p2.fainted()) {
            if (!p1.charged()) m1 = chooseMove(p1);
            if (!p2.charged()) m2 = chooseMove(p2);

            p1.useTurn(m1, p2);
            p2.useTurn(m2, p1);

            BattleField.endOfRound(p1, p2);
            BattleLog.out();
        }
    }
}
