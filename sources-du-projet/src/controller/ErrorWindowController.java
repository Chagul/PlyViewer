package controller;

import java.io.IOException;

import javafx.fxml.FXML;
import vue.WindowError;
import javafx.scene.text.Text;

public class ErrorWindowController {

	
	@FXML
	Text textListError;
	
	public void initialize() throws IOException {
		textListError.setText(WindowError.errorList.toString());
	}
	
	public void buttonPressedOK() {
		WindowError.stage.close();
	}
}
