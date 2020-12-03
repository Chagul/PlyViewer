package modele;

import java.util.ArrayList;

public class Face implements Comparable {
	private ArrayList<Point> listPoint;
	
	public Face(Point p1, Point p2, Point p3) {
		this.listPoint = new ArrayList<Point>();
	}

	public Face() {
		this.listPoint = new ArrayList<Point>();
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
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
	 
	
}
