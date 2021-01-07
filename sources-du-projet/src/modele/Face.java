package modele;

import java.util.ArrayList;

public class Face implements Comparable<Face> {
	
	private ArrayList<Point> listPoint;
	private int[] rgbColor;

	public Face() {
		this.listPoint = new ArrayList<Point>();
		rgbColor = new int[3];
		rgbColor[0] = 255;
		rgbColor[1] = 255;
		rgbColor[2] = 255;
	}
	/**
	 * Deux faces sont considérées comme égales si tout les points qui les composents sont égaux
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Face other = (Face) obj;
		if (this.getListPoint().isEmpty() || other.getListPoint().isEmpty())
			return false;
		if (this.getListPoint().size() != other.getListPoint().size())
			return false;
		int cpt = 0;
		for (Point point : other.getListPoint()) {
			if(!point.equals(this.listPoint.get(cpt)))
				return false;
		cpt++;
	}
		return true;
	}


	@Override
	/**
	 * Compare deux faces par rapport à leur point Z afin de pouvoir les trier
	 */
	public int compareTo(Face other) {
		double moyZThis = 0;
		int cptPoint = 0;
		double moyZOther = 0;
		for(Point p : listPoint) {
			moyZThis += p.getZ();
			cptPoint++;
		}
		/*Vecteur vecteurFace1A = new Vecteur(this.listPoint.get(1).getX()-this.listPoint.get(0).getX(), this.listPoint.get(1).getY()-this.listPoint.get(0).getY(), this.listPoint.get(1).getZ()-this.listPoint.get(0).getZ());
		Vecteur vecteurFace1B = new Vecteur(this.listPoint.get(this.listPoint.size()-1).getX()-this.listPoint.get(0).getX(), this.listPoint.get(this.listPoint.size()-1).getY()-this.listPoint.get(0).getY(), this.listPoint.get(this.listPoint.size()-1).getZ()-this.listPoint.get(0).getZ());
		Vecteur vecteurFace2A = new Vecteur(other.listPoint.get(1).getX()-other.listPoint.get(0).getX(), other.listPoint.get(1).getY()-other.listPoint.get(0).getY(), other.listPoint.get(1).getZ()-other.listPoint.get(0).getZ());
		Vecteur vecteurFace2B = new Vecteur(other.listPoint.get(other.listPoint.size()-1).getX()-other.listPoint.get(0).getX(), other.listPoint.get(other.listPoint.size()-1).getY()-other.listPoint.get(0).getY(), other.listPoint.get(other.listPoint.size()-1).getZ()-other.listPoint.get(0).getZ());
		Vecteur vecteurNormal1 = vecteurFace1A.produitVectoriel(vecteurFace1B);
		Vecteur vecteurNormal2 = vecteurFace2A.produitVectoriel(vecteurFace2B);
		if(vecteurNormal1.getZ() * vecteurNormal2.getZ() < 0) {
			if(vecteurNormal1.getZ() < 0)
				return 1;
			else
				return -1;
		}*/
		moyZThis = moyZThis / cptPoint;
		cptPoint = 0;
		for(Point p : other.getListPoint()) {
			moyZOther += p.getZ();
			cptPoint++;
		}
		//System.out.println("this moyZ" + moyZThis);
		//System.out.println("Other moyZ" + moyZOther);
		if(moyZThis < moyZOther)
			return -1;
		else if(moyZThis > moyZOther)
			return 1;
		else
			return 0;
	}
	 
	public ArrayList<Point> getListPoint() {
		return this.listPoint;
	}

	public void addPoint(Point p) {
		this.listPoint.add(p);
	}
	
	@Override
	public String toString() {
		String tmp  = "";
		for (Point point : listPoint) {
			tmp += point;
		}
		return tmp;
	}
	
	public int[] getRgbColor() {
		return rgbColor;
	}
	
}
