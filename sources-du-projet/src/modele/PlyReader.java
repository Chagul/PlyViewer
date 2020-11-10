package modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
/**
 * 
 * @author planckea
 *
 */
public class PlyReader {

	//attributes
	private String pathToPly;
	private HashMap<Integer,Point> listPoint;
	private ArrayList<Face> listFace;
	private int nbPoint;
	private int nbFace;
	private final String endHeaderString = "end_header";
	Scanner sc;
	private double minX = 0;
	private double maxX = 0;
	private double minY = 0;
	private double maxY = 0;
	/**
	 * Les patterns que nous retrouverons dans un fichier ply
	 */
	private final String FLOAT = "-?[0-9]+((\\.[0-9]+)?(e(\\+|-)?[0-9]+)?)" ;
	private final String patternPoint = "^(\\s)*" + FLOAT + "\\s+" + FLOAT + "\\s+" + FLOAT + "(\\s)*$" ;
	private final String patternFace = "^( ?[0-9]+ ?)* ?$";
	private final Pattern pointP = Pattern.compile("[0-9]+");
	private final Pattern px = Pattern.compile("^-?" + FLOAT + "\\s");
	private final Pattern py = Pattern.compile("\\s" + FLOAT + "\\s");
	private final Pattern pz = Pattern.compile("\\s" + FLOAT + "\\s$");
	private final Pattern nbPointFace = Pattern.compile("^ ?[0-9]+");
	
	//Les attributs suivant servent pour la gestion des exceptions
	private ArrayList<String> listPointErreur = new ArrayList<String>();
	private ArrayList<String> listFaceErreur = new ArrayList<String>();
	//constructor
	public PlyReader(String aPathToAPly) {
		Point.resetNAuto();
		this.pathToPly = aPathToAPly;
		this.listPoint = new HashMap<Integer,Point>();
		this.listFace = new ArrayList<Face>();
		this.nbPoint = 0;
		this.nbFace = 0;
	}
	/**
	 * Lis le fichier, vérifie si le chemin est valide, que c'est bien un fichier ply conforme (pas de points manquants,pas de face avec des points en moins ..) et créé les points et face lues
	 * @return Vrai si le chemin du fichier est valide, que c'est un ply et que la créations des points et faces est valide, faux sinon.
	 * @throws FileNotFoundException 
	 */
	public boolean initPly() throws FileNotFoundException {
		sc = new Scanner(new File(pathToPly));
		boolean endHeader = false;
		int cptLine = 0;
		String tmpReader = "";
		String vertexString = "element vertex ";
		String faceString = "element face ";
		tmpReader = sc.nextLine();
		if(!tmpReader.contains("ply") && cptLine == 0) return false;
		while(sc.hasNextLine() && !endHeader) {
			tmpReader = sc.nextLine();
			if(tmpReader.contains(vertexString)) 
				this.nbPoint = Integer.parseInt(tmpReader.substring(vertexString.length(), tmpReader.length()));

			if(tmpReader.contains(faceString)) 
				this.nbFace = Integer.parseInt(tmpReader.substring(faceString.length(), tmpReader.length()));

			if(tmpReader.equals((endHeaderString))) 
				endHeader = true;
		}
		if(this.nbPoint==0 || this.nbFace==0)
			return false;
		return true;
	}
	/**
	 * Lis les lignes correspondantes aux points et aux faces dans le ply et les créer
	 * @return true si tout s'est bien passé
	 * @throws CreationPointException Quand il y a une erreur de format dans un point
	 * @throws CreationFaceException Quand il y a une erreur de format dans une face
	 */
	public boolean readPly() throws CreationPointException, CreationFaceException {
		String tmpReader = "";
		int cptPoint = 0;
		int cptFace = 0;
		boolean dansPoint = true;
		while(sc.hasNextLine()) {
			tmpReader = sc.nextLine();
			if(Pattern.matches(patternPoint, tmpReader)) {
				creationPoint(tmpReader);			
				cptPoint++;
			}else if(!Pattern.matches(patternPoint, tmpReader) && cptPoint < this.nbPoint) {
				this.listPointErreur.add(tmpReader);
				creationPoint("0 0 0 ");
				cptPoint++;
				throw new CreationPointException();
			}
			if(cptPoint == this.nbPoint) dansPoint = false;

			if(!dansPoint && Pattern.matches(patternFace,tmpReader)) {
				//System.out.println(cptPoint);
				creationFace(tmpReader);
				cptFace++;
			}else if(!dansPoint && !Pattern.matches(patternPoint, tmpReader) && cptFace < this.nbFace) {
				this.listFaceErreur.add(tmpReader);
				creationFace("3 0 0 0 ");
				cptFace++;
				throw new CreationFaceException();
			}

		}
		sc.close();
		return true;
	}

