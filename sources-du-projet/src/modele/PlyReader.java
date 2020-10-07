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
		initPly();
	}

	private boolean initPly() {
		Scanner sc;
		try {
			sc = new Scanner(new File(pathToPly));
		String tmpReader = "";
		String vertexString = "element vertex ";
		String faceString = "element face ";
		String endHeaderString = "end_header";
		String patternPoint = "-?[0-9]*.[0-9]* -?[0-9]*.[0-9]* -?[0-9]*.[0-9]*";
		Pattern px = Pattern.compile("-?[0-9]*.[0-9]* ");
		Pattern py = Pattern.compile(" -?[0-9]*.[0-9]* " );
		Pattern pz = Pattern.compile(" -?[0-9]*.[0-9]*");
		boolean endHeader = false;
		tmpReader = sc.nextLine();
		if(!tmpReader.contains("ply")) return false;
		while(sc.hasNext()) {
			tmpReader = sc.nextLine();
			if(tmpReader.contains(vertexString)) this.nbPoint = Integer.parseInt(tmpReader.substring(vertexString.length(), tmpReader.length()));
			if(tmpReader.contains(vertexString)) this.nbFace = Integer.parseInt(tmpReader.substring(faceString.length(), tmpReader.length()));
			if(tmpReader.equals((endHeaderString))) endHeader = true;
			if(endHeader = true && Pattern.matches(patternPoint, tmpReader)) {
				Matcher mx = px.matcher(tmpReader);
				Matcher my = py.matcher(tmpReader);
				Matcher mz = pz.matcher(tmpReader);
				if(mx.find() && my.find() && mz.find()) {
					this.listPoint.add(new Point(Integer.parseInt(mx.group()), Integer.parseInt(my.group()), Integer.parseInt(mz.group())));
				}else {
					return false;
				}

			}

		}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}
}
