package modele;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import modele.Math.Matrice;
import modele.Math.Rotation;
import modele.Math.Vecteur;
import modele.outils.CouplePoint;
import modele.outils.ErrorList;
import modele.outils.Observable;
import modele.outils.Observateur;
/**
 * Modele ply
 * @author planckea
 *
 */
public class Model3D implements Observable{

	private ArrayList<Face> arrayListFace;
	private Point[] tabPoint;
	private Matrice matricePoint;
	private double rapport;
	private ErrorList errorList;
	public boolean rapportHorizontal;
	private Point pointDuMilieu;
	private Observateur observateurCanvas;
	private boolean traitDessine;
	private boolean faceDessine;
	private boolean lumiereActive;
	private boolean isTurning;
	private Timeline rotationAuto;



	private String author;
	private String description;

	public Model3D(int nbPoint) {
		this.author = "non spécifié";
		this.description = "non spécifié";
		this.arrayListFace = new ArrayList<Face>();
		this.tabPoint = new Point[nbPoint];
		ArrayList<String> listPointErreur = new ArrayList<String>();
		ArrayList<String> listFaceErreur = new ArrayList<String>();
		this.errorList = new ErrorList(listPointErreur, listFaceErreur);
		this.setTraitDessine(false);
		this.setFaceDessine(false);
		this.setLumiereActive(false);
		this.setTurning(false);
		
	}
	

	/**
	 * Permet d'initialiser l'attribut matrice une fois la hashmap de point complète.
	 */
	public void initMatrice() {
		double[][] tabPointPourMatrice = new double[4][tabPoint.length];
		for(Point p : tabPoint) {
			tabPointPourMatrice[0][p.getId()] = p.getX();
			tabPointPourMatrice[1][p.getId()] = p.getY();
			tabPointPourMatrice[2][p.getId()] = p.getZ();
			tabPointPourMatrice[3][p.getId()] = 1;
		}
		this.matricePoint = new Matrice(tabPointPourMatrice);
	}

	/**
	 * Dessine le ply à partir de ses points et de ses faces et permet d'adapter l'image au canvas en proportion, ainsi que retourner l'image
	 * @param canvas Le canvas sur lequel le ply sera dessiné
	 */
	public void firstDraw(Canvas canvas) {
		final double RAPPORT_MISE_A_L_ECHELLE = 0.60;
		final double MISE_A_L_ECHELLE_HORIZONTALE = canvas.getWidth()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
		final double MISE_A_L_ECHELLE_VERTICALE = canvas.getHeight()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
		this.matricePoint = this.matricePoint.translation(-pointDuMilieu.getX(), -pointDuMilieu.getY(), -pointDuMilieu.getZ());
		if(rapportHorizontal) {
			this.matricePoint = this.matricePoint.multiplication( MISE_A_L_ECHELLE_HORIZONTALE);
			this.pointDuMilieu.setX(pointDuMilieu.getX() * MISE_A_L_ECHELLE_HORIZONTALE);
			this.pointDuMilieu.setY(pointDuMilieu.getY() * MISE_A_L_ECHELLE_HORIZONTALE);
			this.pointDuMilieu.setZ(pointDuMilieu.getZ() * MISE_A_L_ECHELLE_HORIZONTALE);
		}
		else {
			this.matricePoint = this.matricePoint.multiplication(MISE_A_L_ECHELLE_VERTICALE);
			this.pointDuMilieu.setX(pointDuMilieu.getX() * MISE_A_L_ECHELLE_VERTICALE);
			this.pointDuMilieu.setY(pointDuMilieu.getY() * MISE_A_L_ECHELLE_VERTICALE);
			this.pointDuMilieu.setZ(pointDuMilieu.getZ() * MISE_A_L_ECHELLE_VERTICALE);
		}

		this.matricePoint = this.matricePoint.rotation(Rotation.X, 180);
		this.matricePoint = this.matricePoint.translation(canvas.getWidth()/2, canvas.getHeight()/2, 0);
		draw(canvas);
	}
	
	/**
	 * Dessine un ply à partir de ses points et de ses faces
	 * @param canvas Le canvas sur lequel le ply sera dessiné
	 */
	public void draw(Canvas canvas)  {
		Set<CouplePoint> tracees = new HashSet<CouplePoint>();
		int ptA;
		int ptB;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.beginPath();
		for (Face face : arrayListFace) {
			for(int i = 0; i < face.getListPoint().size(); i++) {
				ptA = face.getListPoint().get(i).getId();
				if(i < face.getListPoint().size() - 1) 				
					ptB = face.getListPoint().get(i+1).getId();
				else
					ptB = face.getListPoint().get(0).getId();
				tracees.add(new CouplePoint(ptA, ptB));
				gc.strokeLine(matricePoint.getM()[0][ptA], matricePoint.getM()[1][ptA], matricePoint.getM()[0][ptB], matricePoint.getM()[1][ptB]);
			}
		}
		gc.closePath();
	}

