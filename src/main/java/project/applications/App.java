package project.applications;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Create a Button
        Button button = new Button("Click me! If you dare...");
        
        // Set an action when the button is clicked
        button.setOnAction(e -> System.out.println("BOO NIGGA!"));

        // Create a layout and add the button to it
        StackPane root = new StackPane();
        root.getChildren().add(button);

        // Set the scene with the layout
        Scene scene = new Scene(root, 300, 200);

        // Set the stage (window) properties
        primaryStage.setTitle("Simple JavaFX App");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        // Launch the JavaFX application
        launch(args);
    }

   
}