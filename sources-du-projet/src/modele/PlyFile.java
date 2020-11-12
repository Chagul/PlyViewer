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
		double coordonneePointDuMilieuX = this.pointDuMilieu.getX()*(canvas.getWidth()/(this.rapport)*0.60) + canvas.getWidth()/2;
		double coordonneePointDuMilieuY = this.pointDuMilieu.getY()*(canvas.getHeight()/(this.rapport)*0.60) + canvas.getHeight()/2;
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.translate(-coordonneePointDuMilieuX,-coordonneePointDuMilieuY);;
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		gc.translate(coordonneePointDuMilieuX,coordonneePointDuMilieuY);;
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
		//gc.translate(canvas.getWidth()*0.5, canvas.getHeight()*0.80);
	}
	public void firstDraw(Canvas canvas) {
		if(rapportHorizontal)
			this.matricePoint = this.matricePoint.multiplication(-(canvas.getWidth()/(this.rapport)*0.60));
		else 
			this.matricePoint = this.matricePoint.multiplication(-(canvas.getHeight()/(this.rapport)*0.60));	
		double coordonneePointDuMilieuX = this.pointDuMilieu.getX()*(canvas.getWidth()/(this.rapport)*0.60) + canvas.getWidth()/2;
		double coordonneePointDuMilieuY = this.pointDuMilieu.getY()*(canvas.getHeight()/(this.rapport)*0.60) + canvas.getHeight()/2;
		canvas.getGraphicsContext2D().translate(coordonneePointDuMilieuX,coordonneePointDuMilieuY);;
		draw(canvas);
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

	public void initMatrice() {
		for(Point p : hashMapPoint.values()) {
			this.tabPoint[0][p.getId()] = p.getX();
			this.tabPoint[1][p.getId()] = p.getY();
			this.tabPoint[2][p.getId()] = p.getZ();
			this.tabPoint[3][p.getId()] = 1;
		}
		this.matricePoint = new Matrice(this.tabPoint);
		
	}

}
