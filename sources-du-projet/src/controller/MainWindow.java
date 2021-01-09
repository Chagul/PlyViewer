package controller;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import modele.Model3D;
import modele.Observateur;
import modele.PlyReader;
import modele.Rotation;
import vue.WindowError;

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
		MainWindow.stage = stage;
	}
	Timeline rotation;
	ArrayList<Model3D> listOfPlyFiles;
	int nbOngletActifs;

	public static Stage stage;
	ObservableList<File> listLien;

	Model3D ply;
	PlyReader aPlyReader = new PlyReader();

	@FXML
	TabPane onglets;

	@FXML
	ListView<File> listViewFiles;

	@FXML
	VBox listFilesInformations;
	@FXML
	VBox listFiles;

	@FXML
	Label fileName;
	@FXML
	Label fileAuteur;
	@FXML
	Label fileDescription;
	@FXML
	Label fileNbFaces;
	@FXML
	Label fileNbPoints;

	@FXML
	Button parcourir;
	@FXML
	Button importOpen;
	@FXML
	Button aboutUs;

	@FXML
	ToggleButton buttonTrait;
	@FXML
	ToggleButton buttonLumiere;
	@FXML
	ToggleButton buttonAutoTurn;
	@FXML
	ToggleButton buttonFaces;
	@FXML
	Button buttonFaces1;

	@FXML
	Button quitter;

	/**
	 * Peuple les listView avant l'affichage de la fenetre, initialise les eventHandler
	 * @throws IOException
	 */
	public void initialize() throws IOException {

		//Permettre la fermeture des onglets pour lesquels Closable est à true.
		onglets.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

		//Faire corréspondre les informations affichées à l'onglet selectionné par l'utilisateur
		onglets.getSelectionModel().selectedItemProperty().addListener(informationSwitch());
		
		//Met à jour les toggleButton pour chaque modèle
		onglets.getSelectionModel().selectedItemProperty().addListener(buttonMaj());

		//Liste des objets PlyFile
		listOfPlyFiles = new ArrayList<>();

		//Compteur des nombre d'onglest actifs
		nbOngletActifs = 0;

		//Listes observables
		listLien = FXCollections.observableArrayList();


		//Listes ListView
		listViewFiles.setCellFactory(listViewFilesFactory());


		//Ajoute les Objets ObservableList à leurs ListeView respectifs.
		listViewFiles.setItems(listLien);

	}

	/**
	 * Creer un listener qui permet de lier les informations affichées avec le bon onglet
	 * @return le listener créé
	 */
	public ChangeListener<Tab> informationSwitch() {
		ChangeListener<Tab> res = new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				int idxOfPlyFile = onglets.getTabs().indexOf(newValue);
				Model3D currentModel = null;
				if(idxOfPlyFile != -1) { currentModel = listOfPlyFiles.get(idxOfPlyFile); }

				if(currentModel != null) {
					fileName.setText("Nom de Fichier : " + observable.getValue().getText());
					fileAuteur.setText("Auteur : " + currentModel.getAuteur());
					fileDescription.setText("Description : " + currentModel.getDescription());
					fileNbFaces.setText("Nombre de Faces : " + currentModel.getNbFaces());
					fileNbPoints.setText("Nombre de Points : " + currentModel.getNbPoints());
				}
			}
		};
		return res;
	}


	/**
	 * Créer un listener qui permet de lier les états des toggle Button avec l'état du ply dans chaque onglet
	 * @return le listener créé
	 */
	public ChangeListener<Tab> buttonMaj(){
		ChangeListener<Tab> update = new ChangeListener<Tab>() {
			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldValue, Tab newValue) {
				int idx = onglets.getTabs().indexOf(newValue);
				if(idx >= 0) {
					Model3D selectedPly = listOfPlyFiles.get(idx);
					buttonAutoTurn.setSelected(selectedPly.isTurning());
					buttonFaces.setSelected(selectedPly.isFaceDessine());
					buttonTrait.setSelected(selectedPly.isTraitDessine());
					buttonLumiere.setSelected(selectedPly.isLumiereActive());
				}
			}
		};
		return update;
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
						onglets.getSelectionModel().select(tmp);

						listOfPlyFiles.get(nbOngletActifs).firstDraw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());

						nbOngletActifs++;
					}
				}
			}
		};
		return res;
	}

	/**
	 * Gerer les conséquences de l'appel à fermetureOnglet()
	 * @return EventHandler
	 */
	public EventHandler<Event> fermetureOnglet() {
		EventHandler<Event> res = new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				Tab selectedTab = onglets.getSelectionModel().getSelectedItem();
				int idxOfClosedTab = onglets.getSelectionModel().getSelectedIndex();

				if(idxOfClosedTab >= 0) {
					listOfPlyFiles.remove(idxOfClosedTab);
					onglets.getTabs().remove(selectedTab);
					listLien.remove(idxOfClosedTab);
					nbOngletActifs--;
				}

				if(onglets.getTabs().isEmpty()) {
					fileName.setText("Nom de Fichier : " );
					fileAuteur.setText("Auteur : " );
					fileDescription.setText("Description : " );
					fileNbFaces.setText("Nombre de Faces : " );
					fileNbPoints.setText("Nombre de Points : ");
				}
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
		importerFichier();
	}

	/**
	 * Réalise l'import d'un fichier ply.
	 * @return le fichier importé par l'utilisateur
	 */
	public File importerFichier() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().add(
				new FileChooser.ExtensionFilter("PLYFILE", "*.ply"));
		File tmp = fileChooser.showOpenDialog(stage);

		if(tmp != null && !listLien.contains(tmp)) {
			listLien.add(tmp);
		}
		return tmp;
	}

	/**
	 * Lire un fichier passé en paramètre dans un nouvel onglet
	 * @param f
	 * 			f le fichier à lire
	 */
	public void lireFichier(File f) throws IOException{
		try {
			aPlyReader.initPly(f.getAbsolutePath());
			ply = aPlyReader.getPly(f.getAbsolutePath());
			if(!ply.getErrorList().isEmpty()) {
				WindowError errorWindow = new WindowError();
				WindowError.errorList = ply.getErrorList();
				errorWindow.start();
			}

			ply.ajouterObservateur((Observateur) MainWindow.this);
			listOfPlyFiles.add(ply);
			ply = null;
			Canvas newCanvas = new Canvas();
			newCanvas.setHeight(570);
			newCanvas.setWidth(1160);
			newCanvas.setLayoutX(-1.0);
			newCanvas.setId("c" + nbOngletActifs);
			newCanvas.addEventHandler(MouseEvent.ANY, mouseDraggedEvent());
			newCanvas.addEventHandler(ScrollEvent.ANY,mouseScrollEvent());

			Tab tmpTab = new Tab(f.getName().substring(0, f.getName().length()-4)); //Modifie le titre de l'onglet.);
			tmpTab.setClosable(true);
			tmpTab.setOnCloseRequest(fermetureOnglet());
			tmpTab.setContent(newCanvas);

			String name = f.getName().substring(0, f.getName().length()-4);
			tmpTab.setText(name);
			onglets.getTabs().add(tmpTab);
			onglets.getSelectionModel().select(tmpTab);

			//onglets.getTabs().get(nbOngletActifs).setText();  //Modifie le titre de l'onglet.

			listOfPlyFiles.get(nbOngletActifs).firstDraw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());
			nbOngletActifs++;
			this.buttonAutoTurn.setDisable(false);
			this.buttonLumiere.setDisable(false);
			this.buttonTrait.setDisable(false);
			this.buttonFaces.setDisable(false);
		}catch(FileNotFoundException fileException) {
			fileException.printStackTrace();
		}
	}

	/**
	 * Button qui importe un fichier selectionné par l'utilisateur et le lit
	 * @throws IOException
	 */
	public void buttonPressedParcourirEtOuvrir() throws IOException {
		lireFichier(importerFichier());
	}


	/**
	 * Lit un fichier ply à partir de la selection de l'utilisateur, le charge en mémoire et l'affiche
	 * @throws FileNotFoundException si le fichier selectionné n'est pas trouvé
	 * @throws CreationPointException si il y a un problème à la creation d'un point
	 * @throws CreationFaceException si il y a un problème à la création d'une face
	 */
	public void buttonPressedAfficher(){
		try {
			Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
			File tmp = listViewFiles.getSelectionModel().getSelectedItem();
			aPlyReader.initPly(tmp.getAbsolutePath());
			ply = aPlyReader.getPly(tmp.getAbsolutePath());
			selected.addEventHandler(MouseEvent.ANY,mouseDraggedEvent());
			selected.addEventHandler(ScrollEvent.ANY, mouseScrollEvent());
			ply.firstDraw(selected);
			onglets.getTabs().get(onglets.getTabs().size()-1).setText(tmp.getName()); //Modifie le titre de l'onglet.

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
	 * Action effectuée quand le bouton faces est enclenché. Les faces se dessinent.
	 */
	public void buttonPressedFaces() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(!plySelected.isFaceDessine())
			plySelected.setFaceDessine(true);
		else
			plySelected.setFaceDessine(false);

		actualiser();
	}
	/**
	 *Action effectuée quand le bouton trait est enclenché. Les traits se dessinent.
	 */
	public void buttonPressedTraits() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(!plySelected.isTraitDessine())
			plySelected.setTraitDessine(true);
		else
			plySelected.setTraitDessine(false);

		actualiser();

	}
	/**
	 * Action effectuée quand le bouton lumière est enclenché. La lumière est prise en compte et affichée.
	 */
	public void buttonPressedLumiere() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(!plySelected.isLumiereActive()) {
			plySelected.setLumiereActive(true);
			plySelected.setFaceDessine(true);
			this.buttonFaces.setSelected(true);
		}
		else
			plySelected.setLumiereActive(false);

		actualiser();
	}

	/**
	 * Action effectuée quand le bouton autoTurn est enclenché. Le modèle tourne automatiquement jusqu'à réenclement du bouton 
	 */
	public void buttonPressedAutoTurn() {
		Model3D plySelected = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		Canvas selected = (Canvas) onglets.getSelectionModel().getSelectedItem().getContent();
		if(!plySelected.isTurning()) {
			KeyFrame beggining = new KeyFrame(Duration.seconds(0));
			KeyFrame end = new KeyFrame(Duration.millis(100), event ->{
				plySelected.setMatricePoint(plySelected.getMatricePoint().translation(-selected.getWidth() / 2, -selected.getHeight() / 2, 0));
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.X, 1));
				plySelected.setMatricePoint(plySelected.getMatricePoint().rotation(Rotation.Y, 1));
				plySelected.setMatricePoint(plySelected.getMatricePoint().translation(selected.getWidth() / 2, selected.getHeight() / 2, 0));
			});
			rotation = new Timeline(beggining, end);
			rotation.setCycleCount(Timeline.INDEFINITE);
			rotation.play();
		}else {
			rotation.stop();
		}
		plySelected.setTurning(!plySelected.isTurning());
	}

	/**
	 * Button permettant de quitter le programme.
	 */
	public void buttonPressedQuitter() {
		Platform.exit();
		System.exit(1);
	}

	/**
	 * Actualise le modèle
	 */
	@Override
	public void actualiser() {
		Model3D selectedPly = (Model3D) listOfPlyFiles.get(onglets.getSelectionModel().getSelectedIndex());
		if(selectedPly.isFaceDessine())
			selectedPly.drawFaces((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());
		else 
			selectedPly.draw((Canvas) onglets.getSelectionModel().getSelectedItem().getContent());


	}


}
