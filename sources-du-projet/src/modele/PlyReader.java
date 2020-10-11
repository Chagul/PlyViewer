package modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PlyReader {

	//attributes
	private String pathToPly;
	private ArrayList<Point> listPoint;
	private ArrayList<Face> listFace;
	private int nbPoint;
	private int nbFace;

	//constructor
	public PlyReader(String aPathToAPly) {
		this.pathToPly = aPathToAPly;
		this.listPoint = new ArrayList<Point>();
		this.listFace = new ArrayList<Face>();
		this.nbPoint = 0;
		this.nbFace = 0;
	}
	/**
	 * Lis le fichier, vérifie si le chemin est valide, que c'est bien un fichier ply conforme (pas de points manquants,pas de face avec des points en moins ..) et créé les points et face lues
	 * @return Vrai si le chemin du fichier est valide, que c'est un ply et que la créations des points et faces est valide, faux sinon.
	 */
	public boolean initPly() {
		Scanner sc;
		try {
			sc = new Scanner(new File(pathToPly));
			int cptLine = 0;
			String tmpReader = "";
			String vertexString = "element vertex ";
			String faceString = "element face ";
			String endHeaderString = "end_header";
			String patternPoint = "^-?[0-9]+(\\.[0-9]+)?\\s-?[0-9]+(\\.[0-9]+)?\\s-?[0-9]+(\\.[0-9]+)?\\s?$";
			String patternFace = "^ ?[0-9]+ [0-9]+ [0-9]+ [0-9]+ ?$";
			Pattern pointP = Pattern.compile("[0-9]+");
			Pattern px = Pattern.compile("^-?[0-9]+(\\.[0-9]+)? ");
			Pattern py = Pattern.compile(" -?[0-9]+(\\.[0-9]+)? " );
			Pattern pz = Pattern.compile(" -?[0-9]+(\\.[0-9]+)? $");
			boolean endHeader = false;
			tmpReader = sc.nextLine();
			if(!tmpReader.contains("ply") && cptLine == 0) return false;
			while(sc.hasNextLine()) {
				cptLine++;
				//System.out.println(cptLine);
				tmpReader = sc.nextLine();
				if(tmpReader.contains(vertexString)) this.nbPoint = Integer.parseInt(tmpReader.substring(vertexString.length(), tmpReader.length()));
				
				if(tmpReader.contains(faceString)) this.nbFace = Integer.parseInt(tmpReader.substring(faceString.length(), tmpReader.length()));
				
				if(tmpReader.equals((endHeaderString))) endHeader = true;
				
				if(endHeader == true && Pattern.matches(patternPoint, tmpReader)) {
					Matcher mx = px.matcher(tmpReader);
					Matcher my = py.matcher(tmpReader);
					Matcher mz = pz.matcher(tmpReader);
					if(mx.find() && my.find() && mz.find()) {
						//System.out.println("je creer le point");
						this.listPoint.add(new Point(Double.parseDouble(mx.group()), Double.parseDouble(my.group()), Double.parseDouble(mz.group())));
					}else {
						return false;
					}
				}
				
				if(endHeader == true && Pattern.matches(patternFace,tmpReader) ) {
					Matcher pointMatch = pointP.matcher(tmpReader);
					int point1 = 0;
					int point2 = 0;
					int point3 = 0;
					int cpt = 0;
					while(pointMatch.find()) {
						if(cpt == 1) point1 = Integer.parseInt(pointMatch.group());
						if(cpt == 2) point2 = Integer.parseInt(pointMatch.group());
						if(cpt == 3) point3 = Integer.parseInt(pointMatch.group());
						cpt++;
					}
					this.listFace.add(new Face(this.listPoint.get(point1), this.listPoint.get(point2), this.listPoint.get(point3)));
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		sc.close();
		return true;
	}
	
	//getters and setters
	public ArrayList<Point> getListPoint() {
		return listPoint;
	}
	public void setListPoint(ArrayList<Point> listPoint) {
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
}
