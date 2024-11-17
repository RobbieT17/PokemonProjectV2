package project.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;

public class BattleController {

    @FXML
    private Button move1Button, move2Button, move3Button, move4Button;

    @FXML
    private Button switchPokemonButton, forfeitButton;

    @FXML
    private TextArea battleConsole;

    public static void disableSplitPaneResizing(Parent parent) {
        // Iterate over all child nodes of the parent
        for (Node node : parent.getChildrenUnmodifiable()) {
            // Check if the node is an instance of SplitPane
            if (node instanceof SplitPane) {
                SplitPane splitPane = (SplitPane) node;
                // Debugging: Print to check SplitPane is being detected
                System.out.println("Found SplitPane: " + splitPane);
                
                // Disable resizing for all children of the SplitPane
                splitPane.getItems().forEach(item -> SplitPane.setResizableWithParent(item, false));
            }
            // If the node is a Parent, recursively check its children
            if (node instanceof Parent) {
                disableSplitPaneResizing((Parent) node);
            }
        }
    }


    @FXML
    public void initialize() {
    

        move1Button.setOnAction(event -> {
            battleConsole.appendText("Move 1!\n");
        });

        move2Button.setOnAction(event -> {
            battleConsole.appendText("Move 2!\n");
        });

        move3Button.setOnAction(event -> {
            battleConsole.appendText("Move 3!\n");
        });

        move4Button.setOnAction(event -> {
            battleConsole.appendText("Move 4!\n");
        });

        switchPokemonButton.setOnAction(event -> {
            battleConsole.appendText("Switching Pokemon...\n");
        });

        forfeitButton.setOnAction(event -> {
            System.exit(0);
        });

        
    }
    
}
