package vue;

import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import modele.ErrorList;


/**
 * EN test
 * @author aurelien
 *
 */
public class WindowError{
	ErrorList errorList;
	public void setErrorList(ErrorList anErrorList) {
		this.errorList = anErrorList;
	}
	
	public WindowError() {
		
	}
	public void start() {
		Stage stage = new Stage();
		Pane pane = new Pane();
		Text listError = new Text(errorList.toString());
		listError.setX(10);
		listError.setY(10);
		pane.getChildren().add(listError);
		stage.setResizable(false);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Erreur");
		stage.setScene(new Scene(new Pane(), 400, 400));
		stage.show();
	}
}
