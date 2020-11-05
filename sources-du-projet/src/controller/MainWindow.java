package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import modele.CreationFaceException;
import modele.CreationPointException;
import modele.PlyFile;
import modele.PlyReader;
import modele.Rotation;

public class MainWindow {

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	Stage stage;
	ObservableList<String> listLien;
	ObservableList<String> listRecentlyOpened;
	PlyFile ply;
	PlyReader aPlyReader;
	String stringDirectory;
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
	@FXML
	Slider sliderX;
	@FXML 
	Slider sliderY;
	@FXML
	Slider sliderZ;
	@FXML
	Slider sliderZoom;

	/**
	 * Peuple les listView avant l'affichage de la fenetre, initialise les eventHandler
	 * @throws IOException
	 */
	public void initialize() throws IOException {
		listLien = FXCollections.observableArrayList();
		listRecentlyOpened = FXCollections.observableArrayList();
		stringDirectory = "sources-du-projet/exemples/";
		File repertory = new File(stringDirectory);
		if(!repertory.isDirectory()) System.out.println("Pas un repertoire !");
		File fileList[] = repertory.listFiles();
		for(File f : fileList)
			if(f.getName().contains(".ply"))
				listLien.add(f.getName());
		listViewFiles.setItems(listLien);
		recentlyOpened.setItems(listRecentlyOpened);
		/**
		 * On lie les sliders avec les fonctions de matrices qui leur correspondent
		 */
		sliderX.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.X, (double)newValue-(double)oldValue));	
				ply.draw(canvas);
			}		
		});

		sliderY.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {

				ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Y, (double)newValue-(double)oldValue));	
				ply.draw(canvas);
			}		
		});

		sliderZ.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Z, (double)newValue-(double)oldValue));	
				ply.draw(canvas);
			}		
		});
		/**
		 * A revoir
		 */
		sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				ply.setMatricePoint(ply.getMatricePoint().multiplication((double)newValue));	
				ply.draw(canvas);
			}		
		});

	}

	/**
	 * Permet de choisir un dossier contenant des ply pour les afficher dans une listView
	 */
	public void buttonPressedParcourir() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);
		stringDirectory = tmp.getParent();
		if(tmp != null && !listLien.contains((String) tmp.getName())) {
			listLien.add(tmp.getName());
		}
	}

	
	public void buttonPressedAfficher() throws FileNotFoundException, CreationPointException, CreationFaceException {
		aPlyReader = new PlyReader(stringDirectory + listViewFiles.getSelectionModel().getSelectedItem());
		aPlyReader.initPly();
		aPlyReader.readPly();
		ply = new PlyFile(aPlyReader.getListFace(), aPlyReader.getListPoint(), aPlyReader.getPath());
		ply.draw(canvas);
		if(!listRecentlyOpened.contains(listViewFiles.getSelectionModel().getSelectedItem()))
			listRecentlyOpened.add(listViewFiles.getSelectionModel().getSelectedItem());

	}

}
