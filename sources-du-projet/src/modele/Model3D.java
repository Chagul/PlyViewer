package modele;


import java.util.ArrayList;
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
public class Model3D {

	private ArrayList<Face> arrayListFace;
	private Point[] tabPoint;
	private Matrice matricePoint;
	private double rapport;
	private ErrorList errorList;
	public boolean rapportHorizontal;
	private Point pointDuMilieu;

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

	public void drawFaces(Canvas canvas) {
		double[] coordX;
		double[] coordY;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.beginPath();
		//arrayListFace.sort(c);
		for (Face face : arrayListFace) {
			coordX = new double[face.getListPoint().size()];
			coordY = new double[face.getListPoint().size()];
			for(int i = 0; i < face.getListPoint().size(); i++) {
				coordX[i] = face.getListPoint().get(i).getX();
				coordY[i] = face.getListPoint().get(i).getY();
			}
			gc.strokePolygon(coordX, coordY, coordX.length);
			gc.setFill(Color.RED);
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
	}

	public ErrorList getErrorList() {
		return this.errorList;
	}

	public Point getPointDuMilieu() {
		return this.pointDuMilieu;
	}
}
