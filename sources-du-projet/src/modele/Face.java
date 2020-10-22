package modele;

import java.util.ArrayList;

public class Face {
	private Point p1;
	private Point p2;
	private Point p3;
	private ArrayList<Point> listPoint;
	
	public Face(Point p1, Point p2, Point p3) {
		this.listPoint = new ArrayList<Point>();
		this.p1 = p1;
		this.p2 = p2;
		this.p3 = p3;
	}

	public Face() {
		this.listPoint = new ArrayList<Point>();
	}

	public Point getP1() {
		return p1;
	}

	public Point getP2() {
		return p2;
	}

	public Point getP3() {
		return p3;
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

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Face other = (Face) obj;
		if (p1 == null) {
			if (other.p1 != null)
				return false;
		} else if (!p1.equals(other.p1))
			return false;
		if (p2 == null) {
			if (other.p2 != null)
				return false;
		} else if (!p2.equals(other.p2))
			return false;
		if (p3 == null) {
			if (other.p3 != null)
				return false;
		} else if (!p3.equals(other.p3))
			return false;
		return true;
	}
	 
	
}
