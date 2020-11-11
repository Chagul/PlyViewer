package controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.PlyFile;
import modele.PlyReader;
import modele.Rotation;
/**
 * Controller Principal
 * @author planckea kharmacm
 * @version 09/11/2020
 */
public class MainWindow {

	/**
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	Stage stage;
	ObservableList<File> listLien;
	ObservableList<File> listRecentlyOpened;
	PlyFile ply;
	PlyReader aPlyReader = new PlyReader();
	EventHandler<MouseEvent> mouseDraggedEvent;
	EventHandler<ScrollEvent> mousescrollEvent;

	@FXML
	Canvas canvas;
	@FXML
	ListView<File> listViewFiles;
	@FXML
	Button parcourir;
	@FXML
	ListView<File> recentlyOpened;
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


		listViewFiles.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {


			@Override
			public ListCell<File> call(ListView<File> param) {
				return new ListCell<File>() {

					@Override
					protected void updateItem(File value, boolean empty) {

						super.updateItem(value, empty);
						if (empty || value == null || value.getName() == null)
							setText(null);
						else {
							setText(value.getName());
							setOnMouseClicked(mouseClickedEvent -> {
				                if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 2) {
				                	try {
				            			aPlyReader.initPly(value.getAbsolutePath());
				            			ply = aPlyReader.getPly(value.getAbsolutePath());
				            		}catch(FileNotFoundException fileException) {
				            			fileException.printStackTrace();
				            		}finally {
				            			/*if(!ply.getErrorList().isEmpty())
				            				new Stage();*/
				            			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedEvent );
				            			canvas.setOnScroll(mousescrollEvent);
				            			ply.firstDraw(canvas);
				            			if(!listRecentlyOpened.contains(value))
				            				listRecentlyOpened.add(value);
				            		}                       
				                }
				            });
						}
							

					}
				};
			}
		});

		recentlyOpened.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
			@Override
			public ListCell<File> call(ListView<File> param) {
				return new ListCell<File>() {

					@Override
					protected void updateItem(File value, boolean empty) {

						super.updateItem(value, empty);
						if (empty || value == null || value.getName() == null)
							setText(null);
						else
							setText(value.getName());
					}};
			}
		});		
		listViewFiles.setItems(listLien);
		recentlyOpened.setItems(listRecentlyOpened);


		mouseDraggedEvent = new EventHandler<MouseEvent>() {
			double dX;
			double dY;
			double rotationX;
			double rotationY;
			@Override
			public void handle(MouseEvent mouseDragged) {
				rotationX = (mouseDragged.getSceneX()-dX);
				rotationY = (mouseDragged.getSceneY()-dY);

				/**
				 * Clic gauche = rotation X et Y
				 */
				if(mouseDragged.isPrimaryButtonDown() && !mouseDragged.isSecondaryButtonDown() ) {

					ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.X, rotationY));
					ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Y, -rotationX));
					ply.draw(canvas);
				}
				/**
				 * Clic droit = rotation Z
				 */
				if(!mouseDragged.isPrimaryButtonDown()  && mouseDragged.isSecondaryButtonDown()) {
					ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Z, rotationX));	
					ply.draw(canvas);
				}
				/**
				 *  Deux clic en même temps = translation
				 */
				if(mouseDragged.isPrimaryButtonDown()  && mouseDragged.isSecondaryButtonDown()) {
					ply.setMatricePoint(ply.getMatricePoint().translation(mouseDragged.getSceneX(),mouseDragged.getSceneY(),1));	
					ply.draw(canvas);
				}
				dX = mouseDragged.getSceneX();
				dY = mouseDragged.getSceneY();
			}
		};

		/**
		 * Scroll souris = zoom ou dézoom selon le sens
		 */
		mousescrollEvent = new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent wheelScroll) {
				double zoom = 1.05;
				double deltaY = wheelScroll.getDeltaY();
				if(deltaY < 0)
					zoom = 0.95;

				ply.setMatricePoint(ply.getMatricePoint().multiplication(zoom));
				ply.draw(canvas);

			}
		};
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
		sliderZoom.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				double zoom = 1.05;
				if((double) oldValue > (double)newValue)
					zoom = 0.95;
				ply.setMatricePoint(ply.getMatricePoint().multiplication(zoom));	
				ply.draw(canvas);
			}		
		});

	}

	/**
	 * Permet de choisir un dossier contenant des ply pour les afficher dans une listView
	 * @throws IOException 
	 */
	public void buttonPressedParcourir() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);

		if(tmp != null && !listLien.contains(tmp)) {
			listLien.add(tmp);
		}
	}

	/**
	 * Lit un fichier ply à partir de la selection de l'utilisateur, le charge en mémoire et l'affiche
	 * @throws FileNotFoundException si le fichier selectionné n'est pas trouvé
	 * @throws CreationPointException si il y a un problème à la creation d'un point
	 * @throws CreationFaceException si il y a un problème à la création d'une face
	 */
	public void buttonPressedAfficher(){
		try {
			aPlyReader.initPly(listViewFiles.getSelectionModel().getSelectedItem().getAbsolutePath());
			ply = aPlyReader.getPly(listViewFiles.getSelectionModel().getSelectedItem().getAbsolutePath());
		}catch(FileNotFoundException fileException) {
			fileException.printStackTrace();
		}finally {
			/*if(!ply.getErrorList().isEmpty())
				new Stage();*/
			canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED, mouseDraggedEvent );
			canvas.setOnScroll(mousescrollEvent);
			ply.firstDraw(canvas);
			if(!listRecentlyOpened.contains(listViewFiles.getSelectionModel().getSelectedItem()))
				listRecentlyOpened.add(listViewFiles.getSelectionModel().getSelectedItem());
		}

	}

}
