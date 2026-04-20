package project.game.selectors;

import java.util.ArrayList;

import project.game.battle.BattlePosition;
import project.game.move.Move.MoveTarget;
import project.game.pokemon.Pokemon;
import project.game.utility.StatDisplay;
import project.network.ClientHandler;

// TODO: This whole class is so buns right now, fix this soon!
public class MoveTargetSelector {
    
    private final ClientHandler client;
    private final MoveTarget targetType;
    private final BattlePosition self;
    private final BattlePosition[] playerPositions;
    private final BattlePosition[] opponePositions;


    public MoveTargetSelector(
        ClientHandler c, 
        MoveTarget t, 
        BattlePosition self, 
        BattlePosition[] playerPos, BattlePosition[] opponentPos
    ) {
        this.client = c;
        this.targetType = t;
        this.self = self;
        this.playerPositions = playerPos;
        this.opponePositions = opponentPos;
    }

    private void addToList(ArrayList<BattlePosition> list, BattlePosition[] posArr, boolean skipSelf) {
        for (BattlePosition pos : posArr) {
            if (pos.getCurrentPokemon() == null || (skipSelf && pos == this.self)) {
               continue;
            }
            else {
                list.add(pos);
            }
            
        }
    }

    // Check if Pokemon is an ally to the move user
    public boolean isAlly(Pokemon p) {
        return this.self.getTrainer() == p.getOwner();
    }

    public String listTargetOptions(BattlePosition[] options) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < options.length; i++) {
            Pokemon p = options[i].getIllusionPokemon();
            sb.append(String.format(
                "[%d] %s %s <%s> (HP: %s) %s%n", 
                i, 
                this.isAlly(p) ? "(ALLY)" : "",
                p, 
                p.getPokemonType(), 
                p.getHp(), 
                StatDisplay.showCondition(p)));
        }

        return sb.toString();
    }

    private BattlePosition selectAnOption(BattlePosition[] options) {
        BattlePosition pos = null;
        while (true) {
            try {
                // Lists out trainer's pokemon
                this.client.writeToBuffer(this.listTargetOptions(options));
                this.client.writeToBuffer("Please select a target >>");

                String input = this.client.readFromBuffer();
                int i = Integer.parseInt(input);

                pos = options[i];
                break;

            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                this.client.writeToBuffer("Invalid input, try again.");
            }
                
        }

        return pos;
    }

    public BattlePosition[] chooseSelf() {
        return new BattlePosition[] {this.self};
    }

    public BattlePosition[] singleAlly() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.playerPositions, true);
        BattlePosition[] options = list.toArray(BattlePosition[]::new);

        return new BattlePosition[] {this.selectAnOption(options)};
    }

    public BattlePosition[] allAllies() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.playerPositions, false);
        return list.toArray(BattlePosition[]::new);
    }

    public BattlePosition[] singleFoe() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.opponePositions, true);
        BattlePosition[] options = list.toArray(BattlePosition[]::new);

        return new BattlePosition[] {this.selectAnOption(options)};
    }

    public BattlePosition[] allFoes() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.opponePositions, false);
        return list.toArray(BattlePosition[]::new);
    }

    public BattlePosition[] singleAdjacent() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.playerPositions, true);
        this.addToList(list, this.opponePositions, true);
        BattlePosition[] options = list.toArray(BattlePosition[]::new);

        return new BattlePosition[] {this.selectAnOption(options)};
    }

    public BattlePosition[] allAdjacent() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.playerPositions, true);
        this.addToList(list, this.opponePositions, true);
        return list.toArray(BattlePosition[]::new);
    }

    public BattlePosition[] chooseAll() {
        ArrayList<BattlePosition> list = new ArrayList<>();
        this.addToList(list, this.playerPositions, false);
        this.addToList(list, this.opponePositions, false);
        return list.toArray(BattlePosition[]::new);
    }

    public BattlePosition[] select() {
        return switch(this.targetType){
            case MoveTarget.Self -> this.chooseSelf();
            case MoveTarget.Single_Ally -> this.singleAlly();
            case MoveTarget.All_Allies -> this.allAllies();
            case MoveTarget.Single_Foe -> this.singleFoe();
            case MoveTarget.All_Foes -> this.allFoes();
            case MoveTarget.Single_Adjacent -> this.singleAdjacent();
            case MoveTarget.All_Adjacent -> this.allAdjacent();
            case MoveTarget.All -> this.chooseAll();
            default -> throw new IllegalStateException("Invalid move target id: " + this.targetType);
        };
    }

}
