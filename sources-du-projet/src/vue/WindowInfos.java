package vue;

import java.io.IOException;


import javafx.scene.control.Alert;

public class WindowInfos extends Alert {

	public WindowInfos(AlertType arg0) {
		super(arg0);
	}
	
	public void start() throws IOException {
		this.setTitle("Informations");
		this.setHeaderText("Comment utiliser ce programme");
		this.setContentText("Comment manipuler le modèle ? \n"
				+ "-Un clic gauche et un drag permet de faire pivoter votre modèle sur l'axe X et Y \n"
				+ "-Un clic droit et un drag permet de faire pivoter votre modèle sur l'axe Z\n"
				+ "-Les deux clics enfoncés simultanément accompagnés d'un drag vous permet de déplacer votre modèle dans l'espace de travail\n"
				+ "-La molette vous permet de zoomer ou dezoomer");	
		this.getDialogPane().setMinHeight(300);
		this.getDialogPane().setMinWidth(600);
		this.show();
		
	}

	
}
