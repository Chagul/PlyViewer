package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
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
import modele.PlyReader;

public class MainWindow{

	public void setStage(Stage stage) {
		this.stage = stage;
	}
	
	Stage stage;
	ObservableList<String> listLien = FXCollections.observableArrayList();
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
		File repertory = new File("sources-du-projet/exemples/");
		if(!repertory.isDirectory()) System.out.println("Pas un repertoire !");
		File fileList[] = repertory.listFiles();
		for(File f : fileList) {
			listLien.add(f.getCanonicalPath());
			//System.out.println(f.getName());
		}
		listViewFiles.setItems(listLien);
	}

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
	}
	
	public void draw(String pathToPly) throws FileNotFoundException, CreationPointException, CreationFaceException {
		//double ratioX = canvas.getWidth() / (canvas.getWidth()+1280);
		//double ratioY = canvas.getHeight() / (canvas.getHeight() + 800);
		PlyReader aPlyReader = new PlyReader(pathToPly);
		aPlyReader.initPly();
		aPlyReader.readPly();
		//System.out.println(Arrays.deepToString(aPlyReader.getListPointTab()));
		Matrice matricePoint = new Matrice(aPlyReader.getListPointTab());
		//System.out.println(matricePoint.toString());	
		matricePoint = matricePoint.multiplication(100.0);
		//System.out.println(matricePoint.toString());
		ArrayList<Face> listFace = new ArrayList<Face>();
		listFace.addAll(aPlyReader.getListFace());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.beginPath();
		gc.translate(canvas.getWidth()/2, canvas.getHeight()/2);
		for (Face face : listFace) {
			for(int i = 0; i < face.getListPoint().size(); i++) {
				if(i < face.getListPoint().size() - 1)
					gc.strokeLine(face.getListPoint().get(i).getX()*100, face.getListPoint().get(i).getY()*100, face.getListPoint().get(i+1).getX()*100, face.getListPoint().get(i+1).getY()*100);
				else
					gc.strokeLine(face.getListPoint().get(i).getX()*100, face.getListPoint().get(i).getY()*100, face.getListPoint().get(0).getX()*100, face.getListPoint().get(0).getY()*100);
			}
		}
		gc.closePath();
	}
	/*public void initialize() throws FileNotFoundException, CreationPointException, CreationFaceException {
		Point2D p1 = new Point2D(15,15);
		canvas.getGraphicsContext2D().fillRect(p1.getX(), p1.getY(), 150, 150);
		double ratioX = canvas.getWidth() / (canvas.getWidth()+1280);
		double ratioY = canvas.getHeight() / (canvas.getHeight() + 800);
		PlyReader aPlyReader = new PlyReader("sources-du-projet/exemples/dodecahedron.ply");
		aPlyReader.initPly();
		aPlyReader.readPly();
		System.out.println(Arrays.deepToString(aPlyReader.getListPointTab()));
		Matrice matricePoint = new Matrice(aPlyReader.getListPointTab());
		System.out.println(matricePoint.toString());	
		matricePoint = matricePoint.multiplication(100.0);
		System.out.println(matricePoint.toString());
		ArrayList<Face> listFace = new ArrayList<Face>();
		listFace.addAll(aPlyReader.getListFace());
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.beginPath();
		gc.translate(600, 400);
		for (Face face : listFace) {
			for(int i = 0; i < face.getListPoint().size(); i++) {
				if(i < face.getListPoint().size() - 1)
					gc.strokeLine(face.getListPoint().get(i).getX()*100, face.getListPoint().get(i).getY()*100, face.getListPoint().get(i+1).getX()*100, face.getListPoint().get(i+1).getY()*100);
				else
					gc.strokeLine(face.getListPoint().get(i).getX()*100, face.getListPoint().get(i).getY()*100, face.getListPoint().get(0).getX()*100, face.getListPoint().get(0).getY()*100);
			}
		}
		gc.closePath();
	}*/
}
