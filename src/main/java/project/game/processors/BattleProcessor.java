package project.game.processors;

import java.util.ArrayList;
import java.util.Comparator;

import project.game.battle.BattleData;
import project.game.battle.BattleLog;
import project.game.event.EventManager;
import project.game.event.GameEvents.EventID;
import project.game.exceptions.BattleEndedException;
import project.game.move.Move;
import project.game.player.PokemonTrainer;
import project.game.pokemon.Pokemon;
import project.game.utility.Order;

public class BattleProcessor implements Processor {

    private final BattleData battleData;

    private final PokemonTrainer player1;
    private final PokemonTrainer player2;

    private Pokemon[] turnOrder;

    public BattleProcessor(BattleData data) {
        this.battleData = data;
        this.player1 = data.getPlayer1();
        this.player2 = data.getPlayer2();
    }

    private void constructTurnOrder() {
        ArrayList<Order> orderList = new ArrayList<>();
        ArrayList<Pokemon> noActList = new ArrayList<>(); // Keeps track of Pokemon without an action

        // 1. Adds all Pokemon who have selected a move to the order list
        for (Pokemon p : this.battleData.getAllPokemonInBattle()) {
            if (p.getMoveSelected() == null) { // Skips null moves
                noActList.add(p);
                continue;
            }

            EventManager eventManager = new EventManager(this.battleData, p);
            eventManager.notifyUserPokemon(EventID.FIND_MOVE_ORDER);

            Move m = p.getMoveSelected();

            Order o = new Order(m.getPriority(), p.getSpeed().getPower(), p);
            orderList.add(o);
        }

        // 2. Sorts the order list first by move priority, then by move user speed
        orderList.sort(
            Comparator.comparingInt(Order::getPriority)
            .thenComparingInt(Order::getValue).reversed()
            .thenComparingDouble(Order::getTiebreaker) // Random tiebreaker
        );

        // 3. All Pokemon whose move select was null are added first, then move users
        ArrayList<Pokemon> turnOrderList = new ArrayList<>();
        turnOrderList.addAll(noActList);
        
        for (Order o : orderList) {
            turnOrderList.add((Pokemon) o.getRef());
        }

        this.turnOrder = turnOrderList.toArray(Pokemon[]::new);

    }

    /**
     * Each pokemon acts and targets their opponent with the move chosen
     * Function terminates early if a <b>PokemonFaintedException</b> is caught.
     */
    private void processPokemonActions() {
        // Faster pokemon acts first, followed by the second
        for (Pokemon p : this.turnOrder) {
            new PokemonProcessor(this.battleData, p).process();
        }
        
    }

    /**
     * Updates things like weather and terrain effects 
     */
    private void updateBattleData() {
        new WeatherProcessor(this.battleData).process();
    }

    /**
     * Process the after effects of a round.
     * Updates specific events and also
     * reset any Pokemon params needed.
     * 
     */
    private void processRoundEnd() {
        BattleLog.add("===================================================");
        for (Pokemon p : this.turnOrder) {
            new EndRoundProcessor(battleData, p).process();
        }
    }

    /**
     * Checks if a Pokemon has fainted or the battle has been won (player is out of Pokemon)
     */
    private void checkWinConditions() {        
        if (this.player1.isOutOfPokemon() && this.player2.isOutOfPokemon()) {
            throw new BattleEndedException("Both players are out of Pokemon, it's a tie!");
        }
        else if (this.player1.isOutOfPokemon()) {
            throw new BattleEndedException(this.player2, this.player1);
        }
        else if (this.player2.isOutOfPokemon()){
            throw new BattleEndedException(this.player1, this.player2);
        }
    }

    @Override
    public void process() {
        this.constructTurnOrder();
        this.processPokemonActions();
        this.updateBattleData();
        this.processRoundEnd();
        this.checkWinConditions();
    }

}
