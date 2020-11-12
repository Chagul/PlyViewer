package modele;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
/**
 * 
 * @author planckea
 *
 */
public class PlyFile {

	private ArrayList<Face> arrayListFace;
	private HashMap<Integer, Point> hashMapPoint;
	private double[][] tabPoint;
	private Matrice matricePoint;
	private double rapport;
	private ErrorList errorList;
	public boolean rapportHorizontal;
	private Point pointDuMilieu;

	public PlyFile(int nbPoint) {
		this.arrayListFace = new ArrayList<Face>();
		this.hashMapPoint = new HashMap<Integer,Point>();
		this.tabPoint = new double[4][nbPoint];
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
		//this.matricePoint = this.matricePoint.translation(-canvas.getWidth()/2, -canvas.getHeight()/2, 1);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.beginPath();
		for (Face face : arrayListFace) {
			for(int i = 0; i < face.getListPoint().size(); i++) {
				if(i < face.getListPoint().size() - 1) 				
					gc.strokeLine(matricePoint.getM()[0][face.getListPoint().get(i).getId()], matricePoint.getM()[1][face.getListPoint().get(i).getId()], matricePoint.getM()[0][face.getListPoint().get(i+1).getId()],matricePoint.getM()[1][face.getListPoint().get(i+1).getId()] );
				else
					gc.strokeLine(matricePoint.getM()[0][face.getListPoint().get(i).getId()], matricePoint.getM()[1][face.getListPoint().get(i).getId()], matricePoint.getM()[0][face.getListPoint().get(0).getId()],matricePoint.getM()[1][face.getListPoint().get(0).getId()] );
			}
		}
		gc.closePath();
		//this.matricePoint = this.matricePoint.translation(canvas.getWidth()/2, canvas.getHeight()/2, 1);
	}

	public void firstDraw(Canvas canvas) {
		final double RAPPORT_MISE_A_L_ECHELLE = 0.60;
		final double MISE_A_L_ECHELLE_HORIZONTALE = canvas.getWidth()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
		final double MISE_A_L_ECHELLE_VERTICALE = canvas.getWidth()/this.rapport*RAPPORT_MISE_A_L_ECHELLE;
		System.out.println(pointDuMilieu.getX() + " ; " + pointDuMilieu.getY());
		System.out.println(this.matricePoint.toString());
		this.matricePoint = this.matricePoint.translation(pointDuMilieu.getX(), -pointDuMilieu.getY(), 0);
		System.out.println(this.matricePoint.toString());
		//System.out.println(pointDuMilieu.getX() + " ; " + pointDuMilieu.getY());
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
	/*System.out.println("APRES MISE A L'ECHELLE\n " + this.matricePoint.toString());
		this.matricePoint = this.matricePoint.rotation(Rotation.X, 180);
	System.out.println("APRES ROTATION Y\n " + this.matricePoint.toString());
		System.out.println(pointDuMilieu.getX() + " ; " + pointDuMilieu.getY());
		this.matricePoint = this.matricePoint.translation(pointDuMilieu.getX(),  pointDuMilieu.getY(), 0);
	System.out.println("APRES TRANSLATION DU POINT\n " + this.matricePoint.toString());
		this.matricePoint = this.matricePoint.translation(canvas.getWidth()/2, canvas.getHeight()/2, 0);
		System.out.println("APRES TRANSLATION AU MILIEU DU CANVAS\n " + this.matricePoint.toString());*/
		draw(canvas);
	}

	public void initMatrice() {
		for(Point p : hashMapPoint.values()) {
			this.tabPoint[0][p.getId()] = p.getX();
			this.tabPoint[1][p.getId()] = p.getY();
			this.tabPoint[2][p.getId()] = p.getZ();
			this.tabPoint[3][p.getId()] = 1;
		}
		this.matricePoint = new Matrice(this.tabPoint);

	}

	public ArrayList<Face> getArrayListFace() {
		return arrayListFace;
	}

	public void setArrayListFace(ArrayList<Face> arrayListFace) {
		this.arrayListFace = arrayListFace;
	}

	public HashMap<Integer, Point> getHashMapPoint() {
		return hashMapPoint;
	}

	public void setHashMapPoint(HashMap<Integer, Point> hashMapPoint) {
		this.hashMapPoint = hashMapPoint;
	}

	public double[][] getTabPoint() {
		return tabPoint;
	}

	public void setTabPoint(double[][] tabPoint) {
		this.tabPoint = tabPoint;
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
