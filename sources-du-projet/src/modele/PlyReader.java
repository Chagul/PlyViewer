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

	

	private static Scanner sc;
	private static double minX = 0;
	private static double maxX = 0;
	private static double minY = 0;
	private static double maxY = 0;
	private boolean dansPoint = true;
	/**
	 * Les patterns que nous retrouverons dans un fichier ply
	 */
	private static final String FLOAT = "-?[0-9]+((\\.[0-9]+)?(e(\\+|-)?[0-9]+)?)" ;
	private static final String PATTERN_POINT = "^(\\s)*" + FLOAT + "\\s+" + FLOAT + "\\s+" + FLOAT + "(\\s)*$" ;
	private static final String PATTERN_FACE = "^( ?[0-9]+ ?)* ?$";
	private static final Pattern POINT = Pattern.compile("[0-9]+");
	private static final Pattern X_POINT = Pattern.compile("^-?" + FLOAT + "\\s");
	private static final Pattern Y_POINT = Pattern.compile("\\s" + FLOAT + "\\s");
	private static final Pattern Z_POINT = Pattern.compile("\\s" + FLOAT + "\\s$");
	private static final Pattern NOMBRE_POINT_DANS_FACE = Pattern.compile("^?[0-9]+");
	
	/**
	 * Les differents compteurs
	 */
	private static int cptPoint = 0;
	private static int cptFace = 0;
	private int nbPoint;
	private int nbFace;
	//constructor
	public PlyReader() {
	}
	/**
	 * Lis le header du fichier et verifie qu'il est valide
	 * @return Vrai si le chemin du fichier est valide, que c'est un ply et que la créations des points et faces est valide, faux sinon.
	 * @throws FileNotFoundException Quand le chemin du fichier est incorrect
	 */
	public boolean initPly(String pathToPly) throws FileNotFoundException {
		cptPoint = 0;
		cptFace = 0;
		nbPoint = 0;
		dansPoint = true;
		nbFace = 0;
		final String END_HEADER_STRING = "end_header";
		final String VERTEX_STRING = "element vertex ";
		final String FACE_STRING = "element face ";
		
		sc = new Scanner(new File(pathToPly));
		boolean endHeader = false;
		int cptLine = 0;

		String tmpReader = "";


		tmpReader = sc.nextLine();
		if(!tmpReader.contains("ply") && cptLine == 0) return false;
		while(sc.hasNextLine() && !endHeader) {
			tmpReader = sc.nextLine();
			if(tmpReader.contains(VERTEX_STRING)) 
				nbPoint = Integer.parseInt(tmpReader.substring(VERTEX_STRING.length(), tmpReader.length()));

			if(tmpReader.contains(FACE_STRING)) 
				nbFace = Integer.parseInt(tmpReader.substring(FACE_STRING.length(), tmpReader.length()));

			if(tmpReader.equals(END_HEADER_STRING)) 
				endHeader = true;
		}
		if(nbPoint==0 || nbFace==0)
			return false;
		return true;
	}
	/**
	 * Lis les lignes correspondantes aux points et aux faces dans le ply et les créer
	 * @return true si tout s'est bien passé
	 * @throws CreationPointException Quand il y a une erreur de format dans un point
	 * @throws CreationFaceException Quand il y a une erreur de format dans une face
	 */
	public PlyFile getPly(String pathToPly) {
		Point.resetNAuto();
		PlyFile aPlyFile = new PlyFile(nbPoint);
		String tmpReader = "";
		while(sc.hasNextLine()) {
			tmpReader = sc.nextLine();
			try {
				analyseString(tmpReader,aPlyFile);	
			}catch(CreationFormatPointException cfpe){
				aPlyFile.getErrorList().getListPointErreur().add(tmpReader + "erreur dans le format du point ");
				continue;
			}catch(CreationPointManquantException cpme) {
				aPlyFile.getErrorList().getListPointErreur().add("Manque un point !");
				continue;
			}catch(CreationFormatFaceException cffe) {
				aPlyFile.getErrorList().getListFaceErreur().add(tmpReader + "erreur dans le format de la face");
				continue;
			}
		}
		sc.close();
		double rapport = 0;
		boolean rapportHorizontal = false;
		if(maxX - minX > maxY - minY) {
			rapport = maxX -minX;
			rapportHorizontal = true;
		}else
			rapport = maxY - minY;
		aPlyFile.setRapportHorizontal(rapportHorizontal);
		aPlyFile.setRapport(rapport);
		aPlyFile.initMatrice();
		return aPlyFile;
	}
	/**
	 * Analyse la String correspondant à la ligne lue et dispatche le traitement selon la ligne dans d'autres methodes
	 * @param tmpReader la ligne lue
	 * @param aPlyFile l'objet sur lequel on ajouteras les points et face crées
	 * @throws CreationFormatFaceException Si une face ne corresponds pas à une notation normale
	 * @throws CreationFormatPointException Si un point ne corresponds pas à une notation normale
	 * @throws CreationPointManquantException Si il manque des points quand on arrive à la lecture des faces
	 */
	private void analyseString(String tmpReader,PlyFile aPlyFile) throws CreationFormatFaceException, CreationFormatPointException, CreationPointManquantException {
		if(cptPoint == nbPoint) dansPoint = false;
		if(Pattern.matches(PATTERN_POINT, tmpReader)) {
			creationPoint(tmpReader,aPlyFile.getHashMapPoint());			
			cptPoint++;
		}else if(dansPoint && !Pattern.matches(PATTERN_POINT, tmpReader)) {
			creationPoint("0 0 0 ",aPlyFile.getHashMapPoint());
			cptPoint++;
			throw new CreationFormatPointException();
		}else if(!dansPoint && cptPoint != nbPoint) {
			while(cptPoint < nbPoint ) {
			creationPoint("0 0 0 ",aPlyFile.getHashMapPoint());
			cptPoint++;
			}
			throw new CreationPointManquantException();
		}
		
		if(!dansPoint && Pattern.matches(PATTERN_FACE,tmpReader)) {
			creationFace(tmpReader,aPlyFile.getArrayListFace(), aPlyFile.getHashMapPoint());
			cptFace++;
		}else if(!dansPoint && !Pattern.matches(PATTERN_POINT, tmpReader) && cptFace < nbFace) {
			creationFace("3 0 0 0 ", aPlyFile.getArrayListFace(), aPlyFile.getHashMapPoint());
			cptFace++;
			throw new CreationFormatFaceException();
		}
	}

	/**
	 * Créé un point à partir d'un string et l'ajoutes à la hashmap du PlyFile
	 * @param tmpReader Le String correspondant à un point
	 * @param hashMapPoint la hashmap dans laquelle on ajouteras le point
	 * @return Vrai si on trouve bien les x points (1er nombre du string) faux sinon
	 */
	public boolean creationPoint(String tmpReader, HashMap<Integer,Point> hashMapPoint) {
		Matcher mx = X_POINT.matcher(tmpReader);
		Matcher my = Y_POINT.matcher(tmpReader);
		Matcher mz = Z_POINT.matcher(tmpReader);
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
			hashMapPoint.put(tmp.getId(),tmp);
			return true;
		}
		return false;
	}
	/**
	 * Creer une face avec les différents point qui la compose
	 * @param tmpReader le string contenant les points de la face
	 * @return 
	 */
	public boolean creationFace(String tmpReader,ArrayList<Face> listFace, HashMap<Integer, Point> hashMapPoint) {
		Matcher pointMatch = POINT.matcher(tmpReader);
		Matcher pointDansFace = NOMBRE_POINT_DANS_FACE.matcher(tmpReader);
		int cpt = 0;
		Face tmp = new Face();
		if(!pointMatch.find() || !pointDansFace.find()) return false;

		while(pointMatch.find()) {
			tmp.addPoint(hashMapPoint.get(Integer.parseInt(pointMatch.group())));
			cpt++;
		}
		if(cpt != Integer.parseInt(pointDansFace.group())) return false;
		listFace.add(tmp);
		return true;
	}

}
