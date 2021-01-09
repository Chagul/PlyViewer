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
		this.setContentText("blablabla");
		
	}

	
}
