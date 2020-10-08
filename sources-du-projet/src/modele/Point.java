package modele;

public class Point {
	private int id;
	private int x;
	private int y;
	private int z;
	static int nAuto = 0;
	
	public Point(int x, int y, int z) {
		this.id = nAuto;
		nAuto++;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	public int getId() {
		return id;
	}
	
	public int getNAuto() {
		return nAuto;
	}
	@Override
	public String toString() {
		return "Point [x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	public static void resetNAuto() {
		nAuto = 0;
	}
}
