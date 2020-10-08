package modele;

public class Point {
	private int id;
	private double x;
	private double y;
	private double z;
	static int nAuto = 0;
	
	public Point(double x, double y, double z) {
		this.id = nAuto;
		nAuto++;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNAuto() {
		return nAuto;
	}
	
	public static void resetNAuto() {
		nAuto = 0;
	}
	
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
}