	/**
	 * Dessine les faces avec ou sans lumière selon les boutons enclenchés
	 * @param canvas Le canvas sur lequel serra dessiné le modèle
	 */
	public void drawFaces(Canvas canvas) {
		double[] coordX;
		double[] coordY;
		double[] coordZ;
		Vecteur vecteurLumiere = new Vecteur(0, 0, -1);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.beginPath();
		Collections.sort(arrayListFace);
		for (Face face : arrayListFace) {

			coordX = new double[face.getListPoint().size()];
			coordY = new double[face.getListPoint().size()];
			coordZ = new double[face.getListPoint().size()];
			for(int i = 0; i < face.getListPoint().size(); i++) {
				coordX[i] = matricePoint.getM()[0][face.getListPoint().get(i).getId()];
				face.getListPoint().get(i).setX(coordX[i]);
				coordY[i] = matricePoint.getM()[1][face.getListPoint().get(i).getId()];
				face.getListPoint().get(i).setY(coordY[i]);
				coordZ[i] = matricePoint.getM()[2][face.getListPoint().get(i).getId()];
				face.getListPoint().get(i).setZ(coordZ[i]);
			}
			if(this.traitDessine) {
				gc.strokePolygon(coordX, coordY, coordX.length);
			}
			if(this.lumiereActive) {
			Vecteur vecteurFace1 = new Vecteur(coordX[1]-coordX[0], coordY[1]-coordY[0], coordZ[1] - coordZ[0]);
			Vecteur vecteurFace2 = new Vecteur(coordX[coordX.length-1]-coordX[0], coordY[coordY.length-1]-coordY[0], coordZ[coordZ.length-1] - coordZ[0]);
			Vecteur vecteurNormal = vecteurFace1.produitVectoriel(vecteurFace2);
			double coeffLumineux = (Math.cos((vecteurLumiere.Normalisation()).produitScalaire(vecteurNormal.Normalisation())));
			gc.setFill(Color.rgb((int)(face.getRgbColor()[0]*coeffLumineux), (int)(face.getRgbColor()[1]*coeffLumineux), (int)(face.getRgbColor()[2]*coeffLumineux)));
			}else {
				gc.setFill(Color.rgb(face.getRgbColor()[0], face.getRgbColor()[1], (face.getRgbColor()[2])));
			}
			gc.fillPolygon(coordX, coordY, coordX.length);
		}
	}

	@Override
	public void ajouterObservateur(Observateur o) {
		observateurCanvas = o;
		
	}

	@Override
	public void supprimerObservateur(Observateur o) {
		observateurCanvas = null;
		
	}

	public ArrayList<Face> getArrayListFace() {
		return arrayListFace;
	}

	public Point[] getTabPoint() {
		return tabPoint;
	}

	public int getNbFaces() {
		return this.getArrayListFace().size();
		}

	public int getNbPoints() { 
		return this.getTabPoint().length; 
	}

	public String getDescription() { 
		return description; 
		}

	public String getAuteur() { 
		return author;
		}

	public Matrice getMatricePoint() {
		return matricePoint;
	}

	public void setMatricePoint(Matrice matricePoint) {
		this.matricePoint = matricePoint;
		observateurCanvas.actualiser();
	}

	public ErrorList getErrorList() {
		return this.errorList;
	}

	public Point getPointDuMilieu() {
		return this.pointDuMilieu;
	}

	@Override
	public void notifierObservateurs(Observateur o) {
		observateurCanvas.actualiser();
		
	}
	
	public Observateur getObservateurCanvas() {
		return observateurCanvas;
	}

	public boolean isTraitDessine() {
		return traitDessine;
	}

	public void setTraitDessine(boolean traitDessine) {
		this.traitDessine = traitDessine;
	}

	public boolean isFaceDessine() {
		return faceDessine;
	}

	public void setFaceDessine(boolean faceDessine) {
		this.faceDessine = faceDessine;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setRapport(double rapport) {
		this.rapport = rapport;
	}

	public void setRapportHorizontal(boolean rapportHorizontal) {
		this.rapportHorizontal = rapportHorizontal;
	}

	public void setErrorList(ErrorList errorList) {
		this.errorList = errorList;
	}

	public void setPointDuMilieu(Point aPoint) {
		this.pointDuMilieu = aPoint;
	}

	public boolean isLumiereActive() {
		return lumiereActive;
	}


	public void setLumiereActive(boolean lumiereActive) {
		this.lumiereActive = lumiereActive;
	}
	
	public boolean isTurning() {
		return isTurning;
	}

	public void setTurning(boolean isTurning) {
		this.isTurning = isTurning;
	}


	public Timeline getRotationAuto() {
		return rotationAuto;
	}


	public void setRotationAuto(Timeline rotationAuto) {
		this.rotationAuto = rotationAuto;
		this.rotationAuto.setCycleCount(Timeline.INDEFINITE);
		
	}
}
