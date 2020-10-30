package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modele.CreationFaceException;
import modele.CreationPointException;
import modele.Face;
import modele.Matrice;
import modele.MatriceBonne;
import modele.PlyReader;

public class MainWindow{

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	Stage stage;
	ObservableList<String> listLien;
	ObservableList<String> listRecentlyOpened;
	@FXML
	Canvas canvas;
	@FXML
	ListView<String> listViewFiles;
	@FXML
	Button parcourir;
	@FXML
	ListView<String> recentlyOpened;
	@FXML
	Button afficher;
	

	public void initialize() throws IOException {
		listLien = FXCollections.observableArrayList();
		listRecentlyOpened = FXCollections.observableArrayList();
		File repertory = new File("sources-du-projet/exemples/");
		if(!repertory.isDirectory()) System.out.println("Pas un repertoire !");
		File fileList[] = repertory.listFiles();
		for(File f : fileList)
			listLien.add(f.getCanonicalPath());
		listViewFiles.setItems(listLien);
		recentlyOpened.setItems(listRecentlyOpened);
	}

	/**
	 * Permet de choisir un dossier contenant des ply pour les afficher dans une listView
	 */
	public void buttonPressedParcourir() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);
		if(tmp != null) {
			listLien.add(tmp.getName());
		}
	}
	
	public void buttonPressedAfficher() throws FileNotFoundException, CreationPointException, CreationFaceException {
		draw(listViewFiles.getSelectionModel().getSelectedItem());
		if(!listRecentlyOpened.contains(listViewFiles.getSelectionModel().getSelectedItem()))
			listRecentlyOpened.add(listViewFiles.getSelectionModel().getSelectedItem());
		
	}
	
	public void draw(String pathToPly) throws FileNotFoundException, CreationPointException, CreationFaceException {
		//double ratioX = canvas.getWidth() / (canvas.getWidth()+1280);
		//double ratioY = canvas.getHeight() / (canvas.getHeight() + 800);
		PlyReader aPlyReader = new PlyReader(pathToPly);
		aPlyReader.initPly();
		aPlyReader.readPly();
		MatriceBonne matricePoint = new MatriceBonne(aPlyReader.getListPointTab());
		matricePoint = matricePoint.multiplication(100.0);
		ArrayList<Face> listFace = new ArrayList<Face>();
		listFace.addAll(aPlyReader.getListFace());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.translate(canvas.getWidth()/2, canvas.getHeight()/2);
		int ntmp = 0;
		gc.beginPath();
		for (Face face : listFace) {
			for(int i = 0; i < face.getListPoint().size(); i++) {
				if(i < face.getListPoint().size() - 1) 				
					//System.out.println("Face : " + ntmp + "\t" +  matricePoint.getM()[face.getListPoint().get(i).getId()][0] + ":" + matricePoint.getM()[face.getListPoint().get(i).getId()][1] + "\tà\t" + matricePoint.getM()[face.getListPoint().get(i+1).getId()][0] + ":" + matricePoint.getM()[face.getListPoint().get(i+1).getId()][1]);
					gc.strokeLine(matricePoint.getM()[face.getListPoint().get(i).getId()][0], matricePoint.getM()[face.getListPoint().get(i).getId()][1], matricePoint.getM()[face.getListPoint().get(i+1).getId()][0],matricePoint.getM()[face.getListPoint().get(i+1).getId()][1] );
				else
					gc.strokeLine(matricePoint.getM()[face.getListPoint().get(i).getId()][0], matricePoint.getM()[face.getListPoint().get(i).getId()][1], matricePoint.getM()[face.getListPoint().get(0).getId()][0],matricePoint.getM()[face.getListPoint().get(0).getId()][1] );
			}
			ntmp++;
		}
		gc.closePath();
		gc.translate(-canvas.getWidth()/2, -canvas.getHeight()/2);
	}
}
