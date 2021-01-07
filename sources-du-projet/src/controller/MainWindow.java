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
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import modele.Model3D;
import modele.Observateur;
import modele.PlyReader;
import modele.Rotation;
/**
 * Controller Principal
 * @author planckea kharmacm
 * @version 09/11/2020
 */
public class MainWindow implements Observateur{

	/**
	 * 
	 * @param stage
	 */
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	ArrayList<Model3D> listOfPlyFiles;
	int nbOngletActifs;

	Stage stage;
	ObservableList<File> listLien;
	ObservableList<File> listRecentlyOpened;
	Model3D ply;
	PlyReader aPlyReader = new PlyReader();
	@FXML
	TabPane onglets;

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

		//Permettre la fermeture des onglets pour lesquels Closable est à true.
		onglets.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);


		//Liste des objets PlyFile
		listOfPlyFiles = new ArrayList<>();

		//Compteur des nombre d'onglest actifs
		nbOngletActifs = 0;

		//Listes observables
		listLien = FXCollections.observableArrayList();
		listRecentlyOpened = FXCollections.observableArrayList();

		//Listes ListView
		listViewFiles.setCellFactory(listViewFilesFactory());
		recentlyOpened.setCellFactory(recentlyOpenedFactory());

		//Ajoute les Objets ObservableList à leurs ListeView respectifs.
		listViewFiles.setItems(listLien);
		recentlyOpened.setItems(listRecentlyOpened);
	}

	/**
	 * Gerer les propriétés des cellules de la liste des fichiers listViewFiles
	 * @return Callback
	 */
	public Callback<ListView<File>, ListCell<File>> listViewFilesFactory() {
		Callback<ListView<File>, ListCell<File>> res = new Callback<ListView<File>, ListCell<File>>() {
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
							setOnMouseClicked(mouseClickedEvent(value));
						}
					}
				};
			}
		};
		return res;
	}

	/**
	 * Gerer les propriétés des cellules de la liste des fichiers recentlyOpened
	 * @return Callback
	 */
	public Callback<ListView<File>, ListCell<File>> recentlyOpenedFactory() {
		Callback<ListView<File>, ListCell<File>> res = new Callback<ListView<File>, ListCell<File>>() {
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
		};
		return res;
	}

	/**
	 * Méthode gerant les evenemnts de souris sur les cellules d'une liste.
	 * @param value
	 * 		Le fichier contenu dans la cellule.
	 * @return EventHandler
	 */
	public EventHandler<MouseEvent> mouseClickedEvent(File value) {


		EventHandler<MouseEvent> res = new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseClickedEvent) {

				if (mouseClickedEvent.getButton().equals(MouseButton.PRIMARY) && mouseClickedEvent.getClickCount() == 2) {
					try {
						ply = null;
						aPlyReader.initPly(value.getAbsolutePath());
						ply = aPlyReader.getPly(value.getAbsolutePath());
						ply.ajouterObservateur((Observateur) MainWindow.this);
						listOfPlyFiles.add(ply);
					} catch (FileNotFoundException fileException) {
						fileException.printStackTrace();
					} finally {
						Canvas newCanvas = new Canvas();
						newCanvas.setHeight(570);
						newCanvas.setWidth(1160);
						newCanvas.setLayoutX(-1.0);
						newCanvas.setId("c" + nbOngletActifs);
						newCanvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent());
						newCanvas.addEventHandler(ScrollEvent.SCROLL_STARTED, mouseScrollEvent());


						Tab tmp = new Tab(value.getName().substring(0, value.getName().length() - 4));
						tmp.setClosable(true);
						tmp.setOnCloseRequest(fermetureOnglet());
						tmp.setContent(newCanvas);
						actualiser();
						onglets.getTabs().add(tmp);

						listOfPlyFiles.get(nbOngletActifs).firstDraw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());

						if (!listRecentlyOpened.contains(value))
							listRecentlyOpened.add(value);
						nbOngletActifs++;
					}
				}
			}
		};
		return res;
	}

	/**
	 * Gerer les conséquences de fermer un onglet
	 * @return EventHandler
	 */
	public EventHandler<Event> fermetureOnglet() {
		EventHandler<Event> res = new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Tab selectedTab = onglets.getSelectionModel().getSelectedItem();
				int idxOfClosedTab = onglets.getSelectionModel().getSelectedIndex();

				listOfPlyFiles.remove(idxOfClosedTab);
				onglets.getTabs().remove(selectedTab);
			}
		};
		return res;
	}

	/**
	 * On lie les sliders avec les fonctions de matrices qui leur correspondent
	 */
	public ChangeListener<Number> sliderXListener() {
		ChangeListener<Number> res = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.X, (double)newValue-(double)oldValue));

			}
		};
		return res;
	}
	public ChangeListener<Number> sliderYListener() {
		ChangeListener<Number> res = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Y, (double)newValue-(double)oldValue));
			}
		};
		return res;
	}
	public ChangeListener<Number> sliderZListener() {
		ChangeListener<Number> res = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Z, (double)newValue-(double)oldValue));
			}
		};
		return res;
	}
	public ChangeListener<Number> sliderZoomListener() {
		ChangeListener<Number> res = new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> arg0, Number oldValue, Number newValue) {
				Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
				double zoom = 1.05;
				if((double) oldValue > (double)newValue)
					zoom = 0.95;
				plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));
			}
		};
		return res;
	}

	/**
	 * L'évènement de glisser la souris avec les réactions en conséquence.
	 * @return EventHandler
	 */
	public EventHandler<MouseEvent> mouseDraggedEvent() {
		EventHandler<MouseEvent> res = new EventHandler<MouseEvent>() {
			double dX;
			double dY;
			double rotationX;
			double rotationY;
			boolean isDragged = false;
			boolean dansFenetre = true;
			@Override
			public void handle(MouseEvent mouseDragged) {
				if (!isDragged && mouseDragged.isDragDetect()) {
					isDragged = true;
					dX = mouseDragged.getSceneX();
					dY = mouseDragged.getSceneY();
				}
				if (isDragged && !mouseDragged.isDragDetect())
					isDragged = false;

				rotationX = (mouseDragged.getSceneX() - dX);
				rotationY = (mouseDragged.getSceneY() - dY);
				if (mouseDragged.getEventType().equals(MouseEvent.MOUSE_EXITED)) {
					dansFenetre = false;
				}
				if (mouseDragged.getEventType().equals(MouseEvent.MOUSE_ENTERED))
					dansFenetre = true;

				/**
				 * Clic gauche = rotation X et Y
				 */
				if (dansFenetre && mouseDragged.isPrimaryButtonDown() && !mouseDragged.isSecondaryButtonDown()) {
					Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
					Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth() / 2, -selected.getHeight() / 2, 0));
					plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Y, rotationX));
					plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.X, rotationY));
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth() / 2, selected.getHeight() / 2, 0));
				}
				/**
				 * Clic droit = rotation Z
				 */
				if (dansFenetre && !mouseDragged.isPrimaryButtonDown() && mouseDragged.isSecondaryButtonDown()) {
					Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
					Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth() / 2, -selected.getHeight() / 2, 0));
					plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Z, rotationX));
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth() / 2, selected.getHeight() / 2, 0));
				}
				/**
				 *  Deux clic en même temps = translation
				 */
				if (dansFenetre && mouseDragged.isPrimaryButtonDown() && mouseDragged.isSecondaryButtonDown()) {
					Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
					Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth() / 2, -selected.getHeight() / 2, 0));
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(mouseDragged.getSceneX() - dX, mouseDragged.getSceneY() - dY, 0));
					plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth() / 2, selected.getHeight() / 2, 0));
				}
				dX = mouseDragged.getSceneX();
				dY = mouseDragged.getSceneY();
			}
		};
		return res;
	}


	/**
	 * Scroll souris = zoom ou dézoom selon le sens
	 */
	public EventHandler<ScrollEvent> mouseScrollEvent() {
		EventHandler<ScrollEvent> res = new EventHandler<ScrollEvent>() {
			@Override
			public void handle(ScrollEvent wheelScroll) {
				Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
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
			}
		};
		return res;
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
		//WindowError error = new WindowError(); /* NOn utilisé pour l'instant*/
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);

		if(tmp != null && !listLien.contains(tmp)) {
			listLien.add(tmp);

			try {
				//Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
				aPlyReader.initPly(tmp.getAbsolutePath());
				ply = aPlyReader.getPly(tmp.getAbsolutePath());
				ply.ajouterObservateur((Observateur) MainWindow.this);
				listOfPlyFiles.add(ply);
				ply = null;

				//selected.addEventHandler(MouseEvent.ANY,mouseDraggedEvent );
				//selected.addEventHandler(ScrollEvent.ANY, mousescrollEvent);

				sliderX.valueProperty().addListener(sliderXListener());
				sliderY.valueProperty().addListener(sliderYListener());
				sliderZ.valueProperty().addListener(sliderZListener());
				sliderZoom.valueProperty().addListener(sliderZoomListener());

				Canvas newCanvas = new Canvas();
				newCanvas.setHeight(570);
				newCanvas.setWidth(1160);
				newCanvas.setLayoutX(-1.0);
				newCanvas.setId("c" + nbOngletActifs);
				newCanvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent());
				newCanvas.addEventHandler(ScrollEvent.ANY,mouseScrollEvent());

				Tab tmpTab = new Tab(tmp.getName().substring(0, tmp.getName().length()-4)); //Modifie le titre de l'onglet.);
				tmpTab.setClosable(true);
				tmpTab.setOnCloseRequest(fermetureOnglet());
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
			selected.addEventHandler(MouseEvent.ANY,mouseDraggedEvent());
			selected.addEventHandler(ScrollEvent.ANY, mouseScrollEvent());
			sliderX.valueProperty().addListener(sliderXListener());
			sliderY.valueProperty().addListener(sliderYListener());
			sliderZ.valueProperty().addListener(sliderZListener());
			sliderZoom.valueProperty().addListener(sliderZoomListener());
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
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(plySelected != null) {
			double zoom = 1.05;
			plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));
		}
	}

	/**
	 * Zoom- depuis le button.
	 */
	public void buttonPressedZoomMoins() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(plySelected != null) {
			double zoom = 0.95;
			plySelected.setMatricePoint(plySelected.getMatricePoint().multiplication(zoom));
		}
	}

	/**
	 * Button lissage (soon).
	 */
	public void buttonPressedLissage() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(!plySelected.isTraitDessine())
			plySelected.setTraitDessine(true);
		else
			plySelected.setTraitDessine(false);

		actualiser();

	}
	/**
	 * Button ombre (soon).
	 */
	public void buttonPressedOmbre() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(!plySelected.isFaceDessine())
			plySelected.setFaceDessine(true);
		else
			plySelected.setFaceDessine(false);

		actualiser();
	}

	/**
	 * Button vue tranches (soon).
	 */
	public void buttonPressedVueTranches() {
		Thread thr = new Thread(new Runnable() {
			Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
			@Override
			public void run() {
				try {
					while (true) {
						plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.X, 10));
					}
				}finally {
					System.out.println("stop");
				}
			}
		});
	}

	/**
	 * Button permettant de quitter le programme.
	 */
	public void buttonPressedQuitter() {
		Platform.exit();
		System.exit(1);
	}

	@Override
	public void actualiser() {
		Model3D selectedPly = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(selectedPly.isFaceDessine())
			selectedPly.drawFaces((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());
		else 
			selectedPly.draw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());


	}

}
