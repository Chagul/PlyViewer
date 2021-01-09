package vue;

import java.io.IOException;

import controller.MainWindow;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.outils.ErrorList;


/**
 * EN test
 * @author aurelien
 *
 */
public class WindowError{
	static public ErrorList errorList;
	static public Scene scene;
	static public Stage stage;
	
	public void start() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/viewErrorWindow.fxml"));
		AnchorPane root = (AnchorPane) loader.load();
		stage = new Stage();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(MainWindow.stage);
		stage.show();
	}
}
