package controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
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
	ChangeListener<Number> sliderXListener;
	ChangeListener<Number> sliderYListener;
	ChangeListener<Number> sliderZListener;
	ChangeListener<Number> sliderZoomListener;
	/**Button afficher supprimé puisqu'on peut ouvrir en faisant un double clic
	@FXML
	Button afficher;
	 **/

	@FXML
	Canvas canvas;

	@FXML
	ListView<File> listViewFiles;
	@FXML
	ListView<File> recentlyOpened;

	@FXML
	Button parcourir;
	@FXML
	Button importOpen;
	@FXML
	Button aboutUs;

	@FXML 
	Button buttonZoomPlus;
	@FXML
	Button buttonZoomMoins;
	@FXML
	Button buttonLissage;
	@FXML
	Button buttonOmbre;
	@FXML
	Button buttonVueTranches;

	@FXML
	Button quitter;

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
								
								if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY)) {
									ContextMenu popUp = new ContextMenu();
									MenuItem stopReading = new MenuItem("Fermer");
									stopReading.setOnAction((ActionEvent e) -> {
										GraphicsContext gc = canvas.getGraphicsContext2D();
										gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
										listLien.remove(value);
										ply = null;
										canvas.removeEventHandler(MouseEvent.ANY,mouseDraggedEvent );
						                canvas.removeEventHandler(ScrollEvent.SCROLL_STARTED,mousescrollEvent);
										sliderX.valueProperty().removeListener(sliderXListener);
										sliderY.valueProperty().removeListener(sliderYListener);
										sliderZ.valueProperty().removeListener(sliderZListener);
										sliderZoom.valueProperty().removeListener(sliderZoomListener);
									});
									popUp.getItems().add(stopReading);
									setContextMenu(popUp);
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
			boolean isDragged = false;
			boolean dansFenetre = true;
			@Override
			public void handle(MouseEvent mouseDragged) {
				if(!isDragged && mouseDragged.isDragDetect()) {
					isDragged = true;
					dX = mouseDragged.getSceneX();
					dY = mouseDragged.getSceneY();
				}
				if(isDragged && !mouseDragged.isDragDetect())
					isDragged = false;

				rotationX = (mouseDragged.getSceneX()-dX);
				rotationY = (mouseDragged.getSceneY()-dY);
				if(mouseDragged.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
					dansFenetre = false;
				}
				if(mouseDragged.getEventType().equals(MouseEvent.MOUSE_ENTERED))
					dansFenetre = true;
					/**
					 * Clic gauche = rotation X et Y
					 */
					if(dansFenetre && mouseDragged.isPrimaryButtonDown() && !mouseDragged.isSecondaryButtonDown() ) {
						ply.setMatricePoint(ply.getMatricePoint().translation(-canvas.getWidth()/2, -canvas.getHeight()/2, 1));
						ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.X, rotationY));
						ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Y, rotationX));
						ply.setMatricePoint(ply.getMatricePoint().translation(canvas.getWidth()/2, canvas.getHeight()/2, 1));
						ply.draw(canvas);
					}
					/**
					 * Clic droit = rotation Z
					 */
					if(dansFenetre && !mouseDragged.isPrimaryButtonDown()  && mouseDragged.isSecondaryButtonDown()) {
						ply.setMatricePoint(ply.getMatricePoint().translation(-canvas.getWidth()/2, -canvas.getHeight()/2, 1));
						ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Z, rotationX));	
						ply.setMatricePoint(ply.getMatricePoint().translation(canvas.getWidth()/2, canvas.getHeight()/2, 1));
						ply.draw(canvas);
					}
					/**
					 *  Deux clic en même temps = translation
					 */
					if(dansFenetre && mouseDragged.isPrimaryButtonDown()  && mouseDragged.isSecondaryButtonDown()) {
						ply.setMatricePoint(ply.getMatricePoint().translation(-canvas.getWidth()/2, -canvas.getHeight()/2, 1));
						ply.getPointDuMilieu().setX(ply.getPointDuMilieu().getX()+ mouseDragged.getSceneX()-dX);
						ply.getPointDuMilieu().setY(ply.getPointDuMilieu().getY()+ mouseDragged.getSceneY()-dY);
						ply.setMatricePoint(ply.getMatricePoint().translation(mouseDragged.getSceneX()-dX ,mouseDragged.getSceneY()-dY,1));	
						ply.setMatricePoint(ply.getMatricePoint().translation(canvas.getWidth()/2, canvas.getHeight()/2, 1));
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
				ply.setMatricePoint(ply.getMatricePoint().translation(-canvas.getWidth()/2, -canvas.getHeight()/2, 1));
				ply.setMatricePoint(ply.getMatricePoint().multiplication(zoom));
				ply.setMatricePoint(ply.getMatricePoint().translation(canvas.getWidth()/2, canvas.getHeight()/2, 1));
				ply.draw(canvas);

			}
		};
		/**
		 * On lie les sliders avec les fonctions de matrices qui leur correspondent
		 */
		sliderXListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.X, (double)newValue-(double)oldValue));	
				ply.draw(canvas);
			}		
		};

		sliderYListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {

				ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Y, (double)newValue-(double)oldValue));	
				ply.draw(canvas);
			}		
		};

		sliderZListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				ply.setMatricePoint(ply.getMatricePoint().rotation(Rotation.Z, (double)newValue-(double)oldValue));	
				ply.draw(canvas);
			}		
		};
		sliderZoomListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				double zoom = 1.05;
				if((double) oldValue > (double)newValue)
					zoom = 0.95;
				ply.setMatricePoint(ply.getMatricePoint().multiplication(zoom));	
				ply.draw(canvas);
			}		
		};

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

	public void buttonPressedParcourirEtOuvrir() throws IOException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);

		if(tmp != null && !listLien.contains(tmp)) {
			listLien.add(tmp);

			try {
				aPlyReader.initPly(tmp.getAbsolutePath());
				ply = aPlyReader.getPly(tmp.getAbsolutePath());
				canvas.addEventHandler(MouseEvent.ANY,mouseDraggedEvent );
				canvas.setOnScroll(mousescrollEvent);
				sliderX.valueProperty().addListener(sliderXListener);
				sliderY.valueProperty().addListener(sliderYListener);
				sliderZ.valueProperty().addListener(sliderZListener);
				sliderZoom.valueProperty().addListener(sliderZoomListener);
				ply.firstDraw(canvas);
				if(!listRecentlyOpened.contains(tmp))
					listRecentlyOpened.add(tmp);
			}catch(FileNotFoundException fileException) {
				fileException.printStackTrace();
			}
		}
	}


	/**
	 * Lit un fichier ply à partir de la selection de l'utilisateur, le charge en mémoire et l'affiche
	 * @throws FileNotFoundException si le fichier selectionné n'est pas trouvé
	 * @throws CreationPointException si il y a un problème à la creation d'un point
	 * @throws CreationFaceException si il y a un problème à la création d'une face
	 */
	public void buttonPressedAfficher(){
		//WindowError error = new WindowError();
		try {
			aPlyReader.initPly(listViewFiles.getSelectionModel().getSelectedItem().getAbsolutePath());
			ply = aPlyReader.getPly(listViewFiles.getSelectionModel().getSelectedItem().getAbsolutePath());
		}catch(FileNotFoundException fileException) {
			fileException.printStackTrace();
		}finally {
			if(!ply.getErrorList().isEmpty()) {
				/*error.setResizable(false);
				error.initModality(Modality.APPLICATION_MODAL);
				error.show();*/
				System.out.println(ply.getErrorList());

			}
			canvas.addEventHandler(MouseEvent.ANY,mouseDraggedEvent );
			canvas.setOnScroll(mousescrollEvent);
			sliderX.valueProperty().addListener(sliderXListener);
			sliderY.valueProperty().addListener(sliderYListener);
			sliderZ.valueProperty().addListener(sliderZListener);
			sliderZoom.valueProperty().addListener(sliderZoomListener);
			ply.firstDraw(canvas);
			if(!listRecentlyOpened.contains(listViewFiles.getSelectionModel().getSelectedItem()))
				listRecentlyOpened.add(listViewFiles.getSelectionModel().getSelectedItem());
		}

	}

	/**
	 * Possibilité d'avoir des infos sur l'application.
	 */
	public void buttonPressedAboutUs() {

	}

	/**
	 * Zoom+ depuis le bouton.
	 */
	public void buttonPressedZoomPlus() {
		if(this.ply != null) {
			double zoom = 1.05;
			ply.setMatricePoint(ply.getMatricePoint().multiplication(zoom));	
			ply.draw(canvas);
		}
	}

	/**
	 * Zoom- depuis le button.
	 */
	public void buttonPressedZoomMoins() {
		if(this.ply != null) {
			double zoom = 0.95;
			ply.setMatricePoint(ply.getMatricePoint().multiplication(zoom));	
			ply.draw(canvas);
		}
	}

	/**
	 * Button lissage (soon).
	 */
	public void buttonPressedLissage() {

	}
	/**
	 * Button ombre (soon).
	 */
	public void buttonPressedOmbre() {

	}

	/**
	 * Button vue tranches (soon).
	 */
	public void buttonPressedVueTranches() {

	}

	/**
	 * Button permettant de quitter le programme.
	 */
	public void buttonPressedQuitter() {
		Platform.exit();
		System.exit(1);
	}

}
