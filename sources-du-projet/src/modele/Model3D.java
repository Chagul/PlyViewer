package modele;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
/**
 * 
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

	public Model3D(int nbPoint) {
		this.arrayListFace = new ArrayList<Face>();
		this.tabPoint = new Point[nbPoint];
		ArrayList<String> listPointErreur = new ArrayList<String>();
		ArrayList<String> listFaceErreur = new ArrayList<String>();
		this.errorList = new ErrorList(listPointErreur, listFaceErreur);
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
				gc.strokeLine(matricePoint.getM()[0][ptA], 
						matricePoint.getM()[1][ptA], 
						matricePoint.getM()[0][ptB],
						matricePoint.getM()[1][ptB] );
			}
		}
		gc.closePath();
	}

	public void drawFaces(Canvas canvas, boolean trait) {
		double[] coordX;
		double[] coordY;
		double[] coordZ;
		Vecteur vecteurLumiere = new Vecteur(1, 1, 0);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.beginPath();
		arrayListFace.sort(new Comparator<Face>() {

			@Override
			public int compare(Face arg0, Face arg1) {
				arg0.compareTo(arg1);
				return 0;
			}
		});
		for (Face face : arrayListFace) {

			coordX = new double[face.getListPoint().size()];
			coordY = new double[face.getListPoint().size()];
			coordZ = new double[face.getListPoint().size()];
			for(int i = 0; i < face.getListPoint().size(); i++) {
				coordX[i] = matricePoint.getM()[0][face.getListPoint().get(i).getId()];
				coordY[i] = matricePoint.getM()[1][face.getListPoint().get(i).getId()];
				coordZ[i] = matricePoint.getM()[2][face.getListPoint().get(i).getId()];
			}
			if(trait) {
				gc.strokePolygon(coordX, coordY, coordX.length);
			}
			Vecteur vecteurFace1 = new Vecteur(coordX[1]-coordX[0], coordY[1]-coordY[0], coordZ[1] - coordZ[0]);
			Vecteur vecteurFace2 = new Vecteur(coordX[coordX.length-1]-coordX[0], coordY[coordY.length-1]-coordY[0], coordZ[coordZ.length-1] - coordZ[0]);
			Vecteur vecteurNormal = vecteurFace1.produitVectoriel(vecteurFace2);
			double coeffLumineux = (Math.cos((vecteurLumiere.Normalisation()).produitScalaire(vecteurNormal.Normalisation())));
			if( coeffLumineux >= 0) {
				gc.setFill(Color.rgb((int)(face.getRgbColor()[0]*coeffLumineux), (int)(face.getRgbColor()[1]*coeffLumineux), (int)(face.getRgbColor()[2]*coeffLumineux)));
			}
			else
				gc.setFill(Color.rgb(face.getRgbColor()[0], face.getRgbColor()[1], (face.getRgbColor()[2])));
			gc.fillPolygon(coordX, coordY, coordX.length);
		}
	}

	/**
	 * Dessine le ply à partir de ses points et de ses faces et permet d'adapter l'image au canvas en proportion, ainsi que retourner l'image
	 * @param canvas Le canvas sur lequel le ply sera dessiné
	 */
	public void firstDraw(Canvas canvas) {
		System.out.println("Votre modèle comporte " + this.getArrayListFace().size() + " faces et " + this.tabPoint.length + " points.");
		final double RAPPORT_MISE_A_L_ECHELLE = 0.60;
		final double MISE_A_L_ECHELLE_HORIZONTALE = canvas.getWidth()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
		final double MISE_A_L_ECHELLE_VERTICALE = canvas.getHeight()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
		this.matricePoint = this.matricePoint.translation(-pointDuMilieu.getX(), -pointDuMilieu.getY(), 0);
		if(rapportHorizontal) {
			this.matricePoint = this.matricePoint.multiplication( MISE_A_L_ECHELLE_HORIZONTALE);
			this.pointDuMilieu.setX(pointDuMilieu.getX() * MISE_A_L_ECHELLE_HORIZONTALE);
			this.pointDuMilieu.setY(pointDuMilieu.getY() * MISE_A_L_ECHELLE_HORIZONTALE);
		}
		else {
			this.matricePoint = this.matricePoint.multiplication(MISE_A_L_ECHELLE_VERTICALE);
			this.pointDuMilieu.setX(pointDuMilieu.getX() * MISE_A_L_ECHELLE_VERTICALE);
			this.pointDuMilieu.setY(pointDuMilieu.getY() * MISE_A_L_ECHELLE_VERTICALE);
		}

		this.matricePoint = this.matricePoint.rotation(Rotation.X, 180);
		this.matricePoint = this.matricePoint.translation(canvas.getWidth()/2, canvas.getHeight()/2, 0);
		draw(canvas);
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

	public ArrayList<Face> getArrayListFace() {
		return arrayListFace;
	}

	public Point[] getTabPoint() {
		return tabPoint;
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
	public void ajouterObservateur(Observateur o) {
		observateurCanvas = o;
		
	}

	@Override
	public void supprimerObservateur(Observateur o) {
		observateurCanvas = null;
		
	}

	@Override
	public void notifierObservateurs(Observateur o) {
		observateurCanvas.actualiser();
		
	}
	
	public Observateur getObservateurCanvas() {
		return observateurCanvas;
	}
}