	/**
	 * 
	 * @param tmpReader Le String correspondant à un point
	 * @return Vrai si on trouve bien les 3 points faux sinon
	 */
	public boolean creationPoint(String tmpReader) {
		Matcher mx = px.matcher(tmpReader);
		Matcher my = py.matcher(tmpReader);
		Matcher mz = pz.matcher(tmpReader);
		if(mx.find() && my.find() && mz.find()) {
			Point tmp = new Point(Double.parseDouble(mx.group()), Double.parseDouble(my.group()), Double.parseDouble(mz.group()));
			if(minX == 0 && maxX == 0 && minY == 0 && maxY == 0){
				minX = tmp.getX();
				maxX = tmp.getX();
				minY = tmp.getY();
				maxX = tmp.getY();
			}else {
				if(tmp.getX() < minX)
					minX = tmp.getX();
				if(tmp.getX() > maxX)
					maxX = tmp.getX();
				if(tmp.getY() < minY)
					minY = tmp.getY();
				if(tmp.getY() > maxY)
					maxY = tmp.getY();
			}
			this.listPoint.put(tmp.getId(),tmp);
			return true;
		}
		return false;
	}
	/**
	 * Creer une face avec les différents point qui la compose
	 * @param tmpReader le string contenant les points de la face
	 * @return
	 */
	public boolean creationFace(String tmpReader) {
		Matcher pointMatch = pointP.matcher(tmpReader);
		Matcher pointDansFace = nbPointFace.matcher(tmpReader);
		int cpt = 0;
		Face tmp = new Face();
		if(!pointMatch.find() || !pointDansFace.find()) return false;

		while(pointMatch.find()) {
			tmp.addPoint(this.listPoint.get(Integer.parseInt(pointMatch.group())));
			cpt++;
		}
		if(cpt != Integer.parseInt(pointDansFace.group())) return false;
		this.listFace.add(tmp);
		return true;
	}

	//getters and setters
	public HashMap<Integer, Point> getListPoint() {
		return listPoint;
	}
	public void setListPoint(HashMap<Integer, Point> listPoint) {
		this.listPoint = listPoint;
	}
	public ArrayList<Face> getListFace() {
		return listFace;
	}
	public void setListFace(ArrayList<Face> listFace) {
		this.listFace = listFace;
	}
	public int getNbPoint() {
		return nbPoint;
	}
	public void setNbPoint(int nbPoint) {
		this.nbPoint = nbPoint;
	}
	public int getNbFace() {
		return nbFace;
	}
	public void setNbFace(int nbFace) {
		this.nbFace = nbFace;
	}
	/**
	 * 
	 * @param Le numéro du point
	 * @return Le point correspondant à l'indice donné
	 */
	public Point getPoint(int number) {
		return this.listPoint.get(number);
	}

	/**
	 * 
	 * @param number Le numéro de la face
	 * @return	La face correspondant à l'indice donné
	 */
	public Face getFace(int number) {
		return this.listFace.get(number);
	}

	public String getPath() {
		return this.pathToPly;
	}

	public double getMinX() {
		return this.minX;
	}
	public double getMaxX() {
		return this.maxX;
	}

	public double getMinY() {
		return this.minY;
	}
	public double getMaxY() {
		return this.maxY;
	}
	
	public ArrayList<String> getListPointErreur(){
		return this.listPointErreur;
	}
	public ArrayList<String> getListFaceErreur(){
		return this.listFaceErreur;
	}
}
