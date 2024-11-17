package project.applications;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

	public static void disableSplitPaneResizing(Parent parent) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            if (node instanceof SplitPane) {
                SplitPane splitPane = (SplitPane) node;
                // Disable resizing for all children of the SplitPane
                splitPane.getItems().forEach(item -> SplitPane.setResizableWithParent(item, false));
            }
            // If the node is a parent, recurse to find nested SplitPanes
            if (node instanceof Parent) {
                disableSplitPaneResizing((Parent) node);
            }
        }
    }


    @Override
	public void start(Stage primaryStage) {
		
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/main.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		launch(args);
	}

   
}