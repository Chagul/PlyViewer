package controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.PlyFile;
import modele.PlyReader;
import modele.Rotation;
import vue.WindowError;
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
	ArrayList<PlyFile> listOfPlyFiles;
	int nbOngletActifs;
	Tab localOnglet;
	Stage stage;
	ObservableList<File> listLien;
	ObservableList<File> listRecentlyOpened;
	Canvas canvasModele;
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
	Tab ongletBase;

	@FXML
	TabPane onglets;

	/*
	@FXML
	Canvas canvas;
	 */

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
		listOfPlyFiles = new ArrayList<>();
		nbOngletActifs = 0;
		localOnglet = new Tab();
		//localOnglet.setId("$" + ongletBase.getId());
		FXMLLoader loader2 = new FXMLLoader(getClass().getResource("/vue/view2.fxml"));
		canvasModele = (Canvas) loader2.load();
		
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
										listOfPlyFiles.add(ply);
										ply = null;
									}catch(FileNotFoundException fileException) {
										fileException.printStackTrace();
									} finally {
										Canvas newCanvas = new Canvas();
										newCanvas.setHeight(570);
										newCanvas.setWidth(1160);
										newCanvas.setLayoutX(-1.0);
										newCanvas.setId("c" + nbOngletActifs);
										newCanvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent );
										newCanvas.addEventHandler(ScrollEvent.SCROLL_STARTED,mousescrollEvent);


						                Tab tmp = new Tab(value.getName().substring(0, value.getName().length()-4));
						                tmp.setContent(newCanvas);

						                onglets.getTabs().add(tmp);
										//onglets.getTabs().get(nbOngletActifs).setText(value.getName().substring(0, value.getName().length()-4));  //Modifie le titre de l'onglet.

										listOfPlyFiles.get(nbOngletActifs).firstDraw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());

										if(!listRecentlyOpened.contains(value))
											listRecentlyOpened.add(value);
										nbOngletActifs++;
									}                       
								}
								
								if(mouseClickedEvent.getButton().equals(MouseButton.SECONDARY)) {
									ContextMenu popUp = new ContextMenu();
									MenuItem stopReading = new MenuItem("Fermer");
									stopReading.setOnAction((ActionEvent e) -> {
										Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
										PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
										GraphicsContext gc = selected.getGraphicsContext2D();
										gc.clearRect(0, 0, selected.getWidth(), selected.getHeight());
										listLien.remove(value);
										plySelected = null;
										selected.removeEventHandler(MouseEvent.ANY,mouseDraggedEvent );
						                selected.removeEventHandler(ScrollEvent.SCROLL_STARTED,mousescrollEvent);
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
						Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
						PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth()/2, -selected.getHeight()/2, 0));
						plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Y, rotationX));
						plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.X, rotationY));
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth()/2, selected.getHeight()/2, 0));
						plySelected.draw(selected);
					}
					/**
					 * Clic droit = rotation Z
					 */
					if(dansFenetre && !mouseDragged.isPrimaryButtonDown()  && mouseDragged.isSecondaryButtonDown()) {
						Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
						PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth()/2, -selected.getHeight()/2, 0));
						plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Z, rotationX));
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth()/2, selected.getHeight()/2, 0));
						plySelected.draw(selected);
					}
					/**
					 *  Deux clic en même temps = translation
					 */
					if(dansFenetre && mouseDragged.isPrimaryButtonDown()  && mouseDragged.isSecondaryButtonDown()) {
						Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
						PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth()/2, -selected.getHeight()/2, 0));
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(mouseDragged.getSceneX()-dX ,mouseDragged.getSceneY()-dY,0));
						plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth()/2, selected.getHeight()/2, 0));
						plySelected.draw(selected);
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
				PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				double zoom = 1.05;
				double deltaY = wheelScroll.getDeltaY();
				if(deltaY < 0)
					zoom = 0.95;
				plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-plySelected.getPointDuMilieu().getX(), -plySelected.getPointDuMilieu().getY(), 0));
				plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth()/2, -selected.getHeight()/2, 0));

				plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));

				plySelected.getPointDuMilieu().setX(plySelected.getPointDuMilieu().getX() * zoom);
				plySelected.getPointDuMilieu().setY(plySelected.getPointDuMilieu().getY() * zoom);
				
				
				plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth()/2, selected.getHeight()/2, 0));
				plySelected.setMatricePoint(plySelected.getMatricePoint().translation(plySelected.getPointDuMilieu().getX(), plySelected.getPointDuMilieu().getY(), 0));
				plySelected.draw(selected);

			}
		};
		/**
		 * On lie les sliders avec les fonctions de matrices qui leur correspondent
		 */
		sliderXListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.X, (double)newValue-(double)oldValue));
				plySelected.draw(selected);
			}		
		};

		sliderYListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Y, (double)newValue-(double)oldValue));
				plySelected.draw(selected);
			}		
		};

		sliderZListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Z, (double)newValue-(double)oldValue));
				plySelected.draw(selected);
			}		
		};
		sliderZoomListener = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				double zoom = 1.05;
				if((double) oldValue > (double)newValue)
					zoom = 0.95;
				plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));
				plySelected.draw(selected);
			}		
		};
		
		//onglets.getTabs().get(0).setText("1");

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
		WindowError error = new WindowError();
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);

		if(tmp != null && !listLien.contains(tmp)) {
			listLien.add(tmp);

			try {
				//Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				aPlyReader.initPly(tmp.getAbsolutePath());
				ply = aPlyReader.getPly(tmp.getAbsolutePath());
				listOfPlyFiles.add(ply);
				ply = null;

				//selected.addEventHandler(MouseEvent.ANY,mouseDraggedEvent );
				//selected.addEventHandler(ScrollEvent.ANY, mousescrollEvent);

				sliderX.valueProperty().addListener(sliderXListener);
				sliderY.valueProperty().addListener(sliderYListener);
				sliderZ.valueProperty().addListener(sliderZListener);
				sliderZoom.valueProperty().addListener(sliderZoomListener);

				Canvas newCanvas = new Canvas();
				newCanvas.setHeight(570);
				newCanvas.setWidth(1160);
				newCanvas.setLayoutX(-1.0);
				newCanvas.setId("c" + nbOngletActifs);
				newCanvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent );
				newCanvas.addEventHandler(ScrollEvent.SCROLL_STARTED,mousescrollEvent);


				Tab tmpTab = new Tab(tmp.getName().substring(0, tmp.getName().length()-4)); //Modifie le titre de l'onglet.);
				tmpTab.setContent(newCanvas);

				onglets.getTabs().add(tmpTab);
				onglets.getTabs().get(nbOngletActifs).setText(tmp.getName().substring(0, tmp.getName().length()-4));  //Modifie le titre de l'onglet.

				listOfPlyFiles.get(nbOngletActifs).firstDraw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());
				//ply.firstDraw(selected);

				if(!listRecentlyOpened.contains(tmp))
					listRecentlyOpened.add(tmp);
				nbOngletActifs++;
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
			Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
			File tmp = listViewFiles.getSelectionModel().getSelectedItem();
			aPlyReader.initPly(tmp.getAbsolutePath());
			ply = aPlyReader.getPly(tmp.getAbsolutePath());
			selected.addEventHandler(MouseEvent.ANY,mouseDraggedEvent );
			selected.addEventHandler(ScrollEvent.ANY, mousescrollEvent);
			sliderX.valueProperty().addListener(sliderXListener);
			sliderY.valueProperty().addListener(sliderYListener);
			sliderZ.valueProperty().addListener(sliderZListener);
			sliderZoom.valueProperty().addListener(sliderZoomListener);
			ply.firstDraw(selected);
			onglets.getTabs().get(onglets.getTabs().size()-1).setText(tmp.getName()); //Modifie le titre de l'onglet.
			if(!listRecentlyOpened.contains(tmp))
				listRecentlyOpened.add(tmp);
		}catch(FileNotFoundException fileException) {
			fileException.printStackTrace();
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
		PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(plySelected != null) {
			Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
			double zoom = 1.05;
			plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));
			plySelected.draw(selected);
		}
	}

	/**
	 * Zoom- depuis le button.
	 */
	public void buttonPressedZoomMoins() {
		PlyFile plySelected = (PlyFile) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(plySelected != null) {
			Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
			double zoom = 0.95;
			plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));
			plySelected.draw(selected);
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
